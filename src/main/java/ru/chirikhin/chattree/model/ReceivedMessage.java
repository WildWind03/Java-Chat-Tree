package ru.chirikhin.chattree.model;

import java.net.InetSocketAddress;

public abstract class ReceivedMessage {
    private final InetSocketAddress inetSocketAddress;
    private final BaseMessage baseMessage;

    public ReceivedMessage(InetSocketAddress inetSocketAddress, BaseMessage baseMessage) {
        this.inetSocketAddress = inetSocketAddress;
        this.baseMessage = baseMessage;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    public BaseMessage getBaseMessage() {
        return baseMessage;
    }

    public long getGlobalId() {
        return baseMessage.getGlobalID();
    }

    abstract void process(Node node);
}
