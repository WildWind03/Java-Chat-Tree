package ru.chirikhin.chattree.model;

import java.nio.charset.Charset;

public class NewChildMessage extends BaseMessage {
    public NewChildMessage(long globalID) {
        super(globalID);
    }

    @Override
    byte[] bytes() {
        String serializedMessage = MessageType.NEW_CHILD.returnValue() + SEPARATOR_CHAR + getGlobalID() + SEPARATOR_CHAR;
        return serializedMessage.getBytes(Charset.forName("UTF-8"));
    }
}
