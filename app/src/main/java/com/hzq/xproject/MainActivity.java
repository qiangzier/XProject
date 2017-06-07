package com.hzq.xproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.wequick.small.Small;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startImageApp(View view){
        Small.openUri("image", this); // open bundles.main Launch Activity
//         Small.openUri("main", this);// 指定跳转到app.main.page2
    }

    public void startHybridApp(View view){
        Small.openUri("hybrid", this); // open bundles.main Launch Activity
    }

    public void startGsonApp(View view){
        Small.openUri("gson", this); // open bundles.main Launch Activity
    }

    public void startRxjavaApp(View view){
        Small.openUri("rxjava", this); // open bundles.main Launch Activity
    }
}
