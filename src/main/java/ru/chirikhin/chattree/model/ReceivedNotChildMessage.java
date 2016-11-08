package ru.chirikhin.chattree.model;

import java.net.InetSocketAddress;

public class ReceivedNotChildMessage extends ReceivedMessage {
    private final NotChildMessage notChildMessage;
    private final InetSocketAddress inetSocketAddress;

    public ReceivedNotChildMessage(NotChildMessage notChildMessage, InetSocketAddress inetSocketAddress) {
        this.notChildMessage = notChildMessage;
        this.inetSocketAddress = inetSocketAddress;
    }

    public NotChildMessage getNotChildMessage() {
        return notChildMessage;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    @Override
    void process(Node node) {
        node.handleReceivedNotChildMessage(this);
    }
}
