package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.chen.tset.R;

public class GenderpageActivity extends AppCompatActivity {
    private LinearLayout ll_rutgender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genderpage);
        findView();
    }

    private void findView() {
        ll_rutgender = (LinearLayout) findViewById(R.id.ll_rutgender);
        ll_rutgender.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_rutgender:
                    finish();
                    break;
            }
        }
    };
}
