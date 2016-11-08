package ru.chirikhin.chattree;

import org.apache.log4j.Logger;
import ru.chirikhin.chattree.controller.TreeNodeController;

public class Main {
    private final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            TreeNodeController treeNodeController = new TreeNodeController(args);
            treeNodeController.start();

            Runtime.getRuntime().addShutdownHook(new Thread(treeNodeController::stop));

        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
    }
}
