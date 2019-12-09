package com.brace.android.b31.activity.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.SpUtils;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IAllSetDataListener;
import com.veepoo.protocol.listener.data.ICheckWearDataListener;
import com.veepoo.protocol.listener.data.ICustomSettingDataListener;
import com.veepoo.protocol.model.datas.AllSetData;
import com.veepoo.protocol.model.datas.CheckWearData;
import com.veepoo.protocol.model.enums.EFunctionStatus;
import com.veepoo.protocol.model.settings.CheckWearSetting;
import com.veepoo.protocol.model.settings.CustomSetting;
import com.veepoo.protocol.model.settings.CustomSettingData;

/**
 * Created by Admin
 * Date 2019/11/19
 */
public class SwtichActivity extends BaseActivity {

    ImageView commentackImg;
    TextView commentTitleTv;
    ToggleButton b31CheckWearToggleBtn;
    RelativeLayout b31CheckWearRel;
    ToggleButton b31AutoHeartToggleBtn;
    RelativeLayout b31AUtoHeartRel;
    ToggleButton b31AutoBloadToggleBtn;
    RelativeLayout b31AutoBloodRel;
    ToggleButton b31AutoBPOxyToggbleBtn;
    RelativeLayout b31Spo2CheckRel;
    ToggleButton b31SecondToggleBtn;
    RelativeLayout b31SecondRel;
    ToggleButton b31SwitchFindPhoneToggleBtn;
    RelativeLayout b31FindPhoneRel;
    ToggleButton b31SwitchDisAlertTogg;
    RelativeLayout b31DisConnAlertRel;
    ToggleButton b31SwitchTimeTypeTogg;
    RelativeLayout b31TimeTypeRel;
    ToggleButton b31SwitchHlepSos;
    RelativeLayout b31SosRel;
    ToggleButton b31ScienceToggleBtn;
    RelativeLayout b31sScienceRel;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setAllDeviceSwtich();

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_layout);

        findViews();

        initViews();


        readDeviceSwitch();

    }

    private void findViews() {
        commentackImg = findViewById(R.id.commentackImg);
        commentTitleTv = findViewById(R.id.commentTitleTv);
        b31CheckWearToggleBtn = findViewById(R.id.b31CheckWearToggleBtn);
        b31CheckWearRel = findViewById(R.id.b31CheckWearRel);
        b31AutoHeartToggleBtn = findViewById(R.id.b31AutoHeartToggleBtn);
        b31AUtoHeartRel = findViewById(R.id.b31AUtoHeartRel);
        b31AutoBloadToggleBtn = findViewById(R.id.b31AutoBloadToggleBtn);
        b31AutoBloodRel = findViewById(R.id.b31AutoBloodRel);
        b31AutoBPOxyToggbleBtn = findViewById(R.id.b31AutoBPOxyToggbleBtn);
        b31Spo2CheckRel = findViewById(R.id.b31Spo2CheckRel);
        b31SecondToggleBtn = findViewById(R.id.b31SecondToggleBtn);
        b31SecondRel = findViewById(R.id.b31SecondRel);
        b31SwitchFindPhoneToggleBtn = findViewById(R.id.b31SwitchFindPhoneToggleBtn);
        b31FindPhoneRel = findViewById(R.id.b31FindPhoneRel);
        b31SwitchDisAlertTogg = findViewById(R.id.b31SwitchDisAlertTogg);
        b31DisConnAlertRel = findViewById(R.id.b31DisConnAlertRel);
        b31SwitchTimeTypeTogg = findViewById(R.id.b31SwitchTimeTypeTogg);
        b31TimeTypeRel = findViewById(R.id.b31TimeTypeRel);
        b31SwitchHlepSos = findViewById(R.id.b31SwitchHlepSos);
        b31SosRel = findViewById(R.id.b31SosRel);
        b31ScienceToggleBtn = findViewById(R.id.b31ScienceToggleBtn);
        b31sScienceRel = findViewById(R.id.b31sScienceRel);
    }

    private void readDeviceSwitch() {

        //佩戴检测
        boolean isWear = (boolean) SpUtils.getParam(SwtichActivity.this, Constant.FUN_CHECK_WEAR_KEY,true);
        b31CheckWearToggleBtn.setChecked(isWear);


        //读取血氧自动检测的状态
       BaseApplication.getVPOperateManager().readSpo2hAutoDetect(iBleWriteResponse, new IAllSetDataListener() {
            @Override
            public void onAllSetDataChangeListener(AllSetData allSetData) {
                //Log.e(TAG, "---------allSetData=" + allSetData.toString());
                if (allSetData.getOprate() == 1 && allSetData.getIsOpen() == 1) {
                    b31AutoBPOxyToggbleBtn.setChecked(true);
                } else {
                    b31AutoBPOxyToggbleBtn.setChecked(false);
                }
            }
        });


        BaseApplication.getVPOperateManager().readCustomSetting(iBleWriteResponse, new ICustomSettingDataListener() {
            @Override
            public void OnSettingDataChange(CustomSettingData customSettingData) {
                Log.e("开关","----customSettingData---"+customSettingData.toString());
                //自动心率检测
               b31AutoHeartToggleBtn.setChecked(customSettingData.getAutoHeartDetect() == EFunctionStatus.SUPPORT_OPEN);
               //24小时制
                b31SwitchTimeTypeTogg.setChecked(customSettingData.isIs24Hour());

               //自动测量血压
                if(customSettingData.getAutoBpDetect() == EFunctionStatus.UNSUPPORT){
                    b31AutoBloodRel.setVisibility(View.GONE);
                }else{
                    b31AutoBloodRel.setVisibility(View.VISIBLE);
                    b31AutoBloadToggleBtn.setChecked(customSettingData.getAutoBpDetect() == EFunctionStatus.SUPPORT_OPEN);
                }

                //断连提醒
                if(customSettingData.getDisconnectRemind() == EFunctionStatus.UNSUPPORT ){
                    b31DisConnAlertRel.setVisibility(View.GONE);
                }else{
                    b31DisConnAlertRel.setVisibility(View.VISIBLE);
                    b31SwitchDisAlertTogg.setChecked(customSettingData.getDisconnectRemind() == EFunctionStatus.SUPPORT_OPEN);

                }

                //秒表
                if(customSettingData.getSecondsWatch() == EFunctionStatus.UNSUPPORT){
                    b31SecondRel.setVisibility(View.GONE);
                }else{
                    b31SecondRel.setVisibility(View.VISIBLE);
                    b31SecondToggleBtn.setChecked(customSettingData.getSecondsWatch() == EFunctionStatus.SUPPORT_OPEN);
                }

                //查找手机
                if(customSettingData.getFindPhoneUi() == EFunctionStatus.UNSUPPORT){
                    b31FindPhoneRel.setVisibility(View.GONE);
                }else{
                    b31FindPhoneRel.setVisibility(View.VISIBLE);
                    b31SwitchFindPhoneToggleBtn.setChecked(customSettingData.getFindPhoneUi() == EFunctionStatus.SUPPORT_OPEN);

                }

                //SOS
                if(customSettingData.getSOS() == EFunctionStatus.UNSUPPORT ){
                    b31SosRel.setVisibility(View.GONE);
                }else{
                    b31SosRel.setVisibility(View.VISIBLE);
                    b31SwitchHlepSos.setChecked(customSettingData.getSOS() == EFunctionStatus.SUPPORT_OPEN);
                }

                //科学睡眠
                if(customSettingData.getPpg() == EFunctionStatus.UNSUPPORT){
                    b31sScienceRel.setVisibility(View.GONE);
                }else{
                    b31sScienceRel.setVisibility(View.VISIBLE);
                    b31ScienceToggleBtn.setChecked(customSettingData.getPpg() == EFunctionStatus.SUPPORT_OPEN);
                }


            }
        });


    }

    private void initViews() {
        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText(getResources().getString(R.string.string_switch_setting));
        b31CheckWearToggleBtn.setOnCheckedChangeListener(onCheckedChangeListener);
        b31AutoBloadToggleBtn.setOnCheckedChangeListener(onCheckedChangeListener);
        b31AutoHeartToggleBtn.setOnCheckedChangeListener(onCheckedChangeListener);
        b31AutoBPOxyToggbleBtn.setOnCheckedChangeListener(onCheckedChangeListener);
        b31SecondToggleBtn.setOnCheckedChangeListener(onCheckedChangeListener);
        b31SwitchFindPhoneToggleBtn.setOnCheckedChangeListener(onCheckedChangeListener);
        b31SwitchDisAlertTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31SwitchTimeTypeTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31SwitchHlepSos.setOnCheckedChangeListener(onCheckedChangeListener);
        b31ScienceToggleBtn.setOnCheckedChangeListener(onCheckedChangeListener);

        commentackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(!buttonView.isPressed())
                return;
            int id = buttonView.getId();
            if (id == R.id.b31CheckWearToggleBtn) {    //佩戴检测
                setWearSett(isChecked);
                SpUtils.setParam(SwtichActivity.this, Constant.FUN_CHECK_WEAR_KEY, isChecked);
            } else if (id == R.id.b31AutoHeartToggleBtn) {    //自动心率检测
                b31AutoHeartToggleBtn.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31AutoBloadToggleBtn) {    //自动血压检测
                b31AutoBloadToggleBtn.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31AutoBPOxyToggbleBtn) {     //自动血氧检测
                b31AutoBPOxyToggbleBtn.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31SecondToggleBtn) {       //秒表
                b31SecondToggleBtn.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31SwitchFindPhoneToggleBtn) {  //查找手机
                b31SwitchFindPhoneToggleBtn.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31SwitchDisAlertTogg) {    //断连提醒
                b31SwitchDisAlertTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31SwitchTimeTypeTogg) {    //24小时制
                b31SwitchTimeTypeTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31ScienceToggleBtn) {  //科学睡眠
                b31ScienceToggleBtn.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31SwitchHlepSos) { //sos
                b31SwitchHlepSos.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            }

        }
    };


    public void setAllDeviceSwtich(){

        //运动过量提醒 B31不支持
        EFunctionStatus isOpenSportRemain = EFunctionStatus.UNSUPPORT;
        //血压/心率播报 B31不支持
        EFunctionStatus isOpenVoiceBpHeart = EFunctionStatus.UNSUPPORT;
        //查找手表
        EFunctionStatus isOpenFindPhoneUI = b31SwitchFindPhoneToggleBtn.isChecked() ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE;
        //秒表功能  支持
        EFunctionStatus isOpenStopWatch = b31SecondToggleBtn.isChecked()?EFunctionStatus.SUPPORT_OPEN:EFunctionStatus.SUPPORT_CLOSE;
        //低压报警 支持
        EFunctionStatus isOpenSpo2hLowRemind = EFunctionStatus.SUPPORT_OPEN;
        //肤色功能 支持
        EFunctionStatus isOpenWearDetectSkin = EFunctionStatus.SUPPORT_OPEN;

        //自动接听来电 不支持
        EFunctionStatus isOpenAutoInCall = EFunctionStatus.UNSUPPORT;
        //自动检测HRV 支持
        EFunctionStatus isOpenAutoHRV = EFunctionStatus.SUPPORT_OPEN;
        //断连提醒 支持
        EFunctionStatus isOpenDisconnectRemind = b31SwitchDisAlertTogg.isChecked() ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE;
        //SOS  不支持
        EFunctionStatus isOpenSOS = b31SwitchHlepSos.isChecked() ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE;

        //精准睡眠
        EFunctionStatus isPrecisionSleep = b31ScienceToggleBtn.isChecked() ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE;


        //是否公英制
        boolean isMetric = (boolean) SpUtils.getParam(SwtichActivity.this, Constant.FUN_IS_METRIC_KEY,true);
        //

        /*
         * @param isHaveMetricSystem     是否拥有设置公英制的功能，需要先读取个性化设置，调用readCustomSetting方法
         * @param isMetric               true设置公制，false表示设置英制,设备语言设置成[英语或繁体]才能体现英制
         * @param is24Hour               ture表示24小时制，false表示12小时制
         * @param isOpenAutoHeartDetect  true表示打开了自动测量心率功能，false表示关闭自动测量心率功能
         * @param isOpenAutoBpDetect     true表示打开了自动测量血压功能，false表示关闭自动测量血压功能
         * @param isOpenSportRemain      SUPPORT_OPEN 表示打开了运动过量提醒功能，SUPPORT_CLOSE 表示关闭运动过量提醒功能; UNSUPPORT表示不支持
         * @param isOpenVoiceBpHeart     SUPPORT_OPEN 表示打开了心率/血氧/血压播报功能，SUPPORT_CLOSE 表示关闭心率/血氧/血压播报功能; UNSUPPORT表示不支持
         * @param isOpenFindPhoneUI      SUPPORT_OPEN 表示打开了手机查找功能，SUPPORT_CLOSE 表示关闭手机查找功能; UNSUPPORT表示不支持
         * @param isOpenStopWatch        SUPPORT_OPEN 表示打开了秒表功能功能，SUPPORT_CLOSE 表示关闭秒表功能功能; UNSUPPORT表示不支持
         * @param isOpenSpo2hLowRemind   SUPPORT_OPEN 表示打开了低氧提醒功能，SUPPORT_CLOSE 表示关闭低氧提醒功能; UNSUPPORT表示不支持
         * @param isOpenWearDetectSkin    SUPPORT_OPEN 表示偏白色肤色 ，SUPPORT_CLOSE 表示偏黑色肤色; UNSUPPORT表示不支持
         * @param isOpenAutoInCall       SUPPORT_OPEN 表示打开了自动监听功能，SUPPORT_CLOSE 表示关闭自动监听功能; UNSUPPORT表示不支持
         * @param isOpenDisconnectRemind SUPPORT_OPEN 表示打开了断接提醒功能，SUPPORT_CLOSE 表示关闭断接提醒功能; UNSUPPORT表示不支持
         * @param isOpenSOS              SUPPORT_OPEN 表示打开了求救功能，SUPPORT_CLOSE 表示关闭求救功能; UNSUPPORT表示不支持
         */




        CustomSetting customSetting = new CustomSetting(true,isMetric,b31SwitchTimeTypeTogg.isChecked(),
                b31AutoHeartToggleBtn.isChecked(),b31AutoBloadToggleBtn.isChecked(),isOpenSportRemain,isOpenVoiceBpHeart,
                isOpenFindPhoneUI,isOpenStopWatch,isOpenSpo2hLowRemind,isOpenWearDetectSkin,isOpenAutoInCall,isOpenAutoHRV,isOpenDisconnectRemind,isOpenSOS,isPrecisionSleep);
        Log.e("开关","---------开关="+customSetting.toString());
        BaseApplication.getVPOperateManager().changeCustomSetting(iBleWriteResponse, new ICustomSettingDataListener() {
            @Override
            public void OnSettingDataChange(CustomSettingData customSettingData) {
                Log.e("开关","----------设置完后结果="+customSettingData.toString());
            }
        }, customSetting);

    }



    //设置佩戴检测
    private void setWearSett(boolean isChecked){
        //SpUtils.setParam(SwtichActivity.this, Constant.CE)
        CheckWearSetting checkWearSetting = new CheckWearSetting();
        checkWearSetting.setOpen(isChecked);
        BaseApplication.getVPOperateManager().setttingCheckWear(iBleWriteResponse, new ICheckWearDataListener() {
            @Override
            public void onCheckWearDataChange(CheckWearData checkWearData) {

            }
        }, checkWearSetting);
    }


    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };
}
