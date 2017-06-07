package com.hzq.app.hybrid;

import android.os.Bundle;
import android.view.View;

import com.hzq.lib.design.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
    }

    public void onStartHybrid(View view){
        String url = "http://192.168.6.104:8080/hybird/start.html";
        startActivity(HybridWebActivity.buildIntent(this,url));
    }
}
