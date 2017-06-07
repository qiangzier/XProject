package com.hzq.app.rxjava.demo.scheduler;

import com.hzq.app.rxjava.demo.callback.Action0;

/**
 * @author: hezhiqiang
 * @date: 17/5/3
 * @version:
 * @description: 当前线程中执行
 */

public class ImmediateSchedule extends Scheduler {

    public static final ImmediateSchedule INSTANCE = new ImmediateSchedule();

    @Override
    public Worker createWorker() {
        return new ImmediateWorker();
    }

    private class ImmediateWorker extends Worker{

        @Override
        public void schedule(Action0 action0) {
            action0.call();
        }
    }
}
