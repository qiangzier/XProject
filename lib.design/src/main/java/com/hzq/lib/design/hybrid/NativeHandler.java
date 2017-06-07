package com.hzq.lib.design.hybrid;

import org.json.JSONObject;

/**
 * @author: hezhiqiang
 * @date: 17/1/17
 * @version:
 * @description:
 */

public interface NativeHandler {
    /**
     *
     * @param bridge
     * @param params    js调用native时回传的参数
     * @param callback  js回调接口
     * @param hName     js调用native的事件名称
     */
    void handle(JavaScriptBridge bridge, JSONObject params, JsCallback callback, String hName);

    String[] handleName();
}
