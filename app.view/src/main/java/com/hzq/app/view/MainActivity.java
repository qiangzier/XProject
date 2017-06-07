package com.hzq.app.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hzq.app.view.expandablerecycle.ExpandableRecycleActivity;
import com.hzq.lib.design.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startBottomNavigationView(View view){
        startActivity(new Intent(MainActivity.this,BottombarActivity.class));
    }

    public void startExpandableRecycle(View view){
        startActivity(new Intent(MainActivity.this, ExpandableRecycleActivity.class));
    }

    public void dash(View view){
        startActivity(new Intent(MainActivity.this, DashBoardActivity.class));
    }

    public void testDialog(View view){
        Dialog dialog = new Dialog(this);
        TextView t = new TextView(this);
        t.setText("Test dialog");
        dialog.setContentView(t);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }


}
