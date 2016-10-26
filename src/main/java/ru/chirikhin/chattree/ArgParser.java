package ru.chirikhin.chattree;

import java.net.InetSocketAddress;

public class ArgParser {
    private final static int COUNT_OF_ARGS_FOR_CHILD = 5;
    private final static int COUNT_OF_ARGS_FOR_ROOT = 3;
    private final static int MAX_VALUE_FOR_PERCENT_OF_LOSS = 100;
    private final static int MIN_VALUE_FOR_PERCENT_OF_LOSS = 0;

    private final int percentOfLoss;
    private final String name;
    private final int port;
    private final boolean isRoot;
    private final InetSocketAddress parentInetSocketAddress;

    public ArgParser(String args[]) {
        if (null == args) {
            throw new IllegalArgumentException("It's impossible to create ArgParser with null arg");
        }

        if (COUNT_OF_ARGS_FOR_ROOT == args.length) {
            this.isRoot = true;
        } else {
            if (COUNT_OF_ARGS_FOR_CHILD == args.length) {
                this.isRoot = false;
            } else {
                throw new IllegalArgumentException("Count of args is " +
                        "not valid. You should use 5 args for a child node" +
                        " and 3 args for a root node");
            }
        }

        this.name = args[0];

        try {
            this.percentOfLoss = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The second parameter is not a number." +
                    " You should use a number from 0 to 100");
        }

        if (this.percentOfLoss < MIN_VALUE_FOR_PERCENT_OF_LOSS
                || this.percentOfLoss > MAX_VALUE_FOR_PERCENT_OF_LOSS) {
            throw new IllegalArgumentException("Percent of loss has to be from 0 to 100");
        }

        try {
            this.port = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The third parameter is not a number." +
                    " You should use a positive number");
        }

        if (this.port <= 0) {
            throw new IllegalArgumentException("Port must be positive");
        }

        if (isRoot()) {
            parentInetSocketAddress = null;
        } else {
            String parentIp = args[3];

            int parentPort = Integer.parseInt(args[4]);

            try {
                parentInetSocketAddress = new InetSocketAddress(parentIp, parentPort);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Port or hostname for parent node is not valid!", e);
            }
        }
    }

    public boolean isRoot() {
        return isRoot;
    }

    public int getPercentOfLoss() {
        return percentOfLoss;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public InetSocketAddress getParentInetSocketAddress() {
        return parentInetSocketAddress;
    }
}
