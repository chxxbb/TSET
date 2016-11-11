package com.example.chen.tset.Utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/10/7 0007.
 */
public class InquiryrecordHelper extends SQLiteOpenHelper {
    public InquiryrecordHelper(Context context) {
        super(context, "inquiryrecord.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table inquiry(username text,doctorid text,doctorname text,doctoricon text,time text,state text,id text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
