package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

public class NewParentMessage extends BaseMessage {
    private static final Logger logger = Logger.getLogger(NewParentMessage.class.getName());

    private final String newParentIP;

    public NewParentMessage(long globalID, String ip) {
        super(globalID);

        this.newParentIP = ip;
    }

    public String getNewParentIP() {
        return newParentIP;
    }
}
