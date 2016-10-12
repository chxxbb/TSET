package com.example.chen.tset.View;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.chen.tset.Data.Chatcontent;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.JPErrorCode;
import com.example.chen.tset.Data.Pharmacyremind;
import com.example.chen.tset.Data.User;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.Utils.ChatpageDao;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.PharmacyDao;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

public class HomeActivity extends MyBaseActivity implements View.OnClickListener {
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
    Context context;

    public static HomeActivity text_homeactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        text_homeactivity = this;
        JMessageClient.registerEventReceiver(this);
        JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);
        db = new ChatpageDao(this);
        findView();
        init();

        Chatcontent chatcontent = new Chatcontent(null, 0L, null, null, null, User_Http.user.getPhone());
        db.addchatcont(chatcontent);
        sp = new SharedPsaveuser(HomeActivity.this);


        context = getBaseContext();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);

        //保存头像
        if (sp.getTag().getIcon() == null && User_Http.user.getIcon() != null) {

            saveicon();
        }

        jmessage();

        delpharmacy();


        versionUpdate();


    }

    private void versionUpdate() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/VersionUpdate")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("更新", "失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("更新返回", response);
                    }
                });
//        Uri packageURI = Uri.parse("package:com.example.chen.tset");
//        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
//        startActivity(uninstallIntent);

    }

    private void saveicon() {

        new Thread() {
            public void run() {
                try {
                    audioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/text/icon/");
                    audioFile.mkdirs();//创建文件夹
                    sdcardTempFile = File.createTempFile(".icon", ".jpg", audioFile);
                    String urlPath = User_Http.user.getIcon();
                    URL url = new URL(urlPath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(6 * 1000);  // 注意要设置超时，设置时间不要超过10秒，避免被android系统回收
                    if (conn.getResponseCode() != 200) throw new RuntimeException("请求url失败");
                    InputStream inSream = conn.getInputStream();
                    //把图片保存到项目的根目录
                    readAsFile(inSream, new File(String.valueOf(sdcardTempFile)));
                    String icon = sdcardTempFile.getAbsolutePath();
                    //将更改过的头像保存在本地
                    sp.setUsericon(icon);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //将获取的头像转换成流
    public static void readAsFile(InputStream inSream, File file) throws Exception {
        FileOutputStream outStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inSream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inSream.close();

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
        if (User_Http.user.getId() != null) {
            id = User_Http.user.getId();
            name = User_Http.user.getName();
            phone = User_Http.user.getPhone();
            gender = User_Http.user.getGender();
            sp.setspUser(id, phone, name, gender);
        }


    }


    //登录jmeeage
    private void jmessage() {

        String username = null;
        if (User_Http.user.getPhone() == null) {
            username = sp.getTag().getPhone();
        } else {
            username = User_Http.user.getPhone();
        }


        JMessageClient.register(username, "123456", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Log.e("jmessage", "注册成功");
                } else if (i == 898001) {
                    Log.e("jmessage", "注册失败");
                }


            }
        });

        JMessageClient.login(username, "123456", new BasicCallback() {
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


    //接收其他用户发送的消息，会显示到通知栏
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
                Chatcontent chatcontent = new Chatcontent("2" + content, time, null, null, username, sp.getTag().getPhone());
                db.addchatcont(chatcontent);
                break;
            case image:

                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                String mfile = imageContent.getLocalPath();//图片本地地址 无效
                String file = imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址

                Date dt1 = new Date();
                Long time1 = dt1.getTime();
                chatcontent = new Chatcontent("2*2", time1, file, file, msg.getTargetID(), sp.getTag().getPhone());
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


    private void registerjudge() {
        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/login")
                .addParams("phone", sp.getTag().getPhone())
                .addParams("password", sp.getTag().getPassword())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("返回", response);

                        if (response.equals("1")) {

                            Toast.makeText(HomeActivity.this, "密码被修改", Toast.LENGTH_SHORT).show();
                            sp.clearinit();

                            SharedPreferences sp1 = getSharedPreferences("jmlogin", MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp1.edit();
                            edit.putBoolean("isclick", true);
                            edit.commit();

                            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Gson gson = new Gson();
                            User user = gson.fromJson(response, User.class);
                            Log.e("user", user.toString());
                            User_Http.user.setUser(user);
                        }

                    }
                });
    }

    //当前时间大于用户设置的用药提醒时间 则删除这条用药提醒
    private void delpharmacy() {
        PharmacyDao pharmacyDao = new PharmacyDao(this);


        if (sp.getTag().getPhone() != null) {
            List<Pharmacyremind> list = pharmacyDao.chatfind(sp.getTag().getPhone());
            if (list != null) {
                Date d1 = null;
                try {

                    for (int i = 0; i < list.size(); i++) {
                        String date = list.get(i).getOverdate();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        String str = formatter.format(curDate);


                        d1 = new SimpleDateFormat("yyyy年MM月dd").parse(date);
                        SimpleDateFormat nian = new SimpleDateFormat("yyyy");
                        SimpleDateFormat yue = new SimpleDateFormat("MM");
                        SimpleDateFormat ri = new SimpleDateFormat("dd");
                        String nian1 = nian.format(d1);
                        String yue1 = yue.format(d1);
                        String ri1 = ri.format(d1);

                        int pharmacydate = Integer.parseInt(nian1 + yue1 + ri1);
                        int currentdate = Integer.parseInt(str);

                        if (currentdate > pharmacydate) {
                            pharmacyDao.del();
                            Log.e("删除用药", "删除");
                        }

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


    }


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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }


}
