package com.example.chen.tset.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class SetdataActivity extends AppCompatActivity {
    private EditText et_nickname;
    private RadioButton rbtn_man, rb_madam;
    private Button btn;
    private int editStart;
    private int editEnd;
    private int maxLen = 10; // the max byte
    String gender = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setdata);
        finView();
    }

    private void finView() {
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        rbtn_man = (RadioButton) findViewById(R.id.rbtn_man);
        rb_madam = (RadioButton) findViewById(R.id.rb_madam);
        btn = (Button) findViewById(R.id.btn);
        if (rbtn_man.isChecked()) {
            gender = "男士";
        } else if (rb_madam.isChecked()) {
            gender = "女士";
        }
        btn.setOnClickListener(listener);
        et_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = et_nickname.getSelectionStart();
                editEnd = et_nickname.getSelectionEnd();
                if (!TextUtils.isEmpty(et_nickname.getText())) {
                    int varlength = 0;
                    int size = 0;
                    String etstring = et_nickname.getText().toString().trim();
                    char[] ch = etstring.toCharArray();
                    for (int i = 0; i < ch.length; i++) {
                        size++;
                        if (ch[i] >= 0x4e00 && ch[i] <= 0x9fbb) {
                            varlength = varlength + 2;
                        } else
                            varlength++;
                        if (varlength > 12) {
                            break;
                        }
                    }
                    if (varlength > 12) {
                        s.delete(size - 1, etstring.length());
                    }
                }
            }
        });
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            OkHttpUtils
                    .post()
                    .url(Http_data.http_data + "/ChangeNameAndSex" + "?1")
                    .addParams("nickname", et_nickname.getText().toString())
                    .addParams("gender", gender)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SetdataActivity.this, "网络连接失败", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response.equals("0")) {
                                Intent intent = new Intent(SetdataActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else if (response.equals("1")) {
                                Toast.makeText(SetdataActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };

}
