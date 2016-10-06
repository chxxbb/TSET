package com.example.chen.tset.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chen.tset.Data.User_Http;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.View.InquiryrecordActivity;
import com.example.chen.tset.View.MyDoctorActivity;
import com.example.chen.tset.View.MycollectActivity;
import com.example.chen.tset.View.PersonaldataActivity;
import com.example.chen.tset.View.ReservationActivity;
import com.example.chen.tset.View.SetPageActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class MypageFragment extends Fragment {
    View view;
    private RelativeLayout rl_set;
    private RelativeLayout rl_mycollect, rl_myreservation, rl_personaldata, rl_mydpctor, rl_inquiryrecord;
    private CircleImageView iv_ico;
    private TextView tv_name;
    SharedPsaveuser sp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypage, null);

        findView();

        sp = new SharedPsaveuser(getContext());
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
        rl_set.setOnClickListener(listerer);
        rl_mycollect.setOnClickListener(listerer);
        rl_myreservation.setOnClickListener(listerer);
        rl_personaldata.setOnClickListener(listerer);
        rl_mydpctor.setOnClickListener(listerer);
        rl_inquiryrecord.setOnClickListener(listerer);
    }

    @Override
    public void onStart() {
        super.onStart();

        //判断是否为联网状态
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

    }

    private View.OnClickListener listerer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_set:
                    Intent intent = new Intent(getContext(), SetPageActivity.class);

                    startActivity(intent);
                    break;
                case R.id.rl_mycollect:
                    Intent intent1 = new Intent(getContext(), MycollectActivity.class);

                    startActivity(intent1);
                    break;
                case R.id.rl_myreservation:
                    Intent intent2 = new Intent(getContext(), ReservationActivity.class);

                    startActivity(intent2);
                    break;
                case R.id.rl_personaldata:
                    Intent intent3 = new Intent(getContext(), PersonaldataActivity.class);

                    startActivity(intent3);
                    break;
                case R.id.rl_mydpctor:
                    Intent intent4 = new Intent(getContext(), MyDoctorActivity.class);

                    startActivity(intent4);
                    break;
                case R.id.rl_inquiryrecord:
                    Intent intent5 = new Intent(getContext(), InquiryrecordActivity.class);

                    startActivity(intent5);
                    break;

            }

        }
    };
}
