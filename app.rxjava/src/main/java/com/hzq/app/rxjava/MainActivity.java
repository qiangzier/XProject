package com.hzq.app.rxjava;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.hzq.app.rxjava.demo.NetApi;
import com.hzq.app.rxjava.demo.callback.Func1;
import com.hzq.app.rxjava.demo.callback.Observable;
import com.hzq.app.rxjava.demo.callback.Observer;
import com.hzq.app.rxjava.demo.model.NewsItem;
import com.hzq.app.rxjava.demo.model.Response;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

public class MainActivity extends AppCompatActivity {
    final NetApi api = new NetApi();;
    private static final String TAG = MainActivity.class.getSimpleName() + "xx";
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        Transition explode =
                TransitionInflater.from(this).inflateTransition(R.transition.slide);
        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);
        getWindow().setReenterTransition(explode);
//        call();
//        test();
//        testLift();
//        schedulerTest();

        mySchedulerTest();
        testSubject();
    }

    private int test1(){
        int t = 0;
        try {
            return t;
        } finally {
            ++t;
            return t;
        }
    }

    public void start(View view){
        startAct(findViewById(R.id.btn));
    }

    public void start1(View view){
        startAct(findViewById(R.id.btn1));
    }


    public void start2(View view){
        startAct(findViewById(R.id.btn2));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startAct(View view){
        Intent intent = new Intent(this,OtherActivity.class);
        startActivity(intent, ActivityOptions
                .makeSceneTransitionAnimation(this,view,"shareView").toBundle());
    }

    private void mySchedulerTest(){
        Observable<String> just = Observable.just("1", "2", "3");
                just.filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        Log.d(TAG,"filter:" + s + ":" + Thread.currentThread().getName());
                        return "1".equals(s);
                    }
                })
                .subscribeOn(com.hzq.app.rxjava.demo.scheduler.Schedulers.io())
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        Log.d(TAG,"map:" + s + ":" + Thread.currentThread().getName());
                        return Integer.parseInt(s);
                    }
                })
                .subscribe(new com.hzq.app.rxjava.demo.callback.Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG,"subscribe:" + integer.intValue() + ":" + Thread.currentThread().getName());
                    }
                });
    }

    private void testSubject(){
        testAsyncSubject();
        testBehaviorSubject();
        testPublishSubject();
        testReplaySubject();
    }

    /**
     * Observer会接收AsyncSubject的`onComplete()之前的最后一个数据，
     * 如果因异常而终止，AsyncSubject将不会释放任何数据，但是会向Observer传递一个异常通知
     */
    private void testAsyncSubject(){
        AsyncSubject asyncSubject = AsyncSubject.create();
        asyncSubject.onNext("test1");
        asyncSubject.onNext("test2");
        asyncSubject.onCompleted();
        asyncSubject.subscribe(new Action1<String>() {
            @Override
            public void call(String o) {
                Log.d(TAG,"------------->AsyncSubject:"+o);
            }
        });
        asyncSubject.onNext("test3");
        asyncSubject.onNext("test4");
    }

    /**
     * Observer会接收到BehaviorSubject被订阅之前的最后一个数据，再接收其他发射过来的数据，
     * 如果BehaviorSubject被订阅之前没有发送任何数据，则会发送一个默认数据。（注意跟Async
     * Subject的区别，AsyncSubject要手动调用onCompleted()，且它的Observer会接收到
     * onCompleted()前发送的最后一个数据，之后不会再接收数据，而BehaviorSubject不需手动
     * 调用onCompleted()，它的Observer接收的是BehaviorSubject被订阅前发送的最后一个数据，
     * 两个的分界点不一样，且之后还会继续接收数据。）
     */
    private void testBehaviorSubject(){
        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create("default");
//        behaviorSubject.onNext("test1");
//        behaviorSubject.onNext("test2");
        behaviorSubject.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG,"------------->BehaviorSubject:"+s);
            }
        });
        behaviorSubject.onNext("test3");
        behaviorSubject.onNext("test4");
    }

    /**
     * PublishSubject比较容易理解，相对比其他Subject常用，它的Observer只会接收到PublishSubject被订阅之后发送的数据
     */
    private void testPublishSubject(){
        PublishSubject<String> publishSubject = PublishSubject.create();
        publishSubject.onNext("test1");
        publishSubject.onNext("test2");
        publishSubject.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG,"------------->PublishSubject:"+s);
            }
        });
        publishSubject.onNext("test3");
        publishSubject.onNext("test4");
    }

    private void testReplaySubject(){
        //创建默认初始缓存容量大小为16的ReplaySubject，当数据条目超过16会重新分配内存空间，使用这种方式，
        // 不论ReplaySubject何时被订阅，Observer都能接收到数据
//        ReplaySubject<String> replaySubject = ReplaySubject.create();
//        ReplaySubject<String> replaySubject = ReplaySubject.create(2); //指定初始容量
//        ReplaySubject<String> replaySubject = ReplaySubject.createWithSize(2); //只缓存订阅前最后发送的两条数据
        //replaySubject被订阅前的前1秒内发送的数据才能被接收
        ReplaySubject<String> replaySubject = ReplaySubject.createWithTime(1,
                TimeUnit.SECONDS, Schedulers.newThread());
        replaySubject.onNext("test1");
        replaySubject.onNext("test2");
        replaySubject.onNext("test-2");
        replaySubject.onNext("test-1");
        replaySubject.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG,"------------->ReplaySubject:"+s);
            }
        });
        replaySubject.subscribe(new rx.Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG,"------------->ReplaySubject2:"+"onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"------------->ReplaySubject2:"+"onError");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG,"------------->ReplaySubject2:"+s);
            }
        });
        replaySubject.onNext("test3");
        replaySubject.onNext("test4");
    }

    /*private void schedulerTest(){
        rx.Observable.just("1","2","3")
                .filter(new rx.functions.Func1() {
                    @Override
                    public Boolean call(String s) {
                        return !"1".equals(s);
                    }
                })
                .map(new rx.functions.Func1() {
                    @Override
                    public Integer call(String s) {
                        Log.d(TAG,s + ":" + Thread.currentThread().getName());
                        return Integer.parseInt(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .concatMap(new rx.functions.Func1<Integer,Observable<String>>() {
                    @Override
                    public rx.Observable<String> call(Integer integer) {
                        Log.d(TAG,"test"+ integer.intValue() + ":" + Thread.currentThread().getName());
                        return rx.Observable.just("test"+integer.intValue());
                    }
                })
                .observeOn(Schedulers.newThread())
                .map(new rx.functions.Func1<String,Integer>() {
                    @Override
                    public rx.Observable<String> call(String o) {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.newThread()) //不会起作用
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG,s + ":" + Thread.currentThread().getName());
                    }
                });
    }*/

    /**
     * 测试lift变换 将一个Integer转换成String
     */
    private void testLift(){
        //观察者
        Observable observable = Observable.create(new Observable.OnSubscriber<Integer>() {
            @Override
            public void call(Observer<Integer> observerObserver) {
                observerObserver.onNext(11);
            }
        });
    }

    private void call(){

         api.getAsyncList(1,20)
                 .map(new Func1<Response, String>() {
                     @Override
                     public String call(Response response) {
                         return findLastTitle(response);
                     }
                 })
                 .flitMap(new Func1<String, Observable<Uri>>(){
                     @Override
                     public Observable<Uri> call(String s) {
                        return api.save1(s);
                     }
                 })
                 .subscribe(new Observer<Uri>() {
                     @Override
                     public void onNext(Uri uri) {
                         Log.d(TAG,uri.toString());
                     }

                     @Override
                     public void error(Exception e) {

                     }

                     @Override
                     public void onCompleted() {

                     }
                 });


        /*Observable<Uri> andSaveLastTitle1 = new ApiHelper().getAndSaveLastTitle1();
        andSaveLastTitle1.subscribe(new Observer<Uri>() {
            @Override
            public void onNext(Uri uri) {
                Log.d(TAG,uri.toString());
            }

            @Override
            public void error(Exception e) {

            }
        });*/
    }

    private String findLastTitle(Response response) {
        List<NewsItem> list = response.result.list;
        return list.get(list.size() - 1).title;
    }

    private void test(){
        //观察者 类似于callback
        rx.Observer<String> observer = new rx.Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG,"--------------onCompleted() be called");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"--------------onError() be called");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG,"--------------onNext() be called");
                Log.d(TAG,"--------------"+s);
            }
        };

        rx.Subscriber<String> subscriber = new rx.Subscriber<String>(){

            //优先执行
            @Override
            public void onStart() {
                Log.d(TAG,"--------------onStart() be called");
            }

            @Override
            public void onCompleted() {
                Log.d(TAG,"--------------onCompleted() be called");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"--------------onError() be called");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG,"--------------onNext() be called"+":s="+s);
            }
        };

        //被观察者 类似于Button 。。。
        //create
        rx.Observable<String> observable = rx.Observable.create(new rx.Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello");
                subscriber.onCompleted();
//                Log.d(TAG,"--------------subscriber.onNext() be called");
            }
        });

        /*observable.subscribe(subscriber);*/

        //just
//        rx.Observable observable1 = rx.Observable.just("hello","is","a");
//        observable1.subscribe(subscriber);

//        String[] s = {"haha","sss","aaa"};
//        rx.Observable.from(s).subscribe(subscriber);

        Action1<String> onNext = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG,"--------------onNext#call() be called");
            }
        };

        Action1<Throwable> onError = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d(TAG,"--------------onError#call() be called");
            }
        };

        Action0 onCompleted = new Action0() {
            @Override
            public void call() {
                Log.d(TAG,"--------------onCompleted#call() be called");
            }
        };

        observable.subscribe(onNext);
        observable.subscribe(onNext,onError);
        observable.subscribe(onNext,onError,onCompleted);

    }
}
