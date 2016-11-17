package ru.chirikhin.chattree.model;

import java.nio.charset.Charset;

public class ConfirmMessage extends BaseMessage {
    private final long confirmForMessageID;

    public ConfirmMessage(long globalID, long confirmForMessageID) {
        super(globalID);
        this.confirmForMessageID = confirmForMessageID;
    }

    public boolean isConfirmForThisMessage(BaseMessage baseMessage) {
        return baseMessage.getGlobalID() == confirmForMessageID;
    }

    @Override
    byte[] bytes() {
        String serializedMessage = MessageType.CONFIRM.returnValue() + SEPARATOR_CHAR + getGlobalID() + SEPARATOR_CHAR + confirmForMessageID + SEPARATOR_CHAR;
        return serializedMessage.getBytes(Charset.forName("UTF-8"));
    }
}
