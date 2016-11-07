package ru.chirikhin.chattree;

import java.net.InetSocketAddress;

public class ReceivedNewChildMessage extends ReceivedMessage {
    private final NewChildMessage newChildMessage;
    private final InetSocketAddress inetSocketAddress;

    public ReceivedNewChildMessage(NewChildMessage newChildMessage, InetSocketAddress inetSocketAddress) {
        this.newChildMessage = newChildMessage;
        this.inetSocketAddress = inetSocketAddress;
    }

    public NewChildMessage getNewChildMessage() {
        return newChildMessage;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    @Override
    void process(Node node) {
        node.handleNewChildMessage(this);
    }


}
