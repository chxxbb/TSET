package com.example.chen.tset.Utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chen.tset.Data.entity.Inquiryrecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/7 0007.
 * 用于保存问诊记录
 */
public class InquiryrecordDao {
    private Context context;
    private InquiryrecordHelper inquiryrecordHelper;
    private SQLiteDatabase db;

    public InquiryrecordDao(Context context) {
        this.context = context;
        inquiryrecordHelper = new InquiryrecordHelper(context);
        db = inquiryrecordHelper.getReadableDatabase();
    }

    //添加
    public void addInquiryrecord(Inquiryrecord inquiryrecord) {
        ContentValues values = new ContentValues();
        values.put("username", inquiryrecord.getUsername());
        values.put("id", inquiryrecord.getId());
        values.put("doctorid", inquiryrecord.getDoctorid());
        values.put("doctorname", inquiryrecord.getDoctorname());
        values.put("doctoricon", inquiryrecord.getDoctoricon());
        values.put("time", inquiryrecord.getTime());
        values.put("state", inquiryrecord.getState());
        db.insert("inquiry", null, values);
    }


    //根据用户ID查找查找
    public List<Inquiryrecord> chatfind(String username) {
        List<Inquiryrecord> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from inquiry where username=?", new String[]{username});
        while (cursor.moveToNext()) {
            String doctorid = cursor.getString(cursor.getColumnIndex("doctorid"));
            String doctorname = cursor.getString(cursor.getColumnIndex("doctorname"));
            String doctoricon = cursor.getString(cursor.getColumnIndex("doctoricon"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String state = cursor.getString(cursor.getColumnIndex("state"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            Inquiryrecord inquiryrecord = new Inquiryrecord(username, doctorid, id, doctorname, doctoricon, time, state);
            list.add(inquiryrecord);
        }
        return list;
    }

    //查找
    public Inquiryrecord chatfinddoctor(String doctorid) {
        Inquiryrecord inquiryrecord = null;
        Cursor cursor = db.rawQuery("select * from inquiry where doctorid=?", new String[]{doctorid});
        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String doctorname = cursor.getString(cursor.getColumnIndex("doctorname"));
            String doctoricon = cursor.getString(cursor.getColumnIndex("doctoricon"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String state = cursor.getString(cursor.getColumnIndex("state"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
           inquiryrecord = new Inquiryrecord(username, doctorid, id, doctorname, doctoricon, time, state);

        }
        return inquiryrecord;
    }

    public void closedb()
    {
        db.close();
    }
}
