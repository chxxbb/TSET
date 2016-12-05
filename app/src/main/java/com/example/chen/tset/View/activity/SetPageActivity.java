package com.example.chen.tset.View.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;

/**
 * 设置页面
 */
public class SetPageActivity extends MyBaseActivity implements View.OnClickListener {
    private RelativeLayout rl_themese, rl_feedback, rl_aboutus, rl_remidset, rl_setpass, rl_exit;
    private LinearLayout ll_return;
    SharedPsaveuser sp;
    TextView tv_pass_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_page);
        try {


            sp = new SharedPsaveuser(this);
            findView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findView() {
        rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
        rl_aboutus = (RelativeLayout) findViewById(R.id.rl_aboutus);
        rl_remidset = (RelativeLayout) findViewById(R.id.rl_remidset);
        rl_setpass = (RelativeLayout) findViewById(R.id.rl_setpass);
        ll_return = (LinearLayout) findViewById(R.id.ll_return);
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        tv_pass_set = (TextView) findViewById(R.id.tv_pass_set);
        ll_return.setOnClickListener(this);
        rl_exit.setOnClickListener(this);
        rl_feedback.setOnClickListener(this);
        rl_aboutus.setOnClickListener(this);
        rl_remidset.setOnClickListener(this);
        rl_setpass.setOnClickListener(this);


        if (sp.getTag().getPassword() != null) {
            tv_pass_set.setText("密码修改");
        } else {
            tv_pass_set.setText("密码设置");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_feedback:
                //意见反馈
                Intent intent1 = new Intent(SetPageActivity.this, FeedbackActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_aboutus:
                //关于我们
                Intent intent2 = new Intent(SetPageActivity.this, AboutusActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_remidset:
                //提醒设置
                Intent intent3 = new Intent(SetPageActivity.this, RemindsetActivity.class);
                startActivity(intent3);
                break;
            case R.id.rl_setpass:
                //修改密码
                Intent intent4 = new Intent(SetPageActivity.this, PasswordSettingActivity.class);
                startActivity(intent4);
                break;

            case R.id.rl_exit:
                //退出登录

                //清除本地用户信息，重新跳转到登录页面
                sp.clearinit();
                Intent intent5 = new Intent(SetPageActivity.this, LoginSelectActivity.class);
                startActivity(intent5);
                Toast.makeText(SetPageActivity.this, "请重新登录", Toast.LENGTH_SHORT).show();
                HomeActivity.text_homeactivity.finish();
                finish();

                break;
            case R.id.ll_return:
                finish();
                break;
        }
    }
}
