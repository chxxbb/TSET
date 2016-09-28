package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
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
    private Button btn_helth, btn_else;
    private TextView tv_henlth;
    private LinearLayout ll_henlth, ll_describe, linearLayout, ll_consult_return;
    private HorizontalScrollView horscrollview;

    int confirm;
    int rest;


    private ToggleButton tb1, tb2, tb3, tb4, tb5, tb6, tb7, tb8, tb9, tb10, tb11, tb12, tb13, tb14;
    private RadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8, rb9, rb10, rb11, rb12, rb13, rb14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthcondition);
        findView();
    }

    private void findView() {
//        btn_helth = (Button) findViewById(R.id.btn_helth);
//        ll_henlth = (LinearLayout) findViewById(R.id.ll_henlth);
//        tv_henlth = (TextView) findViewById(R.id.tv_henlth);
//        ll_describe = (LinearLayout) findViewById(R.id.ll_describe);
//        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
//        btn_else = (Button) findViewById(R.id.btn_else);
//        ll_consult_return = (LinearLayout) findViewById(R.id.ll_consult_return);

//        horscrollview = (HorizontalScrollView) findViewById(R.id.horscrollview);

//        tb1 = (ToggleButton) findViewById(R.id.tb1);
//        tb2 = (ToggleButton) findViewById(R.id.tb2);
//        tb3 = (ToggleButton) findViewById(R.id.tb3);
//        tb4 = (ToggleButton) findViewById(R.id.tb4);
//        tb5 = (ToggleButton) findViewById(R.id.tb5);
//        tb6 = (ToggleButton) findViewById(R.id.tb6);
//        tb7 = (ToggleButton) findViewById(R.id.tb7);
//        tb8 = (ToggleButton) findViewById(R.id.tb8);
//        tb9 = (ToggleButton) findViewById(R.id.tb9);
//        tb10 = (ToggleButton) findViewById(R.id.tb10);
//        tb11 = (ToggleButton) findViewById(R.id.tb11);
//        tb12 = (ToggleButton) findViewById(R.id.tb12);
//        tb13 = (ToggleButton) findViewById(R.id.tb13);
//        tb14 = (ToggleButton) findViewById(R.id.tb14);
//
//
//        rb1 = (RadioButton) findViewById(R.id.rb1);
//        rb2 = (RadioButton) findViewById(R.id.rb2);
//        rb3 = (RadioButton) findViewById(R.id.rb3);
//        rb4 = (RadioButton) findViewById(R.id.rb4);
//        rb5 = (RadioButton) findViewById(R.id.rb5);
//        rb6 = (RadioButton) findViewById(R.id.rb6);
//        rb7 = (RadioButton) findViewById(R.id.rb7);
//        rb8 = (RadioButton) findViewById(R.id.rb8);
//        rb9 = (RadioButton) findViewById(R.id.rb9);
//        rb10 = (RadioButton) findViewById(R.id.rb10);
//        rb11 = (RadioButton) findViewById(R.id.rb11);
//        rb12 = (RadioButton) findViewById(R.id.rb12);
//        rb13 = (RadioButton) findViewById(R.id.rb13);
//        rb14 = (RadioButton) findViewById(R.id.rb14);


//        ll_henlth.setOnClickListener(listener);
//        btn_helth.setOnClickListener(listener);
//        btn_else.setOnClickListener(listener);
//        ll_consult_return.setOnClickListener(listener);

//        tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb1.setVisibility(View.VISIBLE);
//                } else {
//                    rb1.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb2.setVisibility(View.VISIBLE);
//                } else {
//                    rb2.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        tb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb3.setVisibility(View.VISIBLE);
//                } else {
//                    rb3.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        tb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb4.setVisibility(View.VISIBLE);
//                } else {
//                    rb4.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        tb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb5.setVisibility(View.VISIBLE);
//                } else {
//                    rb5.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        tb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb6.setVisibility(View.VISIBLE);
//                } else {
//                    rb6.setVisibility(View.GONE);
//                }
//            }
//        });
//
//
//        tb7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb7.setVisibility(View.VISIBLE);
//                } else {
//                    rb7.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        tb8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb8.setVisibility(View.VISIBLE);
//                } else {
//                    rb8.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        tb9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb9.setVisibility(View.VISIBLE);
//                } else {
//                    rb9.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        tb10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb10.setVisibility(View.VISIBLE);
//                } else {
//                    rb10.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        tb11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb11.setVisibility(View.VISIBLE);
//                } else {
//                    rb11.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        tb12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb12.setVisibility(View.VISIBLE);
//                } else {
//                    rb12.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        tb13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb13.setVisibility(View.VISIBLE);
//                } else {
//                    rb13.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        tb14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rb14.setVisibility(View.VISIBLE);
//                } else {
//                    rb14.setVisibility(View.GONE);
//                }
//            }
//        });


    }


//    private View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.btn_helth:
//
//                    if(confirm!=1){
//                        ll_henlth.setVisibility(View.GONE);
//                        ll_describe.setVisibility(View.VISIBLE);
//                        linearLayout.setVisibility(View.VISIBLE);
//                        horscrollview.setVisibility(View.VISIBLE);
//                        //第三方动画库
//                        YoYo.with(Techniques.Landing)
//                                .duration(500)
//                                .playOn(findViewById(R.id.ll_describe));
//                        YoYo.with(Techniques.Landing)
//                                .duration(500)
//                                .playOn(findViewById(R.id.linearLayout));
//                        btn_helth.setText("添加我的症状");
//                        confirm=1;
//                    }
//
//
//                    break;
//                case R.id.btn_else:
//
//                    if(rest!=1){
//                        YoYo.with(Techniques.Landing)
//                                .duration(500)
//                                .playOn(findViewById(R.id.ll_describe));
//                        YoYo.with(Techniques.Landing)
//                                .duration(500)
//                                .playOn(findViewById(R.id.ll_henlth));
//                        ll_henlth.setVisibility(View.VISIBLE);
//                        rest=1;
//                    }
//
//                    break;
//                case R.id.ll_consult_return:
//                    finish();
//                    break;
//            }
//        }
//
//
//    };
}
