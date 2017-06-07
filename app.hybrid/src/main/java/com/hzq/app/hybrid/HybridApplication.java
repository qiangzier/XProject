package com.hzq.app.hybrid;

import android.app.Application;

import com.tencent.smtt.sdk.QbSdk;

/**
 * @author: hezhiqiang
 * @date: 17/1/22
 * @version:
 * @description:
 */

public class HybridApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化X5内核
        QbSdk.initX5Environment(this,null);
    }
}
