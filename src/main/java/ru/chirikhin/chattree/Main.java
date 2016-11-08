package ru.chirikhin.chattree;

import org.apache.log4j.Logger;
import ru.chirikhin.chattree.controller.TreeNodeController;

public class Main {
    private final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        TreeNodeController treeNodeController = new TreeNodeController(args);
        try {
            treeNodeController.start();
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
    }
}
