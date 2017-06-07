package com.hzq.app.image;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @author: hezhiqiang
 * @date: 17/1/19
 * @version:
 * @description:
 */

public class ImageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化fresco
        Fresco.initialize(this);
    }
}
