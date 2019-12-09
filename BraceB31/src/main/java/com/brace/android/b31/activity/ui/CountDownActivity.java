package com.brace.android.b31.activity.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.ble.BleConnStatus;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.ICountDownListener;
import com.veepoo.protocol.model.datas.CountDownData;
import com.veepoo.protocol.model.enums.ECountDownStatus;
import com.veepoo.protocol.model.settings.CountDownSetting;

import java.util.Calendar;
import java.util.Date;


/**
 * 倒计时页面
 * Created by Admin
 * Date 2019/11/23
 */
public class CountDownActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private static final String TAG = "CountDownActivity";

    ImageView commentackImg;
    TextView commentTitleTv;
    TextView showCounDownTv;
    ToggleButton showScreentViewTogg;
    TextView oftenDateTv;
    Button startCounDownBtn;
    RelativeLayout oftenDateRel;

    private TimePickerView timePickerView;
    private Calendar calendar;


    private int dataSecond = 0; //设置的秒

    boolean isShowUI = false;
    //是否正在倒计时
    boolean isCountDown = false;

    //常用时间
    private int oftenSecond = 0;


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001) {
                CountDownData countDownData = (CountDownData) msg.obj;
                Log.e(TAG,"------hand="+countDownData.toString());
                if (countDownData.getStatus() == ECountDownStatus.COUNT_ING) {    //正在倒计时
                    startCounDownBtn.setVisibility(View.GONE);
                    int countSecond = countDownData.getCountDownSecondApp();
                    showCounDownTv.setText(secToTime(countSecond) + "");

                }

                //倒计时结束
                if (countDownData.getStatus() == ECountDownStatus.COUNT_END && countDownData.getCountDownSecondApp() == 0) {
                    startCounDownBtn.setVisibility(View.VISIBLE);
                }
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_dowm_layout);


        findViews();

        initViews();

        initData();

        readCoundDown();

    }

    private void findViews() {
        commentackImg = findViewById(R.id.commentackImg);
        commentTitleTv = findViewById(R.id.commentTitleTv);
        showCounDownTv = findViewById(R.id.showCounDownTv);
        showScreentViewTogg = findViewById(R.id.showScreentViewTogg);
        oftenDateTv = findViewById(R.id.oftenDateTv);
        startCounDownBtn = findViewById(R.id.startCounDownBtn);
        oftenDateRel = findViewById(R.id.oftenDateRel);

        startCounDownBtn.setOnClickListener(this);
        oftenDateRel.setOnClickListener(this);
        commentackImg.setOnClickListener(this);
    }

    //读取设备的倒计时信息
    private void readCoundDown() {
        if (BleConnStatus.CONNDEVICENAME != null) {
            BaseApplication.getVPOperateManager().readCountDown(iBleWriteResponse, new ICountDownListener() {
                @Override
                public void OnCountDownDataChange(CountDownData countDownData) {
                    Log.e(TAG, "----countDownData=" + countDownData.toString());
                    isCountDown = countDownData.getStatus() == ECountDownStatus.COUNT_ING;
                    Log.e(TAG, "-----------isCountDown=" + isCountDown);
                    if (isCountDown) {    //正在进行倒计时
                        //startCounDownBtn.setVisibility(View.GONE);
                        Message message = handler.obtainMessage();
                        message.what = 1001;
                        message.obj = countDownData;
                        handler.sendMessage(message);
                    }
                    isShowUI = countDownData.isOpenWatchUI();
                    showScreentViewTogg.setChecked(isShowUI);
                    oftenDateRel.setVisibility(isShowUI ? View.VISIBLE : View.GONE);
                    //常用时间
                    oftenSecond = countDownData.getCountDownSecondWatch();
                    oftenDateTv.setText(secToTime(oftenSecond));
                }
            });
        }
    }


    private void initData() {
        calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, 0, 0);
    }

    private void initViews() {
        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText(getResources().getString(R.string.count_down));
        showScreentViewTogg.setOnCheckedChangeListener(this);
        startCounDownBtn.setText(getResources().getString(R.string.star) + getResources().getString(R.string.count_down));


    }





    //选择时间
    private void chooseCoundDownDate(final int code) {
        timePickerView = new TimePickerBuilder(CountDownActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {

                dataSecond = date.getHours() * 3600 + date.getMinutes() * 60 + date.getSeconds();
                if (code == 0) {  //开始倒计时选择完时间
                    showCounDownTv.setText(secToTime(dataSecond) + "");
                    startCounDown(dataSecond, false);
                } else {
                    oftenDateTv.setText(secToTime(dataSecond) + "");
                    startCounDown(dataSecond, true);
                }

            }
        }).setDate(calendar)
                .setLabel("", "", "", "h", "m", "s")
                .setType(new boolean[]{false, false, false, true, true, true})
                .isCyclic(true).build();
        timePickerView.show();
    }


    //开始倒计时

    /**
     * @param startSecond   设置倒计时显示的时长，秒
     *                      isShowUI 是否在手环界面显示
     * @param isTestByWatch
     */
    private void startCounDown(int startSecond, boolean isTestByWatch) {
        if (BleConnStatus.CONNDEVICENAME != null) {
            CountDownSetting countDownSetting;
            if(BleConnStatus.CONNDEVICENAME.equals("B31")){
                countDownSetting = new CountDownSetting(1,startSecond, isShowUI, isTestByWatch);
            }else{
                countDownSetting = new CountDownSetting(startSecond, isShowUI, isTestByWatch);
            }
            Log.e(TAG,"-------countDownSetting="+countDownSetting.toString());
            BaseApplication.getVPOperateManager().settingCountDown(iBleWriteResponse, countDownSetting,
                    new ICountDownListener() {
                        @Override
                        public void OnCountDownDataChange(CountDownData countDownData) {
                            dataSecond = 0;
                            Log.e(TAG, "---开始倒计时=" + countDownData.toString());
                            Message message = handler.obtainMessage();
                            message.obj = countDownData;
                            message.what = 1001;
                            handler.sendMessage(message);

                        }
                    });
        }
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!buttonView.isPressed())
            return;
        if (buttonView.getId() == R.id.showScreentViewTogg) {
            isShowUI = isChecked;
            if (!isChecked) {
                oftenDateRel.setVisibility(View.GONE);
            } else {
                oftenDateRel.setVisibility(View.VISIBLE);
            }

            startCounDown(oftenSecond, true);
        }
    }

    //根据秒转时分秒
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00" + ":" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else
            retStr = "" + i;
        return retStr;
    }


    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.commentackImg) {
            finish();
        } else if (id == R.id.startCounDownBtn) {
            if (!isCountDown) {
                chooseCoundDownDate(0x00);
            }
        } else if (id == R.id.oftenDateRel) {
            chooseCoundDownDate(0x01);
        }
    }
}
