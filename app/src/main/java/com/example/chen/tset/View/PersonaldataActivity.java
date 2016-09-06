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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

public class PersonaldataActivity extends AppCompatActivity {
    private RelativeLayout rl_name, rl_gender, rl_phone, rl_icon;
    private ImageView iv_icon;
    private LinearLayout ll_rutmypage;
    private File sdcardTempFile;
    private int crop = 180;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata);
        sdcardTempFile = new File("/sdcard/", "tmp_pic_" + SystemClock.currentThreadTimeMillis() + ".jpg");
        findView();
    }

    private void findView() {
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        ll_rutmypage = (LinearLayout) findViewById(R.id.ll_rutmypage);
        rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_icon = (RelativeLayout) findViewById(R.id.rl_icon);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        rl_name.setOnClickListener(listener);
        ll_rutmypage.setOnClickListener(listener);
        rl_gender.setOnClickListener(listener);
        rl_phone.setOnClickListener(listener);
        rl_icon.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_name:
                    Intent intent = new Intent(PersonaldataActivity.this, NamepageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rl_gender:
                    Intent intent1 = new Intent(PersonaldataActivity.this, SexpageActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.rl_phone:
                    Intent intent2 = new Intent(PersonaldataActivity.this, PhonechangeActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.rl_icon:
                    Intent intent3 = new Intent("android.intent.action.PICK");
                    intent3.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                    intent3.putExtra("output", Uri.fromFile(sdcardTempFile));
                    intent3.putExtra("crop", "true");
                    intent3.putExtra("aspectX", 1);// 裁剪框比例
                    intent3.putExtra("aspectY", 1);
                    intent3.putExtra("outputX", crop);// 输出图片大小
                    intent3.putExtra("outputY", crop);
                    startActivityForResult(intent3, 100);
//                    Intent intent3 = new Intent(PersonaldataActivity.this, IconmanageActivity.class);
//                    startActivity(intent3);
                    break;
                case R.id.ll_rutmypage:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
            iv_icon.setImageBitmap(bmp);
        }
        OkHttpUtils
                .postFile()
                .url(Http_data.http_data + "/changeIcon" + "?1")
                .file(sdcardTempFile)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(PersonaldataActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("头像返回", response);

                    }
                });
    }
}
