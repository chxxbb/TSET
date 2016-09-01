package com.example.chen.tset.View;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.app.AlertDialog.Builder;
import android.widget.TextView;

import com.example.chen.tset.R;

public class RegistrationAtivity extends AppCompatActivity {
    private RelativeLayout rl_city;
    private Dialog setHeadDialog;
    private View dialogView;
    private TextView tv_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_ativity);
        findView();
    }

    private void findView() {
        rl_city= (RelativeLayout) findViewById(R.id.rl_city);
        tv_city= (TextView) findViewById(R.id.tv_city);
        rl_city.setOnClickListener(listener);
    }
    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog();

        }
    };
    public void showDialog() {
        setHeadDialog = new Builder(this).create();
        setHeadDialog.show();
        dialogView = View.inflate(getApplicationContext(), R.layout.registration_city_case, null);
        setHeadDialog.getWindow().setContentView(dialogView);
        WindowManager.LayoutParams lp = setHeadDialog.getWindow()
                .getAttributes();
        setHeadDialog.getWindow().setAttributes(lp);
        dialogclick();
    }

    private void dialogclick() {
        Button btn_cancel= (Button)dialogView.findViewById(R.id.btn_cancel);
        Button btn_chnegdu= (Button) dialogView.findViewById(R.id.btn_chengdu);
        Button btn_shenzheng= (Button) dialogView.findViewById(R.id.btn_shenzheng);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
            }
        });
        btn_chnegdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
                tv_city.setText("成都");
                tv_city.setTextColor(android.graphics.Color.parseColor("#323232"));

            }
        });
        btn_shenzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadDialog.dismiss();
                tv_city.setText("深圳");
                tv_city.setTextColor(android.graphics.Color.parseColor("#323232"));
            }
        });
    }
}
