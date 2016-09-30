package com.example.chen.tset.View;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;

/**
 * 健康状况
 */
public class HealthconditionActivity extends MyBaseActivity {

    private LinearLayout ll_consult_return;

    private CardView cv_healthcondition_condition_describe,cv_healthcondition_condition_import;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthcondition);
        findView();
    }

    private void findView() {
        ll_consult_return = (LinearLayout) findViewById(R.id.ll_consult_return);
        cv_healthcondition_condition_describe= (CardView) findViewById(R.id.cv_healthcondition_condition_describe);
        cv_healthcondition_condition_import= (CardView) findViewById(R.id.cv_healthcondition_condition_import);
        ll_consult_return.setOnClickListener(listener);
        cv_healthcondition_condition_describe.setOnClickListener(listener);
        cv_healthcondition_condition_import.setOnClickListener(listener);


    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_consult_return:
                    finish();
                    break;

                case R.id.cv_healthcondition_condition_describe:
                    cv_healthcondition_condition_describe.setVisibility(View.GONE);
                    cv_healthcondition_condition_import.setVisibility(View.VISIBLE);
                    break;
            }
        }


    };
}
