package ru.chirikhin.chattree.model;

import org.apache.log4j.Logger;
import ru.chirikhin.cyclelist.CycleLinkedList;

import java.net.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class Node implements Runnable, Observer {
    private static final Logger logger = Logger.getLogger(Node.class.toString());

    private static final int MAX_COUNT_OF_NOT_CONFIRMED_MESSAGES = 3000;
    private static final int CAPACITY_OF_RECEIVE_QUEUE = 1000;
    private static final int CAPACITY_OF_TO_SEND_QUEUE = 1000;
    private static final int CAPACITY_OF_HANDLED_MESSAGES_QUEUE = 10000;

    private final DatagramSocket datagramSocket;
    private final String name;
    private final GlobalIDGenerator globalIDGenerator;
    private final Thread senderThread;
    private final Thread receiverThread;
    private final Thread resenderThread;

    private boolean isDying  = false;

    private InetSocketAddress parentInetSocketAddress;

    private final BlockingQueue<ReceivedMessage> receivedMessages = new LinkedBlockingQueue<>(CAPACITY_OF_RECEIVE_QUEUE);
    private final BlockingQueue<AddressedMessage> messagesToSend = new LinkedBlockingQueue<>(CAPACITY_OF_TO_SEND_QUEUE);

    private final CycleLinkedList<NotConfirmedAddressedMessage> notConfirmedMessages = new CycleLinkedList<>(MAX_COUNT_OF_NOT_CONFIRMED_MESSAGES);
    private final LinkedBlockingQueue<InetSocketAddress> children = new LinkedBlockingQueue<>();
    private final CycleLinkedList<BaseMessage> handledMessages = new CycleLinkedList<>(CAPACITY_OF_HANDLED_MESSAGES_QUEUE);


    public Node (String name, int percentOfLose, int port, InetSocketAddress parentInetSocketAddress) throws SocketException, UnknownHostException {
        this.datagramSocket = new DatagramSocket(port);
        this.name = name;
        this.parentInetSocketAddress = parentInetSocketAddress;
        this.globalIDGenerator = new GlobalIDGenerator(port, Inet4Address.getLocalHost().getHostAddress());
        this.receiverThread = new Thread(new MessageReceiver(receivedMessages, datagramSocket, percentOfLose), "Message Receiver Thread");
        this.senderThread = new Thread(new MessageSender(messagesToSend, datagramSocket), "Message Sender Thread");
        this.resenderThread = new Thread(new MessageResender(messagesToSend, notConfirmedMessages), "Message Resender Thread");
    }

    @Override
    public void run() {
        this.receiverThread.start();
        this.senderThread.start();
        this.resenderThread.start();

        try {

            if (!isRoot()) {
                BaseMessage newChildMessage = new NewChildMessage(globalIDGenerator.getGlobalID());
                sendMessageWithConfirmation(new AddressedMessage(newChildMessage, parentInetSocketAddress));
            }

            while (!Thread.currentThread().isInterrupted()) {
                ReceivedMessage baseMessage = receivedMessages.take();

                boolean isMessageAlreadyHandled = false;
                for (BaseMessage currentMessage : handledMessages) {
                    if (currentMessage instanceof ConfirmMessage && baseMessage.getBaseMessage() instanceof ConfirmMessage) {
                        if (((ConfirmMessage) currentMessage).getConfirmForMessageID() == ((ConfirmMessage) baseMessage.getBaseMessage()).getConfirmForMessageID()) {
                            isMessageAlreadyHandled = true;
                        }
                    } else {
                        if (currentMessage.getGlobalID() == baseMessage.getGlobalId()) {
                            isMessageAlreadyHandled = true;
                        }
                    }
                }

                if (!isMessageAlreadyHandled) {
                    handledMessages.add(baseMessage.getBaseMessage());
                    baseMessage.process(this);
                } else {
                    System.out.println("Received Message is already handled! " + baseMessage.getInetSocketAddress().getHostName() + ":" + baseMessage.getInetSocketAddress().getPort());
                    sendMessageWithoutConfirmation(new AddressedMessage(new ConfirmMessage(globalIDGenerator.getGlobalID(), baseMessage.getGlobalId()), baseMessage.getInetSocketAddress()));
                }
            }
        } catch (InterruptedException e) {
            logger.info("The thread was interrupted");
        }
    }

    private boolean isRoot() {
        return null == parentInetSocketAddress;
    }

    private void sendMessageWithoutConfirmation(AddressedMessage addressedMessage) {
        messagesToSend.add(addressedMessage);
    }
    private void sendMessageWithConfirmation(AddressedMessage addressedMessage) {
        messagesToSend.add(addressedMessage);
        notConfirmedMessages.add(new NotConfirmedAddressedMessage(addressedMessage, System.currentTimeMillis()));
    }

    private void sendMessageWithEventOnConfirmation(AddressedMessage addressMessage, Runnable runnable) {
        messagesToSend.add(addressMessage);
        NotConfirmedAddressedMessage notConfirmedAddressMessage = new NotConfirmedAddressedMessage(addressMessage, System.currentTimeMillis());
        notConfirmedAddressMessage.setOnConfirmed(runnable);

        notConfirmedMessages.add(notConfirmedAddressMessage);
    }

    public void handleReceivedTextMessage(ReceivedTextMessage textMessage) {
        if (isDying) {
            return;
        }

        logger.info("New text message: " + textMessage.getReceivedMessage().getText());
        System.out.println(textMessage.getReceivedMessage().getText());

        if (!textMessage.getInetSocketAddress().equals(parentInetSocketAddress) && parentInetSocketAddress != null) {
            sendMessageWithConfirmation(new AddressedMessage(textMessage.getReceivedMessage(), parentInetSocketAddress));
        }

        for (InetSocketAddress inetSocketAddress : children) {
            if (!inetSocketAddress.equals(textMessage.getInetSocketAddress())) {
                sendMessageWithConfirmation(new AddressedMessage(textMessage.getReceivedMessage(), inetSocketAddress));
            }
        }

        ConfirmMessage confirmMessage = new ConfirmMessage(globalIDGenerator.getGlobalID(), textMessage.getReceivedMessage().getGlobalID());
        sendMessageWithoutConfirmation(new AddressedMessage(confirmMessage, textMessage.getInetSocketAddress()));
    }

    public void handleReceivedConfirmMessage(ReceivedConfirmMessage confirmMessage) {
        logger.info("Handle received confirm message");
        Iterator<NotConfirmedAddressedMessage> addressedMessageIterator = notConfirmedMessages.iterator();

        System.out.println("Received confirm message");

        while(addressedMessageIterator.hasNext()) {
            NotConfirmedAddressedMessage notConfirmedAddressMessage = addressedMessageIterator.next();
            if (confirmMessage.getConfirmMessage().isConfirmForThisMessage(notConfirmedAddressMessage.getAddressedMessage().getBaseMessage())) {
                notConfirmedAddressMessage.run();
                addressedMessageIterator.remove();
            }
        }
    }

    public void handleReceivedNotChildMessage(ReceivedNotChildMessage notChildMessage) {
        if (isDying) {
            return;
        }

        System.out.println("One of children is dead");
        logger.info("Handle received not child message");
        Iterator<InetSocketAddress> inetSocketAddressIterator = children.iterator();

        while (inetSocketAddressIterator.hasNext()) {
            if (inetSocketAddressIterator.next().equals(notChildMessage.getInetSocketAddress())) {
                inetSocketAddressIterator.remove();
            }
        }

        Iterator<NotConfirmedAddressedMessage> iter = notConfirmedMessages.iterator();

        while(iter.hasNext()) {
            if (iter.next().getAddressedMessage().getReceiverAddress().equals(notChildMessage.getInetSocketAddress())) {
                iter.remove();
            }
        }

        ConfirmMessage confirmMessage = new ConfirmMessage(globalIDGenerator.getGlobalID(), notChildMessage.getNotChildMessage().getGlobalID());
        AddressedMessage addressedMessage = new AddressedMessage(confirmMessage, notChildMessage.getInetSocketAddress());

        sendMessageWithoutConfirmation(addressedMessage);
        System.out.println("Sending confirmation");
    }

    public void handleReceivedNewParentMessage(ReceivedNewParentMessage newParentMessage) {
        if (isDying) {
            return;
        }

        Iterator<AddressedMessage> iter = messagesToSend.iterator();

        while(iter.hasNext()) {
            if (iter.next().getReceiverAddress().equals(parentInetSocketAddress)) {
                iter.remove();
            }
        }

        Iterator<NotConfirmedAddressedMessage> iter1 = notConfirmedMessages.iterator();

        while(iter1.hasNext()) {
            if (iter1.next().getAddressedMessage().getReceiverAddress().equals(parentInetSocketAddress)) {
                iter1.remove();
            }
        }

        ConfirmMessage confirmMessage = new ConfirmMessage(globalIDGenerator.getGlobalID(), newParentMessage.getNewParentMessage().getGlobalID());
        AddressedMessage addressedMessage = new AddressedMessage(confirmMessage, newParentMessage.getInetSocketAddress());

        sendMessageWithoutConfirmation(addressedMessage);

        System.out.println("New parent");

        if (!newParentMessage.getNewParentMessage().getNewParentIP().equals("null")) {
            parentInetSocketAddress = new InetSocketAddress(newParentMessage.getNewParentMessage().getNewParentIP(), newParentMessage.getNewParentMessage().getPort());
            sendMessageWithConfirmation(new AddressedMessage(new NewChildMessage(globalIDGenerator.getGlobalID()), parentInetSocketAddress));
        } else {
            parentInetSocketAddress = null;
        }
    }

    public void handleNewChildMessage(ReceivedNewChildMessage newChildMessage) {
        if (isDying) {
            return;
        }

        System.out.println("New child");
        children.add(newChildMessage.getInetSocketAddress());

        ConfirmMessage confirmMessage = new ConfirmMessage(globalIDGenerator.getGlobalID(), newChildMessage.getNewChildMessage().getGlobalID());
        AddressedMessage addressedMessage = new AddressedMessage(confirmMessage, newChildMessage.getInetSocketAddress());

        sendMessageWithoutConfirmation(addressedMessage);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            logger.info ("New message from console was received: " + arg);

            if (null != parentInetSocketAddress) {
                sendMessageWithConfirmation(new AddressedMessage(new TextMessage(globalIDGenerator.getGlobalID(), name + ": " + arg), parentInetSocketAddress));
            }

            for (InetSocketAddress inetSocketAddress : children) {
                sendMessageWithConfirmation(new AddressedMessage(new TextMessage(globalIDGenerator.getGlobalID(), name + ": " + arg), inetSocketAddress));
            }
        }
    }

    private void stopAllThreads(Thread nodeThread) {
        logger.info("Stop all threads");

        resenderThread.interrupt();
        senderThread.interrupt();
        receiverThread.interrupt();
        nodeThread.interrupt();


        datagramSocket.close();

        try {
            receiverThread.join();
            senderThread.join();
            receiverThread.join();
            nodeThread.join();
        } catch (InterruptedException e) {
            logger.info(e.getMessage());
        }

        logger.error("All node's subthreads are stopped");
    }

    private void deleteChild(InetSocketAddress inetSocketAddress) {
        Iterator<InetSocketAddress> inetSocketIterator = children.iterator();
        while (inetSocketIterator.hasNext()) {
            InetSocketAddress currentInetSocketAddress = inetSocketIterator.next();
            if (currentInetSocketAddress.getHostName().equals(inetSocketAddress.getHostName()) && currentInetSocketAddress.getPort() == inetSocketAddress.getPort()) {
                inetSocketIterator.remove();
            }
        }
    }

    public void stop(Thread nodeThread) {

        if (!isDying) {
            isDying = true;

            messagesToSend.clear();
            receivedMessages.clear();
            notConfirmedMessages.clear();

            if (isRoot()) {
                if (!children.isEmpty()) {
                    Iterator<InetSocketAddress> inetSocketAddressIterator = children.iterator();

                    InetSocketAddress newRootAddress = inetSocketAddressIterator.next();

                    sendMessageWithEventOnConfirmation(new AddressedMessage(new NewParentMessage(globalIDGenerator.getGlobalID(), null, 0), newRootAddress), () -> {
                        if (!children.isEmpty()) {
                            while (inetSocketAddressIterator.hasNext()) {
                                InetSocketAddress inetSocketAddress = inetSocketAddressIterator.next();
                                sendMessageWithEventOnConfirmation(new AddressedMessage(new NewParentMessage(globalIDGenerator.getGlobalID(), newRootAddress.getHostName(), newRootAddress.getPort()), inetSocketAddress), () -> {
                                    deleteChild(inetSocketAddress);

                                    if (children.isEmpty()) {
                                        stopAllThreads(nodeThread);
                                    }
                                });
                            }
                        } else {
                            stopAllThreads(nodeThread);
                        }
                    });
                } else {
                    stopAllThreads(nodeThread);
                }
            } else {
                sendMessageWithEventOnConfirmation(new AddressedMessage(new NotChildMessage(globalIDGenerator.getGlobalID()), parentInetSocketAddress), () -> {
                    if (children.isEmpty()) {
                        stopAllThreads(nodeThread);
                    } else {

                        System.out.println("Confirmation from parent is received!");

                        for (InetSocketAddress inetSocketAddress : children) {
                            sendMessageWithEventOnConfirmation(new AddressedMessage(new NewParentMessage(globalIDGenerator.getGlobalID(), parentInetSocketAddress.getHostName(), parentInetSocketAddress.getPort()), inetSocketAddress), () -> {
                                Iterator<InetSocketAddress> inetSocketIterator = children.iterator();

                                while (inetSocketIterator.hasNext()) {
                                    InetSocketAddress inetSocketAddress1 = inetSocketIterator.next();

                                    if (inetSocketAddress1.equals(inetSocketAddress)) {
                                        deleteChild(inetSocketAddress);
                                    }
                                }

                                if (children.isEmpty()) {
                                    stopAllThreads(nodeThread);
                                }
                            });
                        }
                    }
                });
            }

        }
    }
    }
