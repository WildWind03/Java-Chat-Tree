package ru.chirikhin.chattree.view;

import java.io.Console;
import java.util.Observable;

public class ConsoleInputView extends Observable implements Runnable {

    private final Console console = System.console();

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            notifyObservers(console.readLine());
        }
    }
}
