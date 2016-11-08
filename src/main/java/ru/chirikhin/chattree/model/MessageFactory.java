package ru.chirikhin.chattree.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MessageFactory {
    private static final Logger logger = Logger.getLogger(MessageFactory.class.getName());
    private static final char SEPARATOR_CHAR = '_';

    private MessageFactory() {

    }

    public static ReceivedMessage createMessage(DatagramPacket datagramPacket) {
        byte[] bytes = datagramPacket.getData();
        String string = new String(bytes, Charset.forName("UTF-8"));
        String[] strings = StringUtils.split(string, SEPARATOR_CHAR);
        int messageType = Integer.parseInt(strings[0]);

        MessageType messageTypeEnum = MessageType.values()[messageType];

        ReceivedMessage message;
        long globalID = Long.parseLong(strings[1]);

        switch(messageTypeEnum) {
            case CONFIRM:
                message = new ReceivedConfirmMessage(
                        new ConfirmMessage(globalID, Long.parseLong(strings[1])),
                        new InetSocketAddress(datagramPacket.getAddress().getHostAddress(), datagramPacket.getPort()));
                break;

            case NEW_CHILD:
                message = new ReceivedNewChildMessage(
                        new NewChildMessage(globalID),
                        new InetSocketAddress(datagramPacket.getAddress().getHostAddress(), datagramPacket.getPort()));
                break;

            case NEW_PARENT:
                message = new ReceivedNewParentMessage(
                        new NewParentMessage(globalID, strings[1], Integer.parseInt(strings[2])),
                        new InetSocketAddress(datagramPacket.getAddress().getHostAddress(), datagramPacket.getPort()));
                break;

            case NOT_CHILD:
                message = new ReceivedNotChildMessage(
                        new NotChildMessage(globalID),
                        new InetSocketAddress(datagramPacket.getAddress().getHostAddress(), datagramPacket.getPort()));
                break;

            case TEXT:
                message = new ReceivedTextMessage(
                        new TextMessage(globalID, strings[1]),
                        new InetSocketAddress(datagramPacket.getAddress().getHostAddress(), datagramPacket.getPort()));
                break;

            default:
                throw new IllegalArgumentException("Transferred bytes doesn't match to any existing messages type");
        }

        return message;
    }
}
