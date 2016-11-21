package ru.chirikhin.chattree.model;

import java.net.InetSocketAddress;

public class ReceivedConfirmMessage extends ReceivedMessage {

    public ReceivedConfirmMessage(ConfirmMessage confirmMessage, InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress, confirmMessage);
    }

    public ConfirmMessage getConfirmMessage() {
        return (ConfirmMessage) getBaseMessage();
    }

    @Override
    void process(Node node) {
        node.handleReceivedConfirmMessage(this);
    }
}
