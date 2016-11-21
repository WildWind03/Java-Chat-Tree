package ru.chirikhin.chattree.model;

import java.net.InetSocketAddress;

public class ReceivedNewParentMessage extends ReceivedMessage {

    public ReceivedNewParentMessage(NewParentMessage newParentMessage, InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress, newParentMessage);
    }

    public NewParentMessage getNewParentMessage() {
        return (NewParentMessage) getBaseMessage();
    }

    @Override
    void process(Node node) {
        node.handleReceivedNewParentMessage(this);
    }
}
