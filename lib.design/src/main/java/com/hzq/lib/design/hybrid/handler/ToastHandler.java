package com.hzq.lib.design.hybrid.handler;

import android.widget.Toast;

import com.annotation.JsHandler;
import com.hzq.lib.design.hybrid.HybridConstants;
import com.hzq.lib.design.hybrid.JavaScriptBridge;
import com.hzq.lib.design.hybrid.JsCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: hezhiqiang
 * @date: 17/1/17
 * @version:
 * @description:
 */

@JsHandler(HybridConstants.HandlerName.ALERT_TOAST_METHOD)
public class ToastHandler extends BaseHandler {
    @Override
    public void handle(JavaScriptBridge bridge, JSONObject params, JsCallback callback, String hName) {
        if(!params.has(HybridConstants.ParamsName.TITLE)){
            throw new NullPointerException("must be has title!");
        }
        String title = null;
        try {
            title = params.getString(HybridConstants.ParamsName.TITLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(bridge.getmContext(),title , Toast.LENGTH_SHORT).show();
    }

    @Override
    public String[] handleName() {
        return new String[]{HybridConstants.HandlerName.ALERT_TOAST_METHOD};
    }
}
