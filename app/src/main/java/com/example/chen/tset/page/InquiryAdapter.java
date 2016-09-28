package com.example.chen.tset.page;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Inquiry;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.View.ChatpageActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class InquiryAdapter extends BaseAdapter {
    private Context context;
    private List<Inquiry> list;

    public InquiryAdapter(Context context, List<Inquiry> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Inquiry getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.inquiry_list_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.textView.setText(9.9 + "分");
        viewHolder.tv_title.setText(list.get(position).getTitle());
        viewHolder.tv_name.setText(list.get(position).getName());
        viewHolder.btn_money.setText("￥" + list.get(position).getChatCost());
        viewHolder.tv_intro.setText("擅长：" + list.get(position).getAdept());
        viewHolder.tv_section.setText(list.get(position).getSection());
        ImageLoader.getInstance().displayImage(list.get(position).getIcon(), viewHolder.iv_icon);
        viewHolder.fl_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                submit(position);


                Intent intent = new Intent(context, ChatpageActivity.class);
                intent.putExtra("name", list.get(position).getName());
                intent.putExtra("icon", list.get(position).getIcon());
                intent.putExtra("doctorID", list.get(position).getId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private void submit(final int pos) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Http_data.http_data + "")
                        .addParams("userId", User_Http.user.getId() + "")
                        .addParams("doctorID", list.get(pos).getId())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.e("失败", "失败");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e("返回", response);


                            }
                        });
            }
        }).start();

    }


    static class ViewHolder {
        private TextView textView;
        private CircleImageView iv_icon;
        private TextView tv_title;
        private TextView tv_name;
        private Button btn_money;
        private TextView tv_intro;
        private TextView tv_section;
        private FrameLayout fl_chat;

        ViewHolder(View v) {
            textView = (TextView) v.findViewById(R.id.textView);
            iv_icon = (CircleImageView) v.findViewById(R.id.iv_icon);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            btn_money = (Button) v.findViewById(R.id.btn_money);
            tv_intro = (TextView) v.findViewById(R.id.tv_intro);
            tv_section = (TextView) v.findViewById(R.id.tv_section);
            fl_chat = (FrameLayout) v.findViewById(R.id.fl_chat);
        }
    }
}
