package com.example.chen.tset.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.chen.tset.R;
import com.example.chen.tset.View.SetPageActivity;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class MypageFragment extends Fragment {
    View view;
    private LinearLayout iv_set;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypage, null);
        findView();
        return view;
    }

    private void findView() {
        iv_set = (LinearLayout) view.findViewById(R.id.iv_set);
        iv_set.setOnClickListener(listerer);
    }

    private View.OnClickListener listerer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), SetPageActivity.class);
            startActivity(intent);
        }
    };
}
