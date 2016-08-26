package com.example.chen.tset.View;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.chen.tset.page.EncyclopediaFragment;
import com.example.chen.tset.R;
import com.example.chen.tset.page.LectureroomFragment;

public class HomeActivity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;
    private RadioButton rb_encyclopedia,rb_lectureroom;
    private RadioGroup radioGroup_right,radioGroup_left;
    private FrameLayout framelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findView();
        init();
    }

    private void findView() {
        rb_encyclopedia= (RadioButton) findViewById(R.id.rb_encyclopedia);
        framelayout= (FrameLayout) findViewById(R.id.framelayout);
        rb_lectureroom= (RadioButton) findViewById(R.id.rb_lectureroom);
        radioGroup_left= (RadioGroup) findViewById(R.id.radioGroup_left);
        radioGroup_right= (RadioGroup) findViewById(R.id.radioGroup_right);
        rb_encyclopedia.setOnClickListener(listener);
        rb_lectureroom.setOnClickListener(listener);
    }

    private void init() {
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.replace(R.id.framelayout,new EncyclopediaFragment());
        ft.commit();
    }
    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rb_encyclopedia:
                    radioGroup_right.clearCheck();
                    FragmentTransaction ft1=fm.beginTransaction();
                    ft1.replace(R.id.framelayout,new EncyclopediaFragment());
                    ft1.commit();
                    break;
                case R.id.rb_lectureroom:
                    radioGroup_left.clearCheck();
                    FragmentTransaction ft2=fm.beginTransaction();
                    ft2.replace(R.id.framelayout,new LectureroomFragment());
                    ft2.commit();
                    break;
            }
        }
    };


}
