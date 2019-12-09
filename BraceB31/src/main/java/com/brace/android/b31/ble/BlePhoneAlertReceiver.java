package com.brace.android.b31.ble;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.BraceUtils;
import com.brace.android.b31.utils.SpUtils;
import com.brace.android.b31.utils.ToastUtil;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.model.enums.ESocailMsg;
import com.veepoo.protocol.model.settings.ContentPhoneSetting;
import com.veepoo.protocol.model.settings.ContentSetting;

/**
 * 接收电话的广播
 * Created by Admin
 * Date 2019/11/29
 */
public class BlePhoneAlertReceiver extends BroadcastReceiver {


    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;



    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action == null)
            return;
        if(BleConnStatus.CONNDEVICENAME == null)
            return;
        if(action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
            sendPhoneStatus(context,intent);
        }
    }

    private void sendPhoneStatus(Context context, Intent intent) {
        String phoneNumber =  intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        if(phoneNumber == null)
            return;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(telephonyManager == null)
            return;
        int callStatus = telephonyManager.getCallState();
        switch (callStatus){
            case TelephonyManager.CALL_STATE_RINGING:   //来电，未接状态
                verticalPermiss(context,phoneNumber);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:   //通话中
            case TelephonyManager.CALL_STATE_IDLE:      //挂断电话
                disCallPhone();
                break;
        }

    }



    //挂断电话
    private void disCallPhone(){
        if(BleConnStatus.CONNDEVICENAME == null)
            return;
        BaseApplication.getVPOperateManager().offhookOrIdlePhone(iBleWriteResponse);
    }


    private void verticalPermiss(Context mContext,String phoneNumber){
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ToastUtil.showToast(mContext,"未获取权限!");
            return;
        }
        getPhoneContacts(mContext,phoneNumber);
    }


    //获取联系人
    private void getPhoneContacts(Context mContext,String phoneNumber) {
        try {
            ContentResolver resolver =mContext.getContentResolver();
            if(resolver == null){
                sendPhoneAlert(phoneNumber,"");
                return;
            }
            // 获取手机联系人
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
            if(phoneCursor == null){
                sendPhoneAlert(phoneNumber,"");
                return;
            }
            while (phoneCursor.moveToNext()){
                //得到手机号码
                String phoneNumberStr = phoneCursor.getString(PHONES_NUMBER_INDEX);
                if(BraceUtils.isEmpty(phoneNumberStr))
                    return;
                if (phoneNumberStr.contains("-")) {
                    phoneNumberStr = phoneNumberStr.replace("-", "");
                }
                if (phoneNumberStr.contains(" ")) {
                    phoneNumberStr = phoneNumberStr.replace(" ", "");
                }

                if(phoneNumber.equals(phoneNumberStr)){
                    String contactNames = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX) + "";
                    sendPhoneAlert(phoneNumberStr,phoneNumberStr);
                }
            }
            phoneCursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //推送提醒
    private void sendPhoneAlert(String number,String contact){
        try {
            boolean callPhone = (boolean) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.ISPhone, true);//来电
            if (!callPhone)
                return;
            ContentSetting contentSetting = new ContentPhoneSetting(ESocailMsg.PHONE, contact, number);
            BaseApplication.getVPOperateManager().sendSocialMsgContent(iBleWriteResponse, contentSetting);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };

}
