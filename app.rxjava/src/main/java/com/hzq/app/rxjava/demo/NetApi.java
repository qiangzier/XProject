package com.hzq.app.rxjava.demo;

import android.net.Uri;

import com.hzq.app.rxjava.demo.callback.Observable;
import com.hzq.app.rxjava.demo.callback.Observer;
import com.hzq.app.rxjava.demo.model.Response;

/**
 * @author: hezhiqiang
 * @date: 17/4/26
 * @version:
 * @description:
 */

public class NetApi {

    /**
     * 第一步实现
     * @param page
     * @param itemCount
     * @return
     */
    public Response getList(int page, int itemCount){
        return null;
    }

    public Uri save(String url){
        return null;
    }

    /**
     * 第二步实现
     * @param page
     * @param itemCount
     * @param observer
     */
    public void getList(int page, int itemCount, Observer<Response> observer){
        //这里应该是通过网络拉取数据,先简单new一个对象
        Response response = new Response();
        observer.onNext(response);
    }

    //异步保存数据
    public void save(String url, Observer<Uri> observer){
        Uri uri = Uri.parse("hzq://ahsihi");
        observer.onNext(uri);
    }

    /**
     * 第三步实现
     * @param page
     * @param itemCount
     * @return
     */
    public Observable<Response> getAsyncList(int page, int itemCount){
        Observable.OnSubscriber<Response> onSubscriber = new Observable.OnSubscriber<Response>() {
            @Override
            public void call(final Observer<Response> responseObserver) {
                new Thread() {
                    @Override
                    public void run() {
                        Response response = Response.create();
                        responseObserver.onNext(response);
                    }
                }.start();
            }
        };
        return new Observable<Response>(onSubscriber);
    }

    public Observable<Uri> save1(final String url){
        return new Observable<Uri>(new Observable.OnSubscriber<Uri>() {
            @Override
            public void call(final Observer<Uri> uriObserver) {
                new Thread(){
                    @Override
                    public void run() {
                        Uri uri = Uri.parse("hzq://ahsihi" + url);
                        uriObserver.onNext(uri);
                    }
                }.start();
            }
        });
    }


}
