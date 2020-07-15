package com.brace.android.b31.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.view.widget.CustomCircleProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IFatigueDataListener;
import com.veepoo.protocol.model.datas.FatigueData;
import com.veepoo.protocol.model.enums.EDeviceStatus;

import java.text.DecimalFormat;

/**
 * 疲劳度
 * Created by Admin
 * Date 2020/6/9
 */
public class ManmualFitActivity extends BaseActivity implements View.OnClickListener {


    private ImageView backImg;
    private TextView titleTv;

    CustomCircleProgressBar b31MeaureFaitProgressView;
    ImageView b31FaitImg;

    ImageView fatigueStartImg;
    //未开始测量时显示的
    LinearLayout faitNoManLin;
    //测试时的布局
    LinearLayout faitManLin;
    //显示测试的进度
    TextView fatiCurrTv;
    ImageView showFaitResultImg;
    TextView showFaitResultTv;
    ListView manFatigueListView;
    //疲劳度建议
    TextView showFaitSuggestTv;

    //开始或者停止测量的标识
    private boolean isStart = false;

    DecimalFormat decimalFormat = new DecimalFormat("#");    //不保留小数

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.removeMessages(1001);
            FatigueData fatigueData = (FatigueData) msg.obj;
            if (fatigueData == null)
                return;
            b31MeaureFaitProgressView.setProgress(fatigueData.getProgress(),0);
            fatiCurrTv.setText(fatigueData.getProgress() + "%");
            if(fatigueData.getDeviceState() != EDeviceStatus.FREE){
                showFaitResultTv.setText("设备端正在使用测量功能");
                isStart = false;
                stopManFait();
                return;
            }
            showFaitResultData(fatigueData);


        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manmual_fit_layout);

        initViews();

        initData();

    }

    private void initData() {
        b31MeaureFaitProgressView.setMaxProgress(100);
        b31MeaureFaitProgressView.setTmpTxt(null);
    }

    private void initViews() {
        backImg = findViewById(R.id.commentackImg);
        titleTv = findViewById(R.id.commentTitleTv);
        b31MeaureFaitProgressView = findViewById(R.id.meaureFaitProgressView);
        b31FaitImg = findViewById(R.id.faitImg);
        fatigueStartImg = findViewById(R.id.fatigueStartImg);
        faitManLin = findViewById(R.id.faitManLin);
        faitNoManLin = findViewById(R.id.faitNoManLin);
        fatiCurrTv = findViewById(R.id.fatiCurrTv);
        showFaitResultImg = findViewById(R.id.showFaitResultImg);
        showFaitResultTv = findViewById(R.id.showFaitResultTv);
        manFatigueListView = findViewById(R.id.manFatigueListView);
        showFaitSuggestTv = findViewById(R.id.showFaitSuggestTv);

        titleTv.setText("疲劳度测量");

        fatigueStartImg.setOnClickListener(this);
        backImg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.commentackImg){
            finish();
        }else if(id == R.id.fatigueStartImg){
            startOrEndManFait();
        }

    }


    //开始或结束测量
    private void startOrEndManFait() {
        if (!isStart) {   //开始测量
            isStart = true;
            fatigueStartImg.setImageResource(R.drawable.detect_ftg_stop);
            faitNoManLin.setVisibility(View.GONE);
            faitManLin.setVisibility(View.VISIBLE);
            startManFait();

        } else {  //停止测量
            isStart = false;
            stopManFait();
            //在测试中点击停止测量时停止测量
            BaseApplication.getVPOperateManager().stopDetectFatigue(iBleWriteResponse, new IFatigueDataListener() {
                @Override
                public void onFatigueDataListener(FatigueData fatigueData) {

                }
            });

            showFaitResultTv.setText(getResources().getString(R.string.fatigue_no_test_desc));
        }

    }



    //显示测量的结果
    private void showFaitResultData(FatigueData fatigueData) {

        if (fatigueData.getProgress() == 100) {   //测量完成了
            isStart = false;
            int faitValue = fatigueData.getValue();
            //0=测试无效，1=不疲劳，2=轻度疲劳，3=一般疲劳，4=重度疲劳
           // String currTime = WatchUtils.getLongToDate("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());

            stopManFait();
            String showFaitResultStr = "";
            String showSuggestStr = "";
            switch (faitValue) {
                case 0: //无效
                    showFaitResultStr = getResources().getString(R.string.test_invalid);
                    showFaitResultImg.setBackground(getResources().getDrawable(R.drawable.ftg_bg));

                    break;
                case 1: //状态良好
                    showFaitResultStr = getResources().getString(R.string.fatigue_normal);
                    showFaitResultImg.setBackground(getResources().getDrawable(R.mipmap.ftg_level_1));
                    showSuggestStr = getResources().getString(R.string.fatigue_noral_suggest);
                    break;
                case 2: //轻度疲劳
                    showFaitResultStr = getResources().getString(R.string.fatigue_mild);
                    showFaitResultImg.setBackground(getResources().getDrawable(R.mipmap.ftg_level_2));
                    showSuggestStr = getResources().getString(R.string.fatigue_mild_suggest);
                    break;
                case 3: //中度疲劳
                    showFaitResultStr = getResources().getString(R.string.fatigue_moderate);
                    showFaitResultImg.setBackground(getResources().getDrawable(R.mipmap.ftg_level_3));
                    showSuggestStr = getResources().getString(R.string.fatigue_moderate_suggest);
                    break;
                case 4: //中度疲劳
                    showFaitResultStr = getResources().getString(R.string.fatigue_severe);
                    showFaitResultImg.setBackground(getResources().getDrawable(R.mipmap.ftg_level_4));
                    showSuggestStr = getResources().getString(R.string.fatigue_sebere_suggest);
                    break;
            }

            showFaitResultTv.setText(showFaitResultStr);
            showFaitSuggestTv.setText(showSuggestStr);

            if (faitValue != 0) {
                //是否保存数据库中
                //alertIsSave(faitValue, currTime);
            }

        }
    }


    //开始测试了
    private void startManFait() {
        b31MeaureFaitProgressView.setTmpTxt("");
        //开始动画效果
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(ManmualFitActivity.this).asGif().load(R.drawable.ftggif).apply(options).into(b31FaitImg);
        BaseApplication.getVPOperateManager().startDetectFatigue(iBleWriteResponse, new IFatigueDataListener() {
            @Override
            public void onFatigueDataListener(FatigueData fatigueData) {
               // Log.e(TAG, "---------fatigueData=" + fatigueData.toString());
                Message message = handler.obtainMessage();
                message.obj = fatigueData;
                message.what = 1001;
                handler.sendMessage(message);
            }
        });

    }


    //停止测量
    private void stopManFait() {
        fatigueStartImg.setImageResource(R.drawable.detect_ftg_start);
        faitManLin.setVisibility(View.GONE);
        faitNoManLin.setVisibility(View.VISIBLE);
        showFaitResultImg.setBackground(getResources().getDrawable(R.drawable.ftg_bg));
        //停止播放gif动画
        Glide.with(ManmualFitActivity.this).clear(b31FaitImg);
    }


    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };

}
