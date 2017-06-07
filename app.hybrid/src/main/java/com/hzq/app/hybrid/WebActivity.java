package com.hzq.app.hybrid;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.hzq.lib.design.activity.BaseActivity;
import com.hzq.lib.design.hybrid.CustomWebView;
import com.hzq.lib.design.hybrid.handler.ToolbarHandler;

/**
 * @author: hezhiqiang
 * @date: 17/1/22
 * @version:
 * @description:    不允许直接使用
 */

public abstract class WebActivity extends BaseActivity {

    protected CustomWebView webView;
    protected ToolbarHandler mToolbarHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceId());
        //禁止键盘弹起
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.
                SOFT_INPUT_STATE_HIDDEN);
        initWebViewObj();
        if(webView == null)
            throw new NullPointerException("webview is null");
        initWebView();
        initData();
    }

    protected abstract int getResourceId();

    protected abstract void initData();

    protected abstract void hiddenToolbar();

    protected abstract void initWebViewObj();

    private void initWebView(){
        // 添加了chrome:inspect bebug功能
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)){
                webView.setWebContentsDebuggingEnabled(true);
            }
        }

        mToolbarHandler = new ToolbarHandler(mToolbar){
            @Override
            public void onHiddenTitleBar(boolean hidden) {
                if(hidden){
                    hiddenToolbar();
                }
            }

            @Override
            public void onBackPressed() {
                if(webView.canGoBack()){
                    webView.goBack();
                }else{
                    finish();
                }
            }
        };
        webView.registerHandler(mToolbarHandler);
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
        if(mToolbarHandler != null && mToolbarHandler.isDisableBack()){
            return;
        }

        //检测是否是返回键并且有可以返回的历史记录
        if(webView.canGoBack()){
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    public void AndroidToJsMethod(View view){
        //测试app调用js方法
        webView.loadUrl("javascript:androidToJs('这是个Android原生页面调用JavaScript的测试')");
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webView.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        webView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
