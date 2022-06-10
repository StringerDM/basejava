package ru.javaops.webapp;

public class MainConcurrency {
    static final String LOCK1 = "LOCK1";
    static final String LOCK2 = "LOCK2";

    public static void main(String[] args) {
        deadlock(LOCK1, LOCK2);
        deadlock(LOCK2, LOCK1);
    }

    public static void deadlock(Object obj1, Object obj2) {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " started");
            System.out.println(Thread.currentThread().getName() + " trying to synchronize by " + obj1);
            synchronized (obj1) {
                System.out.println(Thread.currentThread().getName() + " synchronized by " + obj1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + " trying to synchronize by " + obj2);
                synchronized (obj2) {
                    System.out.println(Thread.currentThread().getName() + " synchronized by " + obj2);
                }
            }
        }).start();
    }
}
