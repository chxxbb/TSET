package com.example.chen.tset.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.chen.tset.R;

public class PersonaldataActivity extends AppCompatActivity {
    private RelativeLayout rl_name, rl_gender, rl_phone;
    private LinearLayout ll_rutmypage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata);
        findView();
    }

    private void findView() {
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        ll_rutmypage = (LinearLayout) findViewById(R.id.ll_rutmypage);
        rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_name.setOnClickListener(listener);
        ll_rutmypage.setOnClickListener(listener);
        rl_gender.setOnClickListener(listener);
        rl_phone.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_name:
                    Intent intent = new Intent(PersonaldataActivity.this, NamepageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rl_gender:
                    Intent intent1 = new Intent(PersonaldataActivity.this, GenderpageActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.rl_phone:
                    Intent intent2 = new Intent(PersonaldataActivity.this, PhonechangeActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.ll_rutmypage:
                    finish();
                    break;
            }
        }
    };
}
