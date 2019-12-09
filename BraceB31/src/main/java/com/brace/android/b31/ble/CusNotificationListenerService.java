package com.brace.android.b31.ble;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.service.notification.NotificationListenerService;

/**
 * Created by Admin
 * Date 2019/11/25
 */
@SuppressLint("OverrideAbstract")
public class CusNotificationListenerService extends NotificationListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
        toggleNotificationListenerService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        toggleNotificationListenerService();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Intent sevice = new Intent(this, BleMessageAlertService.class);
        sevice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startService(sevice);
        super.onDestroy();
    }

	
    /**
     * 被杀后再次启动时，监听不生效的问题
     */
    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(new ComponentName(this, BleMessageAlertService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, BleMessageAlertService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
