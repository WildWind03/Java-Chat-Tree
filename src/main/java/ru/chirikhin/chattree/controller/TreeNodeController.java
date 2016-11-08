package ru.chirikhin.chattree.controller;

import ru.chirikhin.chattree.model.Node;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TreeNodeController {
    private final String name;
    private final int percentOfLoss;
    private final int port;
    private final InetSocketAddress parentInetSocketAddress;

    public TreeNodeController(String[] args) {
        ArgParser argParser = new ArgParser(args);

        this.name = argParser.getName();
        this.percentOfLoss = argParser.getPercentOfLoss();
        this.port = argParser.getPort();
        this.parentInetSocketAddress = argParser.getParentInetSocketAddress();
    }

    public void start() throws SocketException, UnknownHostException {
        Node node = new Node(name, percentOfLoss, port, parentInetSocketAddress);
        Thread thread = new Thread(node);
        thread.start();
    }
}
