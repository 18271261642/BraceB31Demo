package com.brace.android.b31.activity.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.adapter.AlarmAdapter;
import com.google.gson.Gson;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IAlarm2DataListListener;
import com.veepoo.protocol.model.datas.AlarmData2;
import com.veepoo.protocol.model.settings.Alarm2Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * 闹钟页面
 * Created by Admin
 * Date 2019/11/19
 */
public class AlarmActivity extends BaseActivity implements
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,AlarmAdapter.AlarmCheckChange , View.OnClickListener {

    //title
    private TextView titleTv;
    private ImageView backImg;
    private Button addBtn;
    //listview
    private ListView alarmListView;
    //adapter
    private AlarmAdapter alarmAdapter;

    /**
     * 手环闹钟列表
     */
    private List<Alarm2Setting> mAlarmList ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_layout);

        initViews();
        initData();

    }


    @Override
    protected void onResume() {
        super.onResume();
        readAlarmFromBleAll();
    }



    /**
     * 从手环读取闹钟数据
     */
    private void readAlarmFromBleAll() {
        //MyApp.getInstance().getVpOperateManager().readAlarm2(iBleWriteResponse, alarmDataListener);

        List<Alarm2Setting> readList = BaseApplication.getVPOperateManager().getAlarm2List();
        showAllAlarm(readList);
    }


    /**
     * 显示所有的闹钟数据
     *
     * @param alarmList 手环闹钟数据源
     */
    private void showAllAlarm(List<Alarm2Setting> alarmList) {
        if (alarmList == null || alarmList.isEmpty())
            return;
        mAlarmList.clear();
        mAlarmList.addAll(alarmList);
        alarmAdapter.notifyDataSetChanged();
    }


    private void initData() {
        mAlarmList = new ArrayList<>();
        alarmAdapter = new AlarmAdapter(AlarmActivity.this,mAlarmList);
        alarmListView.setAdapter(alarmAdapter);
        alarmListView.setOnItemClickListener(this);
        alarmListView.setOnItemLongClickListener(this);
        alarmAdapter.setChangeCallBack(this);

    }

    private void initViews() {
        alarmListView = findViewById(R.id.alarmListView);
        titleTv = findViewById(R.id.commentTitleTv);
        backImg = findViewById(R.id.commentackImg);
        backImg.setVisibility(View.VISIBLE);
        addBtn = findViewById(R.id.alarmAddAlarmBtn);
        addBtn.setOnClickListener(this);
        titleTv.setText(getResources().getString(R.string.string_clock_setting));
        backImg.setOnClickListener(this);

    }

    //item点击
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(AlarmActivity.this, SettingAlarmActivity.class);
        Alarm2Setting alarm2Setting = mAlarmList.get(position);
        String param = new Gson().toJson(alarm2Setting);
        intent.putExtra("param", param);
        startActivityForResult(intent, 100);// 单击跳编辑闹钟界面
    }

    //toggle开关回调
    @Override
    public void onCheckChange(int position) {
        updateAlarm(position);
    }

    /**
     * 开关闹钟
     *
     * @param position 闹钟列表下标
     */
    private void updateAlarm(int position) {
        Alarm2Setting alarm2Setting = mAlarmList.get(position);
        Log.d("----zza--要改变的-", alarm2Setting.toString());
        boolean open = alarm2Setting.isOpen();
        alarm2Setting.setOpen(!open);
        BaseApplication.getVPOperateManager().modifyAlarm2(iBleWriteResponse, alarmDataListener, alarm2Setting);
    }

    private IAlarm2DataListListener alarmDataListener = new IAlarm2DataListListener() {
        @Override
        public void onAlarmDataChangeListListener(AlarmData2 alarmData2) {
            Log.e("B31DeviceActivity","---------读取闹钟返回="+alarmData2.toString());
            showAllAlarm(alarmData2.getAlarm2SettingList());
        }
    };


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.commentackImg) {
            finish();
        } else if (id == R.id.alarmAddAlarmBtn) {
            startActivity(SettingAlarmActivity.class);
        }
    }

    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showDeleteAlarmDia(position);
        return true;
    }

    /**
     * 删除闹钟
     */
    private void showDeleteAlarmDia(final int position) {
        final Alarm2Setting alarm2Setting = mAlarmList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(AlarmActivity.this)
                .setTitle(R.string.deleda)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BaseApplication.getVPOperateManager().deleteAlarm2(iBleWriteResponse, alarmDataListener, alarm2Setting);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancle, null);
        builder.show();
    }


}
