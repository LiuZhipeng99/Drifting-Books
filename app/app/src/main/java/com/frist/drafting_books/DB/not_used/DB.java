package com.frist.drafting_books.DB.not_used;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

//用单例模式。单独把opendb的操作方法解耦出来，只保留了必要方法
public class DB {
    private static DB db; //单例模式，静态的自己对象，实现异步的静态的get实例方法（懒汉模式）
    private SQLiteDatabase sqldb; //需要是可写权限，给其他操作方法使用

    private DB(Context ctx){
        DatabaseHelper dbHelper = new DatabaseHelper(ctx);
        sqldb = dbHelper.getWritableDatabase();
    }
    public synchronized static DB getInstance(Context ctx){
        if(db == null){
            db = new DB(ctx);
        }
        return db;
    }

    public void saveBook(){

    }
    public List<String> getAllBooks(){
        List<String> books = new ArrayList<>();
        return books;
    }
}
