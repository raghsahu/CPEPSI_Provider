package com.provider.admin.cpepsi_provider.Thread;

import java.util.concurrent.ThreadFactory;

public class Popup_thread extends Thread implements ThreadFactory {
    long minPrime = 200;

    public Popup_thread() {

    }

    @Override
    public synchronized void start() {
        super.start();
    }

    public void run(Popup_thread thread) {
        thread.start();
        try {
            thread.wait(minPrime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // compute primes larger than minPrime
    }


    @Override
    public State getState() {
        return super.getState();
    }

    @Override
    public Thread newThread(Runnable r) {
        return null;
    }
}
