package com.brace.android.b31.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.ble.BleConnStatus;
import com.brace.android.b31.utils.BraceUtils;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IHeartDataListener;
import com.veepoo.protocol.model.datas.HeartData;
import com.veepoo.protocol.model.enums.EHeartStatus;

/**
 * 手动测量心率
 * Created by Admin
 * Date 2019/12/23
 */
public class ManmualHeartActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backImg;
    private TextView titleTv;
    private ImageView startImg;

    //测量结果
    private TextView b31MeaureHeartValueTv;
    //旋转图片
    private ImageView b31cirImg;
    //缩放布局动画
    private LinearLayout b31ScaleLin;

    //是否正在测量
    private boolean isManmual = false;

    //缩放动画
    Animation animationRoate;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HeartData heartData = (HeartData) msg.obj;
            if(heartData == null)
                return;
            if(heartData.getHeartStatus() == EHeartStatus.STATE_HEART_BUSY){
                b30StopDetchHeart();
                return;
            }
            b31MeaureHeartValueTv.setText(heartData.getData()+"");
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manmual_heart_layout);

        initViews();


    }

    private void initViews() {
        b31cirImg = findViewById(R.id.b31cirImg);
        backImg = findViewById(R.id.commentackImg);
        titleTv = findViewById(R.id.commentTitleTv);
        startImg = findViewById(R.id.b31MeaureHeartStartBtn);
        b31MeaureHeartValueTv = findViewById(R.id.b31MeaureHeartValueTv);
        b31ScaleLin = findViewById(R.id.b31ScaleLin);
        backImg.setVisibility(View.VISIBLE);
        backImg.setOnClickListener(this);
        startImg.setOnClickListener(this);
        titleTv.setText("手动测量心率");
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.commentackImg) {
            finish();
        } else if (id == R.id.b31MeaureHeartStartBtn) {
            startOrStopManmual();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(BleConnStatus.CONNDEVICENAME != null)
            b30StopDetchHeart();
    }

    private void startOrStopManmual() {
        if(!isManmual){ //开始测量
            isManmual = true;
            b31MeaureHeartValueTv.setText("");
            startImg.setImageResource(R.drawable.detect_heart_pause);
            startAllAnimat(b31ScaleLin, b31cirImg);
            BaseApplication.getVPOperateManager().startDetectHeart(iBleWriteResponse, new IHeartDataListener() {
                @Override
                public void onDataChange(HeartData heartData) {
                    if(heartData == null)
                        return;
                    Message message = handler.obtainMessage();
                    message.obj = heartData;
                    message.what = 0x01;
                    handler.sendMessage(message);
                }
            });
        }else{
            startImg.setImageResource(R.drawable.detect_heart_start);
            isManmual = false;
            stopAllAnimat(b31ScaleLin, b31cirImg);
        }
    }

    private void startAllAnimat(View view1, View view2) {
        startFlick(view1);  //开启缩放动画
        startAnimat(view2); //开启旋转动画

    }


    private void b30StopDetchHeart(){
        startImg.setImageResource(R.drawable.detect_heart_start);
        isManmual = false;
        stopAllAnimat(b31ScaleLin, b31cirImg);
        b31MeaureHeartValueTv.setText(BraceUtils.setBusyDesicStr());
        BaseApplication.getVPOperateManager().stopDetectHeart(iBleWriteResponse);
    }



    //缩放动画
    public static void startFlick(View view) {
        if (null == view) {
            return;
        }
        ScaleAnimation animation_suofang = new ScaleAnimation(1.4f, 1.0f,
                1.4f, 1.0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation_suofang.setDuration(3000);                     //执行时间
        animation_suofang.setRepeatCount(-1);                   //重复执行动画
        animation_suofang.setRepeatMode(Animation.REVERSE);     //重复 缩小和放大效果
        view.startAnimation(animation_suofang);

    }

    //旋转动画
    private void startAnimat(View view) {
        if (view == null) {
            return;
        }
        animationRoate = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        animationRoate.setInterpolator(lin);

        animationRoate.setDuration(3 * 1000);
        animationRoate.setRepeatCount(-1);//动画的反复次数
        animationRoate.setFillAfter(true);//设置为true，动画转化结束后被应用
        view.startAnimation(animationRoate);//開始动画

    }


    //停止所有动画
    private void stopAllAnimat(View view1, View view2) {
        if (view1 != null) {
            view1.clearAnimation();
        }
        if (view2 != null) {
            view2.clearAnimation();
        }
    }


    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };
}
