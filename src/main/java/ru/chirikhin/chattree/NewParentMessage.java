package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

import java.nio.charset.Charset;

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

    @Override
    byte[] bytes() {
        String serializedMessage = "" + MessageType.NEW_PARENT + SEPARATOR_CHAR + getGlobalID() + SEPARATOR_CHAR + getNewParentIP();
        return serializedMessage.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    void process(Node node) {
        node.handleNewParentMessage(this);
    }
}
