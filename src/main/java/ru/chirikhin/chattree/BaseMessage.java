package ru.chirikhin.chattree;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

public abstract class BaseMessage {
    private final long globalID;

    public BaseMessage(long globalID) {
        this.globalID = globalID;
    }

    public long getGlobalID() {
        return globalID;
    }

    //private final int port;
    //private final long localID;
    //private final String ip;

    /*public BaseMessage(long localID, int port, String ip) {
        this.localID = localID;
        this.port = port;
        this.ip = ip;

        String globalIDStr = localID + port + ip.replaceAll("[.]", "");
        this.globalID = Long.parseLong(globalIDStr);
    }

    public BaseMessage(byte[] bytes) {
        String string = new String(bytes, Charset.forName("UTF-8"));

        String[] strings = StringUtils.split(string, '_');

        this.localID = Long.parseLong(strings[1]);
        this.port = Integer.parseInt(strings[2]);
        this.ip = strings[3];

        String globalIDStr = localID + (port + ip.replaceAll("[.]", ""));
        this.globalID = Long.parseLong(globalIDStr);
    }

    abstract byte[] bytes();

    public long getLocalId() {
        return localID;
    }

    public int getPort() {
        return port;
    }

    public String getNewParentIP() {
        return ip;
    }

    public long getGlobalID() {
        return globalID;
    }*/
}
