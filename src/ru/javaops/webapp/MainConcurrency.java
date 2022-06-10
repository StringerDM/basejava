package ru.javaops.webapp;

public class MainConcurrency {
    static final Object LOCK1 = new Object();
    static final Object LOCK2 = new Object();

    public static void main(String[] args) {

        new Thread(() -> {
            synchronized (LOCK1) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (LOCK2) {
                    System.out.println(Thread.currentThread().getName());
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (LOCK2) {
                synchronized (LOCK1) {
                    System.out.println(Thread.currentThread().getName());
                }
            }

        }).start();
    }
}
