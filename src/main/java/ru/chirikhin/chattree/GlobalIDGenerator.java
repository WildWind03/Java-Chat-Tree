package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

public class GlobalIDGenerator {
    private static final Logger logger = Logger.getLogger(GlobalIDGenerator.class.getName());

    private final int port;
    private final String ip;

    private long localID = 0;

    public GlobalIDGenerator(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public long getGlobalID() {
        String globalIDStr = (localID++) + (port + ip.replaceAll("[.]", ""));
        return Long.parseLong(globalIDStr);
    }
}
