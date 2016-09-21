package com.example.chen.tset.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Chatcontent;
import com.example.chen.tset.Data.User;
import com.example.chen.tset.Data.User_Http;

import com.example.chen.tset.R;
import com.example.chen.tset.Utils.ChatpageDao;
import com.example.chen.tset.page.ChatpageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatpageActivity extends AppCompatActivity {
    private EditText et_chat;
    private ImageView iv_chat;
    private ListView listView;
    private TextView tv_doctorname;
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
    private LinearLayout ll_consult_return;
    ChatpageDao db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatpage);
        JMessageClient.registerEventReceiver(this);
        JMessageClient.enterSingleConversation("18302615820");
        db = new ChatpageDao(this);
        list = new ArrayList<>();
        historylist = new ArrayList<>();
        findView();
        init();
    }


    private void findView() {
        //医生姓名
        doctorname = getIntent().getStringExtra("name");
        //医生头像
        doctoricon = getIntent().getStringExtra("icon");
        et_chat = (EditText) findViewById(R.id.et_chat);
        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        listView = (ListView) findViewById(R.id.listView);
        tv_doctorname = (TextView) findViewById(R.id.tv_doctorname);


        ll_consult_return = (LinearLayout) findViewById(R.id.ll_consult_return);


        tv_doctorname.setText(doctorname);

        listView.setVerticalScrollBarEnabled(false);

        iv_chat.setOnClickListener(lisntener);
        ll_consult_return.setOnClickListener(lisntener);

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);

        et_chat.setOnEditorActionListener(ettextlistener);

        listView.setOnItemClickListener(listvlistener);


    }


    private void init() {

        historylist = db.chatfind("18302615820");
        Log.e("数据库", historylist.toString());
        for (int i = 0; i < historylist.size(); i++) {
            list.add(historylist.get(i));

        }
        adapter = new ChatpageAdapter(this, list, doctoricon);
        listView.setAdapter(adapter);
        if (historylist.size() > 8) {
            listView.setStackFromBottom(true);
        }

        adapter.notifyDataSetChanged();

    }

    private TextView.OnEditorActionListener ettextlistener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, final int actionId, KeyEvent event) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        //发送消息
                        Conversation c = JMessageClient.getSingleConversation("18302615820");
//                13625784562
//                13213657894
                        //13456327895
                        if (c == null) {
                            c = Conversation.createSingleConversation("18302615820");
//                13608170152
                        }
                        TextContent textContent = new TextContent(et_chat.getText().toString());
                        Message message = c.createSendMessage(textContent);
                        JMessageClient.sendMessage(message);
                        Date dt = new Date();
                        Long time = dt.getTime();
                        String content = "1" + et_chat.getText().toString();
                        chatcontent = new Chatcontent(content, time, null, null, "18302615820", User_Http.user.getPhone());
                        list.add(chatcontent);
                        handler.sendEmptyMessage(0);
                        db.addchatcont(chatcontent);
                    }
                }
            });
            thread.start();

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


            }
        }
    };


    private View.OnClickListener lisntener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            audioFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/files/");
            audioFile.mkdirs();//创建文件夹
            try {
                sdcardTempFile = File.createTempFile("recording", ".jpg", audioFile);
            } catch (IOException e) {

                e.printStackTrace();
            }
            switch (v.getId()) {
                case R.id.iv_chat:

                    sendpictureDialog();
                    break;

                case R.id.ll_consult_return:
                    JMessageClient.exitConversation();
                    finish();
                    break;
            }

        }
    };

    private void sendpictureDialog() {
        setHeadDialog = new AlertDialog.Builder(this).create();
        setHeadDialog.show();
        dialogView = View.inflate(getApplicationContext(), R.layout.chatpage_picture_dialog, null);
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
        sendpictureclick();
    }

    private void sendpictureclick() {
        Button btn_camera = (Button) dialogView.findViewById(R.id.btn_camera);
        Button btn_cutout = (Button) dialogView.findViewById(R.id.btn_cutout);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);





        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));
                startActivityForResult(intent1, 100);
                setHeadDialog.dismiss();
            }
        });


        btn_cutout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                intent.putExtra("output", Uri.fromFile(sdcardTempFile));
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);// 裁剪框比例
                intent.putExtra("aspectY", 1);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JMessageClient.exitConversation();
    }

    private AdapterView.OnItemClickListener listvlistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (list.get(position).getMasterfile() != null) {
                setHeadDialog = new Dialog(ChatpageActivity.this, R.style.Dialog_Fullscreen);
//            setHeadDialog = new AlertDialog.Builder(ChatpageActivity.this).create();
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
                if (msg.getTargetID().equals("18302615820")) {
                    chatcontent = new Chatcontent(content, time, null, null, msg.getTargetID(), User_Http.user.getPhone());
                    list.add(chatcontent);
                    adapter.notifyDataSetChanged();
                }

                break;

            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                String file = imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                Log.e("接收的图片", file);
                Date dt1 = new Date();
                Long time1 = dt1.getTime();
                chatcontent = new Chatcontent("2*2", time1, file, file, msg.getTargetID(), User_Http.user.getPhone());
                if (msg.getTargetID().equals("18302615820")) {
                    list.add(chatcontent);
                    adapter.notifyDataSetChanged();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Conversation c = JMessageClient.getSingleConversation("18302615820");
                if (c == null) {
                    c = Conversation.createSingleConversation("18302615820");
//                13608170152
                }
                try {
                    ImageContent image = new ImageContent(sdcardTempFile);
                    Message message = c.createSendMessage(image);
                    JMessageClient.sendMessage(message);
                    chatcontent = new Chatcontent("1*1", 0L, sdcardTempFile.getAbsolutePath(), sdcardTempFile.getAbsolutePath(), "18302615820", User_Http.user.getPhone());
                    Log.e("发送图片地址", sdcardTempFile.getAbsolutePath());
                    list.add(chatcontent);
                    db.addchatcont(chatcontent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        });
        thread.start();


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
}
