package com.example.chen.tset.Utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chen.tset.Data.entity.Consult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class HomeEassayDao {
    private Context context;
    private HomeEassayHelper homeEassayHelper;
    private SQLiteDatabase db;

    public HomeEassayDao(Context context) {
        this.context = context;
        homeEassayHelper = new HomeEassayHelper(context);
        db = homeEassayHelper.getReadableDatabase();
    }

    public void addHomeEassay(Consult consult) {
        ContentValues values = new ContentValues();
        values.put("id", consult.getId());
        values.put("icon", consult.getIcon());
        values.put("title", consult.getTitle());
        values.put("content", consult.getContent());
        values.put("time", consult.getTime());
        values.put("collectCount", consult.getCollectCount());
        db.insert("essay", null, values);
    }

    public List<Consult> findHomeEassay() {
        ArrayList<Consult> consultArrayList = new ArrayList<>();
        Cursor cursor = db.query("essay", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String icon = cursor.getString(cursor.getColumnIndex("icon"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int collectCount = cursor.getInt(cursor.getColumnIndex("collectCount"));
            Consult consult = new Consult(id, icon, title, content, time, 0, collectCount);
            consultArrayList.add(consult);

        }
        return consultArrayList;
    }

    public void delHomeEassay() {
        db.delete("essay", null, null);
    }
}
