package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;

//关于我们页面
public class AboutusActivity extends MyBaseActivity {
    private LinearLayout ll_aboutus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        findView();
    }

    private void findView() {
        ll_aboutus = (LinearLayout) findViewById(R.id.ll_aboutus);
        ll_aboutus.setOnClickListener(litener);
    }

    private View.OnClickListener litener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
