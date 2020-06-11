package com.android.braceb31demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.activity.BraceHomeActivity;
import com.brace.android.b31.ble.BleConnDataOperate;
import com.brace.android.b31.ble.BleConnStatus;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.SpUtils;
import com.brace.android.b31.view.ConnBleOperListener;
import com.brace.android.b31.view.CusInputEditView;

import java.util.ArrayList;
import java.util.List;

/**
 * own scan
 * Created by Admin
 * Date 2019/12/9
 */
public class OwnScanActivity extends BaseActivity implements OwnScanAdapter.OwnOnItemClickListener{

    private List<BluetoothDevice> list;
    private OwnScanAdapter adapter;
    private RecyclerView recyclerView;
    private BluetoothAdapter bluetoothAdapter;

    //输入密码的view
    private CusInputEditView cusInputEditView;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.removeMessages(0x02);
            if(bluetoothAdapter != null)
                bluetoothAdapter.stopLeScan(leScanCallback);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_scan_layout);

        initViews();

        regiBroadCast();

        requestPermis();


        setUserInfoData();

        scanDevice();
    }

    //设置用户信息
    private void setUserInfoData() {
        /**
         *
         * @param eSexCode  性别 0-男；1女
         * @param userHeight    身高 - cm
         * @param userWeight    体重 - kg
         * @param userAge   年龄 - integer
         * @param goalStep  目标步数 - integer
         */
        //设置目标步数
        SpUtils.setParam(OwnScanActivity.this,Constant.DEVICE_SPORT_GOAL,10000);
        BleConnDataOperate.getBleConnDataOperate().setBasicMsgData(0,175,60,25);
    }

    //注册广播
    private void regiBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.DEVICE_CONNECT_ACTION);
        intentFilter.addAction(Constant.DEVICE_INPUT_PWD_CODE);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    private void requestPermis() {
        boolean isGet = ContextCompat.checkSelfPermission(OwnScanActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if(isGet){
            ActivityCompat.requestPermissions(OwnScanActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0x01);
           return;
        }

        scanDevice();
    }

    private void scanDevice() {
        if(bluetoothAdapter == null)
            return;
        bluetoothAdapter.startLeScan(leScanCallback);
        handler.sendEmptyMessageDelayed(0x02,15 * 1000);
    }


    private void initViews() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if(bluetoothManager != null){
            bluetoothAdapter = bluetoothManager.getAdapter();
        }
        recyclerView = findViewById(R.id.ownScanRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(OwnScanActivity.this,DividerItemDecoration.VERTICAL));
        list = new ArrayList<>();
        adapter = new OwnScanAdapter(list,OwnScanActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

    }

    //扫描回调
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if(device == null)
                return;
            if(list.contains(device))
                return;
            if(list.size()>40)
                return;
            list.add(device);
            adapter.notifyDataSetChanged();
        }
    };



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0x01)
            requestPermis();

    }

    @Override
    public void onPositionItem(int position) {
        if(bluetoothAdapter != null &&bluetoothAdapter.isDiscovering())
            bluetoothAdapter.startLeScan(leScanCallback);
        BluetoothDevice bd = list.get(position);
        if(bd.getName() == null || bd.getAddress() == null)
            return;
        // start 进度条
        showLoadDialog("conn...");
        //开始连接，bleName,bleMac,pwd , pwd默认0000
        BaseApplication.getBaseApplication().getBleConnStatusService().connBleB31Device(bd.getName(),bd.getAddress(),"0000");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
           if(broadcastReceiver != null)
               unregisterReceiver(broadcastReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action == null)
                return;
            if (action.equals(Constant.DEVICE_INPUT_PWD_CODE)) {  //密码错误，输入密码
                // close 进度条
                closeLoadDialog();
                String bName = intent.getStringExtra("bleName");
                String bMac = intent.getStringExtra("bleMac");
                inputPwd(bName, bMac);
            }
            if (action.equals(Constant.DEVICE_CONNECT_ACTION)) {    //连接成功
                // close 进度条
                closeLoadDialog();
                BleConnDataOperate.getBleConnDataOperate().syncUserInfoData();

                BleConnStatus.isScannInto = true;
                startActivity(new Intent(OwnScanActivity.this,BraceHomeActivity.class));
                finish();
            }
        }
    };


    //输入密码
    private void inputPwd(final String bleName, final String bleMac) {

        if (cusInputEditView == null)
            cusInputEditView = new CusInputEditView(OwnScanActivity.this);
        cusInputEditView.show();
        cusInputEditView.setCancelable(false);
        cusInputEditView.setCusInputDialogListener(new CusInputEditView.CusInputDialogListener() {
            @Override
            public void cusDialogCancle() {     //取消就断开操作，再次搜索
                cusInputEditView.dismiss();
                BaseApplication.getBaseApplication().getBleConnStatusService().disBleConn();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestPermis();
                    }
                }, 2 * 1000);
            }

            @Override
            public void cusDialogSureData(final String data) {
                BaseApplication.getBaseApplication().getBleConnStatusService().continueConnBle(data, new ConnBleOperListener() {
                    @Override
                    public void onBleConnSuccess() {
                        cusInputEditView.dismiss();
                        SpUtils.setParam(OwnScanActivity.this, Constant.CONN_BLE_MAC, bleMac);
                        SpUtils.setParam(OwnScanActivity.this, Constant.CONN_BLE_NAME, bleName);
                        BaseApplication.getBaseApplication().setBleMac(bleMac);
                        SpUtils.setParam(OwnScanActivity.this, Constant.DEVICE_PWD_KEY, data);
                        BleConnStatus.isScannInto = true;
                        startActivity(new Intent(OwnScanActivity.this,BraceHomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onBleConnErrorPwd() {
                        Toast.makeText(OwnScanActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
