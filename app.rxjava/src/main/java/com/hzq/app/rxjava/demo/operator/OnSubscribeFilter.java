package com.hzq.app.rxjava.demo.operator;

import com.hzq.app.rxjava.demo.callback.Func1;
import com.hzq.app.rxjava.demo.callback.Observable;
import com.hzq.app.rxjava.demo.callback.Observer;

/**
 * @author: hezhiqiang
 * @date: 17/5/3
 * @version:
 * @description:
 */

public class OnSubscribeFilter<T> implements Observable.OnSubscriber<T> {

    private Observable<T> source;
    private Func1<T,Boolean> mapper;
    public OnSubscribeFilter(Observable<T> observable, Func1<T,Boolean> func1){
        this.source = observable;
        this.mapper = func1;
    }

    @Override
    public void call(final Observer<T> tObserver) {
        Observer<T> observer = new Observer<T>() {

            @Override
            public void onNext(T t) {
                if(mapper.call(t))
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
        this.source.subscribe(observer);
    }
}
