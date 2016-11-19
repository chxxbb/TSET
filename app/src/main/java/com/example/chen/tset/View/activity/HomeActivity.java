package com.example.chen.tset.View.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.entity.Chatcontent;
import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.entity.Inquiryrecord;
import com.example.chen.tset.Data.entity.Update;
import com.example.chen.tset.Data.entity.User;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.Data.entity.VersionsUpdate;
import com.example.chen.tset.Utils.db.ChatpageDao;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.db.InquiryrecordDao;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.Utils.Version_numberSP;
import com.example.chen.tset.page.fragment.AFragment;
import com.example.chen.tset.page.fragment.ConsultingFragment;
import com.example.chen.tset.page.fragment.EncyclopediaFragment;
import com.example.chen.tset.R;

import com.example.chen.tset.page.fragment.HomepageFragment;
import com.example.chen.tset.page.fragment.MypageFragment;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;

public class HomeActivity extends MyBaseActivity implements View.OnClickListener, IListener {
    FragmentManager fm;
    FragmentTransaction ft;
    private RadioButton rb_encyclopedia, rb_lectureroom, rb_mypage, rb_diagnosis;
    private RadioGroup radioGroup_right, radioGroup_left;
    private FrameLayout framelayout, fl_registration;
    private EncyclopediaFragment encyclopediaFragment;
    private MypageFragment mypageFragment;
    //    private InquiryFragment inquiryFragment;
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

    int fileSize;
    int downLoadFileSize;
    File updateaudioFile;
    File updatesdcardTempFile;

    ProgressBar pb_update_load;
    //更新文件地址
    String updateurl;

    Version_numberSP version_numberSp;

    VersionsUpdate versionsUpdate;

    Update update;

    HomepageFragment homepageFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);


        //注册广播
        ListenerManager.getInstance().registerListtener(this);

        //版本号
        version_numberSp = new Version_numberSP(this);


        //设置版本号
        if (version_numberSp.getversionNumber() == null || version_numberSp.getversionNumber().equals("") || (!version_numberSp.getversionNumber().trim().equals(Http_data.version_number))) {
            version_numberSp.setspversionNumber(Http_data.version_number);
        }


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


        updateaudioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/text/update/");
        updateaudioFile.mkdirs();//创建更新文件夹

        jmessage();

        updatedetection();

        if (Http_data.giveCashState == 2) {
            giveCashCoupons();
            Http_data.giveCashState = 1;
        }

    }


    //检测是否有更新
    private void updatedetection() {
        final Gson gson = new Gson();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "/VersionUpdate")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {


                            }

                            @Override
                            public void onResponse(String response, int id) {

                                update = gson.fromJson(response, Update.class);
                                //检查当前版本与获取的版本是否一致,如果不一致则弹出更新框提示更新
                                if (update.getVersion().trim().equals(version_numberSp.getversionNumber().trim())) {

                                } else {
                                    updatedialog();
                                }

                            }
                        });
            }
        });

        thread.start();

    }


    //更新弹出框
    private void updatedialog() {
        //为dialog设置主题为透明，
        setHeadDialog = new Dialog(this, R.style.CustomDialog);
        setHeadDialog.show();
        dialogView = View.inflate(getApplicationContext(), R.layout.home_update_dialog, null);
        setHeadDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕dialog不消失
        //设置弹窗的布局
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
        //弹窗点击事件
        updatedialogclick();
    }


    private void updatedialogclick() {
        final TextView tv_update = (TextView) dialogView.findViewById(R.id.tv_update);
        pb_update_load = (ProgressBar) dialogView.findViewById(R.id.pb_update_load);
        final RelativeLayout rl_update_confirm = (RelativeLayout) dialogView.findViewById(R.id.rl_update_confirm);
        final RelativeLayout rl_update_cancel = (RelativeLayout) dialogView.findViewById(R.id.rl_update_cancel);
        final TextView tv_update_confirm = (TextView) dialogView.findViewById(R.id.tv_update_confirm);
        final TextView tv_update_cancel = (TextView) dialogView.findViewById(R.id.tv_update_cancel);
        LinearLayout linearlayout = (LinearLayout) dialogView.findViewById(R.id.linearlayout);
        TextView tv_version = (TextView) dialogView.findViewById(R.id.tv_version);
        TextView tv_updateLog = (TextView) dialogView.findViewById(R.id.tv_updateLog);


        //更新版本
        tv_version.setText("最新版本：" + update.getVersion());

        //更新内容
        tv_updateLog.setText(update.getUpdateLog());

        linearlayout.setOnClickListener(null);

        rl_update_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versionUpdate();
                tv_update.setText("下载中，请稍等...");
                rl_update_confirm.setOnClickListener(null);
                tv_update_confirm.setTextColor(getResources().getColor(R.color.xmgray));
                rl_update_cancel.setOnClickListener(null);
                tv_update_cancel.setTextColor(getResources().getColor(R.color.xmgray));
                //设置点击手机返回键都不会隐藏下载框
                setHeadDialog.setOnKeyListener(keylistener);
            }
        });


        rl_update_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });

    }


    //如果点击确认更新，则所有界面不可点击，返回键也不可点击，一直到下载文件完成
    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };


    private void versionUpdate() {


        updateurl = update.getDownloadPath();


        new Thread() {
            public void run() {
                try {
                    down_file(updateurl);
                    //下载文件URL
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

    }


    //下载更新文件
    public void down_file(String url) throws IOException {
        updatesdcardTempFile = new File(updateaudioFile, "textupdate.apk");
        //获取文件名
        URL myURL = new URL(url);
        URLConnection conn = myURL.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();
        this.fileSize = conn.getContentLength();//根据响应获取文件大小

        if (this.fileSize <= 0) {
            throw new RuntimeException("无法获知文件大小 ");
        }

        if (is == null) {
            throw new RuntimeException("下载文件为空");
        }

        FileOutputStream fos = new FileOutputStream(updatesdcardTempFile);
        //把数据存入路径+文件名
        byte buf[] = new byte[1024];
        downLoadFileSize = 0;
        sendMsg(0);
        do {
            //循环读取
            int numread = is.read(buf);
            if (numread == -1) {
                break;
            }
            fos.write(buf, 0, numread);
            downLoadFileSize += numread;

            sendMsg(1);//更新进度条
        } while (true);
        sendMsg(2);//通知下载完成
        try {
            is.close();
        } catch (Exception ex) {
            Toast.makeText(HomeActivity.this, "下载错误", Toast.LENGTH_SHORT).show();
        }

    }


    private void sendMsg(int flag) {
        android.os.Message msg = new android.os.Message();
        msg.what = flag;
        handler.sendMessage(msg);
    }


    //打开下载的文件，自动跳转到安装页面
    private void openFile(File file) {
        Intent intent = new Intent();

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setAction(android.content.Intent.ACTION_VIEW);

        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        startActivity(intent);
    }


    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {//定义一个Handler，用于处理下载线程与UI间通讯
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:
                        pb_update_load.setMax(fileSize);
                    case 1:
                        pb_update_load.setProgress(downLoadFileSize);

                        break;
                    case 2:

                        Toast.makeText(HomeActivity.this, "文件下载完成", Toast.LENGTH_SHORT).show();
                        setHeadDialog.dismiss();
                        try {
                            openFile(updatesdcardTempFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this, "打开更新文件失败", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case -1:

                        Toast.makeText(HomeActivity.this, "错误", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };

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


        Log.e("共享参数数据", sp.getTag().toString());


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
        if (homepageFragment != null) fragmentTransaction.hide(homepageFragment);
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
                //首页
                if (homepageFragment == null) {
                    homepageFragment = new HomepageFragment();
                    ft.add(R.id.framelayout, homepageFragment);
                } else {
                    ft.show(homepageFragment);
                }


                break;


            //讲堂页面，更改为百科页面，讲堂改为一个单独的Activity
            case R.id.rb_lectureroom:
                fl_registration.setVisibility(View.GONE);
                radioGroup_left.clearCheck();
                if (encyclopediaFragment == null) {
                    encyclopediaFragment = new EncyclopediaFragment();
                    ft.add(R.id.framelayout, encyclopediaFragment);
                } else {
                    ft.show(encyclopediaFragment);
                }


                break;

            //我的
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

            //诊疗页面
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
                 * 服务按钮的旋转动画
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


                //通过获取的屏幕宽高，确定2个按钮的移动位置
                translateAnimation = new TranslateAnimation(0.1f, width / 4.6f, 0.1f, height * 0.163f);
                translateAnimation.setDuration(150);
                ll_online_inquiry1.startAnimation(translateAnimation);

                //
                translateAnimation1 = new TranslateAnimation(0.1f, -width / 4.6f, 0.1f, height * 0.163f);
                translateAnimation1.setDuration(150);
                ll_serve_registration1.startAnimation(translateAnimation1);

                //设置再次点击服务按钮及弹出框其他地方时，隐藏弹出框所需要的时间
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
    public void notifyAllActivity(String str) {
        try {

            ft = fm.beginTransaction();
            hideAllFragment(ft);

            //接收到广播显示不同的页面
            if (str.equals("显示诊疗页面")) {


                fl_registration.setVisibility(View.GONE);
                rb_diagnosis.setChecked(true);
                if (consultingFragment == null) {
                    consultingFragment = new ConsultingFragment();
                    ft.add(R.id.framelayout, consultingFragment);
                } else {
                    ft.show(consultingFragment);
                }

                ft.commitAllowingStateLoss();

                //显示到资讯页面
            } else if (str.equals("显示资讯页面")) {
                fl_registration.setVisibility(View.GONE);
                radioGroup_left.clearCheck();
                rb_lectureroom.setChecked(true);

                if (encyclopediaFragment == null) {

                    encyclopediaFragment = new EncyclopediaFragment();
                    ft.add(R.id.framelayout, encyclopediaFragment);
                } else {

                    ft.show(encyclopediaFragment);
                }
                ft.commitAllowingStateLoss();
            }


            else {

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void giveCashCoupons() {
        //为dialog设置主题为透明，
        setHeadDialog = new Dialog(this, R.style.CustomDialog);
        setHeadDialog.show();
        dialogView = View.inflate(getApplicationContext(), R.layout.home_give_cash_coupons_dialog, null);
        setHeadDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕dialog消失
        //设置弹窗的布局
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
        giveCashCouponsClick();
    }

    //点击查看现金卷
    private void giveCashCouponsClick() {
        Button btn_look_cash_coupons = (Button) dialogView.findViewById(R.id.btn_look_cash_coupons);

        LinearLayout ll_give_cash_coupons = (LinearLayout) dialogView.findViewById(R.id.ll_give_cash_coupons);

        ll_give_cash_coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setHeadDialog.dismiss();
            }
        });

        btn_look_cash_coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
                Intent intent = new Intent(HomeActivity.this, MyCashCouponsActivity.class);
                startActivity(intent);
            }
        });
    }


    private long exitTime = 0;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
