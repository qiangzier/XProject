package com.hzq.app.gson;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = (TextView) findViewById(R.id.txt);
    }

    public void click(View view){
        String str = Convert.toJson(build());
        txt.setText(str);
    }

    public UserModel build(){
        UserModel m = new UserModel();
        m.id = "1a";
        m.name = "lily";
        m.address = "china";
        return m;
    }
}
