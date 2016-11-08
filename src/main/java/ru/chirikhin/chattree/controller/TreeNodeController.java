package ru.chirikhin.chattree.controller;

import com.sun.istack.internal.logging.Logger;
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

        consoleInputView = new ConsoleInputView();

        node = new Node(name, percentOfLoss, port, parentInetSocketAddress);
        consoleInputView.addObserver(node);

        nodeThread = new Thread(node);
        consoleInputViewThread = new Thread(consoleInputView);
    }

    public void start() throws SocketException, UnknownHostException {
        nodeThread.start();
        consoleInputViewThread.start();
    }

    public void stop() {
        nodeThread.interrupt();
        consoleInputViewThread.interrupt();

        try {
            nodeThread.join();
            consoleInputViewThread.join();
        } catch (InterruptedException e) {
            logger.info(e.getMessage());
        }
    }
}
