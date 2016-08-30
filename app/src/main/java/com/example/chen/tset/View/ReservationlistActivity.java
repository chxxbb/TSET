package com.example.chen.tset.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.chen.tset.R;

public class ReservationlistActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout ll_myreservationg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationlist);
        findView();
    }

    private void findView() {
        ll_myreservationg = (LinearLayout) findViewById(R.id.ll_myreservationg);
        ll_myreservationg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
