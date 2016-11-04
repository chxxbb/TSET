package com.example.chen.tset.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class HomeFindAllHotHelper extends SQLiteOpenHelper {
    public HomeFindAllHotHelper(Context context) {
        super(context, "homefindallht.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table findallht(site text,id text,title text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
