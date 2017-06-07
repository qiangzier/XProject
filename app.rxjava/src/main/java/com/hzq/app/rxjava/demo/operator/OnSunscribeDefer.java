package com.hzq.app.rxjava.demo.operator;

import com.hzq.app.rxjava.demo.callback.Func0;
import com.hzq.app.rxjava.demo.callback.Observable;
import com.hzq.app.rxjava.demo.callback.Observable.OnSubscriber;
import com.hzq.app.rxjava.demo.callback.Observer;

/**
 * @author: hezhiqiang
 * @date: 17/5/5
 * @version:
 * @description: 延时创建被观察者
 */

public class OnSunscribeDefer<T> implements OnSubscriber<T> {

    Func0<Observable<T>> func0;
    public OnSunscribeDefer(Func0<Observable<T>> func){
        this.func0 = func;
    }

    @Override
    public void call(final Observer<T> tObserver) {
        Observable<T> observable = null;
        try {
            observable = func0.call();
        } catch (Exception e) {
            tObserver.error(e);
            e.printStackTrace();
        }
        observable.subscribe(new Observer<T>(){

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
        });
    }
}
