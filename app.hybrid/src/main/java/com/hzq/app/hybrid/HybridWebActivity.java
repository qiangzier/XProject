package com.hzq.app.hybrid;

import android.content.Intent;
import android.view.View;

import com.hzq.lib.design.activity.BaseActivity;
import com.hzq.lib.design.hybrid.CustomWebView;

/**
 * @author: hezhiqiang
 * @date: 17/1/22
 * @version:
 * @description:
 */

public class HybridWebActivity extends WebActivity {
    public static final String URL = "url";
    public static Intent buildIntent(BaseActivity activity, String url){
        Intent intent = new Intent(activity, HybridWebActivity.class);
        intent.putExtra(URL,url);
        return intent;
    }


    @Override
    protected int getResourceId() {
        return R.layout.web_layout;
    }

    @Override
    protected void initData() {
        webView.loadUrl(getIntent().getStringExtra(URL));
    }

    @Override
    protected void hiddenToolbar() {
        findViewById(R.id.app_bar).setVisibility(View.GONE);
    }

    @Override
    protected void initWebViewObj() {
        webView = (CustomWebView) findViewById(R.id.webView);
        initToolbar();
    }
}
