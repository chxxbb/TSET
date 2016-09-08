package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chen.tset.R;

public class EvaluatepageActivity extends AppCompatActivity {
    private TextView tv_evaluate;
    private EditText et_evaluate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluatepage);
        findView();
    }

    private void findView() {
        tv_evaluate= (TextView) findViewById(R.id.tv_evaluate);
        et_evaluate= (EditText) findViewById(R.id.et_evaluate);
        et_evaluate.addTextChangedListener(textlistener);
    }
    private TextWatcher textlistener=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_evaluate.getText().toString().length() != 0) {
                tv_evaluate.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
            } else {
                tv_evaluate.setTextColor(android.graphics.Color.parseColor("#e0e0e0"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
