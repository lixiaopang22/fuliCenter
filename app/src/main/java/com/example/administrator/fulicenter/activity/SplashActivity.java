package com.example.administrator.fulicenter.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.fulicenter.FuLiCenterApplication;
import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.User;
import com.example.administrator.fulicenter.dao.SharedPreferenceUtils;
import com.example.administrator.fulicenter.dao.UserDao;
import com.example.administrator.fulicenter.utils.MFGT;


public class SplashActivity extends AppCompatActivity {
    static final long sleepTime=5000;
    SplashActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext=this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user= FuLiCenterApplication.getUser();
                String username = SharedPreferenceUtils.getInstance(mContext).getUser();
                if(user==null&&username!=null){
                    UserDao dao=new UserDao(mContext);
                    user = dao.getUser(username);
                }
                if(user!=null){
                    FuLiCenterApplication.setUser(user);
                }
                MFGT.gotoMainActivity(SplashActivity.this);
                finish();
            }
        },sleepTime);
    }
}
