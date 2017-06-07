package com.hzq.lib.design.hybrid;

/**
 * @author: hezhiqiang
 * @date: 17/1/17
 * @version:
 * @description: js回调接口
 */

public interface JsCallback {

    /**
     * js回调接口
     * @param response
     */
    void callback(NativeResponse response);

    /**
     * 校验
     * @return
     */
    boolean isValid();
}
