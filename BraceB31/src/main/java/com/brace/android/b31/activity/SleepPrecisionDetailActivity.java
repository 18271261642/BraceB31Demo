package com.brace.android.b31.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.bean.BraceCommDbInstance;
import com.brace.android.b31.bean.BracePrecisionSleepBean;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.BraceUtils;
import com.brace.android.b31.view.widget.BraceCusSleepView;
import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin
 * Date 2019/11/8
 */
public class SleepPrecisionDetailActivity extends BaseActivity implements View.OnClickListener {

    ImageView commentackImg;
    TextView commentTitleTv;
    TextView rateCurrdateTv;
    BraceCusSleepView b31sDetailCusSleepView;
    SeekBar b31sSleepSeekBar;
    //入睡时间
    TextView b31sStartSleepTimeTv;
    //结束时间
    TextView b31sEndSleepTimeTv;
    //睡眠时间长
    TextView b31sDetailAllSleepTv;
    //苏醒
    TextView b31sDetailAwakeTimesTv;
    //失眠
    TextView detailInsomniaSleepTv;

    //快速眼动
    TextView detailAwakeHeightTv;

    //深睡时长
    TextView b31sDetailDeepTv;
    TextView b31sSleepLengthResultTv;

    //浅睡时长
    TextView b31sDetailHightSleepTv;
    //苏醒次数
    TextView b31sAwakeNumbersTv;
    //入睡效率
    TextView b31sSleepInEfficiencyScoreTv;
    //睡眠效率
    TextView b31sSleepEffectivenessTv;

    //睡眠质量表示星星
    AppCompatRatingBar b31sPercisionSleepQualityRatingBar;
    //苏醒百分比
    TextView b31sAwawkPercentTv;
    //苏醒状态
    TextView b31sSleepAwakeResultTv;
    //失眠百分比
    TextView b31sSleepInsomniaPercentTv;
    //失眠状态
    TextView b31sSleepInsomniaResultTv;
    //快速眼动百分比
    TextView b31sSleepEayPercentTv;
    //快速演的状态
    TextView b31sSleepEayResultTv;
    //深睡百分比
    TextView b31sSleepDeepPercentTv;
    //深睡状态
    TextView b31sSleepDeepResultTv;
    //浅睡百分比
    TextView b31sSleepLowPercentTv;
    //浅睡状态
    TextView b31sSleepLowResultTv;

    private ImageView rateCurrDateLeft,rateCurrDateRight;

    private Gson gson = new Gson();

    List<Integer> sleepLt ;

    private String currDay = BraceUtils.getCurrentDate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precision_sleep_detail_layout);

        findViews();

        initViews();

        findDbSleepData(currDay);
    }

    private void findViews() {
        commentackImg = findViewById(R.id.commentackImg);
        commentTitleTv = findViewById(R.id.commentTitleTv);
        rateCurrdateTv = findViewById(R.id.rateCurrdateTv);
        rateCurrDateLeft = findViewById(R.id.rateCurrDateLeft);
        rateCurrDateRight = findViewById(R.id.rateCurrDateRight);
        b31sDetailCusSleepView = findViewById(R.id.b31sDetailCusSleepView);
        b31sSleepSeekBar = findViewById(R.id.b31sSleepSeekBar);
        b31sStartSleepTimeTv = findViewById(R.id.b31sStartSleepTimeTv);
        b31sEndSleepTimeTv = findViewById(R.id.b31sEndSleepTimeTv);
        b31sDetailAllSleepTv = findViewById(R.id.b31sDetailAllSleepTv);
        b31sDetailAwakeTimesTv = findViewById(R.id.b31sDetailAwakeTimesTv);
        detailInsomniaSleepTv = findViewById(R.id.detailInsomniaSleepTv);
        detailAwakeHeightTv = findViewById(R.id.detailAwakeHeightTv);
        b31sDetailDeepTv = findViewById(R.id.b31sDetailDeepTv);
        b31sSleepLengthResultTv = findViewById(R.id.b31sSleepLengthResultTv);
        b31sDetailHightSleepTv = findViewById(R.id.b31sDetailHightSleepTv);
        b31sAwakeNumbersTv = findViewById(R.id.b31sAwakeNumbersTv);
        b31sSleepInEfficiencyScoreTv = findViewById(R.id.b31sSleepInEfficiencyScoreTv);
        b31sSleepEffectivenessTv = findViewById(R.id.b31sSleepEffectivenessTv);
        b31sPercisionSleepQualityRatingBar = findViewById(R.id.b31sPercisionSleepQualityRatingBar);
        b31sAwawkPercentTv = findViewById(R.id.b31sAwawkPercentTv);
        b31sSleepAwakeResultTv = findViewById(R.id.b31sSleepAwakeResultTv);
        b31sSleepInsomniaPercentTv = findViewById(R.id.b31sSleepInsomniaPercentTv);
        b31sSleepInsomniaResultTv = findViewById(R.id.b31sSleepInsomniaResultTv);
        b31sSleepEayPercentTv = findViewById(R.id.b31sSleepEayPercentTv);
        //快速演的状态
        b31sSleepEayResultTv = findViewById(R.id.b31sSleepEayResultTv);
        //深睡百分比
        b31sSleepDeepPercentTv = findViewById(R.id.b31sSleepDeepPercentTv);
        //深睡状态
        b31sSleepDeepResultTv = findViewById(R.id.b31sSleepDeepResultTv);
        //浅睡百分比
        b31sSleepLowPercentTv = findViewById(R.id.b31sSleepLowPercentTv);
        //浅睡状态
        b31sSleepLowResultTv = findViewById(R.id.b31sSleepLowResultTv);
    }


    private void findDbSleepData(String dayStr){
        rateCurrdateTv.setText(dayStr);
        String bMac = BaseApplication.getBaseApplication().getBleMac();
        if(bMac == null)
            return;
        try {
            String  strData = BraceCommDbInstance.getBraceCommDbInstance().findSingleOrigenData(bMac, dayStr, Constant.DB_TYPE_PRECISION_SLEEP);
            Log.e("TT","-------strData="+strData);
            BracePrecisionSleepBean cusVPSleepPrecisionData = new Gson().fromJson(strData, BracePrecisionSleepBean.class);
            showSleepData(cusVPSleepPrecisionData);
            showSleepView(cusVPSleepPrecisionData);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showSleepView(BracePrecisionSleepBean bracePrecisionSleepBean) {
        //Log.e("SLEEP","---------22="+bracePrecisionSleepBean.toString());
        sleepLt.clear();
        if (bracePrecisionSleepBean == null) {
            b31sDetailCusSleepView.setPrecisionSleep(true);
            b31sDetailCusSleepView.setSleepList(sleepLt);
            return;
        }
        // Log.e(TAG, "------睡眠=" + braceSleepBean.toString());
        String sleepLinStr = bracePrecisionSleepBean.getSleepLine();

        for (int i = 0; i < sleepLinStr.length(); i++) {
            int subStr = Integer.valueOf(sleepLinStr.substring(i, i + 1));
            sleepLt.add(subStr);
        }
        b31sDetailCusSleepView.setSeekBarShow(false);
        b31sDetailCusSleepView.setPrecisionSleep(true);
        b31sDetailCusSleepView.setSleepList(sleepLt);
    }

    //展示睡眠详情
    private void showSleepData(BracePrecisionSleepBean braceSleepBean) {
       // Log.e("SLEEP","---------11="+braceSleepBean.toString());
        //睡眠质量
        int sleepQuality = braceSleepBean == null ? 1 : braceSleepBean.getSleepQulity();
        b31sPercisionSleepQualityRatingBar.setMax(5);
        b31sPercisionSleepQualityRatingBar.setNumStars(sleepQuality);
        //入睡时间
        b31sStartSleepTimeTv.setText(braceSleepBean == null ? "0h0m" : braceSleepBean.getSleepDown().getColck());
        //起床时间
        b31sEndSleepTimeTv.setText(braceSleepBean == null ? "0h0m" : braceSleepBean.getSleepUp().getColck());
        //睡眠时长

        //睡眠时长
        if (braceSleepBean == null) {
            b31sDetailAllSleepTv.setText("0h0m");
            b31sSleepLengthResultTv.setText("--");
        } else {
            int allTime = braceSleepBean.getAllSleepTime();
            b31sDetailAllSleepTv.setText(allTime / 60 + "h" + allTime % 60 + "m");

            //7-9小时之间为正常
            b31sSleepLengthResultTv.setText(allTime <420 ? "偏低" : ( allTime >540 ? "偏高" : "正常"));


        }


        //失眠时长
        detailInsomniaSleepTv.setText(braceSleepBean == null ? "0h0m" : braceSleepBean.getInsomniaLength() / 60 + "h" + braceSleepBean.getInsomniaLength() % 60 + "m");
        //快速眼动时长
        detailAwakeHeightTv.setText(braceSleepBean == null ? "0h0m" : braceSleepBean.getOtherDuration() / 60 + "h" + braceSleepBean.getOtherDuration() % 60 + "m");
        //深度睡眠时长
        b31sDetailDeepTv.setText(braceSleepBean == null ? "0h0m" : braceSleepBean.getDeepSleepTime() / 60 + "h" + braceSleepBean.getDeepSleepTime() % 60 + "m");
        //浅度睡眠时长
        b31sDetailHightSleepTv.setText(braceSleepBean == null ? "0h0m" : braceSleepBean.getLowSleepTime() / 60 + "h" + braceSleepBean.getLowSleepTime() % 60 + "m");
        //苏醒次数
        b31sAwakeNumbersTv.setText(braceSleepBean == null ? "--" : braceSleepBean.getWakeCount() + "次");
        //入睡效率
        b31sSleepInEfficiencyScoreTv.setText(braceSleepBean == null ? "--" : braceSleepBean.getFirstDeepDuration() + "分钟");
        //睡眠效率得分
        b31sSleepEffectivenessTv.setText(braceSleepBean == null ? "--" : braceSleepBean.getSleepEfficiencyScore() + "分");
        if(braceSleepBean == null){
            return;
        }
        //总的睡眠时长
        float countSleepTime = braceSleepBean.getAllSleepTime();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");


        //失眠
        float insomniaDurationTime = braceSleepBean.getInsomniaDuration();
        if(insomniaDurationTime == 0){
            b31sSleepInsomniaPercentTv.setText("0%");
            b31sSleepInsomniaResultTv.setText("正常");
        }else{
            float insomniaPercent = insomniaDurationTime / countSleepTime;
            String insomniPercentV = StringUtils.substringAfter(decimalFormat.format(insomniaPercent), ".");
            b31sSleepInsomniaPercentTv.setText(insomniPercentV + "%");
            int tmpInsomniV = Integer.valueOf(insomniPercentV);
            b31sSleepInsomniaResultTv.setText(tmpInsomniV == 0 ? "正常" : "严重");
        }



        //深睡
        float deepTime = braceSleepBean.getDeepSleepTime();
        float deepPercent = deepTime / countSleepTime;
        //Log.e(TAG,"--------深睡="+deepTime+"--="+deepPercent);
        String tmpPer = decimalFormat.format(deepPercent);
        if(tmpPer.equals("0")){
            b31sSleepDeepPercentTv.setText("0%");
            b31sSleepDeepResultTv.setText("偏低");
        }else{
            String deepPercentV = StringUtils.substringAfter(tmpPer,".");
            b31sSleepDeepPercentTv.setText(deepPercentV+"%");
            int tmpDeepV = Integer.valueOf(deepPercentV);
            b31sSleepDeepResultTv.setText(tmpDeepV>=21?"正常":"偏低");
        }




        //浅睡
        float lowTime = braceSleepBean.getLowSleepTime();
        float lowPercent = lowTime / countSleepTime;
        String lowPercentV = StringUtils.substringAfter(decimalFormat.format(lowPercent),".");
        b31sSleepLowPercentTv.setText(lowPercentV+"%");
        int tmpLowV = Integer.valueOf(lowPercentV);
        b31sSleepLowResultTv.setText((0<=tmpLowV && tmpLowV<=59)?"正常":"偏低");



        //快速眼动
        float otherTime = braceSleepBean.getOtherDuration();
        float otherPercent = otherTime / countSleepTime;
        float formV = Float.valueOf(decimalFormat.format(otherPercent));
        formV = formV * 100;
        String otherPercentV = StringUtils.substringBefore(decimalFormat.format(formV),".");
        b31sSleepEayPercentTv.setText(otherPercentV +"%");
        int tmpOhterV = Integer.valueOf(otherPercentV);
        b31sSleepEayResultTv.setText((tmpOhterV >=10 && tmpOhterV <=30)?"正常":"偏低");


        //苏醒百分比，
        //苏醒时长=总时长-深睡-浅睡-快速眼动
        float awakeTime = countSleepTime - deepTime - lowTime - otherTime;
        if(awakeTime == 0){
            b31sAwawkPercentTv.setText("0%");
            b31sSleepAwakeResultTv.setText( "正常");
            b31sDetailAwakeTimesTv.setText( "0h0m");
        }else{
            float awakePercent = awakeTime / countSleepTime;
            String formStr = StringUtils.substringAfter(decimalFormat.format(awakePercent),".");
            b31sAwawkPercentTv.setText(Integer.valueOf(formStr.trim())+"%");
            int tmpAwakeV = Integer.valueOf(formStr);
            b31sSleepAwakeResultTv.setText(tmpAwakeV <= 1 ? "正常" : "严重");
            //苏醒时长
            b31sDetailAwakeTimesTv.setText(((int)awakeTime) / 60 + "h" + ((int)awakeTime) % 60 + "m");
        }



    }



    private void initViews() {
        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText(getResources().getString(R.string.string_precision_sleep));
        commentackImg.setOnClickListener(this);
        rateCurrDateLeft.setOnClickListener(this);
        rateCurrDateRight.setOnClickListener(this);

        sleepLt = new ArrayList<>();
    }



    /**
     * 根据日期切换数据
     */
    private void changeDayData(boolean left) {
        String date = BraceUtils.obtainAroundDate(currDay, left);
        if (date.equals(currDay) || date.isEmpty()) {
            return;
        }
        currDay = date;
        findDbSleepData(currDay);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.commentackImg) {
            finish();
        } else if (id == R.id.rateCurrDateLeft) {
            changeDayData(true);
        } else if (id == R.id.rateCurrDateRight) {
            changeDayData(false);
        }
    }
}
