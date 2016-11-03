package com.example.chen.tset.page;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chen.tset.Data.Information;
import com.example.chen.tset.Data.Inquiry;
import com.example.chen.tset.R;
import com.example.chen.tset.View.ChatpageActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/10/31 0031.
 */
public class HomeDoctorRecommendAdapter extends BaseAdapter {
    Context context;
    List<Inquiry> list;
    private Dialog setHeadDialog;
    private View dialogView;


    public HomeDoctorRecommendAdapter(Context context, List<Inquiry> list) {
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
            convertView = inflater.inflate(R.layout.home_doctor_recommend_list_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tv_name.setText(list.get(position).getName());
        viewHolder.tv_home_docotor_title.setText(list.get(position).getTitle());
        viewHolder.tv_home_doctor_section.setText(list.get(position).getSection());
        viewHolder.tv_home_doctor_adept.setText(list.get(position).getAdept());
        viewHolder.btn_home_doctor_chatCost.setText("￥" + list.get(position).getChatCost());
        ImageLoader.getInstance().displayImage(list.get(position).getIcon(), viewHolder.iv_home_doctor_icon);

        //点击进入聊天页面
        viewHolder.btn_home_doctor_chatCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setHeadDialog = new Dialog(context, R.style.CustomDialog);
                setHeadDialog.show();
                dialogView = View.inflate(context, R.layout.inquiry_chat_dialog, null);
                setHeadDialog.getWindow().setContentView(dialogView);
                WindowManager.LayoutParams lp = setHeadDialog.getWindow().getAttributes();
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
            }
        });
        return convertView;
    }


    static class ViewHolder {
        private TextView tv_name, tv_home_docotor_title, tv_home_doctor_section, tv_home_doctor_adept;
        private CircleImageView iv_home_doctor_icon;
        private Button btn_home_doctor_chatCost;


        ViewHolder(View v) {
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            iv_home_doctor_icon = (CircleImageView) v.findViewById(R.id.iv_home_doctor_icon);
            tv_home_docotor_title = (TextView) v.findViewById(R.id.tv_home_docotor_title);
            tv_home_doctor_section = (TextView) v.findViewById(R.id.tv_home_doctor_section);
            tv_home_doctor_adept = (TextView) v.findViewById(R.id.tv_home_doctor_adept);
            btn_home_doctor_chatCost = (Button) v.findViewById(R.id.btn_home_doctor_chatCost);

        }
    }
}
