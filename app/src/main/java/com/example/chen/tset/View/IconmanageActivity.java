package com.example.chen.tset.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

public class IconmanageActivity extends AppCompatActivity {
    private ImageView iv_manage, iv_ico;
    private File sdcardTempFile;
    private int crop = 180;
    private LinearLayout ll_rutgender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iconmanage);
        sdcardTempFile = new File("/mnt/sdcard/", "tmp_pic_" + SystemClock.currentThreadTimeMillis() + ".jpg");
        findView();
    }

    private void findView() {
        iv_ico = (ImageView) findViewById(R.id.iv_icon);
        iv_manage = (ImageView) findViewById(R.id.iv_manage);
        ll_rutgender = (LinearLayout) findViewById(R.id.ll_rutgender);
        ll_rutgender.setOnClickListener(listener);
        iv_manage.setOnClickListener(listener);
        iv_ico.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_manage:
                    Intent intent = new Intent("android.intent.action.PICK");
                    intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                    intent.putExtra("output", Uri.fromFile(sdcardTempFile));
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 1);// 裁剪框比例
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", crop);// 输出图片大小
                    intent.putExtra("outputY", crop);
                    startActivityForResult(intent, 100);
                    break;
                case R.id.ll_rutgender:
                    OkHttpUtils
                            .postFile()
                            .url(Http_data.http_data + "/changeIcon" + "?1")
                            .file(sdcardTempFile)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Toast.makeText(IconmanageActivity.this, "修改头像失败", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.e("头像返回", response);

                                }
                            });
//                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
            iv_ico.setImageBitmap(bmp);
        }
    }
}
