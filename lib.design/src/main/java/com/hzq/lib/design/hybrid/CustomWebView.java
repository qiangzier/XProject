package com.hzq.lib.design.hybrid;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.hzq.lib.design.hybrid.handler.AutoGenerateHandler;
import com.hzq.lib.design.hybrid.handler.BaseHandler;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * @author: hezhiqiang
 * @date: 17/1/16
 * @version:
 * @description:
 */

public class CustomWebView extends WebView {
    private Context context;
    private JavaScriptBridge bridge;

    public CustomWebView(Context context) {
        this(context,null);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        this.context = context;
        //设置支持js
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

//        this.getSettings().setUserAgentString(""); //设置设备信息

        //设置支持缩放
        this.getSettings().setSupportZoom(true);
        this.setWebViewClient(new CustomWebViewClient(context));
        this.setWebChromeClient(new CustomWebChromeClient(context));

        //webview缓存相关
        this.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); //不是用缓存
        this.getSettings().setAppCacheEnabled(false);               //不启用webview缓存
//        this.getSettings().setAppCachePath("");                   //webview缓存路径
        this.getSettings().setAppCacheMaxSize(5*1024*1024);         //webview缓存大小,

        //Local Storage缓存
        this.getSettings().setDatabaseEnabled(false);
        this.getSettings().setDomStorageEnabled(true);
        String databasePath = context.getDir("databases", Context.MODE_PRIVATE).getPath();
        this.getSettings().setDatabasePath(databasePath);

        //不支持缩放
        this.getSettings().setBuiltInZoomControls(false);
        this.getSettings().setSupportZoom(false);
        this.getSettings().setDisplayZoomControls(false);
        this.getSettings().setUseWideViewPort(false);    //支持根据设备缩放,自适应屏幕

        this.getSettings().setAllowFileAccess(true);    //允许WebView访问本地文件数据
        this.getSettings().setSupportMultipleWindows(false);    //不允许webview打开多窗口

        this.getSettings().setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        this.getSettings().setLoadsImagesAutomatically(true);  //支持自动加载图片

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            this.getSettings().setAllowUniversalAccessFromFileURLs(true);
            this.getSettings().setAllowFileAccessFromFileURLs(true);
        }

        setInitialScale(0); //设置缩放等级

        bridge = new JavaScriptBridge(context) {
            @Override
            protected void loadJavaScriptToWebView(String jsUrl) {
                excuteJs(jsUrl);
            }
        };
//        bridge.registerHandler(new AlertDialogHandler());
//        bridge.registerHandler(new ToastHandler());
//        bridge.registerHandler(new Camerahandler());

        //通过注解自动生成要注入的handle
        bridge.registerHandler(AutoGenerateHandler.load());
        this.addJavascriptInterface(bridge, "native");

    }

    /**
     * 执行js方法
     * @param jsMethod
     */
    public void excuteJs(String jsMethod){
        if(TextUtils.isEmpty(jsMethod)){
            throw new NullPointerException("jsMethod is null");
        }
        String url = jsMethod;
        if(!jsMethod.contains("javascript:")){
            url = "javascript:" + jsMethod;
        }
        final String finalUrl = url;
        post(new Runnable() {
            @Override
            public void run() {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    //专门用于异步调用JavaScript方法，并且能够得到一个回调结果。
                    evaluateJavascript(finalUrl, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }else{
                    loadUrl(finalUrl);
                }
            }
        });
    }

    /**
     * 有特殊需求得handler可以通过此方法注册到webView中
     * @param baseHandler
     * @return
     */
    public WebView registerHandler(BaseHandler baseHandler){
        if(bridge != null){
            bridge.registerHandler(baseHandler);
        }
        return this;
    }

    @Override
    public void destroy() {
        bridge.onDestroy();
        super.destroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        bridge.onActivityResult(requestCode, resultCode, intent);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        bridge.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
