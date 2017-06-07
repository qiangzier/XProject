package com.hzq.lib.design.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hzq.lib.design.R;

/**
 * @author: hezhiqiang
 * @date: 17/1/22
 * @version:
 * @description:
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar mToolbar;

    protected void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(mToolbar == null){
            throw new NullPointerException("未找到对应的标题栏资源");
        }
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
