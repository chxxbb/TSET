package com.example.chen.tset.View;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Chatcontent;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.page.ChatpageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
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
    Chatcontent chatcontent;
    String doctorname;
    String doctoricon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatpage);
        JMessageClient.registerEventReceiver(this);
        list = new ArrayList<>();
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
        tv_doctorname.setText(doctorname);

        listView.setVerticalScrollBarEnabled(false);

        iv_chat.setOnClickListener(lisntener);


        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);

        et_chat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断点击的是否为软键盘的确定键
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //发送消息
                    Conversation c = JMessageClient.getSingleConversation("13912345678");
                    if (c == null) {
                        c = Conversation.createSingleConversation("13912345678");
//                13608170152
                    }
                    TextContent textContent = new TextContent(et_chat.getText().toString());
                    Message message = c.createSendMessage(textContent);
                    JMessageClient.sendMessage(message);
                    Date dt = new Date();
                    Long time = dt.getTime();
                    String content = "1" + et_chat.getText().toString();
                    chatcontent = new Chatcontent(content, time);
                    list.add(chatcontent);
                    adapter.notifyDataSetChanged();
                    et_chat.setText("");
                }
                return false;
            }
        });

    }


    private void init() {
        adapter = new ChatpageAdapter(this, list, doctoricon);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener lisntener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    //接收消息
    public void onEventMainThread(MessageEvent event) {
        Message msg = event.getMessage();
        switch (msg.getContentType()) {
            case text:
                TextContent textContent = (TextContent) msg.getContent();
                String content = "2" + textContent.getText();
                Date dt = new Date();
                Long time = dt.getTime();
                chatcontent = new Chatcontent(content, time);
                list.add(chatcontent);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
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
