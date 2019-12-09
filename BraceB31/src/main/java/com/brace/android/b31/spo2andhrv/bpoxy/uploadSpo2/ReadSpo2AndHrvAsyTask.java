package com.brace.android.b31.spo2andhrv.bpoxy.uploadSpo2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.spo2andhrv.model.B31HRVBean;
import com.brace.android.b31.spo2andhrv.model.B31Spo2hBean;
import com.brace.android.b31.utils.BraceUtils;
import com.google.gson.Gson;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IHRVOriginDataListener;
import com.veepoo.protocol.listener.data.ISpo2hOriginDataListener;
import com.veepoo.protocol.model.datas.HRVOriginData;
import com.veepoo.protocol.model.datas.Spo2hOriginData;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取血氧和HRV
 * Created by Admin
 * Date 2019/11/26
 */
public class ReadSpo2AndHrvAsyTask extends AsyncTask<Void,Void,Void> {

    private String bleMac = null;
    private Gson gson = new Gson();
    //HRV
    private List<B31HRVBean> b31HRVBeanList;
    private List<B31Spo2hBean> b31Spo2hBeanList;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        bleMac = BaseApplication.getBaseApplication().getBleMac();
        b31HRVBeanList = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        readSpo2Data();

        readHrvData();
        return null;
    }

    private void readHrvData() {
        if(bleMac == null)
            return;
        if(b31HRVBeanList == null)
            b31HRVBeanList = new ArrayList<>();
        b31HRVBeanList.clear();
        final String where = "bleMac = ? and dateStr = ?";

        BaseApplication.getVPOperateManager().readHRVOrigin(iBleWriteResponse, new IHRVOriginDataListener() {
            @Override
            public void onReadOriginProgress(float v) {
                if(String.valueOf(v).equals("1.0")){
                    List<B31HRVBean> currHrvLt = LitePal.where(where,
                            bleMac, BraceUtils.getCurrentDate()).find(B31HRVBean.class);
                    if(currHrvLt == null || currHrvLt.isEmpty()){
                        LitePal.saveAll(b31HRVBeanList);
                    }else{
                        if(currHrvLt.size() != 420){
                            LitePal.deleteAll(B31HRVBean.class, "dateStr=? and bleMac=?", BraceUtils.getCurrentDate()
                                    , bleMac);
                            LitePal.saveAll(b31HRVBeanList);
                        }
                    }
                }

                sendCompleteBraodCast(Constant.B31_HRV_COMPLETE);
            }

            @Override
            public void onReadOriginProgressDetail(int i, String s, int i1, int i2) {

            }

            @Override
            public void onHRVOriginListener(HRVOriginData hrvOriginData) {
                B31HRVBean hrvBean = new B31HRVBean();
                hrvBean.setBleMac(bleMac);
                hrvBean.setDateStr(hrvOriginData.getDate());
                hrvBean.setHrvDataStr(gson.toJson(hrvOriginData));
                b31HRVBeanList.add(hrvBean);
            }

            @Override
            public void onDayHrvScore(int i, String s, int i1) {

            }

            @Override
            public void onReadOriginComplete() {

            }
        }, 1);
    }


    private void readSpo2Data() {
        if(bleMac == null)
            return;
        if(b31Spo2hBeanList == null)
            b31Spo2hBeanList = new ArrayList<>();
        b31Spo2hBeanList.clear();


        final String where = "bleMac = ? and dateStr = ?";

        BaseApplication.getVPOperateManager().readSpo2hOrigin(iBleWriteResponse, new ISpo2hOriginDataListener() {
            @Override
            public void onReadOriginProgress(float v) {
                if (String.valueOf(v).equals("1.0")){
                    List<B31Spo2hBean> currList = LitePal.where(where, bleMac,
                            BraceUtils.getCurrentDate()).find(B31Spo2hBean.class);
                    if(currList == null || currList.isEmpty()){
                        LitePal.saveAll(b31Spo2hBeanList);
                    }else{
                        if(currList.size() != 420){
                            LitePal.deleteAll(B31Spo2hBean.class, "dateStr=? and bleMac=?", BraceUtils.getCurrentDate()
                                    , bleMac);
                            LitePal.saveAll(b31Spo2hBeanList);
                        }

                    }

                }

            }

            @Override
            public void onReadOriginProgressDetail(int i, String s, int i1, int i2) {

            }

            @Override
            public void onSpo2hOriginListener(Spo2hOriginData spo2hOriginData) {
                if(spo2hOriginData == null)
                    return;
                B31Spo2hBean b31Spo2hBean = new B31Spo2hBean();
                b31Spo2hBean.setBleMac(bleMac);
                b31Spo2hBean.setDateStr(spo2hOriginData.getDate());
                b31Spo2hBean.setSpo2hOriginData(gson.toJson(spo2hOriginData));
                b31Spo2hBeanList.add(b31Spo2hBean);
            }

            @Override
            public void onReadOriginComplete() {

            }
        },1 );
    }

    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };



    private void sendCompleteBraodCast(String action){
        Intent intent = new Intent();
        intent.setAction(action);
        BaseApplication.getBaseApplication().sendBroadcast(intent);
    }


}
