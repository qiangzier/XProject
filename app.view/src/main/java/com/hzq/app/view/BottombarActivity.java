package com.hzq.app.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;

import com.hzq.lib.design.activity.BaseActivity;

/**
 * @author: hezhiqiang
 * @date: 17/5/8
 * @version:
 * @description:
 */

public class BottombarActivity extends BaseActivity {

    BottomNavigationView navigation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_bar_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(BottombarActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
//                switch (item.getItemId()){
//                    case R.id.home:
//                        break;
//                    case R.id.im:
//                        break;
//                    case R.id.share:
//                        break;
//                    case R.id.contact:
//                        break;
//                }
                return true;
            }
        });
    }
}
