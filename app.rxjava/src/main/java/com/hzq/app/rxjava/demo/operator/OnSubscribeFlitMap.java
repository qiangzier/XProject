package com.hzq.app.rxjava.demo.operator;

import com.hzq.app.rxjava.demo.callback.Func1;
import com.hzq.app.rxjava.demo.callback.Observable;
import com.hzq.app.rxjava.demo.callback.Observer;

/**
 * @author: hezhiqiang
 * @date: 17/4/27
 * @version:
 * @description:
 */

public class OnSubscribeFlitMap<T,R> implements Observable.OnSubscriber<R> {
    private Observable<T> source;
    private Func1<T,Observable<R>> func1;

    public OnSubscribeFlitMap(Observable<T> source, Func1<T, Observable<R>> func1) {
        this.source = source;
        this.func1 = func1;
    }

    @Override
    public void call(final Observer<R> rObserver) {
        source.subscribe(new Observer<T>() {
            @Override
            public void onNext(T t) {
                Observable<R> call = func1.call(t);
                call.subscribe(new Observer<R>() {
                    @Override
                    public void onNext(R r) {
                        rObserver.onNext(r);
                    }

                    @Override
                    public void error(Exception e) {
                        rObserver.error(e);
                    }

                    @Override
                    public void onCompleted() {
                        rObserver.onCompleted();
                    }
                });
            }

            @Override
            public void error(Exception e) {
                rObserver.error(e);
            }

            @Override
            public void onCompleted() {
                rObserver.onCompleted();
            }
        });
    }
}
