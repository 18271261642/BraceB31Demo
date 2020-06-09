package com.brace.android.b31.activity.ui.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.ScanActivity;
import com.brace.android.b31.activity.ui.AlarmActivity;
import com.brace.android.b31.activity.ui.CountDownActivity;
import com.brace.android.b31.activity.ui.DeviceStyleActivity;
import com.brace.android.b31.activity.ui.LongSitRemendActivity;
import com.brace.android.b31.activity.ui.MessageAlertActivity;
import com.brace.android.b31.activity.ui.PrivageBloodActivity;
import com.brace.android.b31.activity.ui.ResetDevicePwdActivity;
import com.brace.android.b31.activity.ui.SwtichActivity;
import com.brace.android.b31.activity.ui.TrunWristSetActivity;
import com.brace.android.b31.ble.BleConnStatus;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.BraceUtils;
import com.brace.android.b31.utils.SpUtils;
import com.brace.android.b31.utils.ToastUtil;
import com.brace.android.b31.view.LazyFragment;
import com.brace.android.b31.view.whell.widgets.ProfessionPick;
import com.veepoo.protocol.listener.base.IBleWriteResponse;

import java.util.ArrayList;


public class DashboardFragment extends LazyFragment implements View.OnClickListener {

    View root;
    //标题
    TextView commentTitleTv;
    ImageView commBackImg;
    TextView b31DeviceSportGoalTv;
    TextView b31DeviceSleepGoalTv;
    TextView b31DeviceUnitTv;
    TextView DeviceVersionTv;

    RelativeLayout b31DeviceMsgRel, b31DeviceAlarmRel, b31DeviceLongSitRel,
            b31DeviceWristRel,
    b31DeviceSportRel, b31DeviceSleepRel,
    b31DeviceUnitRel, b31sDevicePrivateBloadRel,
    b31DeviceSwitchRel, b31DevicePtoRel,
    b31DeviceCounDownRel, b31DeviceResetRel,
    b31DeviceStyleRel, b31DeviceDfuRel,
    b31DeviceClearDataRel;


    private Button b31DisConnBtn;



    //单位设置的alert
    private AlertDialog.Builder alert;

    //运动目标
    ArrayList<String> sportGoalList;
    //睡眠目标
    ArrayList<String> sleepGoalList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initViews(root);

        initData();

        return root;
    }

    private void initData() {
        sportGoalList = new ArrayList<>();
        sleepGoalList = new ArrayList<>();
        for (int i = 1000; i <= 64000; i += 1000) {
            sportGoalList.add(i + "");
        }

        for (int i = 1; i < 48; i++) {
            sleepGoalList.add(BraceUtils.mul(Double.valueOf(i), 0.5) + "");
        }


        //睡眠目标
        String sleepGoal = (String) SpUtils.getParam(getActivity(), Constant.DEVICE_SLEEP_GOAL,"8.0");
        if(BraceUtils.isEmpty(sleepGoal))
            sleepGoal = "8.0";
        b31DeviceSleepGoalTv.setText(sleepGoal);
        //运动目标
        int sportGoal = (int) SpUtils.getParam(getActivity(), Constant.DEVICE_SPORT_GOAL,0);
        if(sportGoal == 0)
            sportGoal = 8000;
        b31DeviceSportGoalTv.setText(sportGoal+"");

        //单位
        int deviceUnit = (int) SpUtils.getParam(getActivity(), Constant.FUN_IS_METRIC_KEY,0);
        if(deviceUnit == 0)
            deviceUnit = 0;
        b31DeviceUnitTv.setText(deviceUnit==0?getResources().getString(R.string.string_metric) : getResources().getString(R.string.string_imperil));

        //固件版本
        String versionName = (String) SpUtils.getParam(getActivity(), Constant.DEVICE__VERSION_NUMBER,"0");
        if(BraceUtils.isEmpty(versionName))
            versionName = "0-0";
        DeviceVersionTv.setText(versionName);

    }

    private void initViews(View v) {
        commBackImg = v.findViewById(R.id.commentackImg);
        b31DisConnBtn = v.findViewById(R.id.b31DisConnBtn);
        commentTitleTv = v.findViewById(R.id.commentTitleTv);
        b31DeviceSportGoalTv = v.findViewById(R.id.b31DeviceSportGoalTv);
        b31DeviceSleepGoalTv = v.findViewById(R.id.b31DeviceSleepGoalTv);
        b31DeviceUnitTv = v.findViewById(R.id.b31DeviceUnitTv);
        DeviceVersionTv = v.findViewById(R.id.DeviceVersionTv);

        b31DeviceMsgRel = v.findViewById(R.id.b31DeviceMsgRel);
        b31DeviceAlarmRel = v.findViewById(R.id.b31DeviceAlarmRel);
        b31DeviceLongSitRel = v.findViewById(R.id.b31DeviceLongSitRel);
        b31DeviceWristRel = v.findViewById(R.id.b31DeviceWristRel);
        b31DeviceSportRel = v.findViewById(R.id.b31DeviceSportRel);
        b31DeviceSleepRel = v.findViewById(R.id.b31DeviceSleepRel);
        b31DeviceUnitRel = v.findViewById(R.id.b31DeviceUnitRel);
        b31sDevicePrivateBloadRel = v.findViewById(R.id.b31sDevicePrivateBloadRel);
        b31DeviceSwitchRel = v.findViewById(R.id.b31DeviceSwitchRel);
        b31DevicePtoRel = v.findViewById(R.id.b31DevicePtoRel);
        b31DeviceCounDownRel = v.findViewById(R.id.b31DeviceCounDownRel);
        b31DeviceResetRel = v.findViewById(R.id.b31DeviceResetRel);
        b31DeviceStyleRel = v.findViewById(R.id.b31DeviceStyleRel);
        b31DeviceDfuRel = v.findViewById(R.id.b31DeviceDfuRel);
        b31DeviceClearDataRel = v.findViewById(R.id.b31DeviceClearDataRel);


        commentTitleTv.setText(getResources().getString(R.string.string_device));
        commBackImg.setVisibility(View.GONE);

        b31DeviceMsgRel.setOnClickListener(this);
        b31DeviceAlarmRel = v.findViewById(R.id.b31DeviceAlarmRel);
        b31DeviceLongSitRel.setOnClickListener(this);
        b31DeviceWristRel.setOnClickListener(this);
        b31DeviceSportRel.setOnClickListener(this);
        b31DeviceSleepRel.setOnClickListener(this);
        b31DeviceUnitRel.setOnClickListener(this);
        b31sDevicePrivateBloadRel.setOnClickListener(this);
        b31DeviceSwitchRel.setOnClickListener(this);
        b31DevicePtoRel.setOnClickListener(this);
        b31DeviceCounDownRel.setOnClickListener(this);
        b31DeviceResetRel.setOnClickListener(this);
        b31DeviceStyleRel.setOnClickListener(this);
        b31DeviceDfuRel.setOnClickListener(this);
        b31DeviceClearDataRel.setOnClickListener(this);
        b31DisConnBtn.setOnClickListener(this);
        b31DeviceAlarmRel.setOnClickListener(this);


        //是否支持倒计时
        boolean isSupportCountDowm = (boolean) SpUtils.getParam(getContext(),Constant.IS_SUPPORT_COUNT_DOWM,false);
        b31DeviceCounDownRel.setVisibility(isSupportCountDowm ? View.VISIBLE : View.GONE);

        //是否支持血压
        boolean isSupportBp = (boolean) SpUtils.getParam(getContext(),Constant.IS_SUPPORT_BP,false);
        b31sDevicePrivateBloadRel.setVisibility(isSupportBp ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }



    //单位设置
    private void setUnit(){
        String runTypeString[] = new String[]{getResources().getString(R.string.string_metric),
                getResources().getString(R.string.string_imperil)};
        alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getResources().getString(R.string.string_choose_unit))
                .setItems(runTypeString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (i == 0) {
                            b31DeviceUnitTv.setText(getResources().getString(R.string.string_metric));
                            SpUtils.setParam(getActivity(), Constant.FUN_IS_METRIC_KEY,0);
                        } else {
                            //changeCustomSetting(false);
                            b31DeviceUnitTv.setText(getResources().getString(R.string.string_imperil));
                            SpUtils.setParam(getActivity(), Constant.FUN_IS_METRIC_KEY,1);
                        }

                    }
                }).setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        }).show();

    }




    //断开连接
    private void disBleConn(){
      new AlertDialog.Builder(getActivity()).setMessage(getResources().getString(R.string.string_devices_is_disconnected))
              .setPositiveButton("Yest", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
              BaseApplication.getVPOperateManager().disconnectWatch(new IBleWriteResponse() {
                  @Override
                  public void onResponse(int i) {
                      startActivity(new Intent(getActivity(), ScanActivity.class));
                      getActivity().finish();
                  }
              });

          }
      }).setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
          }
      }).create().show();

    }





    //设置运动目标
    private void setSportGoal() {
        ProfessionPick professionPick = new ProfessionPick.Builder(getActivity(),
                new ProfessionPick.OnProCityPickedListener() {
                    @Override
                    public void onProCityPickCompleted(String profession) {
                        b31DeviceSportGoalTv.setText(profession);
                       SpUtils.setParam(getActivity(), Constant.DEVICE_SPORT_GOAL,Integer.valueOf(profession.trim()));

                    }
                }).textConfirm(getResources().getString(R.string.confirm)) //text of confirm button
                .textCancel(getResources().getString(R.string.cancle)) //text of cancel button
                .btnTextSize(18) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .setProvinceList(sportGoalList) //min year in loop
                .dateChose("8000") // date chose when init popwindow
                .build();
        professionPick.showPopWin(getActivity());
    }



    //设置睡眠目标
    private void setSleepGoal() {
        ProfessionPick sleepProfession = new ProfessionPick.Builder(getActivity(),
                new ProfessionPick.OnProCityPickedListener() {
                    @Override
                    public void onProCityPickCompleted(String profession) {
                        b31DeviceSleepGoalTv.setText(profession);
                       SpUtils.setParam(getActivity(), Constant.DEVICE_SLEEP_GOAL,profession);
                    }
                }).textConfirm(getResources().getString(R.string.confirm)) //text of confirm button
                .textCancel(getResources().getString(R.string.cancle)) //text of cancel button
                .btnTextSize(18) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .setProvinceList(sleepGoalList) //min year in loop
                .dateChose("8.0") // date chose when init popwindow
                .build();
        sleepProfession.showPopWin(getActivity());
    }


    @Override
    public void onClick(View v) {
        if(BleConnStatus.CONNDEVICENAME == null && v.getId() != R.id.b31DisConnBtn){
            ToastUtil.showShort(getActivity(),getResources().getString(R.string.string_device_no_conn));
            return;
        }
        int id = v.getId();//拍照
//ota
//清除设备数据
        if (id == R.id.b31DeviceMsgRel) {  //消息提醒
            startActivity(new Intent(getActivity(), MessageAlertActivity.class));
        } else if (id == R.id.b31DeviceAlarmRel) {    //闹钟
            startActivity(new Intent(getActivity(), AlarmActivity.class));
        } else if (id == R.id.b31DeviceLongSitRel) {  //久坐
            startActivity(new Intent(getActivity(), LongSitRemendActivity.class));
        } else if (id == R.id.b31DeviceWristRel) {    //转腕亮屏
            startActivity(new Intent(getActivity(), TrunWristSetActivity.class));
        } else if (id == R.id.b31DeviceSportRel) {    //运动目标
            setSportGoal();
        } else if (id == R.id.b31DeviceSleepRel) {    //睡眠目标
            setSleepGoal();
        } else if (id == R.id.b31DeviceUnitRel) {     //单位
            setUnit();
        } else if (id == R.id.b31sDevicePrivateBloadRel) {    //血压私人模式
            startActivity(new Intent(getActivity(), PrivageBloodActivity.class));
        } else if (id == R.id.b31DeviceSwitchRel) {   //开关
            startActivity(new Intent(getActivity(), SwtichActivity.class));
        } else if (id == R.id.b31DevicePtoRel) {
        } else if (id == R.id.b31DeviceCounDownRel) {     //倒计时
            startActivity(new Intent(getActivity(), CountDownActivity.class));
        } else if (id == R.id.b31DeviceResetRel) {        //重置设备密码
            startActivity(new Intent(getActivity(), ResetDevicePwdActivity.class));
        } else if (id == R.id.b31DeviceStyleRel) {    //主题风格
            startActivity(new Intent(getActivity(), DeviceStyleActivity.class));
        } else if (id == R.id.b31DeviceDfuRel) {
        } else if (id == R.id.b31DeviceClearDataRel) {
        } else if (id == R.id.b31DisConnBtn) {    //断开连接
            disBleConn();
        }
    }
}