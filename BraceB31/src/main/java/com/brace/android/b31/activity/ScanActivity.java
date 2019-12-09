package com.brace.android.b31.activity;


import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.adapter.ScanDeviceAdapter;
import com.brace.android.b31.ble.BleConnStatus;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.SpUtils;
import com.brace.android.b31.view.ConnBleOperListener;
import com.brace.android.b31.view.CusInputEditView;
import com.brace.android.b31.view.OnItemClickListener;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.veepoo.protocol.VPOperateManager;

import java.util.ArrayList;
import java.util.List;


/**
 * 搜索页面
 * Created by Admin
 * Date 2019/10/30
 */
public class ScanActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    private static final String TAG = "ScanActivity";


    RecyclerView scanRecyclerView;

    SwipeRefreshLayout scanSwipeRefresh;

    TextView commentTitleTv;


    private List<BluetoothDevice> list;
    private ScanDeviceAdapter scanDeviceAdapter;

    private CusInputEditView cusInputEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_layout);



        initViews();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.DEVICE_CONNECT_ACTION);
        intentFilter.addAction(Constant.DEVICE_INPUT_PWD_CODE);
        registerReceiver(broadcastReceiver, intentFilter);

        requestPermiss();
    }


    private void requestPermiss() {
        if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
        } else {
            scanDevcie();
        }
    }

    private void initViews() {
        scanRecyclerView = findViewById(R.id.scanRecyclerView);
        scanSwipeRefresh = findViewById(R.id.scanSwipeRefresh);
        commentTitleTv = findViewById(R.id.commentTitleTv);

        commentTitleTv.setText("搜索设备");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scanRecyclerView.setLayoutManager(linearLayoutManager);
        scanRecyclerView.addItemDecoration(new DividerItemDecoration(ScanActivity.this,DividerItemDecoration.VERTICAL));
        list = new ArrayList<>();
        scanDeviceAdapter = new ScanDeviceAdapter(this, list);
        scanRecyclerView.setAdapter(scanDeviceAdapter);
        scanDeviceAdapter.setOnItemClickListener(this);
        scanSwipeRefresh.setOnRefreshListener(this);
    }


    //搜索设备
    private void scanDevcie() {
        list.clear();
        scanSwipeRefresh.setRefreshing(true);
        VPOperateManager.getMangerInstance(this).startScanDevice(new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult searchResult) {
                if (searchResult == null)
                    return;
                if (!list.contains(searchResult.device))
                    list.add(searchResult.device);
                scanDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSearchStopped() {
                scanSwipeRefresh.setRefreshing(false);
            }

            @Override
            public void onSearchCanceled() {
                scanSwipeRefresh.setRefreshing(false);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001)
            scanDevcie();


    }

    @Override
    public void onRefresh() {
        list.clear();
        requestPermiss();
    }


    @Override
    public void onIteClick(int position) {
        if (list.isEmpty())
            return;
        showLoadDialog("conn...");
        BaseApplication.getBaseApplication().getBleConnStatusService().connBleB31Device(list.get(position).getName(), list.get(position).getAddress(), "0000");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (broadcastReceiver != null)
                unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null)
                return;
            if (action.equals(Constant.DEVICE_INPUT_PWD_CODE)) {  //密码错误，输入密码
                closeLoadDialog();
                String bName = intent.getStringExtra("bleName");
                String bMac = intent.getStringExtra("bleMac");
                inputPwd(bName, bMac);
            }
            if (action.equals(Constant.DEVICE_CONNECT_ACTION)) {    //连接成功
                closeLoadDialog();
                BleConnStatus.isScannInto = true;
                startActivity(BraceHomeActivity.class);
                finish();
            }
        }
    };

    private void inputPwd(final String bleName, final String bleMac) {
		
		
        if (cusInputEditView == null)
            cusInputEditView = new CusInputEditView(ScanActivity.this);
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
                        requestPermiss();
                    }
                }, 2 * 1000);
            }

            @Override
            public void cusDialogSureData(final String data) {
                BaseApplication.getBaseApplication().getBleConnStatusService().continueConnBle(data, new ConnBleOperListener() {
                    @Override
                    public void onBleConnSuccess() {
                        cusInputEditView.dismiss();
                        SpUtils.setParam(ScanActivity.this, Constant.CONN_BLE_MAC, bleMac);
                        SpUtils.setParam(ScanActivity.this, Constant.CONN_BLE_NAME, bleName);
                        BaseApplication.getBaseApplication().setBleMac(bleMac);
                        SpUtils.setParam(ScanActivity.this, Constant.DEVICE_PWD_KEY, data);
                        BleConnStatus.isScannInto = true;
                        startActivity(BraceHomeActivity.class);
                        finish();
                    }

                    @Override
                    public void onBleConnErrorPwd() {
                        Toast.makeText(ScanActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
