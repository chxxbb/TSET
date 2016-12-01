package com.example.chen.tset.page.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.View.activity.HomeActivity;
import com.example.chen.tset.View.activity.InquiryrecordActivity;
import com.example.chen.tset.View.activity.LeadActivity;
import com.example.chen.tset.View.activity.LogActivity;
import com.example.chen.tset.View.activity.MyCashCouponsActivity;
import com.example.chen.tset.View.activity.MyDoctorActivity;
import com.example.chen.tset.View.activity.MycollectActivity;
import com.example.chen.tset.View.activity.PersonaldataActivity;
import com.example.chen.tset.View.activity.ReservationActivity;
import com.example.chen.tset.View.activity.SetPageActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class MypageFragment extends Fragment {
    View view;
    private RelativeLayout rl_set;
    private RelativeLayout rl_mycollect, rl_myreservation, rl_personaldata, rl_mydpctor, rl_inquiryrecord, rl_MyCashCoupons, rl_mypage_share;
    private CircleImageView iv_ico;
    private TextView tv_name;
    SharedPsaveuser sp;
    Button btn_user_login_form;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypage, null);

        try {
            findView();

            sp = new SharedPsaveuser(getContext());


        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void findView() {
        rl_set = (RelativeLayout) view.findViewById(R.id.rl_set);
        rl_mycollect = (RelativeLayout) view.findViewById(R.id.rl_mycollect);
        rl_myreservation = (RelativeLayout) view.findViewById(R.id.rl_myreservation);
        rl_personaldata = (RelativeLayout) view.findViewById(R.id.rl_personaldata);
        rl_mydpctor = (RelativeLayout) view.findViewById(R.id.rl_mydpctor);
        rl_inquiryrecord = (RelativeLayout) view.findViewById(R.id.rl_inquiryrecord);
        iv_ico = (CircleImageView) view.findViewById(R.id.iv_icon);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        rl_MyCashCoupons = (RelativeLayout) view.findViewById(R.id.rl_MyCashCoupons);
        btn_user_login_form = (Button) view.findViewById(R.id.btn_user_login_form);
        rl_mypage_share = (RelativeLayout) view.findViewById(R.id.rl_mypage_share);
        rl_set.setOnClickListener(listerer);
        rl_mycollect.setOnClickListener(listerer);
        rl_myreservation.setOnClickListener(listerer);
        rl_personaldata.setOnClickListener(listerer);
        rl_mydpctor.setOnClickListener(listerer);
        rl_inquiryrecord.setOnClickListener(listerer);
        rl_MyCashCoupons.setOnClickListener(listerer);
        rl_mypage_share.setOnClickListener(listerer);


    }

    @Override
    public void onStart() {
        super.onStart();


        try {


            //判断是否为联网状态，选择显示从后台获取的数据或本地数据
            if ((User_Http.user.getIcon() == null || User_Http.user.getIcon().equals("")) && (sp.getTag().getIcon() == null || sp.getTag().getIcon().equals(""))) {

                iv_ico.setImageResource(R.drawable.default_icon);


            } else if (User_Http.user.getIcon() == null || User_Http.user.getIcon().equals("")) {

                ImageLoader.getInstance().displayImage("file:///" + sp.getTag().getIcon(), iv_ico);


            } else {


                ImageLoader.getInstance().displayImage(User_Http.user.getIcon(), iv_ico);
            }


            if (User_Http.user.getName() == null) {
                tv_name.setText(sp.getTag().getName());
            } else {
                tv_name.setText(User_Http.user.getName());
            }

            if (sp.getTag().getPassword() != null) {
                btn_user_login_form.setText("注册登录用户");

            } else {
                btn_user_login_form.setText("一键登录用户");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private View.OnClickListener listerer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_set:
                    //设置页面
                    Intent intent = new Intent(getContext(), SetPageActivity.class);

                    startActivity(intent);
                    break;
                case R.id.rl_mycollect:
                    //我的收藏
                    Intent intent1 = new Intent(getContext(), MycollectActivity.class);

                    startActivity(intent1);
                    break;
                case R.id.rl_myreservation:
                    //我的浴液
                    Intent intent2 = new Intent(getContext(), ReservationActivity.class);

                    startActivity(intent2);
                    break;
                case R.id.rl_personaldata:
                    //个人资料
                    Intent intent3 = new Intent(getContext(), PersonaldataActivity.class);

                    startActivity(intent3);
                    break;
                case R.id.rl_mydpctor:
                    //我的医生
                    Intent intent4 = new Intent(getContext(), MyDoctorActivity.class);

                    startActivity(intent4);
                    break;
                case R.id.rl_inquiryrecord:

                    //问诊历史
                    Intent intent5 = new Intent(getContext(), InquiryrecordActivity.class);

                    startActivity(intent5);
                    break;

                case R.id.rl_MyCashCoupons:
                    Intent intent6 = new Intent(getContext(), MyCashCouponsActivity.class);
                    intent6.putExtra("type", "mypage");
                    startActivity(intent6);
                    break;

                case R.id.rl_mypage_share:
                    send();
                    break;

            }

        }
    };

    private void send() {
        Intent intent = new Intent(getContext(), LeadActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getContext(), 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification noti = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.drawable.image_emoticon25)
                .setContentText("骚年，该吃药了")
                .setContentTitle("一条咸鱼干")
                .setTicker("你有一包咸鱼干到了")
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        noti.defaults = Notification.DEFAULT_ALL;
        mNotificationManager.notify(0, noti);

    }

}
