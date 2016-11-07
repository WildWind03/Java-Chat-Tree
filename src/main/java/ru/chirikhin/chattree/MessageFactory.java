package ru.chirikhin.chattree;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.net.DatagramPacket;
import java.nio.charset.Charset;

public class MessageFactory {
    private static final Logger logger = Logger.getLogger(MessageFactory.class.getName());
    private static final char SEPARATOR_CHAR = '_';

    private MessageFactory() {

    }

    public static BaseMessage createMessage(DatagramPacket datagramPacket) {
        byte[] bytes = datagramPacket.getData();
        String string = new String(bytes, Charset.forName("UTF-8"));
        String[] strings = StringUtils.split(string, SEPARATOR_CHAR);
        int messageType = Integer.parseInt(strings[0]);

        MessageType messageTypeEnum = MessageType.values()[messageType];

        BaseMessage message;
        long globalID = Long.parseLong(strings[1]);

        switch(messageTypeEnum) {
            case CONFIRM:
                message = new ConfirmMessage(globalID, Long.parseLong(strings[1]));
                break;

            case NEW_CHILD:
                message = new NewChildMessage(globalID);
                break;

            case NEW_PARENT:
                message = new NewParentMessage(globalID, strings[1]);
                break;

            case NOT_CHILD:
                message = new NotChildMessage(globalID);
                break;

            case TEXT:
                message = new TextMessage(globalID, strings[1]);
                break;

            default:
                throw new IllegalArgumentException("Transferred bytes doesn't match to any existing messages type");
        }

        return message;
    }
}
