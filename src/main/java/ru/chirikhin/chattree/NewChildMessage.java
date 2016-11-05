package ru.chirikhin.chattree;

import java.nio.charset.Charset;

public class NewChildMessage extends BaseMessage {
    public NewChildMessage(long globalID) {
        super(globalID);
    }
    /*private final String string;

    public NewChildMessage(long localID, int port, String ip) {
        super(localID, port, ip);
        string = MessageType.NEW_CHILD + "_" + localID + "_" + port + "_" + ip;
    }

    public NewChildMessage(byte[] bytes) {
        super(bytes);
        string = new String(bytes, Charset.forName("UTF-8"));
    }

    byte[] bytes() {
        return string.getBytes(Charset.forName("UTF-8"));
    }
    */
}
