package com.hzq.app.rxjava.demo.scheduler;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: hezhiqiang
 * @date: 17/5/3
 * @version:
 * @description:
 */

public class RxThreadFactory extends AtomicLong implements ThreadFactory {

    private String perfix;

    public RxThreadFactory(String perfix){
        this.perfix = perfix;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r,perfix + "_" + incrementAndGet());
    }
}
