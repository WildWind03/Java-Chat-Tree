package ru.chirikhin.chattree;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class MessageFactory {
    private static final Logger logger = Logger.getLogger(MessageFactory.class.getName());
    private static final char SEPARATOR_CHAR = '_';

    private MessageFactory() {

    }

    public static BaseMessage createMessage(byte[] bytes) {
        String string = new String(bytes, Charset.forName("UTF-8"));
        String[] strings = StringUtils.split(string, SEPARATOR_CHAR);
        int messageType = Integer.parseInt(strings[0]);

        MessageType messageTypeEnum = MessageType.values()[messageType];

        BaseMessage message;
        long globalID;

        switch(messageTypeEnum) {
            case CONFIRM:
                globalID = Long.parseLong(strings[1]);
                message = new ConfirmMessage(globalID);
                break;

            case NEW_CHILD:
                globalID = Long.parseLong(strings[1]);
                message = new NewChildMessage(globalID);
                break;

            case NEW_PARENT:
                break;

            case NOT_CHILD:
                break;

            case TEXT:
                break;

            default:
                throw new IllegalArgumentException("Transferred bytes doesn't match to any existing messages type");
        }

        return message;
    }
}
