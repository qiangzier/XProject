package com.hzq.app.rxjava.demo.scheduler;

import android.os.Handler;
import android.os.Message;

import com.hzq.app.rxjava.demo.callback.Action0;

/**
 * @author: hezhiqiang
 * @date: 17/5/3
 * @version:
 * @description:
 */

public class LooperScheduler extends Scheduler {

    private Handler handler;

    public LooperScheduler(Handler handler){
        this.handler = handler;
    }

    @Override
    public Worker createWorker() {
        return new LooperWorker(handler);
    }

    private class LooperWorker extends Worker{
        Handler handler;
        public LooperWorker(Handler handler){
            this.handler = handler;
        }

        @Override
        public void schedule(final Action0 action0) {
            Message message = Message.obtain(handler, new Runnable() {
                @Override
                public void run() {
                    action0.call();
                }
            });
            handler.sendMessage(message);
        }
    }
}
