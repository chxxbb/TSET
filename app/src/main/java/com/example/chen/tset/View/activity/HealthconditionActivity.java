package com.example.chen.tset.View.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.chen.tset.Data.Http_data;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.IListener;
import com.example.chen.tset.Utils.ListenerManager;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.SharedPsaveuser;
import com.example.chen.tset.page.view.NoteEditor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 健康状况
 */
public class HealthconditionActivity extends MyBaseActivity implements IListener {

    private LinearLayout ll_consult_return;

    private CardView cv_healthcondition_condition_import;

    private LinearLayout cv_healthcondition_condition_describe;

    private NoteEditor et_healthcondition;

    private TextView tv_heath_save;

    String content;

    private ToggleButton tb1, tb2, tb3, tb4, tb5, tb6, tb7, tb8, tb9, tb10, tb11, tb12, tb13, tb14;

    String tag;

    List<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthcondition);
        try {

            ListenerManager.getInstance().registerListtener(this);
            list = new ArrayList<>();
            findView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void findView() {
        ll_consult_return = (LinearLayout) findViewById(R.id.ll_consult_return);
        cv_healthcondition_condition_describe = (LinearLayout) findViewById(R.id.cv_healthcondition_condition_describe);
        cv_healthcondition_condition_import = (CardView) findViewById(R.id.cv_healthcondition_condition_import);
        et_healthcondition = (NoteEditor) findViewById(R.id.et_healthcondition);
        tv_heath_save = (TextView) findViewById(R.id.tv_heath_save);

        tb1 = (ToggleButton) findViewById(R.id.tb1);
        tb2 = (ToggleButton) findViewById(R.id.tb2);
        tb3 = (ToggleButton) findViewById(R.id.tb3);
        tb4 = (ToggleButton) findViewById(R.id.tb4);
        tb5 = (ToggleButton) findViewById(R.id.tb5);
        tb6 = (ToggleButton) findViewById(R.id.tb6);
        tb7 = (ToggleButton) findViewById(R.id.tb7);
        tb8 = (ToggleButton) findViewById(R.id.tb8);
        tb9 = (ToggleButton) findViewById(R.id.tb9);
        tb10 = (ToggleButton) findViewById(R.id.tb10);
        tb11 = (ToggleButton) findViewById(R.id.tb11);
        tb12 = (ToggleButton) findViewById(R.id.tb12);
        tb13 = (ToggleButton) findViewById(R.id.tb13);
        tb14 = (ToggleButton) findViewById(R.id.tb14);

        et_healthcondition.addTextChangedListener(textchanglistener);


        ll_consult_return.setOnClickListener(listener);
        cv_healthcondition_condition_describe.setOnClickListener(listener);
        cv_healthcondition_condition_import.setOnClickListener(listener);


        tb1.setOnCheckedChangeListener(tblistener);
        tb2.setOnCheckedChangeListener(tblistener);
        tb3.setOnCheckedChangeListener(tblistener);
        tb4.setOnCheckedChangeListener(tblistener);
        tb5.setOnCheckedChangeListener(tblistener);
        tb6.setOnCheckedChangeListener(tblistener);
        tb7.setOnCheckedChangeListener(tblistener);
        tb8.setOnCheckedChangeListener(tblistener);
        tb9.setOnCheckedChangeListener(tblistener);
        tb10.setOnCheckedChangeListener(tblistener);
        tb11.setOnCheckedChangeListener(tblistener);
        tb12.setOnCheckedChangeListener(tblistener);
        tb13.setOnCheckedChangeListener(tblistener);
        tb14.setOnCheckedChangeListener(tblistener);

        tv_heath_save.setOnClickListener(listener);
        et_healthcondition.setNotesMinLines(5);


    }

    //点击收藏后，判断标签是否被选中，如果选中则放入标签集合中，发送至后台
    private CompoundButton.OnCheckedChangeListener tblistener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {


                case R.id.tb1:
                    if (isChecked) {
                        list.add("流涕");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("流涕")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;


                case R.id.tb2:
                    if (isChecked) {
                        list.add("咳嗽");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("咳嗽")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;

                case R.id.tb3:
                    if (isChecked) {
                        list.add("头疼");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("头疼")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;

                case R.id.tb4:
                    if (isChecked) {
                        list.add("发热");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("发热")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;

                case R.id.tb5:
                    if (isChecked) {
                        list.add("口腔溃疡");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("口腔溃疡")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;

                case R.id.tb6:
                    if (isChecked) {
                        list.add("口干口渴");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("口干口渴")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;

                case R.id.tb7:
                    if (isChecked) {
                        list.add("鼻塞");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("鼻塞")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;

                case R.id.tb8:
                    if (isChecked) {
                        list.add("咽疼");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("咽疼")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;

                case R.id.tb9:
                    if (isChecked) {
                        list.add("出疹");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("出疹")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;

                case R.id.tb10:
                    if (isChecked) {
                        list.add("睡眠问题");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("睡眠问题")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;

                case R.id.tb11:
                    if (isChecked) {
                        list.add("腹泻");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("腹泻")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;


                case R.id.tb12:
                    if (isChecked) {
                        list.add("腹疼");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("腹疼")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;


                case R.id.tb13:
                    if (isChecked) {
                        list.add("腹胀");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("腹胀")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;


                case R.id.tb14:
                    if (isChecked) {
                        list.add("呕吐");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals("呕吐")) {
                                list.remove(i);
                            }
                        }
                    }
                    break;

            }
        }
    };

    //判断是否选中过疾病标签或输入疾病详情，如果是则改变保存按钮状态并且可以点击
    private TextWatcher textchanglistener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_healthcondition.getText().toString().length() == 0 && list.size() == 0) {
                tv_heath_save.setTextColor(android.graphics.Color.parseColor("#bbbbbb"));
            } else {
                tv_heath_save.setTextColor(android.graphics.Color.parseColor("#6fc9e6"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_consult_return:
                    finish();
                    break;

                case R.id.cv_healthcondition_condition_describe:
                    et_healthcondition.requestFocus();
                    //点击后弹出软键盘,edittext获取焦点
                    InputMethodManager imm = (InputMethodManager) HealthconditionActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    cv_healthcondition_condition_describe.setVisibility(View.GONE);
                    cv_healthcondition_condition_import.setVisibility(View.VISIBLE);
                    et_healthcondition.setCursorVisible(false);
                    break;

                case R.id.tv_heath_save:


                    if (list.size() == 0 && et_healthcondition.getText().toString().length() == 0) {
                        Toast.makeText(HealthconditionActivity.this, "请选择或输入您的健康状况", Toast.LENGTH_SHORT).show();
                    } else {

                        httpinit();
                    }
                    break;
            }
        }

    };

    @Override
    public void notifyAllActivity(String str) {

    }


    private void httpinit() {
        content = et_healthcondition.getText().toString();
        String a = list.toString();
        String tag = a.substring(1, a.length() - 1);
        SharedPsaveuser sp = new SharedPsaveuser(this);

        OkHttpUtils
                .post()
                .url(Http_data.http_data + "/AddHealth")
                .addParams("userId", sp.getTag().getId() + "")
                .addParams("tag", tag)
                .addParams("content", content)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(HealthconditionActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (response.equals("1")) {
                            Toast.makeText(HealthconditionActivity.this, "今天你已经添加过你的健康状况了", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("2")) {
                            Toast.makeText(HealthconditionActivity.this, "添加健康状况失败", Toast.LENGTH_SHORT).show();
                        } else {
                            //发送广播通知诊疗页面更新
                            ListenerManager.getInstance().sendBroadCast("更新日历页面");
                            Toast.makeText(HealthconditionActivity.this, "保存成功，你可以在健康日历上面查看", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
}
