package ru.chirikhin.chattree;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

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
