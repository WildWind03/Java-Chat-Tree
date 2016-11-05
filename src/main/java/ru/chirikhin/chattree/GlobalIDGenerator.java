package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

public class GlobalIDGenerator {
    private static final Logger logger = Logger.getLogger(GlobalIDGenerator.class.getName());

    private final String ip;
    private final int port;

    private long currentLocalID = 256;

    public GlobalIDGenerator(int port, String ip) {
        this.ip = ip;
        this.port = port;
    }

    public long getGlobalID() {
        String globalIDStr = (currentLocalID++) + ip.replaceAll("[.]", "") + port;
        logger.info("Next global is is " + globalIDStr);
        return Long.parseLong(globalIDStr);
    }
}
