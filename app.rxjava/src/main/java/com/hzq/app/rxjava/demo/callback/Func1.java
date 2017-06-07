package com.hzq.app.rxjava.demo.callback;

/**
 * @author: hezhiqiang
 * @date: 17/4/27
 * @version:
 * @description:
 */

public interface Func1<T,R> {
    //接收一个T类型的对象,返回一个R类型的对象,也就是将T类型转换成R类型的对象
    R call(T t);
}
