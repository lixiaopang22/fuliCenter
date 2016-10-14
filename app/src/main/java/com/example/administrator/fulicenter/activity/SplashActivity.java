package com.example.administrator.fulicenter.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.utils.MFGT;

public class SplashActivity extends AppCompatActivity {
    static final long sleepTime=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MFGT.gotoMainActivity(SplashActivity.this);
                //MFGT.finish(SplashActivity.this);
                finish();
            }
        },sleepTime);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                long start = System.currentTimeMillis();
//                long costTime = System.currentTimeMillis() - start;
//                if(sleepTime-costTime>0){
//                    try {
//                        //耗时操作
//                        Thread.sleep(sleepTime-costTime);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                finish();
//                MFGT.gotoMainActivity(SplashActivity.this);
//                MFGT.finish(SplashActivity.this);
//            }
//        }).start();
    }
}
