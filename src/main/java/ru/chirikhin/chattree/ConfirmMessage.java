package ru.chirikhin.chattree;

import java.nio.charset.Charset;

public class ConfirmMessage extends BaseMessage {
    private final String string;

    public ConfirmMessage(long localID, int port, String ip) {
        super(localID, port, ip);
        string = MessageType.CONFIRM + "_" + localID + "_" + port + "_" + ip;
    }

    public ConfirmMessage(byte[] bytes) {
        super(bytes);
        string = new String(bytes, Charset.forName("UTF-8"));
    }

    byte[] bytes() {
        return string.getBytes(Charset.forName("UTF-8"));
    }
}
