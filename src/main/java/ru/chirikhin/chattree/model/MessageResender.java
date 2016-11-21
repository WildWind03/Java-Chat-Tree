package ru.chirikhin.chattree.model;

import org.apache.log4j.Logger;
import ru.chirikhin.cyclelist.CycleLinkedList;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

public class MessageResender implements Runnable {

    private static final long MAX_DELAY_TO_RESEND = 500;
    private static final long PERIOD_BETWEEN_CHECKING = 100;
    private static final Logger logger = Logger.getLogger(MessageResender.class);

    private final BlockingQueue<AddressedMessage> messagesToSend;
    private final CycleLinkedList<NotConfirmedAddressedMessage> notConfirmedAddressedMessageCycleLinkedList;

    public MessageResender(BlockingQueue<AddressedMessage> messagesToSend,
                           CycleLinkedList<NotConfirmedAddressedMessage> notConfirmedAddressedMessageCycleLinkedList) {
        this.messagesToSend = messagesToSend;
        this.notConfirmedAddressedMessageCycleLinkedList = notConfirmedAddressedMessageCycleLinkedList;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                long currentTime = System.currentTimeMillis();
                Iterator<NotConfirmedAddressedMessage> iterator = notConfirmedAddressedMessageCycleLinkedList.iterator();
                while (iterator.hasNext()) {
                    NotConfirmedAddressedMessage currentMessage = iterator.next();
                    if (currentTime - currentMessage.getTimeOfAdd() >= MAX_DELAY_TO_RESEND) {
                        logger.info("Message " + currentMessage.getAddressedMessage().getBaseMessage().getGlobalID() + " will be resend. There is not confirmation for the message");
                        currentMessage.updateTimeOfAdd(System.currentTimeMillis());
                        messagesToSend.add(currentMessage.getAddressedMessage());
                    }
                }

                Thread.sleep(PERIOD_BETWEEN_CHECKING);
            }
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }

    }
}
