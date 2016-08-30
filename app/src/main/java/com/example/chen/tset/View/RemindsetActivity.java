package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.chen.tset.R;

public class RemindsetActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout ll_remidsetreturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remindset);
        findView();
    }

    private void findView() {
        ll_remidsetreturn = (LinearLayout) findViewById(R.id.ll_remidsetreturn);
        ll_remidsetreturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_remidsetreturn:
                finish();
                break;
        }
    }
}
