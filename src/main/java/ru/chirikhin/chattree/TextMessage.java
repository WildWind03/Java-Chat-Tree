package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

public class TextMessage extends BaseMessage {
    private static final Logger logger = Logger.getLogger(TextMessage.class.getName());

    private final String text;

    public TextMessage(long globalID, String text) {
        super(globalID);

        this.text = text;
    }

    public String getText() {
        return text;
    }
}
