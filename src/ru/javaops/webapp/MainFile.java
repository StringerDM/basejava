package ru.javaops.webapp;

import java.io.File;

public class MainFile {

    public static void main(String[] args) {
        printAllFileNames("./src/ru/javaops/webapp");
    }

    public static void printAllFileNames(String directoryPath) {
        printFormattedPath(directoryPath, 0);
    }

    private static void printFormattedPath(String directoryPath, int offset) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < offset; i++) {
            sb.append(" ");
        }

        File file = new File(directoryPath);
        System.out.println(sb + ".." + file.getName());
        File[] files = file.listFiles();
        if (files != null) {
            for (File fl : files) {
                if (fl.isFile()) {
                    System.out.println(sb + "  " + fl.getName());
                }
                if (fl.isDirectory()) {
                    printFormattedPath(fl.getPath(), offset + 2);
                }
            }
        }
    }
}
