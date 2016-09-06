package com.example.chen.tset.View;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.chen.tset.page.EncyclopediaFragment;
import com.example.chen.tset.R;
import com.example.chen.tset.page.InquiryFragment;
import com.example.chen.tset.page.InquiryView;
import com.example.chen.tset.page.LectureroomFragment;
import com.example.chen.tset.page.MypageFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    FragmentManager fm;
    FragmentTransaction ft;
    private RadioButton rb_encyclopedia, rb_lectureroom, rb_mypage, rb_diagnosis;
    private RadioGroup radioGroup_right, radioGroup_left;
    private FrameLayout framelayout, fl_registration;
    private EncyclopediaFragment encyclopediaFragment;
    private LectureroomFragment lectureroomFragment;
    private MypageFragment mypageFragment;
    private InquiryFragment inquiryFragment;
    private InquiryView iv_inquiry;
    HomeActivity homeActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findView();
        init();
    }

    private void findView() {
        rb_encyclopedia = (RadioButton) findViewById(R.id.rb_encyclopedia);
        framelayout = (FrameLayout) findViewById(R.id.framelayout);
        rb_lectureroom = (RadioButton) findViewById(R.id.rb_lectureroom);
        rb_mypage = (RadioButton) findViewById(R.id.rb_mypage);
        rb_diagnosis = (RadioButton) findViewById(R.id.rb_diagnosis);
        radioGroup_left = (RadioGroup) findViewById(R.id.radioGroup_left);
        radioGroup_right = (RadioGroup) findViewById(R.id.radioGroup_right);
        iv_inquiry = (InquiryView) findViewById(R.id.iv_inquiry);
        fl_registration = (FrameLayout) findViewById(R.id.fl_registration);
        rb_encyclopedia.setChecked(true);
        rb_encyclopedia.setOnClickListener(this);
        rb_lectureroom.setOnClickListener(this);
        rb_mypage.setOnClickListener(this);
        rb_diagnosis.setOnClickListener(this);
        iv_inquiry.setOnClickListener(this);
        fl_registration.setOnClickListener(listener);
    }

    private void init() {
        fm = getSupportFragmentManager();
        rb_encyclopedia.performClick();
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (encyclopediaFragment != null) fragmentTransaction.hide(encyclopediaFragment);
        if (lectureroomFragment != null) fragmentTransaction.hide(lectureroomFragment);
        if (mypageFragment != null) fragmentTransaction.hide(mypageFragment);
        if (inquiryFragment != null) fragmentTransaction.hide(inquiryFragment);
    }


    @Override
    public void onClick(View v) {
        ft = fm.beginTransaction();
        hideAllFragment(ft);
        switch (v.getId()) {
            case R.id.rb_encyclopedia:
                radioGroup_right.clearCheck();
                fl_registration.setVisibility(View.GONE);
                if (encyclopediaFragment == null) {
                    encyclopediaFragment = new EncyclopediaFragment();
                    ft.add(R.id.framelayout, encyclopediaFragment);
                } else {
                    ft.show(encyclopediaFragment);
                }
                break;
            case R.id.rb_lectureroom:
                fl_registration.setVisibility(View.GONE);
                radioGroup_left.clearCheck();
                if (lectureroomFragment == null) {
                    lectureroomFragment = new LectureroomFragment();
                    ft.add(R.id.framelayout, lectureroomFragment);
                } else {
                    ft.show(lectureroomFragment);
                }
                break;
            case R.id.rb_mypage:
                fl_registration.setVisibility(View.GONE);
                radioGroup_left.clearCheck();
                if (mypageFragment == null) {
                    mypageFragment = new MypageFragment();
                    ft.add(R.id.framelayout, mypageFragment);
                } else {
                    ft.show(mypageFragment);
                }
                break;
            case R.id.rb_diagnosis:
                fl_registration.setVisibility(View.GONE);
                radioGroup_right.clearCheck();
                break;
            case R.id.iv_inquiry:
                radioGroup_right.clearCheck();
                radioGroup_left.clearCheck();
                fl_registration.setVisibility(View.VISIBLE);
                if (inquiryFragment == null) {
                    inquiryFragment = new InquiryFragment();
                    ft.add(R.id.framelayout, inquiryFragment);
                } else {
                    ft.show(inquiryFragment);
                }
                break;
        }
        ft.commit();

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HomeActivity.this, RegistrationAtivity.class);
            startActivity(intent);
        }
    };
}
