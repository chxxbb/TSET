package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;

import com.example.chen.tset.R;

public class ConsultPageActivity extends AppCompatActivity {
    private ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_page);
        findview();
    }

    private void findview() {
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        scrollview.setVerticalScrollBarEnabled(false);
    }
}
