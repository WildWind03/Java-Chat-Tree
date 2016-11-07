package ru.chirikhin.chattree;

import java.net.InetSocketAddress;

public class ReceivedNewParentMessage extends ReceivedMessage {
    private final NewParentMessage newParentMessage;
    private final InetSocketAddress inetSocketAddress;

    public ReceivedNewParentMessage(NewParentMessage newParentMessage, InetSocketAddress inetSocketAddress) {
        this.newParentMessage = newParentMessage;
        this.inetSocketAddress = inetSocketAddress;
    }

    public NewParentMessage getNewParentMessage() {
        return newParentMessage;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    @Override
    void process(Node node) {
        node.handleReceivedNewParentMessage(this);
    }
}
