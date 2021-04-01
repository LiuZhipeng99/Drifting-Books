package com.frist.drafting_books.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cn.leancloud.AVObject;

public class LeancloudDB {
    private static LeancloudDB db; //单例模式，静态的自己对象，实现异步的静态的get实例方法（懒汉模式）

    private final String TableUser = "t_userinfo";
    private AVObject t_user;
    private LeancloudDB(){
        t_user = new AVObject(TableUser);

    }
    public synchronized static LeancloudDB getInstance(){
        if(db == null){
            db = new LeancloudDB();
        }
        return db;
    }
    public void addUser(String name){
        t_user.put("name",name);
        t_user.put("col2","test");
        t_user.saveInBackground().subscribe();
    }
}
