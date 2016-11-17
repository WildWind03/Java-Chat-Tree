package ru.chirikhin.cyclelist;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;

public class CycleLinkedList<T> {
    private static final Logger logger = Logger.getLogger(CycleLinkedList.class.getName());

    private final LinkedList<T> linkedList;
    private final int maxSize;
    private int currentSize;

    public CycleLinkedList(int size) {
        this.maxSize = size;
        linkedList = new LinkedList<>();
    }

    public void push(T t) {
        if (currentSize >= maxSize) {
            linkedList.removeFirst();
        } else {
            currentSize++;
        }

        linkedList.push(t);
    }

    public boolean add(T t) {
        if (currentSize + 1 < maxSize) {
            currentSize++;
            return linkedList.add(t);
        } else {
            return false;
        }
    }

    public int size() {
        return currentSize;
    }

    public Iterator<T> iterator() {
        return linkedList.iterator();
    }
}
