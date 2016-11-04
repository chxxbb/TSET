package com.example.chen.tset.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chen.tset.Data.Inquiry;
import com.example.chen.tset.Data.Inquiryrecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class HomeDoctorDao {
    private Context context;
    private HomeDoctorHelper homeDoctorHelper;
    private SQLiteDatabase db;

    public HomeDoctorDao(Context context) {
        this.context = context;
        homeDoctorHelper = new HomeDoctorHelper(context);
        db = homeDoctorHelper.getReadableDatabase();
    }


    //添加
    public void addHomeDoctor(Inquiry inquiry) {
        ContentValues values = new ContentValues();
        values.put("id", inquiry.getId());
        values.put("icon", inquiry.getIcon());
        values.put("title", inquiry.getTitle());
        values.put("username", inquiry.getUsername());
        values.put("chatCost", inquiry.getChatCost());
        values.put("name", inquiry.getName());
        values.put("hospital", inquiry.getHospital());
        values.put("adept", inquiry.getAdept());
        values.put("section", inquiry.getSection());
        db.insert("doctor", null, values);
    }


    public List<Inquiry> findHomeDoctor(){
        ArrayList<Inquiry> inquiryArrayList=new ArrayList<>();
        Cursor cursor=db.query("doctor",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String id=cursor.getString(cursor.getColumnIndex("id"));
            String icon=cursor.getString(cursor.getColumnIndex("icon"));
            String title=cursor.getString(cursor.getColumnIndex("title"));
            String username=cursor.getString(cursor.getColumnIndex("username"));
            String chatCost=cursor.getString(cursor.getColumnIndex("chatCost"));
            String name=cursor.getString(cursor.getColumnIndex("name"));
            String hospital=cursor.getString(cursor.getColumnIndex("hospital"));
            String adept=cursor.getString(cursor.getColumnIndex("adept"));
            String section=cursor.getString(cursor.getColumnIndex("section"));
            Inquiry inquiry=new Inquiry(icon,title,name,chatCost,adept,section,hospital,id,username);
            inquiryArrayList.add(inquiry);

        }
        return inquiryArrayList;
    }


    public void deldotor() {
        db.delete("doctor", null, null);
    }

}
