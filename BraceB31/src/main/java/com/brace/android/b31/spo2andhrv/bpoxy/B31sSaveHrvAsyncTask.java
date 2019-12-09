package com.brace.android.b31.spo2andhrv.bpoxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.spo2andhrv.model.B31HRVBean;
import com.brace.android.b31.utils.BraceUtils;
import com.google.gson.Gson;
import com.veepoo.protocol.model.datas.HRVOriginData;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin
 * Date 2019/9/25
 */
public class B31sSaveHrvAsyncTask extends AsyncTask<List<HRVOriginData>,Void,Void> {

    private Gson gson = new Gson();


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       BaseApplication.getBaseApplication().registerReceiver(broadcastReceiver,new IntentFilter(Constant.B31_HRV_COMPLETE));

    }

    @Override
    protected Void doInBackground(List<HRVOriginData>... lists) {
        if(isCancelled())return null;
        List<HRVOriginData> hrvOriginDataList = lists[0];
        if(hrvOriginDataList != null){
            saveB31sHrvData(hrvOriginDataList);
        }

        return null;
    }

    private void saveB31sHrvData(List<HRVOriginData> hrvOriginDataList) {
        List<B31HRVBean> list = new ArrayList<>();
        String bleMac = BaseApplication.getBaseApplication().getBleMac();
        String filePath = Environment.getExternalStorageDirectory()+"/DCIM/";
        for(HRVOriginData hrvOriginData : hrvOriginDataList){

            //new GetJsonDataUtil().writeTxtToFile(gson.toJson(hrvOriginData),filePath,"hrv.json");
            //只保存7点之前的
            int timeBefore = hrvOriginData.getmTime().getHMValue();
            //Log.e("HRV","--------hrvO="+timeBefore);
            if(timeBefore <= 420){
                //new GetJsonDataUtil().writeTxtToFile(gson.toJson(hrvOriginData),filePath,"hrv2.json");
                B31HRVBean hrvBean = new B31HRVBean();
                hrvBean.setBleMac(bleMac);
                hrvBean.setDateStr(hrvOriginData.getDate());
                hrvBean.setHrvDataStr(gson.toJson(hrvOriginData));
                list.add(hrvBean);
            }

        }

        int delHRV = LitePal.deleteAll(B31HRVBean.class, "dateStr=? and bleMac=?", BraceUtils.getCurrentDate()
                , bleMac);
        Log.e("SaveHRV","---------删除hrv="+delHRV);
        LitePal.saveAll(list);
        //发送广播，通知更新UI
        Intent intent = new Intent();
        intent.setAction(Constant.B31_HRV_COMPLETE);
        BaseApplication.getBaseApplication().sendBroadcast(intent);
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        if(broadcastReceiver != null)
           BaseApplication.getBaseApplication().unregisterReceiver(broadcastReceiver);
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
}
