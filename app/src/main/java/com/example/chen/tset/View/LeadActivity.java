package com.example.chen.tset.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.chen.tset.R;
import com.example.chen.tset.page.LeadAdapter;

public class LeadActivity extends AppCompatActivity {
    private ViewPager viewpager;
    private Button tv_click;
    private LeadAdapter adapter;
    private ImageView iv_lead_spot1, iv_lead_spot2, iv_lead_spot3, iv_lead_spot4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        SharedPreferences sp = getSharedPreferences("lead", MODE_PRIVATE);
        boolean isclick = sp.getBoolean("isclick", true);
        if (!isclick) {
            Intent intent1 = new Intent(LeadActivity.this, LogActivity.class);
            startActivity(intent1);
            finish();
        } else {
            btclick();
            imageDate();
        }
        fidView();
    }

    private void fidView() {
        iv_lead_spot1 = (ImageView) findViewById(R.id.iv_lead_spot1);
        iv_lead_spot2 = (ImageView) findViewById(R.id.iv_lead_spot2);
        iv_lead_spot3 = (ImageView) findViewById(R.id.iv_lead_spot3);
        iv_lead_spot4 = (ImageView) findViewById(R.id.iv_lead_spot4);

    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            tv_click.setVisibility(View.INVISIBLE);
            if (position == 3) {
                tv_click.setVisibility(View.VISIBLE);
                iv_lead_spot1.setImageResource(R.drawable.lead_slide_sign1);
                iv_lead_spot2.setImageResource(R.drawable.lead_slide_sign1);
                iv_lead_spot4.setImageResource(R.drawable.lead_slide_sign);
                iv_lead_spot3.setImageResource(R.drawable.lead_slide_sign1);
            } else if (position == 0) {
                iv_lead_spot1.setImageResource(R.drawable.lead_slide_sign);
                iv_lead_spot2.setImageResource(R.drawable.lead_slide_sign1);
                iv_lead_spot4.setImageResource(R.drawable.lead_slide_sign1);
                iv_lead_spot3.setImageResource(R.drawable.lead_slide_sign1);
            } else if (position == 1) {
                iv_lead_spot1.setImageResource(R.drawable.lead_slide_sign1);
                iv_lead_spot2.setImageResource(R.drawable.lead_slide_sign);
                iv_lead_spot4.setImageResource(R.drawable.lead_slide_sign1);
                iv_lead_spot3.setImageResource(R.drawable.lead_slide_sign1);
            } else if (position == 2) {
                iv_lead_spot1.setImageResource(R.drawable.lead_slide_sign1);
                iv_lead_spot2.setImageResource(R.drawable.lead_slide_sign1);
                iv_lead_spot4.setImageResource(R.drawable.lead_slide_sign1);
                iv_lead_spot3.setImageResource(R.drawable.lead_slide_sign);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void imageDate() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new LeadAdapter();
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(listener);
        ImageView imageView = null;
        imageView = (ImageView) View.inflate(this, R.layout.activity_lead_item, null);
        imageView.setImageResource(R.drawable.lead_item3);
        adapter.addim(imageView);
        imageView = (ImageView) View.inflate(this, R.layout.activity_lead_item, null);
        imageView.setImageResource(R.drawable.lead_item4);
        adapter.addim(imageView);

        imageView = (ImageView) View.inflate(this, R.layout.activity_lead_item, null);
        imageView.setImageResource(R.drawable.lead_item3);
        adapter.addim(imageView);
        imageView = (ImageView) View.inflate(this, R.layout.activity_lead_item, null);
        imageView.setImageResource(R.drawable.lead_item4);
        adapter.addim(imageView);
        adapter.notifyDataSetChanged();
    }

    private void btclick() {
        tv_click = (Button) findViewById(R.id.tv_click);
        tv_click.setVisibility(View.INVISIBLE);
        tv_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("lead", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("isclick", false);
                edit.commit();
                Intent intent = new Intent(LeadActivity.this, LogActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
