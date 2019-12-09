package com.brace.android.b31.activity.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.utils.BraceUtils;
import com.brace.android.b31.view.whell.widgets.ProvincePick;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.INightTurnWristeDataListener;
import com.veepoo.protocol.model.datas.NightTurnWristeData;
import com.veepoo.protocol.model.datas.TimeData;
import com.veepoo.protocol.model.settings.NightTurnWristSetting;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 转腕亮屏
 * Created by Admin
 * Date 2019/11/19
 */
public class TrunWristSetActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private static final String TAG = "TrunWristSetActivity";

    ImageView commentackImg;
    TextView commentTitleTv;
    ToggleButton turnWristToggleBtn;
    TextView b31TrunWristStartTv;
    TextView b31TrunWristendTv;
    TextView showSeekBarValueTv;
    SeekBar b31TrunWristSeekBar;

    RelativeLayout b31DeviceWristRel,b31WristStartRel,b31WristEndRel;
    Button b31TrunWristSaveBtn;

    private ArrayList<String> hourList;
    private ArrayList<String> minuteList;
    private HashMap<String, ArrayList<String>> minuteMapList;

    int level = 0;
    int progessLevel ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trun_wristset_layout);

        initViews();

        initData();

        readDeviceData();

    }

    private void readDeviceData() {
       BaseApplication.getVPOperateManager().readNightTurnWriste(iBleWriteResponse, new INightTurnWristeDataListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onNightTurnWristeDataChange(NightTurnWristeData nightTurnWristeData) {
                Log.e(TAG,"----nightTurnWristeData="+nightTurnWristeData.toString());
                turnWristToggleBtn.setChecked(nightTurnWristeData.isNightTureWirsteStatusOpen());
                b31TrunWristStartTv.setText(nightTurnWristeData.getStartTime().getColck() + "");
                b31TrunWristendTv.setText(nightTurnWristeData.getEndTime().getColck() + "");
                level = nightTurnWristeData.getLevel();

                if(level == 1){
                    b31TrunWristSeekBar.setProgress(0);
                    showSeekBarValueTv.setText("("+level+")");
                }else {
                    b31TrunWristSeekBar.setProgress(level-1);
                    showSeekBarValueTv.setText("("+level+")");
                }


                if(level == 1){
                    b31TrunWristSeekBar.setProgress(0);
                    showSeekBarValueTv.setText("("+level+")");
                }else {
                    b31TrunWristSeekBar.setProgress(level-1);
                    showSeekBarValueTv.setText("("+level+")");
                }


            }
        });


        b31TrunWristSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showSeekBarValueTv.setText("(" +(progress+1) + ")");
                progessLevel = progress + 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initData() {
        hourList = new ArrayList<>();
        minuteList = new ArrayList<>();
        minuteMapList = new HashMap<>();
        for (int i = 0; i < 60; i++) {
            if (i == 0) {
                minuteList.add("00");
            } else if (i < 10) {
                minuteList.add("0" + i);
            } else {
                minuteList.add(i + "");
            }
        }
        for (int i = 0; i < 24; i++) {
            if (i == 0) {
                hourList.add("00");
                minuteMapList.put("00", minuteList);
            } else if (i < 10) {
                hourList.add("0" + i + "");
                minuteMapList.put("0" + i + "", minuteList);
            } else {
                hourList.add(i + "");
                minuteMapList.put(i + "", minuteList);
            }
        }
    }


    private void initViews() {
        commentackImg = findViewById(R.id.commentackImg);
        commentTitleTv = findViewById(R.id.commentTitleTv);
        turnWristToggleBtn = findViewById(R.id.turnWristToggleBtn);
        b31TrunWristStartTv = findViewById(R.id.b31TrunWristStartTv);
        showSeekBarValueTv = findViewById(R.id.showSeekBarValueTv);
        b31TrunWristSeekBar = findViewById(R.id.b31TrunWristSeekBar);
        b31DeviceWristRel = findViewById(R.id.b31DeviceWristRel);
        b31WristStartRel = findViewById(R.id.b31WristStartRel);
        b31WristEndRel = findViewById(R.id.b31WristEndRel);
        b31TrunWristSaveBtn = findViewById(R.id.b31TrunWristSaveBtn);

        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText(getResources().getString(R.string.string_turning_wrist_screen));
        turnWristToggleBtn.setOnCheckedChangeListener(this);

        commentackImg.setOnClickListener(this);
        b31DeviceWristRel.setOnClickListener(this);
        b31WristStartRel.setOnClickListener(this);
        b31WristEndRel.setOnClickListener(this);
        b31TrunWristSaveBtn.setOnClickListener(this);

    }

    private void saveTrunWristData() {
        //起始时间
        String startD = b31TrunWristStartTv.getText().toString().trim();
        //int startHour = DateTimeUtils.getc
        if(BraceUtils.isEmpty(startD))
            return;

        int startHour = Integer.valueOf(StringUtils.substringBefore(startD, ":").trim());
        int startMine = Integer.valueOf(StringUtils.substringAfter(startD, ":").trim());

        final TimeData startTime = new TimeData(startHour, startMine);
        String endD = b31TrunWristendTv.getText().toString().trim();
        int endHour = Integer.valueOf(StringUtils.substringBefore(endD, ":").trim());
        int endMine = Integer.valueOf(StringUtils.substringAfter(endD, ":").trim());
        final TimeData endTime = new TimeData(endHour, endMine);
        BaseApplication.getVPOperateManager().settingNightTurnWriste(iBleWriteResponse, new INightTurnWristeDataListener() {
            @Override
            public void onNightTurnWristeDataChange(NightTurnWristeData nightTurnWristeData) {
                Log.d("设置--翻腕良品", "onNightTurnWristeDataChange: "+nightTurnWristeData.toString()+"设置的level---"+progessLevel);
                Toast.makeText(TrunWristSetActivity.this, getResources().getString(R.string.settings_success), Toast.LENGTH_SHORT).show();
                finish();

            }
        }, new NightTurnWristSetting(turnWristToggleBtn.isChecked(), startTime, endTime, progessLevel));

    }

    //开始和结束日期
    private void setChooseDate(final int code) {
        ProvincePick starPopWin = new ProvincePick.Builder(TrunWristSetActivity.this, new ProvincePick.OnProCityPickedListener() {
            @Override
            public void onProCityPickCompleted(String province, String city, String dateDesc) {
                if (code == 0) {  //开始时间
                    b31TrunWristStartTv.setText(province + ":" + city);
                } else if (code == 1) {    //结束时间
                    b31TrunWristendTv.setText(province + ":" + city);
                }

            }
        }).textConfirm(getResources().getString(R.string.confirm)) //text of confirm button
                .textCancel(getResources().getString(R.string.cancle)) //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .setProvinceList(hourList) //min year in loop
                .setCityList(minuteMapList) // max year in loop
                .build();
        starPopWin.showPopWin(TrunWristSetActivity.this);
    }






    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!buttonView.isPressed())
           return;
        turnWristToggleBtn.setChecked(isChecked);
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.commentackImg) {
            finish();
        } else if (id == R.id.b31DeviceWristRel) {
            setChooseDate(0);
        } else if (id == R.id.b31WristStartRel) {
            setChooseDate(1);
        } else if (id == R.id.b31TrunWristSaveBtn) {
            saveTrunWristData();
        }
    }
}
