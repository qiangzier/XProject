package com.hzq.app.rxjava.demo.callback;

/**
 * @author: hezhiqiang
 * @date: 17/4/26
 * @version:
 * @description: 减少定义重复接口 观察者
 */

public interface Observer<T> {

    void onNext(T t);
    void error(Exception e);
    void onCompleted();
}
