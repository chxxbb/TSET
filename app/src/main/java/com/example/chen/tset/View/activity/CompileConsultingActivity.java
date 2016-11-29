package com.example.chen.tset.View.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.example.chen.tset.R;
import com.example.chen.tset.Utils.SharedPsaveuser;

public class CompileConsultingActivity extends AppCompatActivity {
    private LinearLayout ll_return;
    private RelativeLayout ll_add_medicine_remind, ll_add_health_condition;
    ToggleButton toggleButton;
    SharedPsaveuser sp = new SharedPsaveuser(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compile_consulting);


        findView();


    }

    private void findView() {
        ll_return = (LinearLayout) findViewById(R.id.ll_return);
        ll_add_medicine_remind = (RelativeLayout) findViewById(R.id.ll_add_medicine_remind);
        ll_add_health_condition = (RelativeLayout) findViewById(R.id.ll_add_health_condition);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        ll_return.setOnClickListener(listener);
        ll_add_medicine_remind.setOnClickListener(listener);
        ll_add_health_condition.setOnClickListener(listener);


        if (sp.getRemindState() == true) {
            toggleButton.setChecked(false);
        } else {
            toggleButton.setChecked(true);
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sp.setRemindState(false);
                } else {
                    sp.setRemindState(true);
                }
            }
        });

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_return:
                    finish();
                    break;
                //用药
                case R.id.ll_add_medicine_remind:
                    Intent intent = new Intent(CompileConsultingActivity.this, CompileremindActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                //跳转至健康状况
                case R.id.ll_add_health_condition:
                    Intent intent1 = new Intent(CompileConsultingActivity.this, HealthconditionActivity.class);
                    startActivity(intent1);
                    finish();
                    break;
            }
        }
    };
}
