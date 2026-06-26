package com.multithreading;

public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("线程1 第 " + i + " 次");
                try { Thread.sleep(1000); } catch (InterruptedException e) { }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("线程2 第 " + i + " 次");
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("两个线程都跑完了，main 才执行这行");
    }
}
