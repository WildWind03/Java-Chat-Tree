package ru.chirikhin.chattree.model;

import java.net.InetSocketAddress;

public class ReceivedNotChildMessage extends ReceivedMessage {

    public ReceivedNotChildMessage(NotChildMessage notChildMessage, InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress, notChildMessage);
    }

    public NotChildMessage getNotChildMessage() {
        return (NotChildMessage) getBaseMessage();
    }

    @Override
    void process(Node node) {
        node.handleReceivedNotChildMessage(this);
    }
}
