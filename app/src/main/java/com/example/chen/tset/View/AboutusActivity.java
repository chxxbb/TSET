package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;
import com.example.chen.tset.Utils.Version_numberSP;

//关于我们页面
public class AboutusActivity extends MyBaseActivity {
    private LinearLayout ll_aboutus;
    private TextView tv_aboutus_version_umber;
    Version_numberSP version_numberSP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        findView();
        init();
    }



    private void findView() {
        ll_aboutus = (LinearLayout) findViewById(R.id.ll_aboutus);
        tv_aboutus_version_umber= (TextView) findViewById(R.id.tv_aboutus_version_umber);
        ll_aboutus.setOnClickListener(litener);



    }

    private void init() {
        version_numberSP=new Version_numberSP(this);
        String version_number=version_numberSP.getversionNumber();
        tv_aboutus_version_umber.setText(version_number);
    }

    private View.OnClickListener litener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
