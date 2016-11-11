package com.example.chen.tset.Utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chen.tset.Data.entity.FindAllHot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class HomeFindAllHotDao {
    private Context context;
    private HomeFindAllHotHelper homeBannerHelper;
    private SQLiteDatabase db;

    public HomeFindAllHotDao(Context context) {
        this.context = context;
        homeBannerHelper = new HomeFindAllHotHelper(context);
        db = homeBannerHelper.getReadableDatabase();
    }

    public void addHomeFindAllHot(FindAllHot findAllHot) {
        ContentValues values = new ContentValues();
        values.put("site", findAllHot.getSite());
        values.put("id", findAllHot.getId());
        values.put("title", findAllHot.getTitle());
        db.insert("findallht", null, values);
    }

    public List<FindAllHot> findHomeFindAllHot() {
        ArrayList<FindAllHot> bannerArrayList = new ArrayList<>();
        Cursor cursor = db.query("findallht", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String site = cursor.getString(cursor.getColumnIndex("site"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            FindAllHot findAllHot = new FindAllHot(site, id, title);
            bannerArrayList.add(findAllHot);

        }
        return bannerArrayList;
    }

    public void delHomeFindAllHot() {
        db.delete("findallht", null, null);
    }
}
