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
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.nostra13.universalimageloader.core.ImageLoader;
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
public class PersonaldataActivity extends AppCompatActivity {
    private RelativeLayout rl_name, rl_gender, rl_phone, rl_icon;
    private ImageView iv_icon;
    private TextView tv_name, tv_phone, tv_sex;
    private LinearLayout ll_rutmypage;
    private File sdcardTempFile;
    private int crop = 180;
    private File audioFile;
    SharedPsaveuser sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata);
        sp = new SharedPsaveuser(PersonaldataActivity.this);
        findView();
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
            if(User_Http.user.getName()==null&User_Http.user.getPhone()==null){
                Toast.makeText(PersonaldataActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }else {
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
                        audioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/files/");
                        audioFile.mkdirs();//创建文件夹
                        try {
                            sdcardTempFile = File.createTempFile("recording", ".jpg", audioFile);
                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                        Intent intent3 = new Intent("android.intent.action.PICK");
                        intent3.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                        intent3.putExtra("output", Uri.fromFile(sdcardTempFile));
                        intent3.putExtra("crop", "true");
                        intent3.putExtra("aspectX", 1);// 裁剪框比例
                        intent3.putExtra("aspectY", 1);
                        intent3.putExtra("outputX", crop);// 输出图片大小
                        intent3.putExtra("outputY", crop);
                        intent3.putExtra("return-data", true);
                        intent3.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                        intent3.putExtra("noFaceDetection", true);
                        startActivityForResult(intent3, 100);
                        break;
                }
            }



        }
    };
    private View.OnClickListener finlistener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {

        OkHttpUtils
                .postFile()
                .url(Http_data.http_data + "/changeIcon" + "?" + User_Http.user.getId())
                .file(sdcardTempFile)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if(sdcardTempFile!=null){
                            Toast.makeText(PersonaldataActivity.this, "头像修改失败", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("头像返回", response);
                        if (resultCode == RESULT_OK) {
                            Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
                            iv_icon.setImageBitmap(bmp);
                        }
                        String icon = sdcardTempFile.getAbsolutePath();
                        Log.e("图片", icon);
                        //将更改过的头像保存在本地，并清除用户实体类中头像
                        sp.setUsericon(icon);
                        User_Http.user.setIcon(null);

                    }
                });

    }


}


