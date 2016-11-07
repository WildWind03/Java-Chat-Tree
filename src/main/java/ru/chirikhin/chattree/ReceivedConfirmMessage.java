package ru.chirikhin.chattree;

import java.net.InetSocketAddress;

public class ReceivedConfirmMessage extends ReceivedMessage {
    private final ConfirmMessage confirmMessage;
    private final InetSocketAddress inetSocketAddress;

    public ReceivedConfirmMessage(ConfirmMessage confirmMessage, InetSocketAddress inetSocketAddress) {
        this.confirmMessage = confirmMessage;
        this.inetSocketAddress = inetSocketAddress;
    }

    public ConfirmMessage getConfirmMessage() {
        return confirmMessage;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    @Override
    void process(Node node) {
        node.handleReceivedConfirmMessage(this);
    }
}
