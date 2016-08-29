package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.chen.tset.R;

public class ThemeselectActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout ll_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themeselect);
        finView();
    }

    private void finView() {
        ll_set= (LinearLayout) findViewById(R.id.ll_set);
        ll_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
