package com.brace.android.b31.spo2andhrv.bpoxy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.brace.android.b31.R;
import com.brace.android.b31.activity.BaseActivity;


/**
 * Created by Admin
 * Date 2018/12/29
 */
public class ShowSpo2DescActivity extends BaseActivity {


    ImageView commentB30BackImg;
    TextView commentB30TitleTv;
    WebView showSpo2DescWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b31_show_spo2desc);


        initViews();

        String desc = getIntent().getStringExtra("spo2_desc");
        //String url = "http://www.baidu.com";
        showSpo2DescWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
        showSpo2DescWebView.loadUrl(getLoadUrl(desc));

    }


    private String getLoadUrl(String desc){
        String url = null;
        if(desc.equals("desc_osah")){
            url =  "file:///android_asset/htmlsrc/osahs_zh.html";
            commentB30TitleTv.setText("OSAH");
        }else if(desc.equals("desc_stop")){
            commentB30TitleTv.setText("呼吸暂停");
            url = "file:///android_asset/htmlsrc/breath_zh.html";
        }else if(desc.equals("desc_spo2")){
            commentB30TitleTv.setText("血氧");
            url = "file:///android_asset/htmlsrc/spo2_zh.html";
        }else if(desc.equals("desc_rate")){
            commentB30TitleTv.setText("呼吸率");
            url = "file:///android_asset/htmlsrc/respiratory_rate.html";
        }else if(desc.equals("desc_low_o2")){
            commentB30TitleTv.setText("低氧时间");
            url = "file:///android_asset/htmlsrc/low_o2_zh.html";
        }else if(desc.equals("desc_heart_load")){
            commentB30TitleTv.setText("心脏负荷");
            url = "file:///android_asset/htmlsrc/heart_load_zh.html";
        }else if(desc.equals("desc_sleep_activity")){
            commentB30TitleTv.setText("睡眠活动幅度");
            url = "file:///android_asset/htmlsrc/sleep_activity_zh.html";
        }
        else if(desc.equals("desc_spo2_comm")){     //呼吸暂停常识
            commentB30TitleTv.setText("呼吸暂停");
            url = "file:///android_asset/htmlsrc/breath_comm_knowle.html";
        }

        return url;

    }



    private void initViews() {

        commentB30BackImg = findViewById(R.id.commentackImg);
        commentB30TitleTv = findViewById(R.id.commentTitleTv);
        showSpo2DescWebView = findViewById(R.id.showSpo2DescWebView);

        commentB30BackImg.setVisibility(View.VISIBLE);
        WebSettings webSettings = showSpo2DescWebView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        commentB30BackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
