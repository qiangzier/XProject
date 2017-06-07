package com.hzq.app.rxjava.demo.operator;

import com.hzq.app.rxjava.demo.callback.Observable;
import com.hzq.app.rxjava.demo.callback.Observer;


/**
 * @author: hezhiqiang
 * @date: 17/4/27
 * @version:
 * @description: 观察者
 */

public class OnSubscribeLift<T,R> implements Observable.OnSubscriber<R> {

    private Observable.OnSubscriber<T> parent;
    private Observable.Operator<R,T> operator;


    /**
     * 将原始的观察者按照operator包装成一个新的观察者。
     * @param onSubscriber 原始的观察者
     * @param operator 转换规则(操作符)
     */
    public OnSubscribeLift(Observable.OnSubscriber<T> onSubscriber, Observable.Operator<R,T> operator) {
        this.parent = onSubscriber;
        this.operator = operator;
    }

    @Override
    public void call(Observer<R> rObserver) {
        Observer<T> call = operator.call(rObserver);
        //在此触发原始观察者的call
        parent.call(call);
    }
}
