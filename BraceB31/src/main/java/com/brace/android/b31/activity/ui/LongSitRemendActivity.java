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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.ble.BleConnStatus;
import com.brace.android.b31.view.whell.widgets.ProfessionPick;
import com.brace.android.b31.view.whell.widgets.ProvincePick;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.ILongSeatDataListener;
import com.veepoo.protocol.model.datas.LongSeatData;
import com.veepoo.protocol.model.enums.ELongSeatStatus;
import com.veepoo.protocol.model.settings.LongSeatSetting;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 久坐设置
 * Created by Admin
 * Date 2019/11/19
 */
public class LongSitRemendActivity extends BaseActivity implements
        CompoundButton.OnCheckedChangeListener , View.OnClickListener {

    ImageView commentackImg;
    TextView commentTitleTv;
    ToggleButton longSitToggleBtn;
    TextView showB31LongSitStartTv;
    TextView showB30LongSitEndTv;
    TextView showB31LongSitTv;

    RelativeLayout b31LongSitStartRel,b31LongSitEndRel,
            b31LongSitTimeRel;
    Button b31LongSitSaveBtn;


    private ArrayList<String> hourList;
    private ArrayList<String> minuteList;
    private HashMap<String, ArrayList<String>> minuteMapList;
    ArrayList<String> longTimeLit;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_sit_layout);

        initViews();

        initData();

        readLongSitData();

    }

    private void initData() {
        hourList = new ArrayList<>();

        minuteList = new ArrayList<>();

        minuteMapList = new HashMap<>();


        longTimeLit = new ArrayList<>();


        for (int i = 30; i <= 240; i++) {
            longTimeLit.add(i + "");
        }

        for (int i = 0; i < 60; i++) {
            if (i == 0) {
                minuteList.add("00");
            } else if (i < 10) {
                minuteList.add("0" + i);
            } else {
                minuteList.add(i + "");
            }
        }
        for (int i = 8; i <= 18; i++) {
            if (i < 10) {
                hourList.add("0" + i + "");
                minuteMapList.put("0" + i + "", minuteList);

            } else {
                hourList.add(i + "");
                minuteMapList.put(i + "", minuteList);

            }
        }

    }


    private void readLongSitData() {
        BaseApplication.getVPOperateManager().readLongSeat(iBleWriteResponse, new ILongSeatDataListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLongSeatDataChange(LongSeatData longSeatData) {
                int startHour = longSeatData.getStartHour();
                int startMin = longSeatData.getStartMinute();

                int endHour = longSeatData.getEndHour();
                int endMine = longSeatData.getEndMinute();

                //开始时间
                showB31LongSitStartTv.setText((startHour<=9?0+""+startHour : startHour) + ":" + (startMin<=9?0+""+startMin : startMin));
                //结束时间
                showB30LongSitEndTv.setText((endHour<=9?0+""+endHour : endHour) + ":" + (endMine<=9?0+""+endMine : endMine));
                //时长
                showB31LongSitTv.setText(longSeatData.getThreshold()+"mine");
                longSitToggleBtn.setChecked(longSeatData.isOpen());

            }
        });
    }

    private void initViews() {

        commentackImg = findViewById(R.id.commentackImg);
        commentTitleTv = findViewById(R.id.commentTitleTv);
        longSitToggleBtn = findViewById(R.id.longSitToggleBtn);
        showB31LongSitStartTv = findViewById(R.id.showB31LongSitStartTv);
        showB30LongSitEndTv = findViewById(R.id.showB30LongSitEndTv);
        showB31LongSitTv = findViewById(R.id.showB31LongSitTv);
        b31LongSitStartRel = findViewById(R.id.b31LongSitStartRel);
        b31LongSitEndRel = findViewById(R.id.b31LongSitEndRel);
        b31LongSitTimeRel = findViewById(R.id.b31LongSitTimeRel);
        b31LongSitSaveBtn = findViewById(R.id.b31LongSitSaveBtn);

        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText(getResources().getString(R.string.sedentaryreminder));
        longSitToggleBtn.setOnCheckedChangeListener(this);

        commentackImg.setOnClickListener(this);
        b31LongSitStartRel.setOnClickListener(this);
        b31LongSitEndRel.setOnClickListener(this);
        b31LongSitTimeRel.setOnClickListener(this);
        b31LongSitSaveBtn.setOnClickListener(this);

    }




    private void saveLongSitData() {
        if (BleConnStatus.CONNDEVICENAME != null) {
            String startD = showB31LongSitStartTv.getText().toString().trim();
            int startHour = Integer.valueOf(StringUtils.substringBefore(startD, ":").trim());
            int startMine = Integer.valueOf(StringUtils.substringAfter(startD, ":").trim());
            String endD = showB30LongSitEndTv.getText().toString().trim();
            int endHour = Integer.valueOf(StringUtils.substringBefore(endD, ":").trim());
            int endMine = Integer.valueOf(StringUtils.substringAfter(endD, ":").trim());
            //时长
            String longD = showB31LongSitTv.getText().toString().trim();
            int longTime = Integer.valueOf(StringUtils.substringBefore(longD, "min").trim());
            BaseApplication.getVPOperateManager().settingLongSeat(iBleWriteResponse, new LongSeatSetting(startHour, startMine, endHour, endMine, longTime, longSitToggleBtn.isChecked()), new ILongSeatDataListener() {
                @Override
                public void onLongSeatDataChange(LongSeatData longSeatData) {
                    Log.e("久坐", "----longSeatData=" + longSeatData.toString());
                    if (longSeatData.getStatus() == ELongSeatStatus.OPEN_SUCCESS || longSeatData.getStatus() == ELongSeatStatus.CLOSE_SUCCESS) {
                        Toast.makeText(LongSitRemendActivity.this,  getResources().getString(R.string.settings_success), Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }
            });
        }
    }



    //选择时间
    private void chooseStartEndDate(final int code) {
        ProvincePick starPopWin = new ProvincePick.Builder(LongSitRemendActivity.this, new ProvincePick.OnProCityPickedListener() {
            @Override
            public void onProCityPickCompleted(String province, String city, String dateDesc) {
                if (code == 0) {  //开始时间
                    showB31LongSitStartTv.setText(province + ":" + city);
                }
                else if (code == 1) {    //结束时间
                    showB30LongSitEndTv.setText(province + ":" + city);

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
        starPopWin.showPopWin(LongSitRemendActivity.this);
    }


    //设置时长
    private void chooseLongTime() {
        ProfessionPick professionPick = new ProfessionPick.Builder(LongSitRemendActivity.this, new ProfessionPick.OnProCityPickedListener() {
            @Override
            public void onProCityPickCompleted(String profession) {
                showB31LongSitTv.setText(profession + "min");
            }
        }).textConfirm(getResources().getString(R.string.confirm)) //text of confirm button
                .textCancel(getResources().getString(R.string.cancle)) //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .setProvinceList(longTimeLit) //min year in loop
                .dateChose("30") // date chose when init popwindow
                .build();
        professionPick.showPopWin(LongSitRemendActivity.this);
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
        longSitToggleBtn.setChecked(isChecked);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.commentackImg) {
            finish();
        } else if (id == R.id.b31LongSitStartRel) {
            chooseStartEndDate(0);
        } else if (id == R.id.b31LongSitEndRel) {
            chooseStartEndDate(1);
        } else if (id == R.id.b31LongSitTimeRel) {
            chooseLongTime();
        } else if (id == R.id.b31LongSitSaveBtn) {
            saveLongSitData();
        }
    }
}
