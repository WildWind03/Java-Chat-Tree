package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

import java.nio.charset.Charset;

public class NewParentMessage extends BaseMessage {
    private static final Logger logger = Logger.getLogger(NewParentMessage.class.getName());

    private final String newParentIP;
    private final int port;

    public NewParentMessage(long globalID, String ip, int port) {
        super(globalID);

        this.newParentIP = ip;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getNewParentIP() {
        return newParentIP;
    }

    @Override
    byte[] bytes() {
        String serializedMessage = "" + MessageType.NEW_PARENT + SEPARATOR_CHAR + getGlobalID() + SEPARATOR_CHAR + getNewParentIP() + SEPARATOR_CHAR + port;
        return serializedMessage.getBytes(Charset.forName("UTF-8"));
    }
}
