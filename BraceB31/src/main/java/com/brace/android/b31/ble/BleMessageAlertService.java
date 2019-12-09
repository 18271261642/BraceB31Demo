package com.brace.android.b31.ble;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.service.notification.StatusBarNotification;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.SpUtils;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.model.enums.ESocailMsg;
import com.veepoo.protocol.model.settings.ContentSocailSetting;

/**
 * 消息提醒的服务
 * Created by Admin
 * Date 2019/11/25
 */
@SuppressLint("OverrideAbstract")
public class BleMessageAlertService extends CusNotificationListenerService {


    //QQ
    private static final String QQ_PACKAGENAME = "com.tencent.mobileqq";
    //微信
    private String WECHAT_PACKAGENAME = "com.tencent.mm";
    //Facebook
    private String FACEBOOK_PACKAGENAME = "com.facebook.katana";
    private String FACEBOOK_PACKAGENAME1 = "com.facebook.orca";

    //twitter
    private String TWITTER_PACKAGENAME = "com.twitter.android";
    //Whats
    private String WHATS_PACKAGENAME = "com.whatsapp";
    //viber
    private String VIBER_PACKAGENAME = "com.viber.voip";
    //instagram
    private  String INSTANRAM_PACKAGENAME = "com.instagram.android";
    //日历
    private String CALENDAR_PACKAGENAME = "com.android.calendar";
    //信息 三星手机信息
    private String SAMSUNG_MSG_PACKNAME = "com.samsung.android.messaging";
    private String SAMSUNG_MSG_SRVERPCKNAME = "com.samsung.android.communicationservice";
    private String MSG_PACKAGENAME = "com.android.mms";//短信
    private String SYS_SMS = "com.android.mms.service";//短信 --- vivo Y85A

    private String SKYPE_PACKAGENAME = "com.skype.raider";
    private  String SKYPE_PACKNAME = "com.skype.rover";
    //line
    private String LINE_PACKAGENAME = "jp.naver.line.android";

    //谷歌邮箱
    private String GMAIL_PACKAGENAME = "com.google.android.gm";
    //Snapchat：
    private String SNAP_PACKAGENAME = "com.snapchat.android";




    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        try {
            //获取应用包名
            String packageName = sbn.getPackageName();
            Notification notification = sbn.getNotification();
            if(notification == null)
                return;
            //获取消息内容----标题加内容
            CharSequence tickerText = notification.tickerText;
            if(tickerText == null)
                return;
            String msgCont = tickerText.toString();
            verticalAppName(packageName,msgCont);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void verticalAppName(String packageName, String msgCont) {

        //短信
        if(packageName.equals(MSG_PACKAGENAME) || packageName.equals(SYS_SMS) ||
                packageName.equals(SAMSUNG_MSG_PACKNAME) || packageName.equals(SAMSUNG_MSG_SRVERPCKNAME)){
            boolean isMsgOpen = (boolean) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.ISMsm,false);
            if(isMsgOpen)
                sendToDeviceMsg(ESocailMsg.SMS,"msg",msgCont);
            return;
        }

        //QQ
        if(packageName.equals(QQ_PACKAGENAME)){
            boolean isQQOpen = (boolean) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.ISQQ,false);
            if(isQQOpen)
                sendToDeviceMsg(ESocailMsg.QQ,"QQ",msgCont);
            return;
        }

        //微信
        if(packageName.equals(WECHAT_PACKAGENAME)){
            boolean isOpenWechat = (boolean) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.ISWechart,false);
            if(isOpenWechat)
                sendToDeviceMsg(ESocailMsg.WECHAT,"WeChat",msgCont);
            return;
        }
        //facebook
        if(packageName.equals(FACEBOOK_PACKAGENAME) || packageName.equals(FACEBOOK_PACKAGENAME1)){
            boolean isOpenFacebook = (boolean) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.ISFacebook,false);
            if(isOpenFacebook)
                sendToDeviceMsg(ESocailMsg.FACEBOOK,"facebook",msgCont);
            return;
        }

        //twitter
        if(packageName.equals(TWITTER_PACKAGENAME)){
            boolean isOpenTwitter = (boolean) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.ISTwitter,false);
            if(isOpenTwitter)
                sendToDeviceMsg(ESocailMsg.TWITTER,"twitter",msgCont);
            return;
        }

        //whatapp
        if(packageName.equals(WHATS_PACKAGENAME)){
            boolean isOpenWhatsapp = (boolean) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.ISWhatsApp,false);
            if(isOpenWhatsapp)
                sendToDeviceMsg(ESocailMsg.WHATS,"whatsapp",msgCont);
            return;
        }
        //INSTANRAM_PACKAGENAME
        if(packageName.equals(INSTANRAM_PACKAGENAME)){
            boolean isOpenInstan = (boolean) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.ISInstagram,false);
            if(isOpenInstan)
                sendToDeviceMsg(ESocailMsg.INSTAGRAM,"instagram",msgCont);
            return;
        }
        //skype
        if(packageName.equals(SKYPE_PACKAGENAME) ||  packageName.equals(SKYPE_PACKNAME)){
            boolean isOpenSkype = (boolean) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.ISSkype,false);
            if(isOpenSkype)
                sendToDeviceMsg(ESocailMsg.SKYPE,"skype",msgCont);
            return;
        }
        //line
        if(packageName.equals(LINE_PACKAGENAME)){
            boolean isOpenLine = (boolean) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.ISLINE,false);
            if(isOpenLine)
                sendToDeviceMsg(ESocailMsg.LINE,"line",msgCont);

            return;
        }

        //SNAP_PACKAGENAME
        if(packageName.equals(SNAP_PACKAGENAME)){
            boolean isOPenSnap = (boolean) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.ISSnapchart,false);
            if(isOPenSnap)
                sendToDeviceMsg(ESocailMsg.SNAPCHAT,"snapchat",msgCont);

            return;
        }

        //GMAIL_PACKAGENAME
        if(packageName.equals(GMAIL_PACKAGENAME)){
            boolean isOpenGmail = (boolean) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.ISGmail,false);
            if(isOpenGmail)
                sendToDeviceMsg(ESocailMsg.GMAIL,"gmail",msgCont);
            return;
        }
    }

    //推送消息
    private void sendToDeviceMsg(ESocailMsg appType, String appName, String msgCont) {
        if(BleConnStatus.CONNDEVICENAME == null)
            return;
        ContentSocailSetting contentSocailSetting = new ContentSocailSetting(appType, appName, msgCont);
        //ContentSetting contentSetting = new ContentSocailSetting(b30msg, 0, 20, appName, context);
        BaseApplication.getVPOperateManager().sendSocialMsgContent(iBleWriteResponse, contentSocailSetting);
    }

    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };
}
