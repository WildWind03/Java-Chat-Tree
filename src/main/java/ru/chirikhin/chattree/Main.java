package ru.chirikhin.chattree;

import org.apache.log4j.Logger;
import ru.chirikhin.chattree.controller.TreeNodeController;

public class Main {
    private final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info ("The app started");

        try {
            TreeNodeController treeNodeController = new TreeNodeController(args);

            logger.info("Tree Node Controller was created");
            treeNodeController.start();

            Runtime.getRuntime().addShutdownHook(new Thread(treeNodeController::stop, "Shutdown Handler Thread"));
            logger.info("Signal handler was set");

        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
    }
}
