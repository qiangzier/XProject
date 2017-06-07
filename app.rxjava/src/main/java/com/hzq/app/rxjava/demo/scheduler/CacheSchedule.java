package com.hzq.app.rxjava.demo.scheduler;

import com.hzq.app.rxjava.demo.callback.Action0;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author: hezhiqiang
 * @date: 17/5/3
 * @version:
 * @description:
 */

public class CacheSchedule extends Scheduler {

    private RxThreadFactory factory = new RxThreadFactory("IoThread");

    @Override
    public Worker createWorker() {
        return new CacheWorker(factory);
    }

    private class CacheWorker extends Worker{

        private ScheduledExecutorService pool;
        public CacheWorker(RxThreadFactory factory){
            pool = Executors.newScheduledThreadPool(1,factory);
        }

        @Override
        public void schedule(final Action0 action0) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    action0.call();
                }
            });
        }
    }
}
