package ru.chirikhin.chattree.controller;

public class Main {
    public static final int COUNT_OF_ARGS_FOR_CHILD_NODE = 3;
    public static final int COUNT_OF_ARGS_FOR_PARENT_NODE = 5;

    public static void main(String[] args) {
        ArgParser argParser = new ArgParser(args);

        String name = argParser.getName();
        int percentOfLoose = argParser.getPercentOfLoss();
        int port = argParser.getPort();

        if (argParser.isRoot()) {

        } else {

        }
    }
}
