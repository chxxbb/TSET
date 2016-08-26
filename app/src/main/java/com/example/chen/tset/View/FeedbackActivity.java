package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chen.tset.R;

public class FeedbackActivity extends AppCompatActivity {
    private TextView tv_feedback;
    private EditText et_feedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        findView();
    }

    private void findView() {
        tv_feedback= (TextView) findViewById(R.id.tv_feedback);
        et_feedback= (EditText) findViewById(R.id.et_feedback);
        et_feedback.addTextChangedListener(textchanglistener);
    }
    private TextWatcher textchanglistener=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tv_feedback.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
            if(et_feedback.getText().toString().length()==0){
                tv_feedback.setTextColor(android.graphics.Color.parseColor("#e0e0e0"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
