package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

import java.net.Inet4Address;
import java.net.InetSocketAddress;

public class AddressedMessage {
    private static final Logger logger = Logger.getLogger(AddressedMessage.class.getName());
    private final BaseMessage baseMessage;
    private final InetSocketAddress receiverAddress;

    public AddressedMessage(BaseMessage baseMessage, InetSocketAddress receiverAddress) {
        this.baseMessage = baseMessage;
        this.receiverAddress = receiverAddress;
    }

    public BaseMessage getBaseMessage() {
        return baseMessage;
    }

    public InetSocketAddress getReceiverAddress() {
        return receiverAddress;
    }
}
