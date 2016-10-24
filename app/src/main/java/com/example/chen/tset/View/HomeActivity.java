package com.example.chen.tset.View;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Dialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chen.tset.Data.Chatcontent;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Inquiryrecord;
import com.example.chen.tset.Data.JPErrorCode;
import com.example.chen.tset.Data.Pharmacyremind;
import com.example.chen.tset.Data.User;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.Utils.ChatpageDao;
import com.example.chen.tset.Utils.InquiryrecordDao;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.PharmacyDao;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.page.AFragment;
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
import java.util.ArrayList;
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
    private AFragment a;
    private ImageView iv_inquiry;
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
    private Dialog setHeadDialog;
    private View dialogView;

    public static HomeActivity text_homeactivity;

    Animation translateAnimation;
    Animation translateAnimation1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        text_homeactivity = this;

        //即时通信，用于接收消息
        JMessageClient.registerEventReceiver(this);
        JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);
        db = new ChatpageDao(this);
        findView();
        init();

        //将用户的手机号保存在本地
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
                    conn.setConnectTimeout(6 * 1000);  // 设置时间不要超过10秒，避免被android系统回收
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

    /***
     * JMessageClient.NOTI_MODE_DEFAULT 显示通知，有声音，有震动。
     * JMessageClient.NOTI_MODE_NO_SOUND 显示通知，无声音，有震动。
     * JMessageClient.NOTI_MODE_NO_VIBRATE 显示通知，有声音，无震动。
     * JMessageClient.NOTI_MODE_SILENCE 显示通知，无声音，无震动。
     * JMessageClient.NOTI_MODE_NO_NOTIFICATION 不显示通知。
     */

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
                //将接送到的文本消息保存在本地数据库中，给予标记用于去区分是接送到的消息还是发送的消息
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
                //将接送到的文本消息保存在本地数据库中，给予标记用于去区分是接送到的消息还是发送的消息
                chatcontent = new Chatcontent("2*2", time1, file, file, msg.getTargetID(), sp.getTag().getPhone());
                db.addchatcont(chatcontent);

        }

    }


    //接送到的消息显示到通知栏，点击后跳转到对应的聊天页面
    public void onEvent(NotificationClickEvent event) {

        InquiryrecordDao db = new InquiryrecordDao(this);
        Message msg = event.getMessage();
        Inquiryrecord inquiryrecord = db.chatfinddoctor(msg.getTargetID());

        Intent intent = new Intent(HomeActivity.this, ChatpageActivity.class);
        intent.putExtra("doctorID", inquiryrecord.getId());
        intent.putExtra("name", inquiryrecord.getDoctorname());
        intent.putExtra("icon", inquiryrecord.getDoctoricon());
        intent.putExtra("username", inquiryrecord.getDoctorid());
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
        iv_inquiry = (ImageView) findViewById(R.id.iv_inquiry);
        fl_registration = (FrameLayout) findViewById(R.id.fl_registration);
        rb_encyclopedia.setChecked(true);
        rb_encyclopedia.setOnClickListener(this);
        rb_lectureroom.setOnClickListener(this);
        rb_mypage.setOnClickListener(this);
        rb_diagnosis.setOnClickListener(this);
        iv_inquiry.setOnClickListener(inquirylistener);
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
        if (consultingFragment != null) fragmentTransaction.hide(consultingFragment);
    }

    //viewpage中fragment的show,hide
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

    private View.OnClickListener inquirylistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            serveshowDialog();
        }
    };


    //服务弹出框
    public void serveshowDialog() {
        //设置弹出框主题
        setHeadDialog = new Dialog(this, R.style.CustomDialog);

        setHeadDialog.show();
        //设置弹出框视图
        dialogView = View.inflate(this, R.layout.inquiry_popup_dialog, null);
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow()
                .getAttributes();
        Window window = setHeadDialog.getWindow();

        window.setWindowAnimations(0);

        setHeadDialog.getWindow().setAttributes(lp);

        serveonclick();

    }

    private void serveonclick() {
        RelativeLayout rl_inquiry_dialog = (RelativeLayout) dialogView.findViewById(R.id.rl_inquiry_dialog);
        LinearLayout ll_serve_registration = (LinearLayout) dialogView.findViewById(R.id.ll_serve_registration);
        LinearLayout ll_online_inquiry = (LinearLayout) dialogView.findViewById(R.id.ll_online_inquiry);
        final ImageView inquiry_dialog_tag = (ImageView) dialogView.findViewById(R.id.inquiry_dialog_tag);
        final LinearLayout ll_online_inquiry1 = (LinearLayout) dialogView.findViewById(R.id.ll_online_inquiry1);
        final LinearLayout ll_serve_registration1 = (LinearLayout) dialogView.findViewById(R.id.ll_serve_registration1);

        rl_inquiry_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 服务按钮的动画，包含在线问诊，一键挂号按钮的平移动画，
                 * 服务按钮的旋转豆花
                 */
                RotateAnimation myAnimation_Rotate = new RotateAnimation(45.0f, 90.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                inquiry_dialog_tag.startAnimation(myAnimation_Rotate);
                myAnimation_Rotate.setDuration(150);
                myAnimation_Rotate.setFillAfter(true);


                //换取手机屏幕 宽和高
                WindowManager wm = (WindowManager) HomeActivity.this.getSystemService(Context.WINDOW_SERVICE);

                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();

                translateAnimation = new TranslateAnimation(0.1f, width / 4.6f, 0.1f, height * 0.163f);
                translateAnimation.setDuration(150);
                ll_online_inquiry1.startAnimation(translateAnimation);


                translateAnimation1 = new TranslateAnimation(0.1f, -width / 4.6f, 0.1f, height * 0.163f);
                translateAnimation1.setDuration(150);
                ll_serve_registration1.startAnimation(translateAnimation1);

                //设置再次点击服务按钮及弹出框其他地方时，隐藏弹出框所寻妖的时间
                new Thread() {
                    public void run() {
                        try {
                            sleep(150);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            setHeadDialog.dismiss();
                        }
                    }
                }.start();
            }
        });

        ll_serve_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至一键挂号
                Intent intent = new Intent(HomeActivity.this, RegistrationAtivity.class);
                startActivity(intent);
                setHeadDialog.dismiss();
            }
        });

        ll_online_inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至在线问诊
                Intent intent = new Intent(HomeActivity.this, InquiryActivity.class);
                startActivity(intent);
                setHeadDialog.dismiss();
            }
        });
        //旋转动画
        RotateAnimation myAnimation_Rotate = new RotateAnimation(0.5f, -45.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        inquiry_dialog_tag.startAnimation(myAnimation_Rotate);
        myAnimation_Rotate.setDuration(150);
        myAnimation_Rotate.setFillAfter(true);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        //平移动画
        translateAnimation = new TranslateAnimation(width / 4.6f, 0.1f, height * 0.163f, 0.1f);

        translateAnimation.setDuration(150);
        ll_online_inquiry1.startAnimation(translateAnimation);


        translateAnimation1 = new TranslateAnimation(-width / 4.6f, 0.1f, height * 0.163f, 0.1f);
        translateAnimation1.setDuration(150);
        ll_serve_registration1.startAnimation(translateAnimation1);
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
