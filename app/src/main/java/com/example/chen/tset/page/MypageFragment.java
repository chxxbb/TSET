package com.example.chen.tset.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.chen.tset.R;
import com.example.chen.tset.View.MycollectActivity;
import com.example.chen.tset.View.ReservationActivity;
import com.example.chen.tset.View.SetPageActivity;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class MypageFragment extends Fragment {
    View view;
    private LinearLayout iv_set;
    private RelativeLayout rl_mycollect, rl_myreservation;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypage, null);
        findView();
        return view;
    }

    private void findView() {
        iv_set = (LinearLayout) view.findViewById(R.id.iv_set);
        rl_mycollect = (RelativeLayout) view.findViewById(R.id.rl_mycollect);
        rl_myreservation = (RelativeLayout) view.findViewById(R.id.rl_myreservation);
        iv_set.setOnClickListener(listerer);
        rl_mycollect.setOnClickListener(listerer);
        rl_myreservation.setOnClickListener(listerer);
    }

    private View.OnClickListener listerer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_set:
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

            }

        }
    };
}
