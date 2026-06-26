package com.multithreading;

public class CounterDemo {
    private static int count = 0;
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        testWithoutLock();
        testWithLock();
    }

    static void testWithoutLock() throws InterruptedException {
        count = 0;
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) count++;
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) count++;
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("不加锁结果：" + count + "（期望 20000）");
    }

    static void testWithLock() throws InterruptedException {
        count = 0;
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                synchronized (lock) { count++; }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                synchronized (lock) { count++; }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("加锁结果：" + count + "（期望 20000）");
    }
}
