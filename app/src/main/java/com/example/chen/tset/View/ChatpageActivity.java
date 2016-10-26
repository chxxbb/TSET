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
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.chen.tset.Utils.ContextUtil;
import com.example.chen.tset.Utils.InquiryrecordDao;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.page.ChatpageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.soundcloud.android.crop.Crop;

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
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.indicator.PtrIndicator;


public class ChatpageActivity extends AppCompatActivity implements PtrUIHandler {
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
    private File sdcardTempFile1;
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

    private Button btn_chat;

    private PtrClassicFrameLayout ptrClassicFrameLayout;

    //用于判断用户是否和医生对话过
    int chatstate = 0;

    //聊天记录页数
    int chatrecordnumber = 1;

    List<Chatcontent> numberlist;


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
        numberlist = new ArrayList<>();
        findView();
        init();
        //设置后在此页面接收此用户消息不会再通知栏中显示
        JMessageClient.enterSingleConversation(username);

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

        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        et_chat = (EditText) findViewById(R.id.et_chat);

        listView = (ListView) findViewById(R.id.listView);
        tv_doctorname = (TextView) findViewById(R.id.tv_doctorname);

        ll_doctor_particulars = (LinearLayout) findViewById(R.id.ll_doctor_particulars);

        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);


        ll_consult_return = (LinearLayout) findViewById(R.id.ll_consult_return);

        btn_chat = (Button) findViewById(R.id.btn_chat);


        tv_doctorname.setText(doctorname);

        listView.setVerticalScrollBarEnabled(false);

        View view1 = View.inflate(this, R.layout.chatpage_listview_footview, null);

        listView.addFooterView(view1);


        //下拉加载更多的聊天记录
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptrClassicFrameLayout);
        ptrClassicFrameLayout.setPadding(0, 0, 0, 50);
        //2次下拉时间间隔
        ptrClassicFrameLayout.setDurationToCloseHeader(1000);


        ll_doctor_particulars.setOnClickListener(doctorlisntener);

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);


        listView.setOnItemClickListener(listvlistener);

        iv_chat.setOnClickListener(lisntener);
        ll_consult_return.setOnClickListener(lisntener);

        btn_chat.setOnClickListener(btnlistener);


    }

    //查看医生详情
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
            //获取所有的聊天记录
            if (historylist.get(i).getMyname().equals(sp.getTag().getPhone())) {
                numberlist.add(historylist.get(i));

            }
        }

        //进入聊天页面默认加载10天数据,如果少于或等于10条则全部显示
        if (numberlist.size() > (chatrecordnumber * 10)) {
            for (int j = numberlist.size() - (chatrecordnumber * 10); j < numberlist.size(); j++) {

                list.add(numberlist.get(j));
            }
        } else {

            for (int i = 0; i < numberlist.size(); i++) {
                list.add(numberlist.get(i));
            }

        }


        adapter = new ChatpageAdapter(this, list, doctoricon);
        listView.setAdapter(adapter);


        //listview从底部开始刷新数据
        listView.setStackFromBottom(true);

        adapter.notifyDataSetChanged();

        //加载更多的聊天记录，
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                //将list清空,聊天页面页数+1
                historylist.clear();
                numberlist.clear();
                list.clear();
                chatrecordnumber++;

                historylist = db.chatfind(username);
                for (int i = 0; i < historylist.size(); i++) {
                    //获取所有的聊天记录
                    if (historylist.get(i).getMyname().equals(sp.getTag().getPhone())) {
                        numberlist.add(historylist.get(i));

                    }
                }

                ptrClassicFrameLayout.postDelayed(new Runnable() {


                    @Override
                    public void run() {
                        //根据页数，获取所需数据
                        if (numberlist.size() > (chatrecordnumber * 10)) {
                            for (int j = numberlist.size() - (chatrecordnumber * 10); j < numberlist.size(); j++) {
                                list.add(numberlist.get(j));
                            }
                            handler.sendEmptyMessage(4);
                        } else {
                            //如果所取的数据条数大于已有数据条数则全部显示
                            for (int i = 0; i < numberlist.size(); i++) {
                                list.add(numberlist.get(i));
                            }

                            handler.sendEmptyMessage(5);
                        }


                    }
                    //设定加载更多聊天记录需要的时间
                }, 1000);


            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, content, header);
            }

        });

    }


    //发送文本消息
    private View.OnClickListener btnlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (et_chat.getText().length() == 0) {

            } else {
                sendmessage();
            }

        }
    };


    private void sendmessage() {
        try {
            //发送文本消息
            Conversation c = JMessageClient.getSingleConversation(username);
            if (c == null) {
                c = Conversation.createSingleConversation(username);

            }
            TextContent textContent = new TextContent(et_chat.getText().toString());
            Message message = c.createSendMessage(textContent);
            JMessageClient.sendMessage(message);
            //获取发送时间
            Date dt = new Date();
            Long time = dt.getTime();
            //存取到数据库中，+1用于判断是发送的消息还是接收的消息,1为自己发送的消息,2为接送到的消息
            String content = "1" + et_chat.getText().toString();
            chatcontent = new Chatcontent(content, time, null, null, username, sp.getTag().getPhone());
            //保存在list集合中，清空输入框，并刷新页面
            list.add(chatcontent);
            et_chat.setText("");
//                            handler.sendEmptyMessage(0);
            db.addchatcont(chatcontent);
            adapter.notifyDataSetChanged();
            chatstate++;

        } catch (Exception e) {
            e.printStackTrace();

            jmessage();
            handler.sendEmptyMessage(2);
        }

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    try {

                        adapter.notifyDataSetChanged();
                        et_chat.setText("");
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    break;
                case 1:
                    try {

                        adapter.notifyDataSetChanged();
                        et_chat.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ChatpageActivity.this, "发送失败，请重新发送", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Toast.makeText(ChatpageActivity.this, "发送失败，请重新发送", Toast.LENGTH_SHORT).show();
                    break;


                case 3:
                    Toast.makeText(ChatpageActivity.this, "发送图片失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    break;

                case 4:
                    adapter.notifyDataSetChanged();
                    //使Listview一直显示在头部
                    listView.setSelection(9);
                    //收回加载更多显示框
                    ptrClassicFrameLayout.refreshComplete();
                    break;
                case 5:
                    adapter.notifyDataSetChanged();
                    ptrClassicFrameLayout.refreshComplete();
                    Toast.makeText(ChatpageActivity.this, "已经没有消息记录了", Toast.LENGTH_SHORT).show();
                    break;


            }
        }
    };



    private View.OnClickListener lisntener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.ll_consult_return:
                    //退出聊天页面
                    JMessageClient.exitConversation();
                    finish();
                    break;

                case R.id.iv_chat:
                    //发送图片
                    sendpictureDialog();
                    break;
            }

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        //关闭聊天页面，再次收到这个医生消息会显示到通知栏
        JMessageClient.exitConversation();
    }

    //图片发送弹出框
    private void sendpictureDialog() {
        jmessage();

        //为dialog设置主题为透明，
        setHeadDialog = new Dialog(this, R.style.CustomDialog);
        setHeadDialog.show();
        dialogView = View.inflate(getApplicationContext(), R.layout.chatpage_picture_dialog, null);
        //设置弹窗的布局
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
        //弹窗点击事件
        sendpictureclick();
    }


    //选择获取图片方式
    private void sendpictureclick() {

        Button btn_camera = (Button) dialogView.findViewById(R.id.btn_camera);
        Button btn_cutout = (Button) dialogView.findViewById(R.id.btn_cutout);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);

        //点击相机
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    //设置相机拍照后的路径
                    sdcardTempFile1 = File.createTempFile("textcamera", ".jpg", audioFile);
                    //相机

                    Log.e("相机",sdcardTempFile1.toString());
                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile1));
                    startActivityForResult(intent1, 100);
                    setHeadDialog.dismiss();


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ChatpageActivity.this, "开启相机失败，请检查是否开启权限或稍后再试", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //开启图库
        btn_cutout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Crop.pickImage(ChatpageActivity.this);
                    setHeadDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ChatpageActivity.this, "打开图库失败，请检查是否开启权限或稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });

    }

    //截取图片
    private void beginCrop(Uri source) {
        try {
            sdcardTempFile = File.createTempFile("textscreenshot", ".jpg", audioFile);
        } catch (IOException e) {

            e.printStackTrace();
        }

        Uri destination = Uri.fromFile(sdcardTempFile);
        //将从图库中选中的图片进行截取
        Crop.of(source, destination).asSquare().start(this);
    }


    //保存发送图片
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (sdcardTempFile != null) {
                        try {
                            Conversation c = JMessageClient.getSingleConversation(username);
                            if (c == null) {
                                c = Conversation.createSingleConversation(username);
                            }

                            ImageContent image = new ImageContent(sdcardTempFile);

                            Message message = c.createSendMessage(image);
                            JMessageClient.sendMessage(message);
                            //将发送的图片本地地址保存在数据库中，加标示用于判断是否是发送图片或接收图片
                            chatcontent = new Chatcontent("1*1", 0L, sdcardTempFile.getAbsolutePath(), sdcardTempFile.getAbsolutePath(), username, sp.getTag().getPhone());
                            //将消息显示在界面中，并保存到数据库中
                            list.add(chatcontent);
                            handler.sendEmptyMessage(1);
                            db.addchatcont(chatcontent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(3);


                        }
                    } else {
                        handler.sendEmptyMessage(3);
                    }


                    chatstate++;
                }
            });
            thread.start();
        } else if (resultCode == Crop.RESULT_ERROR) {
//            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }


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
                //设置弹出框主题为全屏
                setHeadDialog = new Dialog(ChatpageActivity.this, R.style.Dialog_Fullscreen);

                setHeadDialog.show();
                dialogView = View.inflate(getApplicationContext(), R.layout.chat_imgv_dialog, null);

                ImageView iv_chat_dial = (ImageView) dialogView.findViewById(R.id.iv_chat_dial);

                //加载图片

                ImageLoader.getInstance().displayImage("file:///" + list.get(position).getMasterfile(), iv_chat_dial);

                setHeadDialog.getWindow().setContentView(dialogView);

                WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();

                setHeadDialog.getWindow().setAttributes(lp);

                RelativeLayout rl_chat_diss = (RelativeLayout) dialogView.findViewById(R.id.rl_chat_diss);

                //点击任何地方关闭弹出框视图
                rl_chat_diss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setHeadDialog.dismiss();
                    }
                });
            }

        }
    };


    //接收消息
    public void onEventMainThread(MessageEvent event) {
        Message msg = event.getMessage();
        switch (msg.getContentType()) {
            case text:
                //处理接收到的文本消息
                TextContent textContent = (TextContent) msg.getContent();

                String content = "2" + textContent.getText();
                Date dt = new Date();
                Long time = dt.getTime();
                //判断是否为当前聊天对象，如果是则保存在数据库中并在listview中显示，，如果不是则保存在数据库中（保存到数据库方法写在了HomeActivity中），并且发送到通知栏
                if (msg.getTargetID().equals(username)) {
                    chatcontent = new Chatcontent(content, time, null, null, msg.getTargetID(), sp.getTag().getPhone());
                    list.add(chatcontent);
                    adapter.notifyDataSetChanged();
                }
                break;

            case image:
                //处理接收到的图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                String file = imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                //获取发送消息时间
                Date dt1 = new Date();
                Long time1 = dt1.getTime();
                //判断是否为当前聊天对象，如果是则保存在数据库中并在listview中显示，如果不是则保存在数据库中（保存到数据库方法写在了HomeActivity中），并且发送到通知栏
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
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        //判断是否是从图片中选择图片
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
            //获取到从图库选择的图片，进行截取
        } else if (requestCode == Crop.REQUEST_CROP) {

            handleCrop(resultCode, result);
            //拍照，点击确定发送图片
        } else if (resultCode == RESULT_OK && requestCode == 100) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Conversation c = JMessageClient.getSingleConversation(username);

                        if (c == null) {
                            c = Conversation.createSingleConversation(username);

                        }

                        ImageContent image = new ImageContent(sdcardTempFile1);

                        Message message = c.createSendMessage(image);

                        JMessageClient.sendMessage(message);
                        //保存发送的图片地址并显示在聊天界面
                        chatcontent = new Chatcontent("1*1", 0L, sdcardTempFile1.getAbsolutePath(), sdcardTempFile1.getAbsolutePath(), username, sp.getTag().getPhone());
                        list.add(chatcontent);
                        handler.sendEmptyMessage(1);
                        db.addchatcont(chatcontent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(3);
                    }

                    chatstate++;
                }
            });
            thread.start();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        int j = 0;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        //获取当前用户手机号
        if (User_Http.user.getPhone() != null) {
            ilist = inquiryrecorddb.chatfind(sp.getTag().getPhone());
        } else {
            SharedPsaveuser sp = new SharedPsaveuser(this);
            Userinfo userinfo = sp.getTag();
            ilist = inquiryrecorddb.chatfind(userinfo.getPhone());
        }


        //查找数据库中是否有这个医生的聊天记录
        for (int i = 0; i < ilist.size(); i++) {
            if (username.equals(ilist.get(i).getDoctorid())) {
                j++;
            }
        }
        //如果数据库没有这个医生的聊天记录，并且chatstate大于0则保存

        //chatstate用于判断是否发送过消息
        if (j == 0 && chatstate != 0) {
            Inquiryrecord inquiryrecord = new Inquiryrecord(sp.getTag().getPhone(), username, doctorID, doctorname, doctoricon, str, "");
            inquiryrecorddb.addInquiryrecord(inquiryrecord);
        }


    }

    int l = 0;


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        l = list.size();

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

    }
}
