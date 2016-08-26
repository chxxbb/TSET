package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chen.tset.R;

public class PasswordSettingActivity extends AppCompatActivity {
    private TextView tv_pas;
    private EditText et_newpassword, et_forpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setting);
        findView();
    }

    private void findView() {
        tv_pas = (TextView) findViewById(R.id.tv_pas);
        et_newpassword= (EditText) findViewById(R.id.et_newpassword);
        et_forpassword= (EditText) findViewById(R.id.et_forpassword);
        et_newpassword.addTextChangedListener(textchangelisterer);
        et_forpassword.addTextChangedListener(textchangelisterer);
    }
    private TextWatcher textchangelisterer=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(et_newpassword.getText().toString().length()!=0&&et_forpassword.getText().toString().length()!=0){
                tv_pas.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
            }else {
                tv_pas.setTextColor(android.graphics.Color.parseColor("#e0e0e0"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}