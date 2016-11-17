package ru.chirikhin.chattree.view;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Scanner;

public class FileInputView extends Observable implements Runnable {
     public static final Logger logger = Logger.getLogger(FileInputView.class.getName());

    private final String path;

     public FileInputView(String path) {
         this.path = path;
     }

     @Override
     public void run() {
         try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                String text = scanner.nextLine();
                setChanged();
                notifyObservers(text);
            }
         } catch (FileNotFoundException e) {
             logger.error(e.getMessage());
         }
     }
}
