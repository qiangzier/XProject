package com.hzq.app.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hzq.lib.design.activity.BaseActivity;

/**
 * @author: hezhiqiang
 * @date: 2017/6/6
 * @version:
 * @description:
 */

public class DashBoardActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        DashBoardView view = (DashBoardView) findViewById(R.id.dashView);
        view.setDegree(300);
    }
}
