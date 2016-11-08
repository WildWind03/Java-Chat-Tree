package ru.chirikhin.chattree.model;

public class NotConfirmedAddressedMessage {
    private final AddressedMessage addressedMessage;
    private long timeOfAdd;

    public NotConfirmedAddressedMessage(AddressedMessage addressedMessage, long timeOfAdd) {
        this.addressedMessage = addressedMessage;
        this.timeOfAdd = timeOfAdd;
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
