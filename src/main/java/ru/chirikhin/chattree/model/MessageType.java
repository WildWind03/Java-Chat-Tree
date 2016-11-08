package ru.chirikhin.chattree.model;

public enum MessageType {
    NEW_CHILD (0), TEXT(1), NEW_PARENT(2), NOT_CHILD(3), CONFIRM(4);

    private final int value;

    MessageType(int value) {
        this.value = value;
    }

    public int returnValue() {
        return value;
    }
}
