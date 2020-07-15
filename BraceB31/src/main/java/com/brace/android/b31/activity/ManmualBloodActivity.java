package com.brace.android.b31.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.ble.BleConnStatus;
import com.brace.android.b31.utils.BraceUtils;
import com.brace.android.b31.view.widget.CustomCircleProgressBar;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IBPDetectDataListener;
import com.veepoo.protocol.model.datas.BpData;
import com.veepoo.protocol.model.enums.EBPDetectModel;
import com.veepoo.protocol.model.enums.EBPDetectStatus;

/**
 * 手动测量血压
 * Created by Admin
 * Date 2019/12/23
 */
public class ManmualBloodActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ManmualBloodActivity";

    //title
    private TextView titleTv;
    private ImageView backImg;
    //通用模式和私人模式切换
    private RelativeLayout publicRel, privateRel;
    private View pubLin, priLin;

    //无测量时显示的图片
    private ImageView b31MeaurePlaceHolderImg;

    //进度条
    private CustomCircleProgressBar b31MeaureBloadProgressView;
    //结果显示
    private TextView showBpStateTv;
    //开始或暂停按钮
    private ImageView b31MeaureStartImg;

    //是否是私人模式
    private boolean isPrivate = false;
    //是否是正在测量状态
    private boolean isManual = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BpData meaureBpData = (BpData) msg.obj;
            if(meaureBpData == null){
                stopMeaureBoload();
                return;
            }

            b31MeaureBloadProgressView.setProgress(meaureBpData.getProgress(),0);
            if(meaureBpData.getStatus() == EBPDetectStatus.STATE_BP_BUSY){
                showBpStateTv.setText(BraceUtils.setBusyDesicStr());
                stopMeaureBoload();
                return;
            }

            if (meaureBpData.getProgress() == 100) {  //测量结束
                stopMeaureBoload();
                if (b31MeaureBloadProgressView != null) {
                    if (meaureBpData.getHighPressure() < 60 || meaureBpData.getLowPressure() < 30) {
                        b31MeaureBloadProgressView.setTmpTxt("0/0");
                    } else {
                        b31MeaureBloadProgressView.setTmpTxt(meaureBpData.getHighPressure() + "/" + meaureBpData.getLowPressure());//.setProgressText(meaureBpData.getHighPressure() + "/" + meaureBpData.getLowPressure());
                        showBpStateTv.setText("正常");
                    }
                }
            }

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manmual_blood_layout);


        initViews();

    }

    private void initViews() {
        titleTv = findViewById(R.id.commentTitleTv);
        backImg = findViewById(R.id.commentackImg);
        backImg.setVisibility(View.VISIBLE);
        publicRel = findViewById(R.id.manual_blood_public);
        privateRel = findViewById(R.id.manual_blood_private);
        pubLin = findViewById(R.id.manual_blood_public_line);
        priLin = findViewById(R.id.manual_blood_private_line);

        b31MeaurePlaceHolderImg = findViewById(R.id.b31MeaurePlaceHolderImg);
        b31MeaureBloadProgressView = findViewById(R.id.b31MeaureBloadProgressView);
        showBpStateTv = findViewById(R.id.showBpStateTv);
        b31MeaureStartImg = findViewById(R.id.b31MeaureStartImg);
        titleTv.setText("血压测量");
        publicRel.setOnClickListener(this);
        privateRel.setOnClickListener(this);
        backImg.setOnClickListener(this);
        b31MeaureStartImg.setOnClickListener(this);
        b31MeaureBloadProgressView.setMaxProgress(100);
        b31MeaureBloadProgressView.setTmpTxt(null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.commentackImg) {   //返回
            finish();
        } else if (id == R.id.manual_blood_public) {   //通用模式
            isPrivate = false;
            priLin.setVisibility(View.GONE);
            pubLin.setVisibility(View.VISIBLE);

        } else if (id == R.id.manual_blood_private) {  //私人模式
            isPrivate = true;
            priLin.setVisibility(View.VISIBLE);
            pubLin.setVisibility(View.GONE);
        } else if (id == R.id.b31MeaureStartImg) { //开始或停止测量
            startOrEndBlood();
        }
    }


    private void startOrEndBlood() {
        if (BleConnStatus.CONNDEVICENAME == null) {
            showBpStateTv.setText(getResources().getString(R.string.string_device_no_conn));
            return;
        }
        b31MeaurePlaceHolderImg.setVisibility(View.GONE);
        b31MeaureBloadProgressView.setVisibility(View.VISIBLE);
        if (!isManual) {
            isManual = true;
            showBpStateTv.setText("");
            b31MeaureStartImg.setImageResource(R.drawable.detect_bp_pause);
            b31MeaureBloadProgressView.setTmpTxt(null);
            if (BleConnStatus.CONNDEVICENAME != null) {
                BaseApplication.getVPOperateManager().startDetectBP(iBleWriteResponse, new IBPDetectDataListener() {
                    @Override
                    public void onDataChange(BpData bpData) {
                        Message message = handler.obtainMessage();
                        message.what = 1001;
                        message.obj = bpData;
                        handler.sendMessage(message);
                    }
                }, isPrivate ? EBPDetectModel.DETECT_MODEL_PRIVATE : EBPDetectModel
                        .DETECT_MODEL_PUBLIC);
            }
        } else {
            stopMeaureBoload();
        }
    }

    //停止测量
    private void stopMeaureBoload() {
        isManual = false;
        b31MeaureStartImg.setImageResource(R.drawable.detect_bp_start);
        b31MeaureBloadProgressView.stopAnim();
        BaseApplication.getVPOperateManager().stopDetectBP(iBleWriteResponse, isPrivate ? EBPDetectModel.DETECT_MODEL_PRIVATE : EBPDetectModel
                .DETECT_MODEL_PUBLIC);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(BleConnStatus.CONNDEVICENAME != null)
            stopMeaureBoload();
    }

    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };
}
