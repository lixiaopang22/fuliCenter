package com.example.administrator.fulicenter;

import android.app.Application;

import com.example.administrator.fulicenter.bean.User;

/**
 * Created by Administrator on 2016/10/17.
 */
public class FuLiCenterApplication extends Application{
    private static FuLiCenterApplication instance;

    private static String username;
    private static User user;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        FuLiCenterApplication.username = username;
    }

    public FuLiCenterApplication(){
        instance=this;
    }
    public static FuLiCenterApplication getInstance(){
        if(instance==null){
            instance=new FuLiCenterApplication();
        }
        return instance;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        FuLiCenterApplication.user = user;
    }
}
