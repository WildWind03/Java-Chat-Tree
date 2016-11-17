package ru.chirikhin.chattree.model;

import org.apache.log4j.Logger;
import ru.chirikhin.cyclelist.CycleLinkedList;

import java.net.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Node implements Runnable, Observer {
    private static final Logger logger = Logger.getLogger(Node.class.toString());
    private static final int MAX_COUNT_OF_NOT_CONFIRMED_MESSAGES = 3000;
    private static final int CAPACITY_OF_RECEIVE_QUEUE = 1000;
    private static final int CAPACITY_OF_TO_SEND_QUEUE = 1000;

    private final DatagramSocket datagramSocket;
    private final String name;
    private final GlobalIDGenerator globalIDGenerator;
    private final Thread senderThread;
    private final Thread receiverThread;
    private final Thread resenderThread;

    private InetSocketAddress parentInetSocketAddress;

    private final BlockingQueue<ReceivedMessage> receivedMessages = new LinkedBlockingQueue<>(CAPACITY_OF_RECEIVE_QUEUE);
    private final BlockingQueue<AddressedMessage> messagesToSend = new LinkedBlockingQueue<>(CAPACITY_OF_TO_SEND_QUEUE);

    private final CycleLinkedList<NotConfirmedAddressedMessage> notConfirmedMessages = new CycleLinkedList<>(MAX_COUNT_OF_NOT_CONFIRMED_MESSAGES);
    private final LinkedList<InetSocketAddress> children = new LinkedList<>();


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
                baseMessage.process(this);
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

    private void sendMessageToAllNeighbours(BaseMessage baseMessage) {
        if (null != parentInetSocketAddress) {
            sendMessageWithConfirmation(new AddressedMessage(baseMessage, parentInetSocketAddress));
        }

        for (InetSocketAddress child : children) {
            sendMessageWithConfirmation(new AddressedMessage(baseMessage, child));
        }
    }

    public void handleReceivedTextMessage(ReceivedTextMessage textMessage) {
        logger.info("New text message: " + textMessage.getReceivedMessage().getText());
        System.out.println(textMessage.getReceivedMessage().getText());

        if (!textMessage.getInetSocketAddress().equals(parentInetSocketAddress)) {
            sendMessageWithoutConfirmation(new AddressedMessage(textMessage.getReceivedMessage(), parentInetSocketAddress));
        }

        for (InetSocketAddress inetSocketAddress : children) {
            if (!inetSocketAddress.equals(textMessage.getInetSocketAddress())) {
                sendMessageWithoutConfirmation(new AddressedMessage(textMessage.getReceivedMessage(), inetSocketAddress));
            }
        }
    }

    public void handleReceivedConfirmMessage(ReceivedConfirmMessage confirmMessage) {
        logger.info("Handle received confirm message");
        Iterator<NotConfirmedAddressedMessage> addressedMessageIterator = notConfirmedMessages.iterator();

        while(addressedMessageIterator.hasNext()) {
            if (confirmMessage.getConfirmMessage().isConfirmForThisMessage(addressedMessageIterator.next().getAddressedMessage().getBaseMessage())) {
                addressedMessageIterator.remove();
            }
        }
    }

    public void handleReceivedNotChildMessage(ReceivedNotChildMessage notChildMessage) {
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
    }

    public void handleReceivedNewParentMessage(ReceivedNewParentMessage newParentMessage) {
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
        AddressedMessage addressedMessage = new AddressedMessage(confirmMessage, new InetSocketAddress(newParentMessage.getNewParentMessage().getNewParentIP(), newParentMessage.getNewParentMessage().getPort()));

        sendMessageWithoutConfirmation(addressedMessage);

        parentInetSocketAddress = new InetSocketAddress(newParentMessage.getNewParentMessage().getNewParentIP(), newParentMessage.getNewParentMessage().getPort());
    }

    public void handleNewChildMessage(ReceivedNewChildMessage newChildMessage) {
        children.add(newChildMessage.getInetSocketAddress());

        ConfirmMessage confirmMessage = new ConfirmMessage(globalIDGenerator.getGlobalID(), newChildMessage.getNewChildMessage().getGlobalID());
        AddressedMessage addressedMessage = new AddressedMessage(confirmMessage, newChildMessage.getInetSocketAddress());

        sendMessageWithoutConfirmation(addressedMessage);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            logger.info ("New message from console was received: " + arg);
            sendMessageToAllNeighbours(new TextMessage(globalIDGenerator.getGlobalID(), name + ": " + arg));
        }
    }

    public void stop() {
        resenderThread.interrupt();
        senderThread.interrupt();
        receiverThread.interrupt();

        try {
            receiverThread.join();
            senderThread.join();
            receiverThread.join();
        } catch (InterruptedException e) {
            logger.info(e.getMessage());
        }

        logger.error("All node's subthreads are stopped");
    }
}
