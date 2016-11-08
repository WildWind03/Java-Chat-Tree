package ru.chirikhin.chattree.model;

import ru.chirikhin.cyclelist.CycleLinkedList;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

public class MessageResender implements Runnable {

    private static final long MAX_DELAY_TO_RESEND = 500;

    private final BlockingQueue<AddressedMessage> messagesToSend;
    private final CycleLinkedList<NotConfirmedAddressedMessage> notConfirmedAddressedMessageCycleLinkedList;

    public MessageResender(BlockingQueue<AddressedMessage> messagesToSend,
                           CycleLinkedList<NotConfirmedAddressedMessage> notConfirmedAddressedMessageCycleLinkedList) {
        this.messagesToSend = messagesToSend;
        this.notConfirmedAddressedMessageCycleLinkedList = notConfirmedAddressedMessageCycleLinkedList;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            long currentTime = System.currentTimeMillis();
            Iterator<NotConfirmedAddressedMessage> iterator = notConfirmedAddressedMessageCycleLinkedList.iterator();

            while (iterator.hasNext()) {
                NotConfirmedAddressedMessage currentMessage = iterator.next();
                if (currentTime - currentMessage.getTimeOfAdd() >= MAX_DELAY_TO_RESEND) {
                    currentMessage.updateTimeOfAdd(System.currentTimeMillis());
                    messagesToSend.add(currentMessage.getAddressedMessage());
                }
            }
        }
    }
}
