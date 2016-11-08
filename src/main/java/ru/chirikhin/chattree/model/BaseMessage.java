package ru.chirikhin.chattree.model;

public abstract class BaseMessage {
    protected final static char SEPARATOR_CHAR = '_';

    private final long globalID;

    public BaseMessage(long globalID) {
        this.globalID = globalID;
    }

    public long getGlobalID() {
        return globalID;
    }

    abstract byte[] bytes();
}
