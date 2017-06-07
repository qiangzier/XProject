package com.hzq.app.rxjava.demo.scheduler;

import android.os.Handler;
import android.os.Looper;

/**
 * @author: hezhiqiang
 * @date: 17/5/3
 * @version:
 * @description:
 */

public class Schedulers {
    public static Scheduler io(){
        return new CacheSchedule();
    }

    public static Scheduler immediate(){
        return ImmediateSchedule.INSTANCE;
    }

    public static Scheduler mainThread(){
        return new LooperScheduler(new Handler(Looper.getMainLooper()));
    }
}
