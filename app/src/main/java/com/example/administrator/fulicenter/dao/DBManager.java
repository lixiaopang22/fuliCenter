package com.example.administrator.fulicenter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.fulicenter.bean.User;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DBManager {
    private static DBManager dbMg=new DBManager();
    private DAOpenHelper dbHelper;
    //打开数据库的操作
    void onInit(Context context) {
        dbHelper = new DAOpenHelper(context);
    }
    //synchronized表示只能操作其中一条指令
    public static synchronized DBManager getInstance(){
        return dbMg;
    }
    //关闭数据库的操作
    public synchronized void closeDB(){
        if(dbHelper!=null){
            dbHelper.closeDB();
        }
    }
    //保存到数据库中
    public synchronized boolean saveUser(User user){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME,user.getMuserName());
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID,user.getMavatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_PATH,user.getMavatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE,user.getMavatarLastUpdateTime());
        if(db.isOpen()){
            //重新登录或者改密码后重新覆盖，成功后返回-1
            return db.replace(UserDao.USER_TABLE_NAME,null,values)!=-1;
        }
        return false;
    }
    public synchronized User getUser(String username) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String sql="select * from "+UserDao.USER_TABLE_NAME + " where "+UserDao.USER_COLUMN_NAME + "=?";
        User user=null;
        //结果集，封装到User里
        Cursor cursor=db.rawQuery(sql,new String[]{username});
        if (cursor.moveToNext()) {
            user=new User();
            user.setMuserName(username);
            user.setMuserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICK)));
            user.setMavatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID)));
            user.setMavatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
            user.setMavatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
            user.setMavatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
            user.setMavatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LASTUPDATE)));
            return user;
        }
        return user;
    }

    public synchronized boolean updateUser(User user) {
        int count=-1;
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String sql=UserDao.USER_COLUMN_NAME + "=?";
        ContentValues values=new ContentValues();
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        if(db.isOpen()){
            count=db.update(UserDao.USER_TABLE_NAME,values,sql,new String[]{user.getMuserName()});
        }
        return count>0;
    }
}
