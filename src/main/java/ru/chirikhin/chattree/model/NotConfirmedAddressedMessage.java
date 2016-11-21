package ru.chirikhin.chattree.model;

import java.util.Optional;
import java.util.function.Consumer;

public class NotConfirmedAddressedMessage {
    private final AddressedMessage addressedMessage;
    private long timeOfAdd;
    private Runnable onConfirmed;

    public NotConfirmedAddressedMessage(AddressedMessage addressedMessage, long timeOfAdd) {
        this.addressedMessage = addressedMessage;
        this.timeOfAdd = timeOfAdd;
    }

    public void setOnConfirmed(Runnable runnable) {
        this.onConfirmed = runnable;
    }

    public void run() {
        if (null != onConfirmed) {
            onConfirmed.run();
        }
    }

    public AddressedMessage getAddressedMessage() {
        return addressedMessage;
    }

    public long getTimeOfAdd() {
        return timeOfAdd;
    }

    public void updateTimeOfAdd(long newTimeOfAdd) {
        this.timeOfAdd = newTimeOfAdd;
    }
}
