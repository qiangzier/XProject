package com.hzq.app.rxjava.demo.callback;

/**
 * @author: hezhiqiang
 * @date: 17/4/27
 * @version:
 * @description:
 */

public class ActionSubscriber<T> implements Observer<T> {
    private Action1<T> onNext;
    private Action1<Throwable> onError;
    private Action0 onCompleted;

    public ActionSubscriber(Action1<T> onNext, Action1<Throwable> onError, Action0 onCompleted) {
        this.onNext = onNext;
        this.onError = onError;
        this.onCompleted = onCompleted;
    }

    @Override
    public void onCompleted() {
        if(onCompleted != null)
            onCompleted.call();
    }

    @Override
    public void error(Exception e) {
        if(onError != null)
            onError.call(e);
    }

    @Override
    public void onNext(T t) {
        if(onNext != null)
            onNext.call(t);
    }
}
