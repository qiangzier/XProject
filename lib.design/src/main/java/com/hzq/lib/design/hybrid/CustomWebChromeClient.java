package com.hzq.lib.design.hybrid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * @author: hezhiqiang
 * @date: 17/1/17
 * @version:
 * @description:
 */

public class CustomWebChromeClient  extends WebChromeClient {
    Context context;
    public CustomWebChromeClient(Context context){
        this.context = context;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        //拦截并改造js的alert提示框样式
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 17/1/16

            }
        });
        builder.create().show();
        result.confirm();
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        result.confirm();
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        result.confirm();
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    //通知应用程序当前网页加载的进度
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }

    //获取网页title标题,获取标题的时间主要取决于网页前段设置标题的位置，一般设置在页面加载前面，可以较早调用到这个函数
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        Log.d("onReceivedTitle-->",title);
    }

    //网页中有H5播放flash video的时候按下全屏按钮将会调用到这个方法，一般用作设置网页播放全屏操作
    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
    }

    //对应的取消全屏方法
    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
    }

    //在Android 5.0 API 21后 借助新的 onShowFileChooser() 方法,
    // 您现在不但可以在 WebView 中使用输入表单字段,而且可以启动文件选择器从 Android 设备中选择图片和文件
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }
}
