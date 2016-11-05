package ru.chirikhin.chattree;

import org.apache.log4j.Logger;

import java.util.LinkedList;

public class CycleLinkedList<T> {
    private static final Logger logger = Logger.getLogger(CycleLinkedList.class.getName());

    private final LinkedList<T> linkedList;
    private final int maxSize;
    private int currentSize;

    public CycleLinkedList(int size) {
        this.maxSize = size;
        linkedList = new LinkedList<T>();
    }

    public void push(T t) {
        if (currentSize >= maxSize) {
            linkedList.removeFirst();
        }

        linkedList.push(t);
    }
}
