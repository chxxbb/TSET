package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chen.tset.R;

public class PhonechangeActivity extends AppCompatActivity {
    private EditText et_verificationcode, et_phone;
    private TextView tv_pas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonechange);
        findView();
    }

    private void findView() {
        et_verificationcode = (EditText) findViewById(R.id.et_verificationcode);
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_pas = (TextView) findViewById(R.id.tv_pas);
        et_verificationcode.addTextChangedListener(textListener);
        et_phone.addTextChangedListener(textListener);
    }

    private TextWatcher textListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_phone.length() != 0 && et_verificationcode.length() != 0) {
                tv_pas.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
            } else {
                tv_pas.setTextColor(android.graphics.Color.parseColor("#e0e0e0"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
