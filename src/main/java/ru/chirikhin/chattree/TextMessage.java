package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

import java.nio.charset.Charset;

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

    @Override
    byte[] bytes() {
        String serializedMessage = "" + MessageType.NEW_PARENT + SEPARATOR_CHAR + getGlobalID() + SEPARATOR_CHAR + getText();
        return serializedMessage.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    void process(Node node) {
        node.handleTextMessage(this);
    }
}
