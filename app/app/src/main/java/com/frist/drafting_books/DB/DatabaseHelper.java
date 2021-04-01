package com.frist.drafting_books.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "DraftingBooks.db";
    public static String TABLE_NAME = "t_userinfo";
    public static String TABLE_NAME2 = "t_books";

    public String COL_2 = "username";
    public String COL_3 = "password";
    public String COL_4 = "marks";
    //--||username|password|nickname|booksID||--
    //--||booksID|book|
    public static final String CREATE_FRIEND = "create table if not exists friend("
            + "userid integer ,"
            + "friendid integer,"
            + "name text,"
            + "birthday text," + "gender integer," + "photo blob)";
    public static final String CREATE_MESSAGE = "create table if not exists message("
            + "userid integer,"
            + "senderid integer,"
            + "name text,"
            + "sendtime text,"
            + "content text,"
            + "unread integer,"
            + "type integer)";
    public static final String CREATE_CHAT_MESSAGE = "create table if not exists chat_message("
            + "userid integer,"
            + "friendid integer,"
            + "type integer,"
            + "sendtime text," + "content text)";

    //Simplified constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create the database
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "id integer primary key autoincrement," +
                "name text," +
                "surname text," +
                "marks integer)");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME2 + " (" +
                "id integer primary key autoincrement," +
                "name text," +
                "surname text," +
                "marks integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertData(String name, String surname, String marks) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, marks);

        //Table name, null and the content values are needed as param

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            //Insert has failed
            return false;
        } else {
            //Successful insertion
            return true;
        }
    }

    public Cursor readData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }
}