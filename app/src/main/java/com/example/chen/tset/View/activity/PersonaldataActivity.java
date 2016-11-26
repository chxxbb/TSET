package com.example.chen.tset.View.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.qiniu.android.common.AutoZone;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.soundcloud.android.crop.Crop;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;

/**
 * 个人资料，修改头像此页面可直接修改
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
        //头像的保存文件夹
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

        /**
         * 判断是否是联网状态 如果用户实体类中数据为空 则使用本地保存的数据，显示头像，昵称等
         */
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


        //如果从后台获取到的头像为空，则显示默认头像
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

                    try {

                        //打开图片选择需要截取的图片
                        Crop.pickImage(PersonaldataActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(PersonaldataActivity.this, "打开图库失败，请查看是否开启权限或稍后再试", Toast.LENGTH_SHORT).show();
                    }

                    break;

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
            //图片文件，icon前面+“.”用于可以在用户手机上隐藏此头像，避免用户误删除
            sdcardTempFile = File.createTempFile(".icon", ".jpg", audioFile);
        } catch (IOException e) {

            e.printStackTrace();
        }

        Uri destination = Uri.fromFile(sdcardTempFile);
        //获取到了需要截取的头像，将头像给予截取页面进行截取
        Crop.of(source, destination).asSquare().start(this);
    }


    String uptoken;

    //保存头像
    private void handleCrop(final int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {


            OkHttpUtils
                    .get()
                    .url(Http_data.http_data + "/getToken")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(PersonaldataActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            uptoken = response;
                            h.sendEmptyMessage(1);
                        }
                    });


        } else if (resultCode == Crop.RESULT_ERROR) {

        }
    }


    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            boolean https = true;
            Zone z1 = new AutoZone(https, null);
            Configuration config = new Configuration.Builder().zone(z1).build();

            //new一个uploadManager类
            UploadManager uploadManager = new UploadManager(config);

            //获取当前日期时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

            Date curDate = new Date(System.currentTimeMillis());

            String time = formatter.format(curDate);

            //设置上传后文件的key
            String upkey = sp.getTag().getId() + time;

            uploadManager.put(sdcardTempFile, upkey, uptoken,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            //res包含hash、key等信息，具体字段取决于上传策略的设置

                            changeIcon(key);
                        }
                    }, null);


        }
    };


    public void changeIcon(String key) {

        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/changeIcon")
                .addParams("userId", sp.getTag().getId() + "")
                .addParams("fileName", key)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        Toast.makeText(PersonaldataActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (response.equals("2")) {
                            Toast.makeText(PersonaldataActivity.this, "修改头像失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());

                            iv_icon.setImageBitmap(bmp);

                            String icon = sdcardTempFile.getAbsolutePath();

                            //将更改过的头像保存在本地，并清除用户实体类中头像，使显示的头像为当前最新头像
                            sp.setUsericon(icon);
                            User_Http.user.setIcon(null);

                            Toast.makeText(PersonaldataActivity.this, "修改头像成功", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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


