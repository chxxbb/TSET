package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.chen.tset.R;

public class CompilePharmacyRemindActivity extends AppCompatActivity {
    String startdate;
    String overdate;
    String time1;
    String content1;
    String time2;
    String content2;
    String time3;
    String content3;

    private TextView tv_startdate, tv_overdate, tv_time1, tv_time2, tv_time3, tv_content1, tv_content2, tv_content3, tv_passet;

    private ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compile_pharmacy_remind);
        fidView();
    }

    private void fidView() {
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        scrollview.setVerticalScrollBarEnabled(false);
        tv_passet = (TextView) findViewById(R.id.tv_passet);
        tv_startdate = (TextView) findViewById(R.id.tv_startdate);
        tv_overdate = (TextView) findViewById(R.id.tv_overdate);
        tv_time1 = (TextView) findViewById(R.id.tv_time1);
        tv_content1 = (TextView) findViewById(R.id.tv_content1);
        tv_time2 = (TextView) findViewById(R.id.tv_time2);
        tv_content2 = (TextView) findViewById(R.id.tv_content2);
        tv_time3 = (TextView) findViewById(R.id.tv_time3);
        tv_content3 = (TextView) findViewById(R.id.tv_content3);
        startdate = getIntent().getStringExtra("startdate");
        overdate = getIntent().getStringExtra("overdate");
        time1 = getIntent().getStringExtra("time1");
        content1 = getIntent().getStringExtra("content1");
        time2 = getIntent().getStringExtra("time2");
        content2 = getIntent().getStringExtra("content2");
        time3 = getIntent().getStringExtra("time3");
        content3 = getIntent().getStringExtra("content3");

        tv_startdate.setText(startdate);
        tv_overdate.setText(overdate);
        tv_time1.setText(time1);
        tv_time2.setText(time2);
        tv_time3.setText(time3);
        tv_content1.setText(content1);
        tv_content2.setText(content2);
        tv_content3.setText(content3);

        tv_passet.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_passet:
                    finish();
                    break;

            }
        }
    };
}
