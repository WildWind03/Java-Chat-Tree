package ru.chirikhin.chattree;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

public class NewChildMessage {
    private final String string;
    private final long localID;
    private final int port;
    private final String ip;
    private final long globalID;

    public NewChildMessage(long localID, int port, String ip) {
        string = MessageType.NEW_CHILD + "_" + localID + "_" + port + "_" + ip;

        this.localID = localID;
        this.port = port;
        this.ip = ip;

        String globalIDStr = localID + port + ip.replaceAll("[.]", "");
        this.globalID = Long.parseLong(globalIDStr);
    }

    public NewChildMessage(byte[] bytes) {
        string = new String(bytes, Charset.forName("UTF-8"));

        String[] strings = StringUtils.split(string, '_');

        this.localID = Long.parseLong(strings[1]);
        this.port = Integer.parseInt(strings[2]);
        this.ip = strings[3];

        String globalIDStr = localID + (port + ip.replaceAll("[.]", ""));
        this.globalID = Long.parseLong(globalIDStr);
    }

    byte[] bytes() {
        return string.getBytes(Charset.forName("UTF-8"));
    }

    public long getLocalId() {
        return localID;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public long getGlobalID() {
        return globalID;
    }

}
