package ru.chirikhin.chattree;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

public class NewChildMessage {
    private final String string;
    private final long localID;
    private final long port;
    private final String ip;
    private final long globalID;

    public NewChildMessage(long localID, int port, String ip) {
        string = localID + "_" + port + "_" + localID;
        this.localID = localID;
        this.port = port;
        this.ip = ip;

        String globalIDStr = localID + port + ip.replaceAll("[.]", "");
        this.globalID = Long.parseLong(globalIDStr);
    }

    public NewChildMessage(byte[] bytes) {
        string = new String(bytes, Charset.forName("UTF-8"));

        String[] strings = StringUtils.split(string, '_');

        this.localID = Long.parseLong(strings[0]);
        this.port = Long.parseLong(strings[1]);
        this.ip = strings[2];

        String globalIDStr = localID + port + ip.replaceAll("[.]", "");
        this.globalID = Long.parseLong(globalIDStr);
    }

    byte[] bytes() {
        return string.getBytes(Charset.forName("UTF-8"));
    }

    public long getLocalId() {
        return localID;
    }

    public long getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

}
