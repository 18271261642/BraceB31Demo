package com.brace.android.b31.activity.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.ble.BleConnStatus;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.SpUtils;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.ISocialMsgDataListener;
import com.veepoo.protocol.model.datas.FunctionSocailMsgData;
import com.veepoo.protocol.model.enums.EFunctionStatus;

/**
 * 消息提醒
 * Created by Admin
 * Date 2019/11/25
 */
public class MessageAlertActivity extends BaseActivity implements View.OnClickListener {

    ImageView commentackImg;
    TextView commentTitleTv;
    ToggleButton b31SkypeTogg;
    ToggleButton b31WhatsAppTogg;
    ToggleButton b31FacebookTogg;
    ToggleButton b31LinkedTogg;
    ToggleButton b31TwitterTogg;
    ToggleButton b31ViberTogg;
    ToggleButton b31LineTogg;
    ToggleButton b31SnapchartTogg;
    ToggleButton b31InstagramTogg;
    ToggleButton b31GmailTogg;
    ToggleButton b31WechatTogg;
    ToggleButton b31QQTogg;
    ToggleButton b31MessageTogg;
    ToggleButton b31PhoneTogg;
    ToggleButton b31OhterTogg;

    LinearLayout msgOpenNitLin;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeLoadDialog();
            setMsgSwitch();
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_alert_layout);

        findViews();

        initViews();

        readMsgStatus();

    }

    private void findViews() {
        commentackImg = findViewById(R.id.commentackImg);
        commentTitleTv = findViewById(R.id.commentTitleTv);
        b31SkypeTogg = findViewById(R.id.b31SkypeTogg);
        b31WhatsAppTogg = findViewById(R.id.b31WhatsAppTogg);
        b31FacebookTogg = findViewById(R.id.b31FacebookTogg);
        b31LinkedTogg = findViewById(R.id.b31LinkedTogg);
        b31TwitterTogg = findViewById(R.id.b31TwitterTogg);
        b31ViberTogg = findViewById(R.id.b31ViberTogg);
        b31LineTogg = findViewById(R.id.b31LineTogg);
        b31SnapchartTogg = findViewById(R.id.b31SnapchartTogg);
        b31InstagramTogg = findViewById(R.id.b31InstagramTogg);
        b31GmailTogg = findViewById(R.id.b31GmailTogg);
        b31WechatTogg = findViewById(R.id.b31WechatTogg);
        b31QQTogg = findViewById(R.id.b31QQTogg);
        b31MessageTogg = findViewById(R.id.b31MessageTogg);
        b31PhoneTogg = findViewById(R.id.b31PhoneTogg);
        b31OhterTogg = findViewById(R.id.b31OhterTogg);
        msgOpenNitLin = findViewById(R.id.msgOpenNitBtn);


    }

    private void readMsgStatus() {
        if(BleConnStatus.CONNDEVICENAME == null)
            return;
        BaseApplication.getVPOperateManager().readSocialMsg(iBleWriteResponse, new ISocialMsgDataListener() {
            @Override
            public void onSocialMsgSupportDataChange(FunctionSocailMsgData functionSocailMsgData) {
                if(functionSocailMsgData == null)
                    return;
                b31SkypeTogg.setChecked(functionSocailMsgData.getSkype() == EFunctionStatus.SUPPORT_OPEN);
                b31WhatsAppTogg.setChecked(functionSocailMsgData.getWhats() == EFunctionStatus.SUPPORT_OPEN);
                b31FacebookTogg.setChecked(functionSocailMsgData.getFacebook() == EFunctionStatus.SUPPORT_OPEN);
                b31LinkedTogg.setChecked(functionSocailMsgData.getLinkin() == EFunctionStatus.SUPPORT_OPEN);
                b31TwitterTogg.setChecked(functionSocailMsgData.getTwitter() == EFunctionStatus.SUPPORT_OPEN);
                //不支持viber
                //b31ViberTogg.setChecked();
                b31LineTogg.setChecked(functionSocailMsgData.getLine() == EFunctionStatus.SUPPORT_OPEN);
                b31SnapchartTogg.setChecked(functionSocailMsgData.getSnapchat() == EFunctionStatus.SUPPORT_OPEN);
                b31InstagramTogg.setChecked(functionSocailMsgData.getInstagram() == EFunctionStatus.SUPPORT_OPEN);
                b31GmailTogg.setChecked(functionSocailMsgData.getGmail() == EFunctionStatus.SUPPORT_OPEN);
                b31WechatTogg.setChecked(functionSocailMsgData.getWechat() == EFunctionStatus.SUPPORT_OPEN);
                b31QQTogg.setChecked(functionSocailMsgData.getQq() == EFunctionStatus.SUPPORT_OPEN);
                b31MessageTogg.setChecked(functionSocailMsgData.getMsg() == EFunctionStatus.SUPPORT_OPEN);
                b31PhoneTogg.setChecked(functionSocailMsgData.getPhone() == EFunctionStatus.SUPPORT_OPEN);
                b31OhterTogg.setChecked(functionSocailMsgData.getOther() == EFunctionStatus.SUPPORT_OPEN);

            }
        });
    }

    private void initViews() {
        commentTitleTv.setText("消息提醒");
        commentackImg.setVisibility(View.VISIBLE);
        b31SkypeTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31WhatsAppTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31FacebookTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31LinkedTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31TwitterTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31ViberTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31LineTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31SnapchartTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31InstagramTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31GmailTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31WechatTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31QQTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31MessageTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31PhoneTogg.setOnCheckedChangeListener(onCheckedChangeListener);
        b31OhterTogg.setOnCheckedChangeListener(onCheckedChangeListener);

        msgOpenNitLin.setOnClickListener(this);
        commentackImg.setOnClickListener(this);
    }



    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(!buttonView.isPressed())
                return;
            showLoadDialog("Loading...");
            int id = buttonView.getId();
            if (id == R.id.b31SkypeTogg) {
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISSkype, isChecked);
                b31SkypeTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31WhatsAppTogg) {
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISWhatsApp, isChecked);
                b31WhatsAppTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31FacebookTogg) {
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISFacebook, isChecked);
                b31FacebookTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31LinkedTogg) {
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISLinkendln, isChecked);
                b31LinkedTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31TwitterTogg) {
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISTwitter, isChecked);
                b31TwitterTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31ViberTogg) {
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISViber, isChecked);
                b31ViberTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31LineTogg) {
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISLINE, isChecked);
                b31LineTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31SnapchartTogg) {
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISSnapchart, isChecked);
                b31SnapchartTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31InstagramTogg) {
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISInstagram, isChecked);
                b31InstagramTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31GmailTogg) {
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISGmail, isChecked);
                b31GmailTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31WechatTogg) {
                requestPermiss(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISWechart, isChecked);
                b31WechatTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31QQTogg) {
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISQQ, isChecked);
                b31QQTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31MessageTogg) {   //短信
                requestPermiss(new String[]{Manifest.permission.READ_SMS});
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISMsm, isChecked);
                b31MessageTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31PhoneTogg) {     //电话
                requestPermiss(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS});
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISPhone, isChecked);
                b31PhoneTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            } else if (id == R.id.b31OhterTogg) {
                SpUtils.setParam(MessageAlertActivity.this, Constant.ISOhter, isChecked);
                b31OhterTogg.setChecked(isChecked);
                handler.sendEmptyMessage(0x01);
            }

        }
    };


    private void requestPermiss(String[] permiss){
        ActivityCompat.requestPermissions(MessageAlertActivity.this,permiss,1001);
    }



    //设置开关
    private void setMsgSwitch(){
        if(BleConnStatus.CONNDEVICENAME == null)
            return;
        boolean isSkeype = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISSkype,false);
        boolean isWhatsApp = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISWhatsApp,false);
        boolean isFaceBook = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISFacebook,false);
        boolean isLinked = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISLinkendln,false);
        boolean isTwitter = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISTwitter,false);
        boolean isViber = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISViber,false);
        boolean isLine = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISLINE,false);
        boolean isSnap = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISSnapchart,false);
        boolean isInstagram = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISInstagram,false);
        boolean isGmail = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISGmail,false);
        boolean isWechat = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISWechart,false);
        boolean isQQ = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISQQ,false);
        boolean isSmsg = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISMsm,false);
        boolean isPhone = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISPhone,false);
        boolean isOther = (boolean) SpUtils.getParam(MessageAlertActivity.this, Constant.ISOhter,false);

        FunctionSocailMsgData socailMsgData = new FunctionSocailMsgData();
        socailMsgData.setSkype(isSkeype?EFunctionStatus.SUPPORT_OPEN:EFunctionStatus.SUPPORT_CLOSE);
        socailMsgData.setWhats(isWhatsApp ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE);
        socailMsgData.setFacebook(isFaceBook ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE);
        socailMsgData.setLinkin(isLinked ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE);
        socailMsgData.setTwitter(isTwitter ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE);
        socailMsgData.setLine(isLine ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE);
        socailMsgData.setSnapchat(isSnap ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE);
        socailMsgData.setInstagram(isInstagram ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE);
        socailMsgData.setGmail(isGmail ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE);
        socailMsgData.setWechat(isWechat ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE);
        socailMsgData.setQq(isQQ ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE);
        socailMsgData.setMsg(isSmsg ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE);
        socailMsgData.setPhone(isPhone ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE);
        socailMsgData.setOther(isOther ? EFunctionStatus.SUPPORT_OPEN : EFunctionStatus.SUPPORT_CLOSE);

        BaseApplication.getVPOperateManager().settingSocialMsg(iBleWriteResponse, new ISocialMsgDataListener() {
            @Override
            public void onSocialMsgSupportDataChange(FunctionSocailMsgData functionSocailMsgData) {

            }
        }, socailMsgData);



    }


    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.commentackImg) {
            finish();
        } else if (id == R.id.msgOpenNitBtn) {
            Intent intentr = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivityForResult(intentr, 1001);
        }
    }
}
