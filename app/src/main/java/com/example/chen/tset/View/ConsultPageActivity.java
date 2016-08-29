package com.example.chen.tset.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.chen.tset.R;

public class ConsultPageActivity extends AppCompatActivity implements View.OnClickListener{
    private ScrollView scrollview;
    private LinearLayout ll_consult_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_page);
        findview();
    }

    private void findview() {
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        ll_consult_return= (LinearLayout) findViewById(R.id.ll_consult_return);
        scrollview.setVerticalScrollBarEnabled(false);
        ll_consult_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_consult_return:
                Intent intent=new Intent(ConsultPageActivity.this,HomeActivity.class);
                startActivity(intent);
                break;


        }
    }
}
