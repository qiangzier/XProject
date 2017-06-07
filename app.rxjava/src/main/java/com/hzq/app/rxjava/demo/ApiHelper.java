package com.hzq.app.rxjava.demo;

import android.net.Uri;

import com.hzq.app.rxjava.demo.callback.Observable;
import com.hzq.app.rxjava.demo.callback.Observer;
import com.hzq.app.rxjava.demo.callback.Func1;
import com.hzq.app.rxjava.demo.model.NewsItem;
import com.hzq.app.rxjava.demo.model.Response;

import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 17/4/26
 * @version:
 * @description:
 */

public class ApiHelper {

    private NetApi netApi;
    public ApiHelper(){
        netApi = new NetApi();
    }

    /**
     * 第一步:同步处理
     * @return
     */
    public Uri getAndSaveLastTitle(){
        Response response = netApi.getList(0,20);
        String title = findLastTitle(response);
        Uri uri = netApi.save(title);
        return uri;
    }

    /**
     * 第二步:异步处理
     * @return
     */
    public void getAndSaveLastTitle1(final Observer<Uri> callBack){
        netApi.getList(0, 20, new Observer<Response>(){
            @Override
            public void onNext(Response response) {
                String title = findLastTitle(response);
                netApi.save(title, new Observer<Uri>() {
                    @Override
                    public void onNext(Uri uri) {
                        callBack.onNext(uri);
                    }

                    @Override
                    public void error(Exception e) {
                        callBack.equals(e);
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
            }

            @Override
            public void error(Exception e) {
                callBack.equals(e);
            }

            @Override
            public void onCompleted() {

            }
        });
    }

    /**
     * 第三步:优化异步处理
     * @return
     */
    public Observable<Uri> getAndSaveLastTitle1(){
        //1.
        final Observable<Response> asyncList = netApi.getAsyncList(0, 20);

        final Observable<String> titleJob = asyncList.map(new Func1<Response, String>() {
            @Override
            public String call(Response response) {
                return findLastTitle(response);
            }
        });

        Observable<Uri> uriObservable = titleJob.flitMap(new Func1<String, Observable<Uri>>() {
            @Override
            public Observable<Uri> call(String s) {
                return netApi.save1(s);
            }
        });

        //2.
        /*final Observable<String> titleJob = new Observable<String>() {
            @Override
            public void subscribe(final Observer<String> callback) {
                asyncList.subscribe(new Observer<Response>() {
                    @Override
                    public void onNext(Response response) {
                        String title = findLastTitle(response);
                        callback.onNext(title);
                    }

                    @Override
                    public void error(Exception e) {
                        callback.error(e);
                    }
                });
            }
        };*/

        //3.
        /*Observable<Uri> uriAsyncJob1 = new Observable<Uri>() {
            @Override
            public void subscribe(final Observer<Uri> callback) {
                map.subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String s) {
                        Observable<Uri> uriAsyncJob1 = netApi.save1(s);
                        uriAsyncJob1.subscribe(new Observer<Uri>() {
                            @Override
                            public void onNext(Uri uri) {
                                callback.onNext(uri);
                            }

                            @Override
                            public void error(Exception e) {
                                callback.error(e);
                            }
                        });
                    }

                    @Override
                    public void error(Exception e) {
                        callback.error(e);
                    }
                });
            }
        };*/

        return uriObservable;

        /*return new Observable<Uri>() {
            @Override
            public void subscribe(final Observer<Uri> callback) {
                Observable<Response> asyncList = netApi.getAsyncList(0, 20);
                asyncList.subscribe(new Observer<Response>() {
                    @Override
                    public void onNext(Response response) {
                        String title = findLastTitle(response);
                        Observable<Uri> uriObservable = netApi.save1(title);
                        uriObservable.subscribe(new Observer<Uri>() {
                            @Override
                            public void onNext(Uri uri) {
                                callback.onNext(uri);
                            }

                            @Override
                            public void error(Exception e) {
                                callback.error(e);
                            }
                        });
                    }

                    @Override
                    public void error(Exception e) {
                        callback.error(e);
                    }
                });
            }
        };*/
    }

    private String findLastTitle(Response response) {
        List<NewsItem> list = response.result.list;
        return list.get(list.size() - 1).title;
    }
}
