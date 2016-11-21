package ru.chirikhin.chattree.model;

import java.net.InetSocketAddress;

public class ReceivedNewChildMessage extends ReceivedMessage {
    public ReceivedNewChildMessage(NewChildMessage newChildMessage, InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress, newChildMessage);
    }

    public NewChildMessage getNewChildMessage() {
        return (NewChildMessage) getBaseMessage();
    }

    @Override
    void process(Node node) {
        node.handleNewChildMessage(this);
    }


}
