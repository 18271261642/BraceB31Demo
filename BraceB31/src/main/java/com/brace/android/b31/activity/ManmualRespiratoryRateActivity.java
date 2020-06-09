package com.brace.android.b31.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.view.widget.CustomCircleProgressBar;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IBreathDataListener;
import com.veepoo.protocol.model.datas.BreathData;

/**
 * Created by Admin
 * Date 2020/6/9
 */
public class ManmualRespiratoryRateActivity extends BaseActivity implements View.OnClickListener {


    private ImageView backImg;
    private TextView titleTv;
    private ImageView meaureRateStartImg;
    private CustomCircleProgressBar meaureRateProgressView;
    private TextView showRateStateTv;


    //开始或者停止测量的标识
    private boolean isStart = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1001){
                BreathData breathData = (BreathData) msg.obj;
                if(breathData == null)
                    return;
                meaureRateProgressView.setProgress(breathData.getProgressValue());
                if(breathData.getDeviceState() != 0){
                    showRateStateTv.setText("设备端正在使用测量功能");
                    meaureRateProgressView.stopAnim();
                    stopMan();
                    return;
                }

                if(breathData.getProgressValue() == 100){
                    stopMan();
                    meaureRateProgressView.setTmpTxt(breathData.getValue()+" "+getResources().getString(R.string.cishu)+"/"+getResources().getString(R.string.signle_minute));
                }
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respiratory_rate_layout);

        initViews();

    }

    private void initViews() {
        backImg = findViewById(R.id.commentackImg);
        titleTv = findViewById(R.id.commentTitleTv);
        meaureRateStartImg = findViewById(R.id.meaureRateStartImg);
        meaureRateProgressView = findViewById(R.id.meaureRateProgressView);
        showRateStateTv = findViewById(R.id.showRateStateTv);
        backImg.setOnClickListener(this);
        meaureRateStartImg.setOnClickListener(this);

        titleTv.setText("呼吸率");

        meaureRateProgressView.setMaxProgress(100);
        meaureRateProgressView.setTmpTxt(null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.commentackImg){
            finish();
        }else if(id == R.id.meaureRateStartImg){
            startOrStopManRate();
        }
    }


    private void startOrStopManRate() {
        if(!isStart){
            showRateStateTv.setText("");
            meaureRateProgressView.setTmpTxt(null);
            isStart = true;
            meaureRateStartImg.setImageResource(R.drawable.detect_breath_stop);
            BaseApplication.getVPOperateManager().startDetectBreath(iBleWriteResponse, new IBreathDataListener() {
                @Override
                public void onDataChange(BreathData breathData) {
                    //Log.e(TAG,"-----------breathData="+breathData.toString());
                    Message message = handler.obtainMessage();
                    message.what = 1001;
                    message.obj = breathData;
                    handler.sendMessage(message);
                }
            });
            return;
        }

        stopMan();
    }

    //停止测量
    private void stopMan() {
        isStart = false;
        meaureRateStartImg.setImageResource(R.drawable.detect_breath_start);
        meaureRateProgressView.stopAnim();
        BaseApplication.getVPOperateManager().stopDetectBreath(iBleWriteResponse, new IBreathDataListener() {
            @Override
            public void onDataChange(BreathData breathData) {

            }
        });
    }

    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMan();
    }
}
