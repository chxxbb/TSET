package com.example.chen.tset.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.soundcloud.android.crop.Crop;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;

/**
 * 修改头像页面,已改为个人资料页面点击修改头像
 */
public class PersonaldataActivity extends MyBaseActivity {
    private RelativeLayout rl_name, rl_gender, rl_phone, rl_icon;
    private ImageView iv_icon;
    private TextView tv_name, tv_phone, tv_sex;
    private LinearLayout ll_rutmypage;
    private File sdcardTempFile;
    private File audioFile;
    private int crop = 180;
    SharedPsaveuser sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata);
        sp = new SharedPsaveuser(PersonaldataActivity.this);
        findView();
        audioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/text/icon/");
        audioFile.mkdirs();//创建文件夹
    }

    private void findView() {
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        ll_rutmypage = (LinearLayout) findViewById(R.id.ll_rutmypage);
        rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_icon = (RelativeLayout) findViewById(R.id.rl_icon);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        rl_name.setOnClickListener(listener);
        rl_gender.setOnClickListener(listener);
        rl_phone.setOnClickListener(listener);
        rl_icon.setOnClickListener(listener);
        ll_rutmypage.setOnClickListener(finlistener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //判断是否是联网状态 如果用户实体类中数据为空 则使用本地保存的数据
        if (User_Http.user.getGender() == null) {
            tv_sex.setText(sp.getTag().getGender());
        } else {
            tv_sex.setText(User_Http.user.getGender());
        }


        if (User_Http.user.getName() == null) {
            tv_name.setText(sp.getTag().getName());
        } else {
            tv_name.setText(User_Http.user.getName());
        }


        if (User_Http.user.getPhone() == null) {
            tv_phone.setText(sp.getTag().getPhone());
            rl_phone.setOnClickListener(null);
        } else {
            tv_phone.setText(User_Http.user.getPhone());
            rl_phone.setOnClickListener(null);
        }


        if ((User_Http.user.getIcon() == null || User_Http.user.getIcon().equals("")) && (sp.getTag().getIcon() == null || sp.getTag().getIcon().equals(""))) {

            iv_icon.setBackgroundResource(R.drawable.default_icon);


        } else if (User_Http.user.getIcon() == null || User_Http.user.getIcon().equals("")) {
            ImageLoader.getInstance().displayImage("file:///" + sp.getTag().getIcon(), iv_icon);
        } else {

            ImageLoader.getInstance().displayImage(User_Http.user.getIcon(), iv_icon);
        }


    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (User_Http.user.getName() == null & User_Http.user.getPhone() == null) {
                Toast.makeText(PersonaldataActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            } else {
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
                        Crop.pickImage(PersonaldataActivity.this);


                        break;
                }
            }


        }
    };
    private View.OnClickListener finlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    //截取图片
    private void beginCrop(Uri source) {
        try {
            sdcardTempFile = File.createTempFile(".icon", ".jpg", audioFile);
        } catch (IOException e) {

            e.printStackTrace();
        }

        Uri destination = Uri.fromFile(sdcardTempFile);
        Crop.of(source, destination).asSquare().start(this);
    }


    //保存头像
    private void handleCrop(final int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            OkHttpUtils
                    .postFile()
                    .url(Http_data.http_data + "/changeIcon" + "?" + User_Http.user.getId())
                    .file(sdcardTempFile)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (sdcardTempFile != null) {

                            }
                            Log.e("失败", "失败");

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("头像返回", response);

                            Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());

                            iv_icon.setImageBitmap(bmp);

                            String icon = sdcardTempFile.getAbsolutePath();

                            //将更改过的头像保存在本地，并清除用户实体类中头像
                            sp.setUsericon(icon);
                            User_Http.user.setIcon(null);

                        }
                    });


        } else if (resultCode == Crop.RESULT_ERROR) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }



}


