package ru.chirikhin.chattree.view;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.Observable;
import java.util.Scanner;

public class ConsoleInputView extends Observable implements Runnable {

    private static final Logger logger = Logger.getLogger(ConsoleInputView.class);
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        logger.info("Console input view started working");

        try {
            while (!Thread.currentThread().isInterrupted() && scanner.hasNextLine()) {
                String text = scanner.nextLine();
                logger.info ("New string was read from console");
                setChanged();
                notifyObservers(text);
            }
        } catch (Throwable t) {
            logger.error(t.getMessage());
            logger.error(Arrays.toString(t.getStackTrace()));
        }
    }

    public void stop() {
        logger.info("Attempt to stop");

        try {
            scanner.close();
            logger.info("Closed");
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
    }
}
