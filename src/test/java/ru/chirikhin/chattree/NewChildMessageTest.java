package ru.chirikhin.chattree;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class NewChildMessageTest {

    @Test
    public void testSerialization() {
        NewChildMessage newChildMessage = new NewChildMessage(1234, 55, "225.255.192.193");
        byte[] bytes = newChildMessage.bytes();

        NewChildMessage restoredChildMessage = new NewChildMessage(bytes);

        Assert.assertEquals(1234, restoredChildMessage.getLocalId());
        Assert.assertEquals(55, restoredChildMessage.getPort());
        Assert.assertEquals("225.255.192.193", restoredChildMessage.getIp());
        Assert.assertEquals(restoredChildMessage.getGlobalID(), 123455225255192193L);
    }
}