package com.hzq.app.rxjava.demo.callback;

import com.hzq.app.rxjava.demo.operator.OnSubscribeFilter;
import com.hzq.app.rxjava.demo.operator.OnSubscribeFlitMap;
import com.hzq.app.rxjava.demo.operator.OnSubscribeLift;
import com.hzq.app.rxjava.demo.operator.OnSubscribeMap;
import com.hzq.app.rxjava.demo.operator.OnSunscribeDefer;
import com.hzq.app.rxjava.demo.operator.OperatorSubscribeOn;
import com.hzq.app.rxjava.demo.scheduler.Scheduler;

/**
 * @author: hezhiqiang
 * @date: 17/4/26
 * @version:
 * @description: 包装对象(被观察者)
 */

public class Observable<T> {

    OnSubscriber<T> onSubscriber;
    public Observable(OnSubscriber<T> onSubscriber){
        this.onSubscriber = onSubscriber;
    }
    //执行特定的任务
    public void subscribe(Observer<T> observer){
        this.onSubscriber.call(observer);
    }

    //不完整的回调
    public void subscribe(Action1<T> next){
        subscribe(new ActionSubscriber<T>(next,ERROR_EMPTY,COMPLETED_EMPTY));
    }

    public void subscribe(Action1<T> next,Action1<Throwable> error){
        subscribe(new ActionSubscriber<T>(next,error,COMPLETED_EMPTY));
    }

    public void subscribe(Action1<T> next,Action1<Throwable> error,Action0 completed){
        subscribe(new ActionSubscriber<T>(next,error,completed));
    }

    Action1<Throwable> ERROR_EMPTY = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            //
        }
    };

    Action0 COMPLETED_EMPTY = new Action0() {
        @Override
        public void call() {
            //
        }
    };

    public interface OnSubscriber<T> extends Action1<Observer<T>>{

    }

    /**变换观察者**/
    public interface Operator<R,T> extends Func1<Observer<R>,Observer<T>> {

    }

    /**变换被观察者**/
    public interface Transform<T,R> extends Func1<Observable<T>,Observable<R>> {

    }

    public static <T> Observable<T> create(OnSubscriber<T> subscriber){
        return new Observable(subscriber);
    }

    public static <T> Observable<T> just(final T...t){
        OnSubscriber<T> onSubscriber = new OnSubscriber<T>() {
            @Override
            public void call(Observer<T> tObserver) {
                for (T t1 : t) {
                    tObserver.onNext(t1);
                }
                tObserver.onCompleted();
            }
        };
        return create(onSubscriber);
    }

    /**
     * 作用:将T类型的对象转换成R类型的对象
     * @param func1
     * @param <R>
     * @return
     */
    public <R> Observable<R> map(final Func1<T,R> func1){
        OnSubscribeMap subscribeMap = new OnSubscribeMap(this, func1);
        return create(subscribeMap);
    }

    /**
     * 将T类型的对象转换成R类型对象,需要多次变换
     * @param func1
     * @param <R>
     * @return
     */
    public <R> Observable<R> flitMap(final Func1<T, Observable<R>> func1) {
        OnSubscribeFlitMap flitMap = new OnSubscribeFlitMap(this, func1);
        return create(flitMap);
    }

    /**
     * 说明: Observable.subscribe(Observer<T> observer) 订阅的是一个T类型的Observer观察者
     * 而现有的是一个Observer<R> R类型的观察者,
     *
     * 将一个观察者 转变 成另一个的观察者,用原始的Obervable(被观察者)去订阅这个新的观察者
     * @param operator 转换操作符,将Observer<R> 转换成Observer<T>对象
     * @param <R>
     * @return
     */
    public <R> Observable<R> lift(final Operator<R,T> operator){
        OnSubscribeLift lift = new OnSubscribeLift(onSubscriber,operator);
        return create(lift);
    }

    /**
     * 整条链变换(变换被观察者)
     * @param trTransform
     * @param <R>
     * @return
     */
    public <R> Observable<R> compose(Transform<T,R> trTransform){
        return trTransform.call(this);
    }


    public static <T> Observable<T> defer(Func0<Observable<T>> func){
        return create(new OnSunscribeDefer<T>(func));
    }

    /**
     * 过滤
     * @param func1
     * @return
     */
    public Observable<T> filter(Func1<T,Boolean> func1){
        return create(new OnSubscribeFilter<T>(this, func1));
    }

    public Observable<T> subscribeOn(Scheduler scheduler){
        return create(new OperatorSubscribeOn<T>(this,scheduler));
    }

}
