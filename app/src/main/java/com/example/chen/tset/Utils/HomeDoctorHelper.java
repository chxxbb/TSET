package com.example.chen.tset.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class HomeDoctorHelper extends SQLiteOpenHelper {
    public HomeDoctorHelper(Context context) {
        super(context, "homedoctor", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table doctor(id text,icon text,title text,username text,chatCost text,name text,hospital text,adept text,section text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
