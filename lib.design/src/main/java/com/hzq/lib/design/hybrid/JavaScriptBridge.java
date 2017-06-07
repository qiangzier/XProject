package com.hzq.lib.design.hybrid;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.hzq.lib.design.hybrid.handler.BaseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: hezhiqiang
 * @date: 17/1/17
 * @version:
 * @description: js调用native时所绑定的对象,连接js与native的桥梁
 */

public abstract class JavaScriptBridge {
    public String TAG = JavaScriptBridge.class.getSimpleName();

    private Context mContext;
    private Handler mHandler = new Handler();
    /**注册native接口,供js调用**/
    private Map<String,BaseHandler> handlerMap = new HashMap<>();

    public JavaScriptBridge(@NonNull Context context){
        this.mContext = context;
    }

    public Context getmContext(){
        return this.mContext;
    }

    public JavaScriptBridge registerHandler(String name, BaseHandler baseHandler){
        if(name == null){
            throw new NullPointerException("name mustn't be null");
        }
        if(baseHandler == null){
            throw new NullPointerException("handler mustn't be null");
        }
        handlerMap.put(name,baseHandler);
        return this;
    }

    public JavaScriptBridge registerHandler(BaseHandler baseHandler){
        String[] keys = baseHandler.handleName();
        if(keys == null){
            throw new NullPointerException("name mustn't be null");
        }
        for (String key : keys) {
            registerHandler(key,baseHandler);
        }
        return this;
    }

    public JavaScriptBridge registerHandler(Map<String,BaseHandler> maps){
        if(maps != null){
            for (Map.Entry<String,BaseHandler> entry : maps.entrySet()) {
                registerHandler(entry.getKey(),entry.getValue());
            }
        }
        return this;
    }

    /**
     * native暴露给js的统一入口
     * @param handle        调用的方法名称
     * @param params        参数 json结构,必须与js端约定
     * @param callbackId    回调js方法id
     */
    @JavascriptInterface
    public void callNative(final String handle, String params, String callbackId){
        final NativeHandler nativeHandler = handlerMap.get(handle);
        final JsCallback jsCallback = new JavaScriptCallBack(callbackId);
        if(nativeHandler == null){
            jsCallback.callback(new NativeResponse(NativeResponse.STATUS_FAILED,
                    "sorry, no handler registered for " + handle));
            return;
        }

        JSONObject json = null;
        if(!TextUtils.isEmpty(params)){
            try {
                json = new JSONObject(params);
            } catch (JSONException e) {
                jsCallback.callback(new NativeResponse(NativeResponse.STATUS_FAILED,e.getMessage()));
                return;
            }
        }

        final JSONObject finalJson = json;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                nativeHandler.handle(JavaScriptBridge.this, finalJson,jsCallback,handle);
            }
        });
    }

    class JavaScriptCallBack implements JsCallback{
        private String callbackId;
        public JavaScriptCallBack(String callId){
            this.callbackId = callId;
        }

        @Override
        public void callback(NativeResponse response) {
            if(isValid()){
                Log.e(TAG,"callback id is null");
                return;
            }

            final String jsCommand = String.format("javascript:bridgeCallJS('%s', '%s');",
                    callbackId,
                    doubleEscapeString(response.toJson()));
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    loadJavaScriptToWebView(jsCommand);
                }
            });
        }

        @Override
        public boolean isValid() {
            return TextUtils.isEmpty(callbackId);
        }
    }

    private String doubleEscapeString(String javascript) {
        String result;
        result = javascript.replace("\\", "\\\\");
        result = result.replace("\"", "\\\"");
        result = result.replace("\'", "\\\'");
        result = result.replace("\n", "\\n");
        result = result.replace("\r", "\\r");
        result = result.replace("\f", "\\f");
        return result;
    }

    protected abstract void loadJavaScriptToWebView(String jsUrl);

    public void onDestroy(){
        for (BaseHandler baseHandler : handlerMap.values()) {
            baseHandler.onDestroy();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        for (BaseHandler baseHandler : handlerMap.values()) {
            baseHandler.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        for (BaseHandler baseHandler : handlerMap.values()) {
            baseHandler.onActivityResult(requestCode,resultCode,intent);
        }
    }
}
