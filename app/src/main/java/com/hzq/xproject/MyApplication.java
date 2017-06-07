package com.hzq.xproject;

import android.app.Application;

import net.wequick.small.Small;

/**
 * @author: hezhiqiang
 * @date: 17/1/19
 * @version:
 * @description:
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Small.preSetUp(this);

        Small.setBaseUri("xpt://hzq.com/mode/");// 浏览器跳转url
        Small.setUp(this, null);
        // Small.setUp(this, new Small.OnCompleteListener(){...});
    }
}
