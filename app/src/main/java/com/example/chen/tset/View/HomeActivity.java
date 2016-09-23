package com.example.chen.tset.View;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.chen.tset.Data.Chatcontent;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.JPErrorCode;
import com.example.chen.tset.Data.User;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.Utils.ChatpageDao;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.page.ConsultingFragment;
import com.example.chen.tset.page.EncyclopediaFragment;
import com.example.chen.tset.R;
import com.example.chen.tset.page.InquiryFragment;
import com.example.chen.tset.page.InquiryView;
import com.example.chen.tset.page.LectureroomFragment;
import com.example.chen.tset.page.MypageFragment;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Set;
import java.util.logging.Handler;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    FragmentManager fm;
    FragmentTransaction ft;
    private RadioButton rb_encyclopedia, rb_lectureroom, rb_mypage, rb_diagnosis;
    private RadioGroup radioGroup_right, radioGroup_left;
    private FrameLayout framelayout, fl_registration;
    private EncyclopediaFragment encyclopediaFragment;
    private LectureroomFragment lectureroomFragment;
    private MypageFragment mypageFragment;
    private InquiryFragment inquiryFragment;
    private ConsultingFragment consultingFragment;
    private InquiryView iv_inquiry;
    ChatpageDao db;
    Set<User> set;
    File sdcardTempFile;
    File audioFile;
    String icon;
    Integer id;
    String name;
    String phone;
    String gender;
    SharedPsaveuser sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        JMessageClient.registerEventReceiver(this);
        JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);
        db = new ChatpageDao(this);
        findView();
        init();
        jmessage();
        Chatcontent chatcontent = new Chatcontent(null, 0L, null, null, null, User_Http.user.getPhone());
        db.addchatcont(chatcontent);
        sp = new SharedPsaveuser(HomeActivity.this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        spStorage();
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }

    //将用户基本信息保存在本地
    private void spStorage() {
        if(User_Http.user.getId()!=null){
            id = User_Http.user.getId();
            name = User_Http.user.getName();
            phone = User_Http.user.getPhone();
            gender = User_Http.user.getGender();
            sp.setspUser(id, phone, name, gender);
            Log.e("保存",sp.getTag().toString());
        }


    }





    private void jmessage() {
        JMessageClient.register(User_Http.user.getPhone(), "123456", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Log.e("jmessage", "注册成功");
                } else if (i == 898001) {
                    Log.e("jmessage", "注册失败");
                }
            }
        });

        JMessageClient.login(User_Http.user.getPhone(), "123456", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Log.e("jmessage", "登录成功");
                } else {
                    Log.e("jmessage", "登录失败");
                }
            }
        });
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onEventMainThread(MessageEvent event) {
        Message msg = event.getMessage();
        switch (msg.getContentType()) {
            case text:


                TextContent textContent = (TextContent) msg.getContent();
                String content = textContent.getText();
                String username = msg.getFromID();
                Date dt = new Date();
                Long time = dt.getTime();
                Chatcontent chatcontent = new Chatcontent("2" + content, time, null, null, username, User_Http.user.getPhone());
                db.addchatcont(chatcontent);
                break;
            case image:


                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                String mfile = imageContent.getLocalPath();//图片本地地址 无效
                String file = imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                Log.e("接收的图片", file);
                Date dt1 = new Date();
                Long time1 = dt1.getTime();
                chatcontent = new Chatcontent("2*2", time1, file, file, msg.getTargetID(), User_Http.user.getPhone());
                db.addchatcont(chatcontent);

        }

    }

    public void onEvent(NotificationClickEvent event) {
        Message msg = event.getMessage();
        Intent intent = new Intent(HomeActivity.this, ChatpageActivity.class);
        intent.putExtra("sendID", msg.getTargetID());
        startActivity(intent);//自定义跳转到指定页面
    }


    private void findView() {
        rb_encyclopedia = (RadioButton) findViewById(R.id.rb_encyclopedia);
        framelayout = (FrameLayout) findViewById(R.id.framelayout);
        rb_lectureroom = (RadioButton) findViewById(R.id.rb_lectureroom);
        rb_mypage = (RadioButton) findViewById(R.id.rb_mypage);
        rb_diagnosis = (RadioButton) findViewById(R.id.rb_diagnosis);
        radioGroup_left = (RadioGroup) findViewById(R.id.radioGroup_left);
        radioGroup_right = (RadioGroup) findViewById(R.id.radioGroup_right);
        iv_inquiry = (InquiryView) findViewById(R.id.iv_inquiry);
        fl_registration = (FrameLayout) findViewById(R.id.fl_registration);
        rb_encyclopedia.setChecked(true);
        rb_encyclopedia.setOnClickListener(this);
        rb_lectureroom.setOnClickListener(this);
        rb_mypage.setOnClickListener(this);
        rb_diagnosis.setOnClickListener(this);
        iv_inquiry.setOnClickListener(this);
        fl_registration.setOnClickListener(listener);
    }



    private void init() {
        fm = getSupportFragmentManager();
        rb_encyclopedia.performClick();
    }



    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (encyclopediaFragment != null) fragmentTransaction.hide(encyclopediaFragment);
        if (lectureroomFragment != null) fragmentTransaction.hide(lectureroomFragment);
        if (mypageFragment != null) fragmentTransaction.hide(mypageFragment);
        if (inquiryFragment != null) fragmentTransaction.hide(inquiryFragment);
        if (consultingFragment != null) fragmentTransaction.hide(consultingFragment);
    }


    @Override
    public void onClick(View v) {
        ft = fm.beginTransaction();
        hideAllFragment(ft);
        switch (v.getId()) {
            case R.id.rb_encyclopedia:
                radioGroup_right.clearCheck();
                fl_registration.setVisibility(View.GONE);
                if (encyclopediaFragment == null) {
                    encyclopediaFragment = new EncyclopediaFragment();
                    ft.add(R.id.framelayout, encyclopediaFragment);
                } else {
                    ft.show(encyclopediaFragment);
                }
                break;
            case R.id.rb_lectureroom:
                fl_registration.setVisibility(View.GONE);
                radioGroup_left.clearCheck();
                if (lectureroomFragment == null) {
                    lectureroomFragment = new LectureroomFragment();
                    ft.add(R.id.framelayout, lectureroomFragment);
                } else {
                    ft.show(lectureroomFragment);
                }
                break;
            case R.id.rb_mypage:
                fl_registration.setVisibility(View.GONE);
                radioGroup_left.clearCheck();
                if (mypageFragment == null) {
                    mypageFragment = new MypageFragment();
                    ft.add(R.id.framelayout, mypageFragment);
                } else {
                    ft.show(mypageFragment);
                }
                break;
            case R.id.rb_diagnosis:
                fl_registration.setVisibility(View.GONE);
                radioGroup_right.clearCheck();
                if (consultingFragment == null) {
                    consultingFragment = new ConsultingFragment();
                    ft.add(R.id.framelayout, consultingFragment);
                } else {
                    ft.show(consultingFragment);
                }
                break;
            case R.id.iv_inquiry:
                radioGroup_right.clearCheck();
                radioGroup_left.clearCheck();
                fl_registration.setVisibility(View.VISIBLE);
                if (inquiryFragment == null) {
                    inquiryFragment = new InquiryFragment();
                    ft.add(R.id.framelayout, inquiryFragment);
                } else {
                    ft.show(inquiryFragment);
                }
                break;
        }
        ft.commit();

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HomeActivity.this, RegistrationAtivity.class);
            startActivity(intent);
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
}
