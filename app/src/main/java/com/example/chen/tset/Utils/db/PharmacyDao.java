package com.example.chen.tset.Utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chen.tset.Data.entity.Pharmacyremind;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/8 0008.
 * 用于保存用药提醒，后变更用药提醒保存在服务器中
 */
public class PharmacyDao {
    private Context context;
    private PharmacyHelper pharmacyHelper;
    private SQLiteDatabase db;

    public PharmacyDao(Context context) {
        this.context = context;
        pharmacyHelper = new PharmacyHelper(context);
        db = pharmacyHelper.getReadableDatabase();
    }

    //添加
    public void addPharmacy(Pharmacyremind pharmacyremind) {
        ContentValues values = new ContentValues();
        values.put("username", pharmacyremind.getUsername());
        values.put("startdate", pharmacyremind.getStartdate());
        values.put("overdate", pharmacyremind.getOverdate());
        values.put("time1", pharmacyremind.getTime1());
        values.put("content1", pharmacyremind.getContent1());
        values.put("time2", pharmacyremind.getTime2());
        values.put("content2", pharmacyremind.getContent2());
        values.put("time3", pharmacyremind.getTime3());
        values.put("content3", pharmacyremind.getContent3());
        db.insert("pharmacy", null, values);
    }


    //查找
    public List<Pharmacyremind> chatfind(String username) {
        List<Pharmacyremind> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from pharmacy where username=?", new String[]{username});
        while (cursor.moveToNext()) {
            String startdate = cursor.getString(cursor.getColumnIndex("startdate"));
            String overdate = cursor.getString(cursor.getColumnIndex("overdate"));
            String time1 = cursor.getString(cursor.getColumnIndex("time1"));
            String content1 = cursor.getString(cursor.getColumnIndex("content1"));
            String time2 = cursor.getString(cursor.getColumnIndex("time2"));
            String content2 = cursor.getString(cursor.getColumnIndex("content2"));
            String time3 = cursor.getString(cursor.getColumnIndex("time3"));
            String content3 = cursor.getString(cursor.getColumnIndex("content3"));
            Pharmacyremind pharmacyremind = new Pharmacyremind(username, startdate, overdate, time1, content1, time2, content2, time3, content3);
            list.add(pharmacyremind);
        }
        return list;
    }

    public void del() {
        db.delete("pharmacy", null, null);
    }
}
