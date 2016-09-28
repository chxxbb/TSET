package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;

/**
 * 修改主题
 */
public class ThemeselectActivity extends MyBaseActivity implements View.OnClickListener {
    private LinearLayout ll_set;
    private RadioButton rbtn_white_front, rbtn_white_queen, rbtn_blue_front, rbtn_blue_queen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themeselect);
        finView();
    }

    private void finView() {
        ll_set = (LinearLayout) findViewById(R.id.ll_set);
        rbtn_white_front = (RadioButton) findViewById(R.id.rbtn_white_front);
        rbtn_white_queen = (RadioButton) findViewById(R.id.rbtn_white_queen);
        rbtn_blue_front = (RadioButton) findViewById(R.id.rbtn_blue_front);
        rbtn_blue_queen = (RadioButton) findViewById(R.id.rbtn_blue_queen);
        rbtn_white_queen.setChecked(true);
        rbtn_white_queen.setText("使用中");
        rbtn_white_front.setChecked(true);
        rbtn_blue_front.setChecked(false);
        rbtn_blue_queen.setChecked(false);
        rbtn_blue_queen.setText("使用");
        rbtn_blue_front.setEnabled(false);
        rbtn_white_front.setEnabled(false);
        ll_set.setOnClickListener(this);
        rbtn_white_queen.setOnClickListener(this);
        rbtn_blue_queen.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_set:
                finish();
                break;
            case R.id.rbtn_white_queen:
                rbtn_white_queen.setText("使用中");
                rbtn_white_front.setChecked(true);
                rbtn_blue_front.setChecked(false);
                rbtn_blue_queen.setChecked(false);
                rbtn_blue_queen.setText("使用");
                break;
            case R.id.rbtn_blue_queen:
                rbtn_blue_queen.setText("使用中");
                rbtn_white_queen.setText("使用");
                rbtn_white_front.setChecked(false);
                rbtn_white_queen.setChecked(false);
                rbtn_blue_front.setChecked(true);
        }
    }
}
