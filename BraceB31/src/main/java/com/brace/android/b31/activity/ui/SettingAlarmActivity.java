package com.brace.android.b31.activity.ui;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.adapter.ImageAdapter;
import com.brace.android.b31.utils.BraceUtils;
import com.brace.android.b31.view.whell.widgets.ProvincePick;
import com.google.gson.Gson;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IAlarm2DataListListener;
import com.veepoo.protocol.model.datas.AlarmData2;
import com.veepoo.protocol.model.settings.Alarm2Setting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by Admin
 * Date 2019/11/23
 */
public class SettingAlarmActivity extends BaseActivity implements AdapterView.OnItemClickListener , View.OnClickListener {


    ImageView commentackImg;

    TextView commentTitleTv;
    //时间
    TextView tvAlarmTime;
    //星期
    TextView tvAlarmType;
    ImageView ivAlarmType;
    LinearLayout alarmTypeLine;
    GridView gvAlarmType;

    //提醒类型的adapter
    private ImageAdapter imageAdapter;

    /**
     * 小时数据源
     */
    private ArrayList<String> hourList;
    /**
     * 小时包含分钟的数据源
     */
    private HashMap<String, ArrayList<String>> minuteMapList;


    /**
     * 闹钟详情(新增时为空,编辑时有)
     */
    private Alarm2Setting mAlarm2Setting;

    /**
     * 保存选择的重复天数
     */
    private boolean[] checkedItems = {false, false, false, false, false, false, false};
    /**
     * 闹钟图标数组,资源ID
     */
    private final int[] alarmTypeImageList = {R.mipmap.selected1, R.mipmap.selected2,
            R.mipmap.selected3, R.mipmap.selected4, R.mipmap.selected5, R.mipmap.selected6,
            R.mipmap.selected7, R.mipmap.selected8, R.mipmap.selected9, R.mipmap.selected10,
            R.mipmap.selected11, R.mipmap.selected12, R.mipmap.selected13, R.mipmap.selected14,
            R.mipmap.selected15, R.mipmap.selected16, R.mipmap.selected17, R.mipmap.selected18,
            R.mipmap.selected19, R.mipmap.selected20};


    //是否是修改闹钟
    private boolean editAlarm = false;

    private Button b30AlarmSaveBtn;
    private LinearLayout tv_alarm_time_layout,tv_alarm_type_layout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm_layout);

        initViews();

        initData();

        initParams();

    }


    /**
     * 初化参数
     */
    private void initParams() {
        //编辑闹钟的参数
        String param = getIntent().getStringExtra("param");
        if(BraceUtils.isEmpty(param)){  //参数为空时是新建闹钟
            editAlarm = false;
            mAlarm2Setting = new Alarm2Setting();
            mAlarm2Setting.setAlarmHour(8);
            mAlarm2Setting.setAlarmMinute(0);
            mAlarm2Setting.setScene(1);
            mAlarm2Setting.setUnRepeatDate(getTimeNow());
            mAlarm2Setting.setRepeatStatus(getWeek((getTimeNow())));
            mAlarm2Setting.setOpen(true);
            commentTitleTv.setText(R.string.new_alarm_clock);
            showAlarm(mAlarm2Setting);
            return;
        }

        //不为空时是编辑闹钟
        mAlarm2Setting = new Gson().fromJson(param, Alarm2Setting.class);
        if(mAlarm2Setting == null)
            return;
        editAlarm = true;
        showAlarm(mAlarm2Setting);
    }


    //显示闹钟
    private void showAlarm(Alarm2Setting alarm2Setting) {
        int hour = alarm2Setting.getAlarmHour();
        int minute = alarm2Setting.getAlarmMinute();
        String hourStr = hour < 10 ? "0" + hour : "" + hour;
        String minuteStr = minute < 10 ? "0" + minute : "" + minute;
        String showTime = hourStr + ":" + minuteStr;
        tvAlarmTime.setText(showTime);// 时间
        Log.e("---zza---", mAlarm2Setting.getUnRepeatDate());
        if (alarm2Setting.getUnRepeatDate().equals("0000-00-00")) {
            initRepeat(alarm2Setting.getRepeatStatus());
        } else {

            String week = getWeek(getTimeNow());
            if (week == null || week.length() != 7) week = "0000000";
            for (int i = 0; i < 7; i++) {
                checkedItems[i] = week.charAt(i) == '1';
            }
            tvAlarmType.setText(BraceUtils.obtainAlarmDate(this, week));
        }
        ivAlarmType.setImageResource(alarmTypeImageList[alarm2Setting.getScene()>0?alarm2Setting.getScene() - 1:0]);
    }


    private void initData() {
        hourList = new ArrayList<>();
        minuteMapList = new HashMap<>();
        //分钟数据源
        ArrayList<String> minuteList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            String minute = "" + i;
            if (i < 10) minute = "0" + minute;
            minuteList.add(minute);
        }
        for (int i = 0; i <= 23; i++) {
            if (i < 10) {
                hourList.add("0" + i);
                minuteMapList.put("0" + i, minuteList);
            } else {
                hourList.add("" + i);
                minuteMapList.put("" + i, minuteList);
            }
        }


        String week = getWeek(getTimeNow());
        for (int i = 0; i < 7; i++) {
            checkedItems[i] = week.charAt(i) == '1';
        }
        tvAlarmTime.setText(BraceUtils.obtainAlarmDate(this, week));

        imageAdapter = new ImageAdapter(SettingAlarmActivity.this);
        gvAlarmType.setAdapter(imageAdapter);
        gvAlarmType.setOnItemClickListener(this);

    }

    private void initViews() {
        commentackImg = findViewById(R.id.commentackImg);
        commentTitleTv = findViewById(R.id.commentTitleTv);
        tvAlarmTime = findViewById(R.id.tv_alarm_time);
        tvAlarmType = findViewById(R.id.tv_alarm_type);
        ivAlarmType = findViewById(R.id.iv_alarm_type);
        alarmTypeLine = findViewById(R.id.alarm_type_line);
        gvAlarmType = findViewById(R.id.gv_alarm_type);

        b30AlarmSaveBtn = findViewById(R.id.b30AlarmSaveBtn);
        tv_alarm_time_layout = findViewById(R.id.tv_alarm_time_layout);
        tv_alarm_type_layout = findViewById(R.id.tv_alarm_type_layout);



        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText(getResources().getString(R.string.string_alarm_type));

        commentackImg.setOnClickListener(this);
        alarmTypeLine.setOnClickListener(this);
        b30AlarmSaveBtn.setOnClickListener(this);
        tv_alarm_time_layout.setOnClickListener(this);
        tv_alarm_type_layout.setOnClickListener(this);

    }


    /**
     * 初始化重复状态
     */
    private void initRepeat(String repeat) {
        if (repeat == null || repeat.length() != 7) repeat = "0000000";
        for (int i = 0; i < 7; i++) {
            checkedItems[i] = repeat.charAt(i) == '1';
        }
        tvAlarmType.setText(BraceUtils.obtainAlarmDate(this, repeat));
    }




    //保存闹钟
    private void saveSettingAlarm(){
        mAlarm2Setting.setOpen(true);
        if (BraceUtils.isEmpty(mAlarm2Setting.getRepeatStatus()) || mAlarm2Setting.getRepeatStatus().equals("0000000")) {
            mAlarm2Setting.setRepeatStatus("0000000");
            initRepeat(mAlarm2Setting.getRepeatStatus());
        }
        Log.d("SETT", mAlarm2Setting.toString());


        if (editAlarm) {
           BaseApplication.getVPOperateManager().modifyAlarm2(iBleWriteResponse, new IAlarm2DataListListener() {
                @Override
                public void onAlarmDataChangeListListener(AlarmData2 alarmData2) {
                    Log.d("-----zza-修改闹钟-", alarmData2.toString());
                   closeLoadDialog();
                    SettingAlarmActivity.this.setResult(1001);
                    finish();
                }
            }, mAlarm2Setting);
        } else {
            BaseApplication.getVPOperateManager().addAlarm2(iBleWriteResponse, new IAlarm2DataListListener() {
                @Override
                public void onAlarmDataChangeListListener(AlarmData2 alarmData2) {
                    closeLoadDialog();
                    Log.d("-----zza-新建闹钟-", alarmData2.toString());
                    SettingAlarmActivity.this.setResult(1001);
                    finish();
                }
            }, mAlarm2Setting);
        }


    }



    /**
     * 根据当前日期获得是星期几
     * time=yyyy-MM-dd
     *
     * @return
     */
    public static String getWeek(String time) {
        String Week = "0000000";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //1111111
        int wek = c.get(Calendar.DAY_OF_WEEK)-1;
        if(wek < 0){
            wek = 0;
        }
        if (wek == 0) {
//            Week += "星期日";
            Week = "1000000";
        }
        if (wek == 1) {
//            Week += "星期一";
            Week = "0000001";
        }
        if (wek == 2) {
//            Week += "星期二";
            Week = "0000010";
        }
        if (wek == 3) {
//            Week += "星期三";
            Week = "0000100";
        }
        if (wek == 4) {
//            Week += "星期四";
            Week = "0001000";
        }
        if (wek == 5) {
//            Week += "星期五";
            Week = "0010000";
        }
        if (wek == 6) {
//            Week += "星期六";
            Week = "0100000";
        }
        return Week;
    }


    private String getTimeNow() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        Log.d("---zza---", "Date获取当前日期时间" + simpleDateFormat.format(date));
        return simpleDateFormat.format(date);
    }

    /**
     * 计算重复情况: 1100000_周日至周一
     */
    private void obtainRepeat() {
        StringBuilder builder = new StringBuilder();
        for (boolean item : checkedItems) {
            builder.append(item ? "1" : "0");
        }
        String result = builder.toString();
        mAlarm2Setting.setRepeatStatus(result);
        tvAlarmType.setText(BraceUtils.obtainAlarmDate(this, result));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mAlarm2Setting == null)
            return;
        mAlarm2Setting.setScene(position + 1);// 存一下新场景
        ivAlarmType.setImageResource(alarmTypeImageList[position]);
    }

    /**
     * 闹钟时间选择
     */
    private void chooseDate() {
        ProvincePick starPopWin = new ProvincePick.Builder(this, new ProvincePick.OnProCityPickedListener() {
            @Override
            public void onProCityPickCompleted(String province, String city, String dateDesc) {
                String showTime = province + ":" + city;
                tvAlarmTime.setText(showTime);
                mAlarm2Setting.setAlarmHour(Integer.valueOf(province));
                mAlarm2Setting.setAlarmMinute(Integer.valueOf(city));
            }
        }).textConfirm(getResources().getString(R.string.confirm)) //text of obtainRepeat button
                .textCancel(getResources().getString(R.string.cancle)) //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(30) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of obtainRepeat button
                .setProvinceList(hourList) //min year in loop
                .setCityList(minuteMapList) // max year in loop
                .build();
        starPopWin.showPopWin(this);
    }


    /**
     * 选择重复星期几闹钟
     */
    public void showMultiAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_repeat_week);
        builder.setMultiChoiceItems(R.array.WeekItems, checkedItems, new DialogInterface
                .OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedItems[which] = isChecked;
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                obtainRepeat();
            }
        });
        builder.setNegativeButton(R.string.cancle, null);
        builder.show();
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
        } else if (id == R.id.tv_alarm_time_layout) { //选择时间
            chooseDate();
        } else if (id == R.id.tv_alarm_type_layout) { //选择日期
            showMultiAlertDialog();
        } else if (id == R.id.b30AlarmSaveBtn) {  //保存闹钟
            saveSettingAlarm();
        }
    }
}
