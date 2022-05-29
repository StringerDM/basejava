package ru.javaops.webapp;

import java.io.File;

public class MainFile {

    public static void main(String[] args) {
        printAllFileNames("./src/ru/javaops/webapp");
    }

    public static void printAllFileNames(String directoryPath) {
        File file = new File(directoryPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fl : files) {
                if (fl.isFile()) {
                    System.out.println(fl.getName());
                }
                if (fl.isDirectory()) {
                    printAllFileNames(fl.getPath());
                }
            }
        }
    }
}
