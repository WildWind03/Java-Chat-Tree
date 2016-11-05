package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

public class GlobalIDGenerator {
    private static final Logger logger = Logger.getLogger(GlobalIDGenerator.class.getName());

    private final String ip;

    private long currentLocalID = 256;

    public GlobalIDGenerator(String ip) {
        this.ip = ip;
    }

    public long getGlobalID() {
        String globalIDStr = (currentLocalID++) + ip.replaceAll("[.]", "");
        return Long.parseLong(globalIDStr);
    }
}
