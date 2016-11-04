package com.example.chen.tset.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class HomeBannerHelper extends SQLiteOpenHelper{
    public HomeBannerHelper(Context context) {
        super(context, "homebanner.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table banner(site text,id text,cover text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
