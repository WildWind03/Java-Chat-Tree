package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

public class NotChildMessage extends BaseMessage {
    private static final Logger logger = Logger.getLogger(NotChildMessage.class.getName());

    public NotChildMessage(long globalID) {
        super(globalID);
    }
}
