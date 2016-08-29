package com.example.chen.tset.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.chen.tset.R;

public class SetPageActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rl_themese, rl_feedback, rl_aboutus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_page);
        findView();
    }

    private void findView() {
        rl_themese = (RelativeLayout) findViewById(R.id.rl_themese);
        rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
        rl_aboutus = (RelativeLayout) findViewById(R.id.rl_aboutus);
        rl_themese.setOnClickListener(this);
        rl_feedback.setOnClickListener(this);
        rl_aboutus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_themese:
                Intent intent = new Intent(SetPageActivity.this, ThemeselectActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_feedback:
                Intent intent1 = new Intent(SetPageActivity.this, FeedbackActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_aboutus:
                Intent intent2 = new Intent(SetPageActivity.this, AboutusActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
