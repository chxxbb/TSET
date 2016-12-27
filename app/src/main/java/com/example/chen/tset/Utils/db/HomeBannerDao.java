package com.example.chen.tset.Utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chen.tset.Data.entity.DiseaseBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class HomeBannerDao {
    private Context context;
    private HomeBannerHelper homeBannerHelper;
    private SQLiteDatabase db;

    public HomeBannerDao(Context context) {
        this.context = context;
        homeBannerHelper = new HomeBannerHelper(context);
        db = homeBannerHelper.getReadableDatabase();
    }

    public void addbanner(DiseaseBanner banner) {
        ContentValues values = new ContentValues();
        values.put("site", banner.getSite());
        values.put("id", banner.getId());
        values.put("cover", banner.getCover());
        values.put("cyclopediaId", banner.getCyclopediaId());
        db.insert("banner", null, values);
    }

    public List<DiseaseBanner> findHomebanner() {
        ArrayList<DiseaseBanner> bannerArrayList = new ArrayList<>();
        Cursor cursor = db.query("banner", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String site = cursor.getString(cursor.getColumnIndex("site"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String cover = cursor.getString(cursor.getColumnIndex("cover"));
            String cyclopediaId = cursor.getString(cursor.getColumnIndex("cyclopediaId"));
            DiseaseBanner banner = new DiseaseBanner(site, id, cover, cyclopediaId);
            bannerArrayList.add(banner);

        }
        return bannerArrayList;
    }

    public void delbanner() {
        db.delete("banner", null, null);
    }

    public void closedb(){
        db.close();
    }
}
