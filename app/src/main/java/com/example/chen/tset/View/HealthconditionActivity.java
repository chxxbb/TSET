package com.example.chen.tset.View;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.MyBaseActivity;

/**
 * 健康状况
 */
public class HealthconditionActivity extends MyBaseActivity {

    private LinearLayout ll_consult_return;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthcondition);
        findView();
    }

    private void findView() {
        ll_consult_return = (LinearLayout) findViewById(R.id.ll_consult_return);
        ll_consult_return.setOnClickListener(listener);




    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_consult_return:
                    finish();
                    break;
            }
        }


    };
}
