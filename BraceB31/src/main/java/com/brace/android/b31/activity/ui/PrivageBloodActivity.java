package com.brace.android.b31.activity.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.ble.BleConnStatus;
import com.brace.android.b31.utils.ToastUtil;
import com.brace.android.b31.view.widget.ScrollPickerView;
import com.brace.android.b31.view.widget.StringScrollPicker;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IBPSettingDataListener;
import com.veepoo.protocol.model.datas.BpSettingData;
import com.veepoo.protocol.model.enums.EBPDetectModel;
import com.veepoo.protocol.model.settings.BpSetting;

import java.util.ArrayList;
import java.util.List;


/**
 * 血压私人模式
 * Created by Admin
 * Date 2019/11/25
 */
public class PrivageBloodActivity extends BaseActivity implements  ScrollPickerView.OnSelectedListener,
        View.OnClickListener {


    ImageView commentackImg;
    TextView commentTitleTv;
    ToggleButton privateBloadToggleBtn;
    StringScrollPicker hightBloadView;
    StringScrollPicker lowBloadView;
    TextView bloodValueTv;
    Button b30SetPrivateBloadBtn;

    //血压数据
    private List<String> hightBloadList;
    //低压
    private List<String> lowBloadList;

    private int highBload;
    private int lowBload;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_blood_layout);

        findViews();

        initViews();

        initData();

        readDeviceData();
    }

    private void findViews() {

        commentackImg = findViewById(R.id.commentackImg);
        commentTitleTv = findViewById(R.id.commentTitleTv);
        privateBloadToggleBtn = findViewById(R.id.privateBloadToggleBtn);
        hightBloadView = findViewById(R.id.hightBloadView);
        lowBloadView = findViewById(R.id.lowBloadView);
        bloodValueTv = findViewById(R.id.bloodValueTv);
        b30SetPrivateBloadBtn = findViewById(R.id.b30SetPrivateBloadBtn);


        commentackImg.setOnClickListener(this);
        b30SetPrivateBloadBtn.setOnClickListener(this);

    }

    private void readDeviceData() {
        if(BleConnStatus.CONNDEVICENAME == null)
            return;
        BaseApplication.getVPOperateManager().readDetectBP(bleWriteResponse, new IBPSettingDataListener() {
            @Override
            public void onDataChange(BpSettingData bpSettingData) {
                handBloodData(bpSettingData);
            }
        });

    }

    private void handBloodData(BpSettingData bpSettingData) {
        highBload = bpSettingData.getHighPressure();
        lowBload = bpSettingData.getLowPressure();
        String hint = highBload + "/" + lowBload;
        bloodValueTv.setText(hint);
        hightBloadView.setSelectedPosition(hightBloadList.indexOf(highBload + ""));
        lowBloadView.setSelectedPosition(lowBloadList.indexOf(lowBload + ""));
        boolean privateBlood = bpSettingData.getModel() == EBPDetectModel.DETECT_MODEL_PRIVATE;
        privateBloadToggleBtn.setChecked(privateBlood);
    }

    private void initData() {
        hightBloadList = new ArrayList<>();
        lowBloadList = new ArrayList<>();
        for (int i = 80; i <= 209; i++) {
            hightBloadList.add(i + 1 + "");
        }
        for (int k = 46; k <= 179; k++) {
            lowBloadList.add(k + 1 + "");
        }
        hightBloadView.setData(hightBloadList);
        lowBloadView.setData(lowBloadList);
        hightBloadView.setOnSelectedListener(this);
        lowBloadView.setOnSelectedListener(this);
    }


    private void initViews() {
        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText(getResources().getString(R.string.string_blood_pressure_mode));
        privateBloadToggleBtn.setOnCheckedChangeListener(onCheckedChangeListener);


    }



    private void savePrivateBloodData() {
        if(BleConnStatus.CONNDEVICENAME == null)
            return;
        BpSetting bpSetting = new BpSetting(true, highBload, lowBload);
        BaseApplication.getVPOperateManager().settingDetectBP(bleWriteResponse, new IBPSettingDataListener() {
            @Override
            public void onDataChange(BpSettingData bpSettingData) {
                handBloodData(bpSettingData);
                ToastUtil.showShort(PrivageBloodActivity.this, getResources().getString(R.string.settings_success));
                finish();
            }
        }, bpSetting);
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(!buttonView.isPressed())
                return;
            privateBloadToggleBtn.setChecked(isChecked);
        }
    };

    @Override
    public void onSelected(ScrollPickerView scrollPickerView, int position) {
        int id = scrollPickerView.getId();
        if (id == R.id.hightBloadView) {   //高压
            highBload = Integer.valueOf(hightBloadList.get(position));
        } else if (id == R.id.lowBloadView) { //低压
            lowBload = Integer.valueOf(lowBloadList.get(position));
        }
    }

    private IBleWriteResponse bleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.commentackImg) {
            finish();
        } else if (id == R.id.b30SetPrivateBloadBtn) {
            savePrivateBloodData();
        }
    }
}
