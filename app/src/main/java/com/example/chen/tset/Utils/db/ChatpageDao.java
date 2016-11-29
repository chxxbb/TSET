package com.example.chen.tset.Utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chen.tset.Data.entity.Chatcontent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/19 0019.
 * 用于保存聊天记录
 */
public class ChatpageDao {
    private Context context;
    private ChatpageHelper chatpageHelper;
    private SQLiteDatabase db;

    public ChatpageDao(Context context) {
        this.context = context;
        chatpageHelper = new ChatpageHelper(context);
        db = chatpageHelper.getReadableDatabase();
    }

    //添加
    public void addchatcont(Chatcontent chatcontent) {
        ContentValues values = new ContentValues();
        values.put("username", chatcontent.getUsername());
        values.put("time", chatcontent.getTime());
        values.put("content", chatcontent.getContent());
        values.put("masterfile", chatcontent.getMasterfile());
        values.put("file", chatcontent.getFile());
        values.put("myname", chatcontent.getMyname());
        db.insert("chat", null, values);
    }


    //查找
    public List<Chatcontent> chatfind(String username) {
        List<Chatcontent> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from chat where username=?", new String[]{username});
        while (cursor.moveToNext()) {
            String content = cursor.getString(cursor.getColumnIndex("content"));
            long time = cursor.getLong(cursor.getColumnIndex("time"));
            String masterfile = cursor.getString(cursor.getColumnIndex("masterfile"));
            String file = cursor.getString(cursor.getColumnIndex("file"));
            String myname = cursor.getString(cursor.getColumnIndex("myname"));
            Chatcontent chatcontent = new Chatcontent(content, time, file, masterfile, username, myname);
            list.add(chatcontent);
        }

        return list;
    }




}
