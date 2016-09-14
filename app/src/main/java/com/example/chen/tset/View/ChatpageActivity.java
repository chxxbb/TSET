package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.R;
import com.example.chen.tset.page.ChatpageAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

public class ChatpageActivity extends AppCompatActivity {
    private EditText et_chat;
    private ImageView iv_chat;
    private ListView listView;
    ChatpageAdapter adapter;
    List<String> list;

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
        et_chat = (EditText) findViewById(R.id.et_chat);
        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        listView = (ListView) findViewById(R.id.listView);
        iv_chat.setOnClickListener(lisntener);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
//        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

    }

    private void init() {
        adapter = new ChatpageAdapter(this, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener lisntener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Conversation c = JMessageClient.getSingleConversation("13608170152");
            if (c == null) {
                c = Conversation.createSingleConversation("13608170152");
            }
            TextContent textContent = new TextContent(et_chat.getText().toString());
            Message message = c.createSendMessage(textContent);
            JMessageClient.sendMessage(message);
            list.add("1" + et_chat.getText().toString());
            adapter.notifyDataSetChanged();
        }
    };

    public void onEventMainThread(MessageEvent event) {
        Message msg = event.getMessage();
        switch (msg.getContentType()) {
            case text:

                TextContent textContent = (TextContent) msg.getContent();
                String a = textContent.getText();
                list.add("2" + a);
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
