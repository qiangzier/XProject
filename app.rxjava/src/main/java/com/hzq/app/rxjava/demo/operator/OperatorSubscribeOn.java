package com.hzq.app.rxjava.demo.operator;

import com.hzq.app.rxjava.demo.callback.Action0;
import com.hzq.app.rxjava.demo.callback.Observable;
import com.hzq.app.rxjava.demo.callback.Observer;
import com.hzq.app.rxjava.demo.scheduler.Scheduler;

/**
 * @author: hezhiqiang
 * @date: 17/5/3
 * @version:
 * @description:
 */

public class OperatorSubscribeOn<T> implements Observable.OnSubscriber<T> {

    private Observable<T> source;
    private Scheduler scheduler;

    public OperatorSubscribeOn(Observable<T> source, Scheduler scheduler) {
        this.source = source;
        this.scheduler = scheduler;
    }

    @Override
    public void call(final Observer<T> tObserver) {
        //获取工作线程
        Scheduler.Worker worker = scheduler.createWorker();

        //在线程中执行调度
        worker.schedule(new Action0() {
            @Override
            public void call() {
                Observer<T> innerObserver = new Observer<T>() {

                    @Override
                    public void onNext(T t) {
                        tObserver.onNext(t);
                    }

                    @Override
                    public void error(Exception e) {
                        tObserver.error(e);
                    }

                    @Override
                    public void onCompleted() {
                        tObserver.onCompleted();
                    }
                };
                source.subscribe(innerObserver);
            }
        });
    }
}
