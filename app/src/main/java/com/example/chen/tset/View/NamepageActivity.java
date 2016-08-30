package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chen.tset.R;

public class NamepageActivity extends AppCompatActivity {
    private EditText et_name_save;
    private TextView tv_pas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namepage);
        findView();
    }

    private void findView() {
        et_name_save= (EditText) findViewById(R.id.et_name_save);
        tv_pas= (TextView) findViewById(R.id.tv_pas);
        et_name_save.addTextChangedListener(textListener);
    }
    private TextWatcher textListener=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tv_pas.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
            if (et_name_save.length()==0){
                tv_pas.setTextColor(android.graphics.Color.parseColor("#e0e0e0"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
