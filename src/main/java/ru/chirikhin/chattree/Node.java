package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Node implements Runnable {
    private static final Logger logger = Logger.getLogger(Node.class.toString());

    private final DatagramSocket datagramSocket;
    private final String name;
    private final int percentOfLose;
    private final InetSocketAddress parentInetSocketAddress;

    public Node (String name, int percentOfLose, int port, InetSocketAddress parentInetSocketAddress) throws SocketException {
        this.datagramSocket = new DatagramSocket(port);
        this.name = name;
        this.percentOfLose = percentOfLose;
        this.parentInetSocketAddress = parentInetSocketAddress;
    }

    @Override
    public void run() {
        Integer example;
    }

    private void sendMessageToAllNeighbours() {

    }

}
