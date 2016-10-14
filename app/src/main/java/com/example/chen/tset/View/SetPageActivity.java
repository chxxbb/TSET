package com.example.chen.tset.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chen.tset.Data.User_Http;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_page);

        sp = new SharedPsaveuser(this);
        findView();
    }

    private void findView() {
        rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
        rl_aboutus = (RelativeLayout) findViewById(R.id.rl_aboutus);
        rl_remidset = (RelativeLayout) findViewById(R.id.rl_remidset);
        rl_setpass = (RelativeLayout) findViewById(R.id.rl_setpass);
        ll_return = (LinearLayout) findViewById(R.id.ll_return);
        rl_exit = (RelativeLayout) findViewById(R.id.rl_exit);
        ll_return.setOnClickListener(this);
        rl_exit.setOnClickListener(this);
        rl_feedback.setOnClickListener(this);
        rl_aboutus.setOnClickListener(this);
        rl_remidset.setOnClickListener(this);
        rl_setpass.setOnClickListener(this);
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

                //清除本地用户信息
                sp.clearinit();
                Intent intent5 = new Intent(SetPageActivity.this, LoginActivity.class);
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
