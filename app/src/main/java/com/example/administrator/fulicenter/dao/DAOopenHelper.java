package com.example.administrator.fulicenter.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.fulicenter.utils.I;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DAOopenHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION=1;
    private static DAOopenHelper instance;
    private static final String FILICENTER_USER_TABLE_CREATE="CREATE TABLE "
            + UserDao.USER_TABLE_NAME + " ("
            +UserDao.USER_COLUMN_NAME + "TEXT PRIMARY KEY,"
            +UserDao.USER_COLUMN_NICK + " TEXT,"
            +UserDao.USER_COLUMN_AVATAR_ID + " INTEGER,"
            +UserDao.USER_COLUMN_AVATAR_TYPE + " TEXT,"
            +UserDao.USER_COLUMN_AVATAR_PATH +" TEXT,"
            +UserDao.USER_COLUMN_AVATART_SUFFIX + " TEXT);";

    public static DAOopenHelper getInstance(Context context) {
        if(instance==null){
            instance=new DAOopenHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DAOopenHelper(Context context){
        super(context,getUserDataName(),null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(FILICENTER_USER_TABLE_CREATE);
    }

    private static String getUserDataName(){
        return I.User.TABLE_NAME + "_demo.db";
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void closeDB() {
        if(instance!=null){
            SQLiteDatabase db = instance.getWritableDatabase();
            db.close();
            instance=null;
        }
    }
}
