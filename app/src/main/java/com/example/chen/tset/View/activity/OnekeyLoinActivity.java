package com.example.chen.tset.View.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.chen.tset.R;

/**
 * 一键登录页面
 */
public class OnekeyLoinActivity extends AppCompatActivity {

    private LinearLayout ll_use_password_login;

    private EditText onekeylogin_phone_edittext,activity_onekey_Verification_code;

    private Button activity_onekeyr_Verification_code_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onekey_loin);
        findView();
    }

    private void findView() {
        ll_use_password_login = (LinearLayout) findViewById(R.id.ll_use_password_login);

        onekeylogin_phone_edittext= (EditText) findViewById(R.id.onekeylogin_phone_edittext);

        activity_onekey_Verification_code= (EditText) findViewById(R.id.activity_onekey_Verification_code);

        activity_onekeyr_Verification_code_button= (Button) findViewById(R.id.activity_onekeyr_Verification_code_button);

        ll_use_password_login.setOnClickListener(listener);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(OnekeyLoinActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };
}
