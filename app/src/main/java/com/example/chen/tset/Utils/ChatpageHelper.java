package com.example.chen.tset.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class ChatpageHelper extends SQLiteOpenHelper {
    public ChatpageHelper(Context context) {
        super(context, "tsetchat.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table chat(username text,time text,content text,masterfile text,file text,myname text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
