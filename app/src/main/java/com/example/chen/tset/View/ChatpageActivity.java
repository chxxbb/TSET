package com.example.chen.tset.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Chatcontent;
import com.example.chen.tset.Data.Inquiryrecord;
import com.example.chen.tset.Data.User;
import com.example.chen.tset.Data.User_Http;

import com.example.chen.tset.Data.Userinfo;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.ChatpageDao;
import com.example.chen.tset.Utils.InquiryrecordDao;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.page.ChatpageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatpageActivity extends AppCompatActivity {
    private EditText et_chat;
    private ImageView iv_chat;
    private ListView listView;
    private TextView tv_doctorname;
    private RelativeLayout rl_loading;
    ChatpageAdapter adapter;
    List<Chatcontent> list;
    List<Chatcontent> historylist;
    Chatcontent chatcontent;
    String doctorname;
    String doctoricon;
    private File sdcardTempFile;
    private File audioFile;
    private Dialog setHeadDialog;
    private View dialogView;
    private LinearLayout ll_consult_return, ll_doctor_particulars;
    ChatpageDao db;
    String doctorID;
    String username;
    SharedPsaveuser sp = new SharedPsaveuser(ChatpageActivity.this);
    InquiryrecordDao inquiryrecorddb;
    List<Chatcontent> data;
    List<Inquiryrecord> ilist;


    //用于判断用户是否和医生对话过
    int chatstate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatpage);
        JMessageClient.registerEventReceiver(this);

        inquiryrecorddb = new InquiryrecordDao(this);

        audioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/text/chatprint/");
        audioFile.mkdirs();//创建文件夹
        data = new ArrayList<>();


    }


    @Override
    protected void onStart() {
        super.onStart();

        db = new ChatpageDao(this);
        list = new ArrayList<>();
        historylist = new ArrayList<>();
        findView();
        init();

    }

    //登录jmeeage
    private void jmessage() {

        String username = null;
        if (User_Http.user.getPhone() == null) {
            username = sp.getTag().getPhone();
        } else {
            username = User_Http.user.getPhone();
        }


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


    private void findView() {
        doctorID = getIntent().getStringExtra("doctorID");
        //医生姓名
        doctorname = getIntent().getStringExtra("name");
        //医生头像
        doctoricon = getIntent().getStringExtra("icon");
        username = getIntent().getStringExtra("username");
        et_chat = (EditText) findViewById(R.id.et_chat);
        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        listView = (ListView) findViewById(R.id.listView);
        tv_doctorname = (TextView) findViewById(R.id.tv_doctorname);

        ll_doctor_particulars = (LinearLayout) findViewById(R.id.ll_doctor_particulars);

        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);


        ll_consult_return = (LinearLayout) findViewById(R.id.ll_consult_return);


        tv_doctorname.setText(doctorname);

        listView.setVerticalScrollBarEnabled(false);


        ll_doctor_particulars.setOnClickListener(doctorlisntener);

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);


        listView.setOnItemClickListener(listvlistener);
        iv_chat.setOnClickListener(lisntener);
        ll_consult_return.setOnClickListener(lisntener);
        et_chat.setOnEditorActionListener(ettextlistener);

        //设置后在此页面接收此用户消息不会再通知栏中显示
        JMessageClient.enterSingleConversation(username);


    }

    private View.OnClickListener doctorlisntener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ChatpageActivity.this, DoctorparticularsActivity.class);
            intent.putExtra("doctot_id", doctorID);
            startActivity(intent);
        }
    };


    private void init() {
        //从数据库中获取此用户聊天记录
        historylist = db.chatfind(username);
        for (int i = 0; i < historylist.size(); i++) {


            if (historylist.get(i).getMyname().equals(sp.getTag().getPhone())) {
                list.add(historylist.get(i));


            }


        }
        adapter = new ChatpageAdapter(this, list, doctoricon);
        listView.setAdapter(adapter);


        //listview从底部开始刷新数据
        listView.setStackFromBottom(true);

        adapter.notifyDataSetChanged();


    }


    private TextView.OnEditorActionListener ettextlistener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, final int actionId, KeyEvent event) {
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
            try {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //发送文本消息
                    Conversation c = JMessageClient.getSingleConversation(username);
                    if (c == null) {
                        c = Conversation.createSingleConversation(username);

                    }
                    TextContent textContent = new TextContent(et_chat.getText().toString());
                    Message message = c.createSendMessage(textContent);
                    JMessageClient.sendMessage(message);
                    Date dt = new Date();
                    Long time = dt.getTime();
                    //存取到数据库中，+1用于判断是发送的消息还是接收的消息,1为自己发送的消息,2为接送到的消息
                    String content = "1" + et_chat.getText().toString();
                    chatcontent = new Chatcontent(content, time, null, null, username, sp.getTag().getPhone());
                    list.add(chatcontent);
                    et_chat.setText("");
//                            handler.sendEmptyMessage(0);
                    db.addchatcont(chatcontent);
                    adapter.notifyDataSetChanged();
                    chatstate++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                jmessage();
                handler.sendEmptyMessage(2);
            }

//                }
//            });
//            thread.start();

            return false;
        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    et_chat.setText("");
                    break;
                case 1:
                    adapter.notifyDataSetChanged();
                    et_chat.setText("");
                    break;
                case 2:
                    Toast.makeText(ChatpageActivity.this, "发送失败，请重新发送", Toast.LENGTH_SHORT).show();

                    break;


            }
        }
    };


    //发送图片
    private View.OnClickListener lisntener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.iv_chat:
                    //点击选择相机或截图
                    sendpictureDialog();
                    break;

                case R.id.ll_consult_return:
                    //退出聊天页面
                    JMessageClient.exitConversation();
                    finish();
                    break;
            }

        }
    };


    //图片发送弹出框
    private void sendpictureDialog() {
        jmessage();
//        setHeadDialog = new AlertDialog.Builder(this).create();
        setHeadDialog = new Dialog(this, R.style.CustomDialog);
        setHeadDialog.show();
        dialogView = View.inflate(getApplicationContext(), R.layout.chatpage_picture_dialog, null);
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
        sendpictureclick();
    }


    //选择获取图片方式
    private void sendpictureclick() {

        Button btn_camera = (Button) dialogView.findViewById(R.id.btn_camera);
        Button btn_cutout = (Button) dialogView.findViewById(R.id.btn_cutout);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);


        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    sdcardTempFile = File.createTempFile("textcamera", ".jpg", audioFile);

                } catch (IOException e) {

                    e.printStackTrace();
                }


                //相机
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));
                startActivityForResult(intent1, 100);
                setHeadDialog.dismiss();




            }
        });


        btn_cutout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    sdcardTempFile = File.createTempFile("textscreenshot", ".jpg", audioFile);
                } catch (IOException e) {

                    e.printStackTrace();
                }

                //图片
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                intent.putExtra("output", Uri.fromFile(sdcardTempFile));
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 2);// 裁剪框比例
                intent.putExtra("aspectY", 2);
                startActivityForResult(intent, 100);
                setHeadDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });


    }


    //用户点击返回键，关闭与改用户聊天
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JMessageClient.exitConversation();
    }


    //点击图片查看图片详情
    private AdapterView.OnItemClickListener listvlistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (list.get(position).getMasterfile() != null) {
                setHeadDialog = new Dialog(ChatpageActivity.this, R.style.Dialog_Fullscreen);

                setHeadDialog.show();
                dialogView = View.inflate(getApplicationContext(), R.layout.chat_imgv_dialog, null);

                ImageView iv_chat_dial = (ImageView) dialogView.findViewById(R.id.iv_chat_dial);

                ImageLoader.getInstance().displayImage("file:///" + list.get(position).getMasterfile(), iv_chat_dial);

                setHeadDialog.getWindow().setContentView(dialogView);

                WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();

                setHeadDialog.getWindow().setAttributes(lp);
            }

        }
    };


    //接收消息
    public void onEventMainThread(MessageEvent event) {
        Message msg = event.getMessage();
        switch (msg.getContentType()) {
            case text:
                //文本消息
                TextContent textContent = (TextContent) msg.getContent();

                String content = "2" + textContent.getText();
                Date dt = new Date();
                Long time = dt.getTime();
                if (msg.getTargetID().equals(username)) {
                    chatcontent = new Chatcontent(content, time, null, null, msg.getTargetID(), sp.getTag().getPhone());
                    list.add(chatcontent);
                    adapter.notifyDataSetChanged();
                }

                break;

            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                String file = imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                Date dt1 = new Date();
                Long time1 = dt1.getTime();
                chatcontent = new Chatcontent("2*2", time1, file, file, msg.getTargetID(), sp.getTag().getPhone());
                if (msg.getTargetID().equals(username)) {
                    list.add(chatcontent);
                    adapter.notifyDataSetChanged();
                }

                break;
        }
    }


    //发送图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Conversation c = JMessageClient.getSingleConversation(username);
                if (c == null) {
                    c = Conversation.createSingleConversation(username);
//                13608170152
                }
                try {

                    ImageContent image = new ImageContent(sdcardTempFile);

                    Message message = c.createSendMessage(image);
                    JMessageClient.sendMessage(message);
                    chatcontent = new Chatcontent("1*1", 0L, sdcardTempFile.getAbsolutePath(), sdcardTempFile.getAbsolutePath(), username, sp.getTag().getPhone());
                    list.add(chatcontent);
                    db.addchatcont(chatcontent);
                } catch (Exception e) {
                    e.printStackTrace();

                }
                handler.sendEmptyMessage(1);
                chatstate++;
            }
        });
        thread.start();


    }


    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        int j = 0;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        if (User_Http.user.getPhone() != null) {
            ilist = inquiryrecorddb.chatfind(sp.getTag().getPhone());
        } else {
            SharedPsaveuser sp = new SharedPsaveuser(this);
            Userinfo userinfo = sp.getTag();
            ilist = inquiryrecorddb.chatfind(userinfo.getPhone());
        }


        //判断数据库中是否有这个医生的聊天记录
        for (int i = 0; i < ilist.size(); i++) {
            if (username.equals(ilist.get(i).getDoctorid())) {
                j++;
            }
        }

        if (j == 0 && chatstate != 0) {
            Inquiryrecord inquiryrecord = new Inquiryrecord(sp.getTag().getPhone(), username, doctorID, doctorname, doctoricon, str, "");
            inquiryrecorddb.addInquiryrecord(inquiryrecord);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
}
