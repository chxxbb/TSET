package com.example.chen.tset.View.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.chen.tset.R;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        init();
    }

    private void init() {
        url = getIntent().getStringExtra("url");

        webView = (WebView) findViewById(R.id.webView);

        //设置WebView属性，能够执行Javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(url);

        webView.setWebViewClient(new HelloWebViewClient());
    }


    //Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
