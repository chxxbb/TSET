package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chen.tset.R;

public class HealthconditionActivity extends AppCompatActivity {
    private Button btn_helth, btn_else;
    private TextView tv_henlth;
    private LinearLayout ll_henlth, ll_describe, linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthcondition);
        findView();
    }

    private void findView() {
        btn_helth = (Button) findViewById(R.id.btn_helth);
        ll_henlth = (LinearLayout) findViewById(R.id.ll_henlth);
        tv_henlth = (TextView) findViewById(R.id.tv_henlth);
        ll_describe = (LinearLayout) findViewById(R.id.ll_describe);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        btn_else = (Button) findViewById(R.id.btn_else);
        btn_helth.setOnClickListener(listener);
        btn_else.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_helth:
                    ll_describe.setVisibility(View.VISIBLE);
//                    linearLayout.setVisibility(View.VISIBLE);
                    AnimationSet animationSet = new AnimationSet(true);
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                    alphaAnimation.setDuration(500);
                    animationSet.addAnimation(alphaAnimation);
                    animationSet.setFillAfter(true);
                    ll_describe.startAnimation(animationSet);
                    linearLayout.startAnimation(animationSet);

//            btn_helth.setVisibility(View.GONE);
//            tv_henlth.setVisibility(View.GONE);
//            ll_henlth.setVisibility(View.GONE);

                    break;
                case R.id.btn_else:
                    tv_henlth.setVisibility(View.VISIBLE);
                    ll_henlth.setVisibility(View.VISIBLE);
                    btn_helth.setVisibility(View.VISIBLE);
                    btn_helth.setText("添加我的症状");
                    btn_else.setText("取消");
            }
        }


    };
}
