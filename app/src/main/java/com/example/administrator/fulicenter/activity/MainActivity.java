package com.example.administrator.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.utils.L;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.i("main","MainActivity onCreate()");
    }
    public void onCheckedChange(View view){

    }
}
