package com.brace.android.b31.spo2andhrv.bpoxy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.spo2andhrv.model.B31Spo2hBean;
import com.brace.android.b31.utils.BraceUtils;
import com.google.gson.Gson;
import com.veepoo.protocol.model.datas.Spo2hOriginData;
import com.veepoo.protocol.model.enums.ESpo2hDataType;
import com.veepoo.protocol.util.Spo2hOriginUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**展示血氧的每个图表的详细数据
 * Created by Admin
 * Date 2019/1/5
 */
public class ShowSpo2DetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ShowSpo2DetailActivity";


    ImageView commentB30BackImg;
    TextView commentB30TitleTv;
    TextView commArrowDate;
    TextView showSpo2DetailLeftTv;
    TextView showSpo2DetailMiddleTv;
    TextView showSpo2DetailRightTv;
    RecyclerView showSpo2DetailRecyclerView;

    ImageView commArrowLeft,commArrowRight;

    private List<Spo2hOriginData> list = new ArrayList<>();
    private Gson gson = new Gson();
    private Spo2hOriginUtil spo2hOriginUtil;

    //需要的数据源
    private List<Map<String,Float>> resultListMap;
    //adapter
    private ShowSpo2DetailAdapter showSpo2DetailAdapter;

    int spo2Int;
    private String currDay;

    private Spo2SecondDialogView spo2SecondDialogView;


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            closeLoadDialog();
            List<Spo2hOriginData> resultList = (List<Spo2hOriginData>) msg.obj;
            showResultDesc(resultList,spo2Int);
        }
    };




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_spo2_detail_layout);


        findViews();

        initViews();

        initData();

        readLocalDeviceData(currDay);

    }

    private void findViews() {
        commentB30BackImg = findViewById(R.id.commentackImg);
        commentB30TitleTv = findViewById(R.id.commentTitleTv);
        commArrowDate = findViewById(R.id.commArrowDate);
        showSpo2DetailLeftTv = findViewById(R.id.showSpo2DetailLeftTv);
        showSpo2DetailMiddleTv = findViewById(R.id.showSpo2DetailMiddleTv);
        showSpo2DetailRightTv = findViewById(R.id.showSpo2DetailRightTv);
        showSpo2DetailRecyclerView = findViewById(R.id.showSpo2DetailRecyclerView);
        commArrowLeft = findViewById(R.id.commArrowLeft);
        commArrowRight = findViewById(R.id.commArrowRight);

        commentB30BackImg.setOnClickListener(this);
        commArrowLeft.setOnClickListener(this);
        commArrowRight.setOnClickListener(this);

    }

    private void initData() {
        String titleTxt = getIntent().getStringExtra("title");
        commentB30TitleTv.setText(titleTxt);
        String spo2Type = getIntent().getStringExtra("spo2_tag");
        if(BraceUtils.isEmpty(spo2Type)){
            spo2Int = 0;
        }
        spo2Int = Integer.valueOf(spo2Type);
        currDay = getIntent().getStringExtra(Constant.DETAIL_DATE);
        setTitleDesc(spo2Int);

    }


    //查询本地保存的数据
    private void readLocalDeviceData(final String currDay){
        commArrowDate.setText(currDay);
        showLoadDialog("Loading...");
        list.clear();
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                //查询保存的数据
                String whereStr = "bleMac = ? and dateStr = ?";
                String bleMac = BaseApplication.getBaseApplication().getBleMac();
                List<B31Spo2hBean> spo2hBeanList = LitePal.where(whereStr, bleMac, currDay).find(B31Spo2hBean.class);
                //Log.e(TAG,"---22------查询数据="+currDay+spo2hBeanList.size());
                if (spo2hBeanList == null || spo2hBeanList.isEmpty()) {
                    Message message = handler.obtainMessage();
                    message.what = 1001;
                    message.obj = list;
                    handler.sendMessage(message);
                    return;
                }
                for (B31Spo2hBean hBean : spo2hBeanList) {
                    //Log.e(TAG,"---------血氧单条数据="+hBean.toString());
                    list.add(gson.fromJson(hBean.getSpo2hOriginData(), Spo2hOriginData.class));
                }

                Message message = handler.obtainMessage();
                message.what = 1001;
                message.obj = list;
                handler.sendMessage(message);
            }
        };
        thread.start();
    }



    //展示结果
    private void showResultDesc(List<Spo2hOriginData> resultList, int tag) {
        resultListMap.clear();
        spo2hOriginUtil = new Spo2hOriginUtil(resultList);
        //获取每10分钟的数据
        List<Map<String, Float>> tenMinuteData = spo2hOriginUtil.getTenMinuteData(getSpo2Type(tag));
        resultListMap.addAll(tenMinuteData);
        showSpo2DetailAdapter.setSpowTag(tag);
        showSpo2DetailAdapter.notifyDataSetChanged();

    }

    @SuppressLint("SetTextI18n")
    private void setTitleDesc(int code){
        switch (code){
            case 0x00:     //血氧，呼吸暂停 cishu
                showSpo2DetailMiddleTv.setText(getResources().getString(R.string.vpspo2h_state_breathbreak)
                        +"("+getResources().getString(R.string.cishu)+")");
                showSpo2DetailRightTv.setText(getResources().getString(R.string.ave_value)+"(%)");

                break;
            case 0x01:     //心脏负荷
                showSpo2DetailMiddleTv.setText("");
                showSpo2DetailRightTv.setText(getResources().getString(R.string.ave_value)+"(pi)");
                break;
            case 0x02:     //睡眠活动
                showSpo2DetailMiddleTv.setText("");
                showSpo2DetailRightTv.setText(getResources().getString(R.string.accumulate));
                break;
            case 0x03:     //呼吸率
                showSpo2DetailMiddleTv.setText("");
                showSpo2DetailRightTv.setText(getResources().getString(R.string.ave_value)+getResources().getString(R.string.cishu)+"/"+getResources().getString(R.string.signle_minute));
                break;
            case 0x04:     //低氧时间
                showSpo2DetailMiddleTv.setText("");
                showSpo2DetailRightTv.setText(getResources().getString(R.string.accumulate)+getResources().getString(R.string.second)+"/"+getResources().getString(R.string.signle_minute));
                break;
        }
    }


    private ESpo2hDataType getSpo2Type(int tag){
        ESpo2hDataType eSpo2hDataType = null;
        switch (tag){
            case 0:     //血氧，呼吸暂停
                eSpo2hDataType =  ESpo2hDataType.TYPE_SPO2H;
                break;
            case 1:     //心脏负荷
                eSpo2hDataType =  ESpo2hDataType.TYPE_HEART;
                break;
            case 2:     //睡眠活动
                eSpo2hDataType =  ESpo2hDataType.TYPE_SLEEP;
                break;
            case 3:     //呼吸率
                eSpo2hDataType =  ESpo2hDataType.TYPE_BREATH;
                break;
            case 4:     //低氧时间
                eSpo2hDataType =  ESpo2hDataType.TYPE_LOWSPO2H;
                break;
        }
        return eSpo2hDataType;
    }



    private void initViews() {
        commentB30BackImg.setVisibility(View.VISIBLE);
        resultListMap = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        showSpo2DetailRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        showSpo2DetailRecyclerView.setLayoutManager(linearLayoutManager);
        showSpo2DetailAdapter = new ShowSpo2DetailAdapter(resultListMap, ShowSpo2DetailActivity.this);
        showSpo2DetailRecyclerView.setAdapter(showSpo2DetailAdapter);
        showSpo2DetailAdapter.setShowDialogItemClickListener(showDialogItemClickListener);

    }


    private void changeCurrDay(boolean isDay) {
        String date = BraceUtils.obtainAroundDate(currDay, isDay);
        if (date.equals(currDay) || date.isEmpty()) {
            return;// 空数据,或者大于今天的数据就别切了
        }
        currDay = date;
        readLocalDeviceData(currDay);

    }


    private ShowSpo2DetailAdapter.ShowDialogItemClickListener showDialogItemClickListener = new ShowSpo2DetailAdapter.ShowDialogItemClickListener() {
        @Override
        public void itemPosition(int position) {
            if(spo2SecondDialogView == null){
                spo2SecondDialogView = new Spo2SecondDialogView(ShowSpo2DetailActivity.this);
            }
            List<Map<String, Float>> lt = spo2hOriginUtil.getDetailList(getSpo2Type(spo2Int),resultListMap.size()-position-1);
            if(lt == null || lt.size() == 0)
                return;
            spo2SecondDialogView.show();
            spo2SecondDialogView.setSpo2Type(spo2Int);
            spo2SecondDialogView.setMapList(lt);
           // spo2SecondDialogView.setSporUtils(spo2hOriginUtil);
        }
    };


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.commentackImg) {    //返回
            finish();
        } else if (id == R.id.commArrowLeft) {    //向前切
            changeCurrDay(true);
        } else if (id == R.id.commArrowRight) {   //向后切
            changeCurrDay(false);
        }
    }
}
