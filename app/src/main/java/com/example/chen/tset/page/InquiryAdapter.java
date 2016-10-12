package com.example.chen.tset.page;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.Data.Inquiry;
import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.View.ChatpageActivity;
import com.example.chen.tset.View.ReservationActivity;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class InquiryAdapter extends BaseAdapter {
    private Context context;
    private List<Inquiry> list;
    private Dialog setHeadDialog;
    private View dialogView;


    RadioButton rb_wenx;

    RadioButton rb_zhifb;

    LinearLayout ll_cancel;

    Button btn_confirm_payment;

    ProgressBar progressBar;

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
                SimpleDateFormat formatter = new SimpleDateFormat("HH");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);




                if (str.equals("12") || str.equals("13")) {
                    Toast.makeText(context, "现在医生不在，请稍后再来", Toast.LENGTH_SHORT).show();
                }

//                else if(Integer.getInteger(str)>18||Integer.getInteger(str)<=8){
//                    Toast.makeText(context, "医生已经下班了", Toast.LENGTH_SHORT).show();
//                }


                setHeadDialog = new Dialog(context, R.style.CustomDialog);
                setHeadDialog.show();
                dialogView = View.inflate(context, R.layout.inquiry_chat_dialog, null);
                setHeadDialog.getWindow().setContentView(dialogView);
                WindowManager.LayoutParams lp = setHeadDialog.getWindow()
                        .getAttributes();
                setHeadDialog.getWindow().setAttributes(lp);

                RelativeLayout rl_confirm = (RelativeLayout) dialogView.findViewById(R.id.rl_confirm);
                RelativeLayout lr_cancel = (RelativeLayout) dialogView.findViewById(R.id.lr_cancel);


                lr_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setHeadDialog.dismiss();
                    }
                });


                rl_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ChatpageActivity.class);
                        intent.putExtra("name", list.get(position).getName());
                        intent.putExtra("icon", list.get(position).getIcon());
                        intent.putExtra("doctorID", list.get(position).getId());
                        intent.putExtra("username", list.get(position).getUsername());
                        context.startActivity(intent);
                        setHeadDialog.dismiss();
                    }
                });
//                payDialog(position);

//                OkHttpUtils
//                        .post()
//                        .url(Http_data.http_data)
//                        .build()
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onError(Call call, Exception e, int id) {
//
//                            }
//
//                            @Override
//                            public void onResponse(String response, int id) {
//
//                            }
//                        });


//                Intent intent = new Intent(context, ChatpageActivity.class);
//                intent.putExtra("name", list.get(position).getName());
//                intent.putExtra("icon", list.get(position).getIcon());
//                intent.putExtra("doctorID", list.get(position).getId());
//                context.startActivity(intent);
            }

        });
        return convertView;
    }

    //支付弹出框
    private void payDialog(int pos) {

        setHeadDialog = new Dialog(context, R.style.CustomDialog);
        setHeadDialog.show();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        dialogView = View.inflate(context, R.layout.payment_dialog, null);


        rb_wenx = (RadioButton) dialogView.findViewById(R.id.rb_wenx);
        rb_zhifb = (RadioButton) dialogView.findViewById(R.id.rb_zhifb);
        ll_cancel = (LinearLayout) dialogView.findViewById(R.id.ll_cancel);

        //确认支付
        btn_confirm_payment = (Button) dialogView.findViewById(R.id.btn_confirm_payment);

        rb_wenx.setChecked(true);
        progressBar = (ProgressBar) dialogView.findViewById(R.id.progressBar);
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
        lp.width = display.getWidth();
        setHeadDialog.getWindow().setAttributes(lp);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int progressBarMax = progressBar.getMax();
                try {
                    //设置progressBar时间
                    while (progressBarMax != progressBar.getProgress()) {
                        int stepProgress = progressBarMax / 1000;
                        int currentprogress = progressBar.getProgress();
                        progressBar.setProgress(currentprogress + stepProgress);
                        Thread.sleep(180);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        });
        thread.start();

        paydialogonclick(pos);

    }


    //支付点击事件
    private void paydialogonclick(final int pos) {
        rb_zhifb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_wenx.setChecked(false);
            }
        });
        rb_wenx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_zhifb.setChecked(false);
            }
        });
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });

        btn_confirm_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
