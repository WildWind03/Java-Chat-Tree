package ru.chirikhin.chattree;

import java.net.InetSocketAddress;

public class ReceivedTextMessage extends ReceivedMessage {
    private final ReceivedMessage receivedMessage;
    private final InetSocketAddress inetSocketAddress;

    public ReceivedTextMessage(ReceivedMessage receivedMessage, InetSocketAddress inetSocketAddress) {
        this.receivedMessage = receivedMessage;
        this.inetSocketAddress = inetSocketAddress;
    }

    public ReceivedMessage getReceivedMessage() {
        return receivedMessage;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }


    @Override
    void process(Node node) {
        node.handleReceivedTextMessage(this);
    }
}
