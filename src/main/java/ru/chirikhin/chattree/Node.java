package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Node implements Runnable {
    private static final Logger logger = Logger.getLogger(Node.class.toString());
    private static final int SIZE_OF_DATAGRAM_PACKET = 2048;
    private static final int DATAGRAM_SOCKET_TIMEOUT = 1000;
    private static final int SIZE_OF_BLOCKING_QUEUE = 3000;

    private final DatagramSocket datagramSocket;
    private final String name;
    private final int percentOfLose;
    private final InetSocketAddress parentInetSocketAddress;
    private final BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(SIZE_OF_BLOCKING_QUEUE);

    private long counter = 0;

    public Node (String name, int percentOfLose, int port, InetSocketAddress parentInetSocketAddress) throws SocketException {
        this.datagramSocket = new DatagramSocket(port);
        this.datagramSocket.setSoTimeout(DATAGRAM_SOCKET_TIMEOUT);
        this.name = name;
        this.percentOfLose = percentOfLose;
        this.parentInetSocketAddress = parentInetSocketAddress;
    }

    @Override
    public void run() {
        NewChildMessage newChildMessage;

        try {
            newChildMessage = new NewChildMessage(counter++, datagramSocket.getPort(), Inet4Address.getLocalHost().getHostAddress());
            datagramSocket.send(new DatagramPacket(newChildMessage.bytes(), newChildMessage.bytes().length, parentInetSocketAddress));
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
            return;
        } catch (IOException e) {
            logger.error("Can not send info to parent");
            return;
        }

        DatagramPacket datagramPacket = new DatagramPacket(new byte[SIZE_OF_DATAGRAM_PACKET], SIZE_OF_DATAGRAM_PACKET);
        String message;

        while(true) {
            try {
                datagramSocket.receive(datagramPacket);

            } catch (SocketTimeoutException e) {
                logger.info ("No received message");
            } catch (IOException e) {
                logger.info(e.getMessage());
            }

            if (null != (message = blockingQueue.poll())) {
                sendMessageToAllNeighbours(message);
            }
        }
    }

    private void sendMessageToAllNeighbours(String string) {

    }

}
