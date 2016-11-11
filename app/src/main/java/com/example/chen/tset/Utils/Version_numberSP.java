package com.example.chen.tset.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
public class Version_numberSP {
    Context context;

    public Version_numberSP(Context context) {
        this.context = context;
    }

    //设置版本号
    public void setspversionNumber(String versionNumber) {
        SharedPreferences sp = context.getSharedPreferences("version_umber", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("versionNumber",versionNumber);
        editor.commit();
    }

    //获取版本号
    public String getversionNumber() {
        SharedPreferences sp = context.getSharedPreferences("version_umber", Context.MODE_PRIVATE);
        String versionNumber=sp.getString("versionNumber",null);
        return versionNumber;
    }
}
