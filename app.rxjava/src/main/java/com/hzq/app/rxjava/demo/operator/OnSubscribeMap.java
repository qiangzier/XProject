package com.hzq.app.rxjava.demo.operator;

import com.hzq.app.rxjava.demo.callback.Func1;
import com.hzq.app.rxjava.demo.callback.Observable;
import com.hzq.app.rxjava.demo.callback.Observer;

/**
 * @author: hezhiqiang
 * @date: 17/4/27
 * @version:
 * @description: 将T类型转换成R类型的对象
 */

public class OnSubscribeMap<T,R> implements Observable.OnSubscriber<R> {

    private Observable<T> source;
    private Func1<T,R> mapFunc1;

    public OnSubscribeMap(Observable<T> source, Func1<T, R> mapFunc1) {
        this.source = source;
        this.mapFunc1 = mapFunc1;
    }

    @Override
    public void call(final Observer<R> observer) {
        source.subscribe(new Observer<T>() {
            @Override
            public void onNext(T t) {
                R call = mapFunc1.call(t);
                observer.onNext(call);
            }

            @Override
            public void error(Exception e) {
                observer.error(e);
            }

            @Override
            public void onCompleted() {

            }
        });
    }
}
