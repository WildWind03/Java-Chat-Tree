package ru.chirikhin.chattree.model;

import java.nio.charset.Charset;

public class NewChildMessage extends BaseMessage {
    public NewChildMessage(long globalID) {
        super(globalID);
    }

    @Override
    byte[] bytes() {
        String serializedMessage = "" + MessageType.NEW_CHILD + SEPARATOR_CHAR + getGlobalID();
        return serializedMessage.getBytes(Charset.forName("UTF-8"));
    }
}
