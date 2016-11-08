package ru.chirikhin.chattree.model;

import java.net.InetSocketAddress;

public class ReceivedTextMessage extends ReceivedMessage {
    private final TextMessage textMessage;
    private final InetSocketAddress inetSocketAddress;

    public ReceivedTextMessage(TextMessage textMessage, InetSocketAddress inetSocketAddress) {
        this.textMessage = textMessage;
        this.inetSocketAddress = inetSocketAddress;
    }

    public TextMessage getReceivedMessage() {
        return textMessage;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }


    @Override
    void process(Node node) {
        node.handleReceivedTextMessage(this);
    }
}
