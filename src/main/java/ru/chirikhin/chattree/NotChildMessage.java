package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

import java.nio.charset.Charset;

public class NotChildMessage extends BaseMessage {
    private static final Logger logger = Logger.getLogger(NotChildMessage.class.getName());

    public NotChildMessage(long globalID) {
        super(globalID);
    }

    @Override
    byte[] bytes() {
        String serializedMessage = "" + MessageType.NOT_CHILD + SEPARATOR_CHAR + getGlobalID();
        return serializedMessage.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    void process(Node node) {
        node.handleNotChildMessage(this);
    }
}
