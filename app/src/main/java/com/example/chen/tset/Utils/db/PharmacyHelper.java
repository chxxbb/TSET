package com.example.chen.tset.Utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/10/8 0008.
 */
public class PharmacyHelper extends SQLiteOpenHelper{
    public PharmacyHelper(Context context) {
        super(context, "pharmacyremind.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table pharmacy(username text,startdate text,overdate text,time1 text,content1 text,time2 text,content2 text,time3 text,content3 text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
