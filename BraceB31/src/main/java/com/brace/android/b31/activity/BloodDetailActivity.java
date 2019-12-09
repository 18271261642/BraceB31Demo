package com.brace.android.b31.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.adapter.BloodDetailAdapter;
import com.brace.android.b31.bean.BraceCommDbInstance;
import com.brace.android.b31.bean.BraceHalfBpBean;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.BraceUtils;
import com.brace.android.b31.view.widget.BraceCusDetailBloodView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 详细血压页面
 * Created by Admin
 * Date 2019/11/8
 */
public class BloodDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "BloodDetailActivity";

    ImageView commentackImg,rateCurrDateLeft,rateCurrDateRight;
    TextView commentTitleTv;
    TextView rateCurrdateTv;
    BraceCusDetailBloodView cusDetailBloodView;
    RecyclerView bloodDetailRecyclerView;

    List<BraceHalfBpBean> halfBpBeanList ;
    private BloodDetailAdapter bloodDetailAdapter;

    private Gson gson = new Gson();

    //数据源
    private List<Map<String,Map<Integer,Integer>>> cusResultMap;
    //y轴的数据
    private List<Integer> yValueList = new ArrayList<>();

    private String currDay = BraceUtils.getCurrentDate();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_detail_layout);

        findViews();

        initViews();

        findDbBloodData(currDay);
    }

    private void findViews() {
        commentackImg = findViewById(R.id.commentackImg);
        commentTitleTv = findViewById(R.id.commentTitleTv);
        rateCurrdateTv = findViewById(R.id.rateCurrdateTv);
        cusDetailBloodView = findViewById(R.id.cusDetailBloodView);
        bloodDetailRecyclerView = findViewById(R.id.bloodDetailRecyclerView);
        rateCurrDateLeft = findViewById(R.id.rateCurrDateLeft);
        rateCurrDateRight = findViewById(R.id.rateCurrDateRight);

        commentackImg.setOnClickListener(this);
        rateCurrDateLeft.setOnClickListener(this);
        rateCurrDateRight.setOnClickListener(this);

    }


    private void findDbBloodData(String dayStr){
        rateCurrdateTv.setText(dayStr);
        halfBpBeanList.clear();
        String mac = BaseApplication.getBaseApplication().getBleMac();
        if(mac == null)
            return;
        try {
            String bloodStr = BraceCommDbInstance.getBraceCommDbInstance().findSingleOrigenData(mac, dayStr, Constant.DB_TYPE_BLOOD);
            if (bloodStr == null) {
                cusDetailBloodView.setResultMapData(new ArrayList<Map<String, Map<Integer, Integer>>>());
                halfBpBeanList.clear();
                bloodDetailAdapter.notifyDataSetChanged();
                return;
            }
            List<BraceHalfBpBean> bloodList = gson.fromJson(bloodStr, new TypeToken<List<BraceHalfBpBean>>() {
            }.getType());
            cusResultMap.clear();
            yValueList.clear();
            if (bloodList != null && bloodList.size() > 0) {
                halfBpBeanList.addAll(bloodList);
                bloodDetailAdapter.notifyDataSetChanged();

                for (BraceHalfBpBean halfHourBpData : bloodList) {
                    Map<Integer, Integer> mp = new ArrayMap<>();
                    mp.put(halfHourBpData.getLowValue(), halfHourBpData.getHighValue());

                    Map<String,Map<Integer,Integer>> mMap = new HashMap<>();
                    mMap.put(halfHourBpData.getTime().getColck(),mp);
                    cusResultMap.add(mMap);
                }
                cusDetailBloodView.setResultMapData(cusResultMap);

            } else {
                cusDetailBloodView.setResultMapData(cusResultMap);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void initViews() {
        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText(getResources().getString(R.string.string_blood_detail));
        cusResultMap = new ArrayList<>();

       // bloodDetailRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //linearLayoutManager.setSmoothScrollbarEnabled(true);

        bloodDetailRecyclerView.setLayoutManager(linearLayoutManager);
        bloodDetailRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        halfBpBeanList = new ArrayList<>();
        bloodDetailAdapter = new BloodDetailAdapter(BloodDetailActivity.this,halfBpBeanList, R.layout.item_sport_healty_data_layout);
        bloodDetailRecyclerView.setAdapter(bloodDetailAdapter);

    }


    /**
     * 根据日期切换数据
     */
    private void changeDayData(boolean left) {
        String date = BraceUtils.obtainAroundDate(currDay, left);
        if (date.equals(currDay) || date.isEmpty()) {
            return;
        }
        currDay = date;
        findDbBloodData(currDay);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.commentackImg) {
            finish();
        } else if (id == R.id.rateCurrDateLeft) {
            changeDayData(true);
        } else if (id == R.id.rateCurrDateRight) {
            changeDayData(false);
        }
    }
}
