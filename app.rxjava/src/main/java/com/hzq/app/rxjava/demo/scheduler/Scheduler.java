package com.hzq.app.rxjava.demo.scheduler;

import com.hzq.app.rxjava.demo.callback.Action0;

/**
 * @author: hezhiqiang
 * @date: 17/5/3
 * @version:
 * @description:
 */

public abstract class Scheduler {

    public abstract Worker createWorker();

    public abstract static class Worker{
        public abstract void schedule(Action0 action0);
    }
}
