package ru.chirikhin.chattree.model;

import java.net.InetSocketAddress;

public class ReceivedTextMessage extends ReceivedMessage {

    public ReceivedTextMessage(TextMessage textMessage, InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress, textMessage);
    }

    public TextMessage getReceivedMessage() {
        return (TextMessage) getBaseMessage();
    }


    @Override
    void process(Node node) {
        node.handleReceivedTextMessage(this);
    }
}
