package com.brace.android.b31.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.bean.BraceCommB31Db;
import com.brace.android.b31.bean.BraceCommDbInstance;
import com.brace.android.b31.bean.BraceSleepBean;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.BraceUtils;
import com.brace.android.b31.view.widget.BraceCusSleepView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通睡眠
 * Created by Admin
 * Date 2019/11/8
 */
public class SleepDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SleepDetailActivity";


    ImageView commentackImg;
    TextView commentTitleTv;
    ImageView rateCurrDateLeft,rateCurrDateRight;
    TextView rateCurrdateTv;
    BraceCusSleepView detailCusSleepView;
    RatingBar detailSleepQuitRatingBar;
    SeekBar sleepSeekBar;
    TextView startSleepTimeTv;
    TextView endSleepTimeTv;
    TextView detailAllSleepTv;
    TextView detailAwakeNumTv;
    TextView detailStartSleepTv;
    TextView detailAwakeTimeTv;
    TextView detailDeepTv;
    TextView detailHightSleepTv;


    private Gson gson = new Gson();

    List<Integer> sleepLt;

    String currDay = BraceUtils.getCurrentDate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_detail_layout);


        findViews();

        initViews();

        findDbSleepData(currDay);

    }

    private void findViews() {
        commentackImg = findViewById(R.id.commentackImg);
        commentTitleTv = findViewById(R.id.commentTitleTv);
        rateCurrDateLeft = findViewById(R.id.rateCurrDateLeft);
        rateCurrdateTv = findViewById(R.id.rateCurrdateTv);
        detailCusSleepView = findViewById(R.id.detailCusSleepView);
        detailSleepQuitRatingBar = findViewById(R.id.detailSleepQuitRatingBar);
        sleepSeekBar = findViewById(R.id.sleepSeekBar);
        startSleepTimeTv = findViewById(R.id.startSleepTimeTv);
        endSleepTimeTv = findViewById(R.id.endSleepTimeTv);
        detailAllSleepTv = findViewById(R.id.detailAllSleepTv);
        detailAwakeNumTv = findViewById(R.id.detailAwakeNumTv);
        detailStartSleepTv = findViewById(R.id.detailStartSleepTv);
        detailAwakeTimeTv = findViewById(R.id.detailAwakeTimeTv);
        detailDeepTv = findViewById(R.id.detailDeepTv);
        detailHightSleepTv = findViewById(R.id.detailHightSleepTv);
        rateCurrDateRight = findViewById(R.id.rateCurrDateRight);



        commentackImg.setOnClickListener(this);
        rateCurrDateLeft.setOnClickListener(this);
        rateCurrDateRight.setOnClickListener(this);

    }

    private void initViews() {
        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText(getResources().getString(R.string.string_sleep_detail));
        sleepLt = new ArrayList<>();

    }


    private void findDbSleepData(String dayStr) {
        String bMac = BaseApplication.getBaseApplication().getBleMac();
        if (bMac == null)
            return;
        sleepLt.clear();
        rateCurrdateTv.setText(dayStr);
        try {
            final List<BraceCommB31Db> sleepList = BraceCommDbInstance.getBraceCommDbInstance()
                    .findSavedDataForType(bMac,dayStr, Constant.DB_TYPE_GENERAL_SLEEP);
            if (sleepList == null) {
                detailCusSleepView.setPrecisionSleep(false);
                detailCusSleepView.setSleepList(new ArrayList<Integer>());
                detailSleepQuitRatingBar.setMax(5);
                detailSleepQuitRatingBar.setNumStars(1);
                noDataMethod();
                return;
            }
            BraceSleepBean braceSleepBean = gson.fromJson(sleepList.get(0).getDataSourceStr(), BraceSleepBean.class);
            showCountSleep(braceSleepBean);
            Log.e(TAG, "------睡眠=" + braceSleepBean.toString());
            String sleepLinStr = braceSleepBean.getSleepLine();

            for (int i = 0; i < sleepLinStr.length(); i++) {
                int subStr = Integer.valueOf(sleepLinStr.substring(i, i + 1));
                sleepLt.add(subStr);
            }
            sleepLt.add(0, 2);
            sleepLt.add(0);
            sleepLt.add(2);
            detailCusSleepView.setPrecisionSleep(false);
            detailCusSleepView.setSleepList(sleepLt);



            detailCusSleepView.setSeekBarShow(false);
            sleepSeekBar.setMax(sleepLt.size());
            //入睡时间
            final int sleepDownTime = braceSleepBean.getSleepDown().getHMValue();

            sleepSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(progress == sleepLt.size())
                        return;
                    //当前滑动的时间
                    int currD = sleepDownTime + ((progress == 0 ? -1 : progress - 1) * 5);   //当前的分钟
                    //转换成时：分
                    int hour = (int) Math.floor(currD / 60);
                    if (hour >= 24)
                        hour = hour - 24;
                    int mine = currD % 60;
                    detailCusSleepView.setSleepDateTxt((hour<=9?"0"+hour:hour)+":"+(mine<=9?"0"+mine:mine));
                    detailCusSleepView.setSeekBarSchdue(progress);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    detailCusSleepView.setSeekBarShow(true,0);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showCountSleep(BraceSleepBean braceSleepBean) {
        if(braceSleepBean == null){
            noDataMethod();
            return;
        }

        detailSleepQuitRatingBar.setMax(5);
        detailSleepQuitRatingBar.setNumStars(braceSleepBean.getSleepQulity());



        //睡眠时长
        int countSleepTime = braceSleepBean.getAllSleepTime();
        int countSleepHour = countSleepTime / 60;
        int countSleepMine = countSleepTime % 60;
        detailAllSleepTv.setText((countSleepHour<=9?"0"+countSleepHour:countSleepHour)+"h"+(countSleepMine<=9?"0"+countSleepMine:countSleepMine)+"m");

        //苏醒次数
        detailAwakeNumTv.setText(braceSleepBean.getWakeCount()+"");
        //入睡时间
        detailStartSleepTv.setText(braceSleepBean.getSleepDown().getColck());
        startSleepTimeTv.setText(braceSleepBean.getSleepDown().getColck());


        //起床时间
        detailAwakeTimeTv.setText(braceSleepBean.getSleepUp().getColck());
        endSleepTimeTv.setText(braceSleepBean.getSleepUp().getColck());


        //深度睡眠
        int deepTime = braceSleepBean.getDeepSleepTime();
        int deepHour = deepTime / 60;
        int deepMine = deepTime % 60;

        detailDeepTv.setText(deepHour+"h"+(deepMine<=9?"0"+deepMine:deepMine)+"m");

        //浅度睡眠
        int lowSleepTime = braceSleepBean.getLowSleepTime();
        int lowHour = lowSleepTime / 60;
        int lowMine = lowSleepTime % 60;
        detailHightSleepTv.setText(lowHour+"h"+(lowMine<=9?"0"+lowMine:lowMine)+"m");




    }

    private void noDataMethod(){
        detailAllSleepTv.setText("");

        //苏醒次数
        detailAwakeNumTv.setText("");
        //入睡时间
        detailStartSleepTv.setText("");
        startSleepTimeTv.setText("");

        //起床时间
        detailAwakeTimeTv.setText("");
        endSleepTimeTv.setText("");

        detailDeepTv.setText("");
        detailHightSleepTv.setText("");
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
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.commentackImg) {
            finish();
        } else if (id == R.id.rateCurrDateLeft) {
            changeDayData(true);
        } else if (id == R.id.rateCurrDateRight) {
            changeDayData(false);
        }
    }
}
