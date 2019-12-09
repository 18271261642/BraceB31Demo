package com.brace.android.b31;


import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.brace.android.b31.ble.BleConnStatusService;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.SpUtils;
import com.veepoo.protocol.VPOperateManager;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;


/**
 * Created by Admin
 * Date 2019/10/30
 */
public  class BaseApplication extends Application {


    private static BaseApplication baseApplication;

    private static VPOperateManager vpOperateManager;

    private  BleConnStatusService bleConnStatusService;

    /**
     * 连接设备的Mac地址
     */
    private String bleMac = null;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;

        initData();

    }

    private void initData() {
        LitePal.initialize(baseApplication);
        bindBleConnService();
    }

    private void bindBleConnService() {
        Intent intent = new Intent(this,BleConnStatusService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);

    }


    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }


    public static VPOperateManager getVPOperateManager(){
        if(vpOperateManager == null){
            vpOperateManager = VPOperateManager.getMangerInstance(baseApplication);
        }
        return vpOperateManager;
    }


    public  BleConnStatusService getBleConnStatusService(){
        if(bleConnStatusService == null){
            bindBleConnService();
        }
        return bleConnStatusService;
    }


    /**
     * 获取设备的Mac地址
     * @return
     */
    public String getBleMac(){
        if(bleMac == null)
            bleMac = (String) SpUtils.getParam(baseApplication, Constant.CONN_BLE_MAC,"");
        return bleMac;
    }


    public void  setBleMac(String mac){
        SpUtils.setParam(baseApplication, Constant.CONN_BLE_MAC,mac);
    }



    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(service != null){
                bleConnStatusService = ((BleConnStatusService.B31Loader )service).getBleService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
