package com.brace.android.b31.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.adapter.SportDetailAdapter;
import com.brace.android.b31.bean.BraceCommB31Db;
import com.brace.android.b31.bean.BraceCommDbInstance;
import com.brace.android.b31.bean.BraceHalfHourSportBean;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.BraceUtils;
import com.brace.android.b31.view.widget.BraceCusStepDetailView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by Admin
 * Date 2019/11/28
 */
public class StepDetailActivity extends BaseActivity implements View.OnClickListener {


    ImageView commentackImg;
    TextView commentTitleTv;
    ImageView rateCurrDateLeft,rateCurrDateRight;
    TextView rateCurrdateTv;
    BraceCusStepDetailView braceCusSportView;
    RecyclerView sportDetailRecyclerView;

    private List<BraceHalfHourSportBean> halfHourSportBeanList;
    private SportDetailAdapter sportDetailAdapter;

    private List<Integer> setSourList;

    private Gson gson = new Gson();

    private String currDay = BraceUtils.getCurrentDate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail_layout);

        findViews();

        initViews();

        initData();

        findSportData(currDay);

    }

    private void findViews() {
        commentackImg = findViewById(R.id.commentackImg);
        commentTitleTv = findViewById(R.id.commentTitleTv);
        rateCurrDateRight = findViewById(R.id.rateCurrDateRight);
        rateCurrDateLeft = findViewById(R.id.rateCurrDateLeft);
        rateCurrdateTv = findViewById(R.id.rateCurrdateTv);
        braceCusSportView = findViewById(R.id.braceCusSportView);
        sportDetailRecyclerView = findViewById(R.id.sportDetailRecyclerView);

        commentackImg.setOnClickListener(this);
        rateCurrDateLeft.setOnClickListener(this);
        rateCurrDateRight.setOnClickListener(this);

    }

    private void initData() {
        setSourList = new ArrayList<>();
    }

    private void findSportData(String dayStr) {
        rateCurrdateTv.setText(dayStr);
        String bleMac = BaseApplication.getBaseApplication().getBleMac();
        if(bleMac == null)
            return;
        try {
            setSourList.clear();
            halfHourSportBeanList.clear();
            List<BraceCommB31Db> stepDeDbList = BraceCommDbInstance.getBraceCommDbInstance()
                    .findSavedDataForType(bleMac,dayStr, Constant.DB_TYPE_SPORT);
            if(stepDeDbList == null){
                braceCusSportView.setSourList(setSourList);
                sportDetailAdapter.notifyDataSetChanged();
                return;
            }

            BraceCommB31Db braceCommB31Db = stepDeDbList.get(0);
            String sportStr = braceCommB31Db.getDataSourceStr();
            List<BraceHalfHourSportBean> halfHourSportBeans = gson.fromJson(sportStr,new TypeToken<List<BraceHalfHourSportBean>>(){}.getType());

            halfHourSportBeanList.addAll(halfHourSportBeans);
            sportDetailAdapter.notifyDataSetChanged();


            Map<String,Object> sportMap = BraceUtils.setHalfDateMap();
            for(BraceHalfHourSportBean bs : halfHourSportBeans){
                sportMap.put(bs.getTime().getColck(),bs.getStepValue());
            }
            //遍历map的key
            Set set = sportMap.keySet();
            //转换为数组
            Object[] objects = set.toArray();
            if (objects == null)
                return;
            Arrays.sort(objects);
            for (Object ob : objects) {
                setSourList.add(Integer.valueOf(sportMap.get(ob) + ""));
            }
            braceCusSportView.setSourList(setSourList);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initViews() {
        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText(getResources().getString(R.string.string_move_ment));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        sportDetailRecyclerView.setLayoutManager(linearLayoutManager);
        sportDetailRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        halfHourSportBeanList = new ArrayList<>();
        sportDetailAdapter = new SportDetailAdapter(this,halfHourSportBeanList, R.layout.item_sport_healty_data_layout);
        sportDetailRecyclerView.setAdapter(sportDetailAdapter);


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
        findSportData(currDay);
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
