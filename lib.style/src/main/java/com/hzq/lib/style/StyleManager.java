package com.hzq.lib.style;

import android.app.Application;

import net.wequick.small.Small;

/**
 * @author: hezhiqiang
 * @date: 17/1/19
 * @version:
 * @description:
 */

public class StyleManager extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Small.setWebActivityTheme(R.style.AppTheme);
    }
}
