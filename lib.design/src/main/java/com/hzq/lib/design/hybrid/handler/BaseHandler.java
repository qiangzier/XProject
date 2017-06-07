package com.hzq.lib.design.hybrid.handler;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.hzq.lib.design.hybrid.NativeHandler;

/**
 * @author: hezhiqiang
 * @date: 17/1/17
 * @version:
 * @description:
 */

public abstract class BaseHandler implements NativeHandler {

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //do noting;
    }

    public void onDestroy() {

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

    }
}
