package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Node implements Runnable {
    private static final Logger logger = Logger.getLogger(Node.class.toString());
    private static final int DATAGRAM_SOCKET_TIMEOUT = 1000;
    private static final int SIZE_OF_MESSAGE_QUEUE = 3000;
    private static final int MAX_COUNT_OF_NOT_CONFIRMED_MESSAGES = 3000;
    private static final int CAPACITY_OF_RECEIVE_QUEUE = 1000;
    private static final int CAPACITY_OF_TO_SEND_QUEUE = 1000;

    private final DatagramSocket datagramSocket;
    private final String name;
    private final int percentOfLose;
    private final InetSocketAddress parentInetSocketAddress;
    private final GlobalIDGenerator globalIDGenerator;
    private final Thread senderThread;
    private final Thread receiverThread;

    private final BlockingQueue<BaseMessage> receivedMessages = new LinkedBlockingQueue<>(CAPACITY_OF_RECEIVE_QUEUE);
    private final BlockingQueue<AddressedMessage> messagesToSend = new LinkedBlockingQueue<>(CAPACITY_OF_TO_SEND_QUEUE);

    private final CycleLinkedList<Long> notConfirmedMessages = new CycleLinkedList<>(MAX_COUNT_OF_NOT_CONFIRMED_MESSAGES);
    private final LinkedList<InetSocketAddress> children = new LinkedList<>();


    public Node (String name, int percentOfLose, int port, InetSocketAddress parentInetSocketAddress) throws SocketException, UnknownHostException {
        this.datagramSocket = new DatagramSocket(port);
        this.datagramSocket.setSoTimeout(DATAGRAM_SOCKET_TIMEOUT);
        this.name = name;
        this.percentOfLose = percentOfLose;
        this.parentInetSocketAddress = parentInetSocketAddress;
        this.globalIDGenerator = new GlobalIDGenerator(port, Inet4Address.getLocalHost().getHostAddress());
        this.receiverThread = new Thread(new MessageReceiver(receivedMessages, datagramSocket));
        this.senderThread = new Thread(new MessageSender(messagesToSend, datagramSocket));
    }

    @Override
    public void run() {
        BaseMessage newChildMessage = new NewChildMessage(globalIDGenerator.getGlobalID());

        try {
            sendMessage(new AddressedMessage(newChildMessage, parentInetSocketAddress));

            while (!Thread.currentThread().isInterrupted()) {
                BaseMessage baseMessage = receivedMessages.take();
                baseMessage.process(this);
            }
        } catch (InterruptedException e) {
            logger.info("The thread was interrupted");
        }
    }

    private void sendMessage(AddressedMessage addressedMessage) throws InterruptedException {
        messagesToSend.put(addressedMessage);
        notConfirmedMessages.push(addressedMessage.getBaseMessage().getGlobalID());
    }

    private void sendMessageToAllNeighbours(BaseMessage baseMessage) throws InterruptedException {
        sendMessage(new AddressedMessage(baseMessage, parentInetSocketAddress));

        for (InetSocketAddress child : children) {
            sendMessage(new AddressedMessage(baseMessage, child));
        }
    }

    public void handleTextMessage(TextMessage textMessage) {

    }

    public void handleConfirmMessage(ConfirmMessage confirmMessage) {
    }

    public void handleNotChildMessage(NotChildMessage notChildMessage) {

    }

    public void handleNewParentMessage(NewParentMessage newParentMessage) {

    }

    public void handleNewChildMessage(NewChildMessage newChildMessage) {
        long id = newChildMessage.getGlobalID(); //порт в id генератор!!!

        children.add(new InetSocketAddress(ip, port));
        notConfirmedMessages.add(id);
    }

}
