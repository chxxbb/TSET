package com.example.chen.tset.Utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class HomeEassayHelper extends SQLiteOpenHelper{
    public HomeEassayHelper(Context context) {
        super(context, "homeessay.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table essay(id text,icon text,title text,content text,time text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
