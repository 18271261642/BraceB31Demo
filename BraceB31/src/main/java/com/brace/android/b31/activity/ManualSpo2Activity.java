package com.brace.android.b31.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.MainActivity;
import com.brace.android.b31.R;
import com.brace.android.b31.view.widget.CustomCircleProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.ISpo2hDataListener;
import com.veepoo.protocol.model.datas.Spo2hData;
import com.veepoo.protocol.model.enums.EDeviceStatus;
import com.veepoo.protocol.model.enums.ESPO2HStatus;

/**
 * 手动测量血氧
 * Created by Admin
 * Date 2020/6/9
 */
public class ManualSpo2Activity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ManualSpo2Activity";

    private ImageView backImg;
    private TextView titleTv;
    private TextView showSpo2ResultTv;
    private ImageView spo2ShowGifImg;
    private CustomCircleProgressBar meaureSpo2ProgressView;
    private ImageView meaureStartImg;

    //开始或者停止测量的标识
    private boolean isStart = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001) {
                Spo2hData spo2hData = (Spo2hData) msg.obj;
                if (spo2hData == null)
                    return;
                if (spo2hData.getDeviceState() != EDeviceStatus.FREE || (spo2hData.getSpState() == ESPO2HStatus.CLOSE && !spo2hData.isChecking())) {   //设置端正在使用测量功能，
                    showSpo2ResultTv.setText("设备端正在使用测量功能");
                    isStart = true;
                    startOrStopManSpo2();
                    return;
                }

                if (spo2hData.getSpState() == ESPO2HStatus.OPEN && spo2hData.isChecking()) {
                    meaureSpo2ProgressView.setProgress(spo2hData.getCheckingProgress());
                }


                if (spo2hData.getCheckingProgress() == 0x00 && !spo2hData.isChecking()) {
                    int spo2Value = spo2hData.getValue();
                    meaureSpo2ProgressView.setTmpTxt(spo2hData.getValue() + "%");
                    showSpo2ResultTv.setText(spo2Value == 0 ? "" : verSpo2Status(spo2hData.getValue()));
                    meaureSpo2ProgressView.setOxyDexcStr(spo2Value == 0 ? getResources().getString(R.string.calibrating) : getResources().getString(R.string.string_spo2_concent));

                    RequestOptions options = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                    Glide.with(ManualSpo2Activity.this).asGif().load(R.drawable.spgif).apply(options).into(spo2ShowGifImg);
                }

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_spo2_layout);

        initViews();


    }

    private void initViews() {
        backImg = findViewById(R.id.commentackImg);
        titleTv = findViewById(R.id.commentTitleTv);
        showSpo2ResultTv = findViewById(R.id.showSpo2ResultTv);
        spo2ShowGifImg = findViewById(R.id.spo2ShowGifImg);
        meaureSpo2ProgressView = findViewById(R.id.meaureSpo2ProgressView);
        meaureStartImg = findViewById(R.id.meaureStartImg);
        backImg.setOnClickListener(this);
        meaureStartImg.setOnClickListener(this);

        titleTv.setText("血氧测量");
        meaureSpo2ProgressView.setInsideColor(Color.parseColor("#72CBEE"));
        meaureSpo2ProgressView.setOutsideColor(Color.WHITE);

        spo2ShowGifImg.setImageResource(R.drawable.spgif);
        meaureSpo2ProgressView.setMaxProgress(100);
        meaureSpo2ProgressView.setOxyDexcStr(getResources().getString(R.string.spo2_calibration_pro));
        meaureSpo2ProgressView.setOxyCh(true);
        meaureSpo2ProgressView.setTmpTxt(null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.commentackImg) {
            finish();
        } else if (id == R.id.meaureStartImg) {
            startOrStopManSpo2();
        }
    }


    private void startOrStopManSpo2() {
        if (!isStart) {   //开始测量
            showSpo2ResultTv.setText("环境校验准备中,请保持正确姿势");
            isStart = true;
            //当前时间
            meaureSpo2ProgressView.setTmpTxt(null);
            meaureStartImg.setImageResource(R.drawable.detect_sp_stop);
            meaureSpo2ProgressView.stopAnim();
            BaseApplication.getVPOperateManager().startDetectSPO2H(iBleWriteResponse, new ISpo2hDataListener() {
                @Override
                public void onSpO2HADataChange(Spo2hData spo2hData) {
                    Log.e(TAG, "----------spo2hData=" + spo2hData.toString());
                    Message message = handler.obtainMessage();
                    message.what = 1001;
                    message.obj = spo2hData;
                    handler.sendMessage(message);
                }
            });

        } else {  //停止测量
            isStart = false;
            Glide.with(ManualSpo2Activity.this).clear(spo2ShowGifImg);
            spo2ShowGifImg.setImageResource(R.drawable.spgif);
            meaureStartImg.setImageResource(R.drawable.detect_sp_start);
            meaureSpo2ProgressView.setOxyDexcStr(getResources().getString(R.string.spo2_calibration_pro));
            meaureSpo2ProgressView.stopAnim(0);
            BaseApplication.getVPOperateManager().stopDetectSPO2H(iBleWriteResponse, new ISpo2hDataListener() {
                @Override
                public void onSpO2HADataChange(Spo2hData spo2hData) {

                }
            });

        }
    }

    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };

    //判断血氧浓度是否正常
    //范围是[0-99], [0-79]=血氧远低于正常值，警告用户要重视,[80-89]=血氧浓度低，提醒用户重视，
    // [90-95]=血氧浓度偏低,[95-99]=血氧正常
    private String verSpo2Status(int spo2V) {

        if (spo2V >= 95 && spo2V <= 99) {
            return getResources().getString(R.string.string_normal);
        }
        if (spo2V >= 90 && spo2V <= 95) {
            return getResources().getString(R.string.string_spo2_low);
        }
        if (spo2V >= 80 && spo2V <= 89) {
            return getResources().getString(R.string.string_spo2_lowest);
        }
        if (spo2V <= 79) {
            if (spo2V == 1) {
                return getResources().getString(R.string.try_again);
            }
            return getResources().getString(R.string.string_spo2_no_normal);

        }
        return null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isStart)
            BaseApplication.getVPOperateManager().stopDetectSPO2H(iBleWriteResponse, new ISpo2hDataListener() {
                @Override
                public void onSpO2HADataChange(Spo2hData spo2hData) {

                }
            });
    }
}
