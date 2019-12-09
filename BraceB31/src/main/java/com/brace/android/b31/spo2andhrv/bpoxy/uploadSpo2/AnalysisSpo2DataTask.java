package com.brace.android.b31.spo2andhrv.bpoxy.uploadSpo2;


import android.os.AsyncTask;
import android.util.Log;
import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.spo2andhrv.model.B31HRVBean;
import com.brace.android.b31.spo2andhrv.model.B31Spo2hBean;
import com.brace.android.b31.utils.BraceUtils;
import com.google.gson.Gson;
import com.veepoo.protocol.model.datas.HRVOriginData;
import com.veepoo.protocol.model.datas.Spo2hOriginData;
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.List;

/**保存Spo2数据
 * Created by Admin
 * Date 2019/12/7
 */
public class AnalysisSpo2DataTask extends AsyncTask<List<Spo2hOriginData>, Void, Void> {

    private static final String TAG = "AnalysisSpo2DataTask";

    private Gson gson = new Gson();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(List<Spo2hOriginData>... lists) {
        List<Spo2hOriginData> originDataList = lists[0];

        saveSpo2ToDbServer(originDataList);


        return null;
    }


    private void saveSpo2ToDbServer(List<Spo2hOriginData> spo2hOriginDataList) {
        List<B31Spo2hBean> lt = new ArrayList<>();
        String bleMac = BaseApplication.getBaseApplication().getBleMac();
        for (Spo2hOriginData spo2hOriginData : spo2hOriginDataList) {
            int spo2TimeBefore = spo2hOriginData.getmTime().getHMValue();
            if (spo2TimeBefore < 420) {
                B31Spo2hBean b31Spo2hBean = new B31Spo2hBean();
                b31Spo2hBean.setBleMac(bleMac);
                b31Spo2hBean.setDateStr(spo2hOriginData.getDate());
                b31Spo2hBean.setSpo2hOriginData(gson.toJson(spo2hOriginData));
                lt.add(b31Spo2hBean);
            }

        }

        int delSpo2 = LitePal.deleteAll(B31Spo2hBean.class, "dateStr=? and bleMac=?", BraceUtils.getCurrentDate()
                , bleMac);
        Log.e(TAG,"--------删除spo2="+delSpo2);

        LitePal.saveAll(lt);

    }


}
