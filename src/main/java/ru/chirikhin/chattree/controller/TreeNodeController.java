package ru.chirikhin.chattree.controller;

import org.apache.log4j.Logger;
import ru.chirikhin.chattree.model.Node;
import ru.chirikhin.chattree.view.ConsoleInputView;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TreeNodeController {
    private final static Logger logger = Logger.getLogger(TreeNodeController.class);

    private final String name;
    private final int percentOfLoss;
    private final int port;
    private final InetSocketAddress parentInetSocketAddress;
    private final Node node;
    private final ConsoleInputView consoleInputView;

    private final Thread nodeThread;
    private final Thread consoleInputViewThread;

    public TreeNodeController(String[] args) throws SocketException, UnknownHostException {
        ArgParser argParser = new ArgParser(args);

        this.name = argParser.getName();
        this.percentOfLoss = argParser.getPercentOfLoss();
        this.port = argParser.getPort();
        this.parentInetSocketAddress = argParser.getParentInetSocketAddress();

        logger.info("Tree node controller was created");
        logger.info("Name: " + name);
        logger.info("Percent of Loss: " + percentOfLoss);
        logger.info("Port: " + port);
        logger.info("Parent Inet Socket Address: " + parentInetSocketAddress);

        consoleInputView = new ConsoleInputView();

        node = new Node(name, percentOfLoss, port, parentInetSocketAddress);
        consoleInputView.addObserver(node);

        nodeThread = new Thread(node, "Node Thread");
        consoleInputViewThread = new Thread(consoleInputView, "Console Input View Thread");
    }

    public void start() throws SocketException, UnknownHostException {
        nodeThread.start();
        consoleInputViewThread.start();

        logger.info("Node thread and consoleInputViewThread started!");
    }

    public void stop() {
        logger.info("Interrupt");

        consoleInputViewThread.interrupt();
        nodeThread.interrupt();

        node.stop();
        consoleInputView.stop();

        try {
            nodeThread.join();
            consoleInputViewThread.join();
        } catch (InterruptedException e) {
            logger.info(e.getMessage());
        }

        logger.info("Before exit");
    }
}
