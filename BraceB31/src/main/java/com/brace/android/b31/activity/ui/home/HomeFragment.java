package com.brace.android.b31.activity.ui.home;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.BloodDetailActivity;
import com.brace.android.b31.activity.HeartDetailActivity;
import com.brace.android.b31.activity.ManmualBloodActivity;
import com.brace.android.b31.activity.ManmualFitActivity;
import com.brace.android.b31.activity.ManmualHeartActivity;
import com.brace.android.b31.activity.ManmualRespiratoryRateActivity;
import com.brace.android.b31.activity.ManualSpo2Activity;
import com.brace.android.b31.activity.SleepDetailActivity;
import com.brace.android.b31.activity.SleepPrecisionDetailActivity;
import com.brace.android.b31.activity.StepDetailActivity;
import com.brace.android.b31.bean.BraceCommB31Db;
import com.brace.android.b31.bean.BraceCommDbInstance;
import com.brace.android.b31.bean.BraceHalfBpBean;
import com.brace.android.b31.bean.BraceHalfHeartBean;
import com.brace.android.b31.bean.BraceHalfHourSportBean;
import com.brace.android.b31.bean.BraceSleepBean;
import com.brace.android.b31.bean.SportBasicData;
import com.brace.android.b31.ble.BleConnDataOperate;
import com.brace.android.b31.ble.BleConnStatus;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.spo2andhrv.bpoxy.B31BpOxyAnysisActivity;
import com.brace.android.b31.spo2andhrv.bpoxy.util.ChartViewUtil;
import com.brace.android.b31.spo2andhrv.hrv.B31HrvDetailActivity;
import com.brace.android.b31.spo2andhrv.model.B31HRVBean;
import com.brace.android.b31.spo2andhrv.model.B31Spo2hBean;
import com.brace.android.b31.utils.BraceUtils;
import com.brace.android.b31.utils.SpUtils;
import com.brace.android.b31.view.LazyFragment;
import com.brace.android.b31.view.OnCurrentCountStepsListener;
import com.brace.android.b31.view.OnDataCompleteListener;
import com.brace.android.b31.view.widget.BraceCusBloodView;
import com.brace.android.b31.view.widget.BraceCusHeartView;
import com.brace.android.b31.view.widget.BraceCusSleepView;
import com.brace.android.b31.view.widget.BraceCusStepDetailView;
import com.brace.android.b31.view.widget.WaveProgress;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.veepoo.protocol.model.datas.HRVOriginData;
import com.veepoo.protocol.model.datas.Spo2hOriginData;
import com.veepoo.protocol.util.HRVOriginUtil;
import com.veepoo.protocol.util.HrvScoreUtil;
import com.veepoo.protocol.util.Spo2hOriginUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.brace.android.b31.spo2andhrv.bpoxy.enums.Constants.CHART_MAX_HRV;
import static com.brace.android.b31.spo2andhrv.bpoxy.enums.Constants.CHART_MAX_SPO2H;
import static com.brace.android.b31.spo2andhrv.bpoxy.enums.Constants.CHART_MIN_HRV;
import static com.brace.android.b31.spo2andhrv.bpoxy.enums.Constants.CHART_MIN_SPO2H;
import static com.veepoo.protocol.model.enums.ESpo2hDataType.TYPE_BEATH_BREAK;
import static com.veepoo.protocol.model.enums.ESpo2hDataType.TYPE_HRV;
import static com.veepoo.protocol.model.enums.ESpo2hDataType.TYPE_SPO2H;

/**
 * 数据主页面
 */
public class HomeFragment extends LazyFragment implements OnCurrentCountStepsListener, OnDataCompleteListener, View.OnClickListener {

    private static final String TAG = "HomeFragment";


    private final static int HEART_CODE = 0x01;
    private final static int BLOOD_CODE = 0x02;
    private final static int SPO2_CODE = 1001;
    private final static int HRV_CODE = 1002;


    private final static int SLEEP_GENERAL_CODE = 0x03;
    private final static int SLEEP_PRECISION_CODE = 0x04;


    View root;
    TextView commentTitleTv;
    ImageView commBackImg;
    WaveProgress b31ProgressBar;
    TextView goalStepTv;

    //血氧布局
    FrameLayout homeSpo2Frame;
    //hrv布局
    FrameLayout homeHrvFrame;
    //血压布局
    LinearLayout cusBloadLin;


    //心率图表
    BraceCusHeartView cusHeartChart;
    //睡眠图表
    BraceCusSleepView cusSleepView;
    //血压图表
    BraceCusBloodView cusBloodChart;
    //血氧图表
    LineChart homeSpo2LinChartView;
    TextView b31Spo2AveTv;
    //最近一次
    TextView lastTimeTv;
    TextView b30HeartValueTv;
    TextView b30StartEndTimeTv;
    //血压
    TextView bloadLastTimeTv;
    TextView b30BloadValueTv;
    TextView hrvHeartSocreTv;
    LineChart b31HomeHrvChart;
    BraceCusStepDetailView braceCusStepDView;
    TextView sportMaxNumTv;
    //连接状态
    TextView homeConnStatusTv;
    TextView homeDeviceMacTTv;


    //手动测量心率img
    ImageView homeB31ManHeartImg;
    //手动测量血压，部分设备不支持血压
    LinearLayout homeBpManLin;
    ImageView homeB31ManBpImg;
    //手动测量血氧
    ImageView homeB31ManBloadOxImg;
    //手动测量呼吸率
    ImageView homeB31ManFatigueImg;

    //手动测量呼吸率
    ImageView homeB31ManRespRateImg;


    private Context mContext = null;
    private Gson gson = new Gson();

    //点击
    View stepViewV,b31HrvView,b31BpOxyLin;
    LinearLayout cusSleepLin,cusHeartLin;

    //血压的集合map，key : 时间；value : 血压值map
    private List<Map<String, Map<Integer, Integer>>> resultBpMapList;


    //运动步数的list
    private List<Integer> sportList;


    //目标步数
    int goalStep;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HEART_CODE:    //心率
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        List<BraceHalfHeartBean> resultHtList = (List<BraceHalfHeartBean>) msg.obj;
                        showHeartView(resultHtList);
                    }
                    break;
                case BLOOD_CODE:    //血压
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        List<BraceHalfBpBean> btList = (List<BraceHalfBpBean>) msg.obj;
                        showBloodView(btList);
                    }
                    break;
                case SPO2_CODE:     //血氧
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        List<Spo2hOriginData> tmpLt = (List<Spo2hOriginData>) msg.obj;
                        updateSpo2View(tmpLt);

                    }
                    break;
                case HRV_CODE:  //hrv
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        List<HRVOriginData> tmpHrvList = (List<HRVOriginData>) msg.obj;
                        showHrvData(tmpHrvList);
                    }
                    break;
            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.DEVICE_CONNECT_ACTION);
        intentFilter.addAction(Constant.DEVICE_DISANCE_ACTION);
        intentFilter.addAction(Constant.B31_SPO2_COMPLETE);
        intentFilter.addAction(Constant.B31_HRV_COMPLETE);
        getmContext().registerReceiver(broadcastReceiver, intentFilter);

        goalStep = (int) SpUtils.getParam(getmContext(), Constant.DEVICE_SPORT_GOAL, 0);
        if (goalStep == 0) {
            goalStep = 8000;
            SpUtils.setParam(getmContext(), Constant.DEVICE_SPORT_GOAL, 8000);
        }

        //从设备读取数据的时间
        String saveDate = (String) SpUtils.getParam(getmContext(), "saveDate", "");
        if (BraceUtils.isEmpty(saveDate)) {
            SpUtils.setParam(getmContext(), "saveDate", System.currentTimeMillis() / 1000 + "");
        }

        BleConnDataOperate.getBleConnDataOperate().setCurrentCountStepsListener(this);
        BleConnDataOperate.getBleConnDataOperate().setOnDataCompleteListener(this);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        findViews(root);

        initViews();

        initData();

        return root;
    }

    private void findViews(View view) {
        commBackImg = view.findViewById(R.id.commentackImg);
        commentTitleTv = view.findViewById(R.id.commentTitleTv);
        b31ProgressBar = view.findViewById(R.id.b31ProgressBar);
        goalStepTv = view.findViewById(R.id.goalStepTv);
        cusHeartChart = view.findViewById(R.id.cusHeartChart);

        cusSleepView = view.findViewById(R.id.cusSleepView);
        cusBloodChart = view.findViewById(R.id.cusBloodChart);
        homeSpo2LinChartView = view.findViewById(R.id.homeSpo2LinChartView);
        b31Spo2AveTv = view.findViewById(R.id.b31Spo2AveTv);
        lastTimeTv = view.findViewById(R.id.lastTimeTv);
        b30HeartValueTv = view.findViewById(R.id.b30HeartValueTv);
        b30StartEndTimeTv = view.findViewById(R.id.b30StartEndTimeTv);
        bloadLastTimeTv =view.findViewById(R.id.bloadLastTimeTv);
        b30BloadValueTv = view.findViewById(R.id.b30BloadValueTv);
        hrvHeartSocreTv = view.findViewById(R.id.hrvHeartSocreTv);
        b31HomeHrvChart = view.findViewById(R.id.b31HomeHrvChart);
        braceCusStepDView = view.findViewById(R.id.braceCusStepDView);
        sportMaxNumTv = view.findViewById(R.id.sportMaxNumTv);
        homeConnStatusTv = view.findViewById(R.id.homeConnStatusTv);
        homeDeviceMacTTv = view.findViewById(R.id.homeDeviceMacTTv);

        stepViewV = view.findViewById(R.id.stepViewV);
        b31HrvView = view.findViewById(R.id.b31HrvView);
        b31BpOxyLin = view.findViewById(R.id.b31BpOxyLin);
        cusBloadLin = view.findViewById(R.id.cusBloadLin);
        cusSleepLin = view.findViewById(R.id.cusSleepLin);
        cusHeartLin = view.findViewById(R.id.CusHeartLin);

        homeSpo2Frame = view.findViewById(R.id.homeSpo2Frame);
        homeHrvFrame = view.findViewById(R.id.homeHrvFrame);

        homeB31ManHeartImg = view.findViewById(R.id.homeB31ManHeartImg);
        homeBpManLin = view.findViewById(R.id.homeBpManLin);
        homeB31ManBpImg = view.findViewById(R.id.homeB31ManBpImg);
        homeB31ManBloadOxImg = view.findViewById(R.id.homeB31ManBloadOxImg);
        homeB31ManFatigueImg = view.findViewById(R.id.homeB31ManFatigueImg);
        homeB31ManRespRateImg = view.findViewById(R.id.homeB31ManRespRateImg);

        commBackImg.setVisibility(View.GONE);

        stepViewV.setOnClickListener(this);
        b31HrvView.setOnClickListener(this);
        cusBloadLin.setOnClickListener(this);
        cusSleepLin.setOnClickListener(this);
        cusHeartLin.setOnClickListener(this);
        b31BpOxyLin.setOnClickListener(this);
        homeB31ManHeartImg.setOnClickListener(this);
        homeB31ManBpImg.setOnClickListener(this);
        homeB31ManBloadOxImg.setOnClickListener(this);
        homeB31ManFatigueImg.setOnClickListener(this);
        homeB31ManRespRateImg.setOnClickListener(this);
    }

    private void initData() {
        //是否支持血压
        boolean isSupportBp = (boolean) SpUtils.getParam(getmContext(),Constant.IS_SUPPORT_BP,false);
        homeBpManLin.setVisibility(isSupportBp ? View.VISIBLE : View.GONE);

        resultBpMapList = new ArrayList<>();
        sportList = new ArrayList<>();
    }

    private void initViews() {
        commentTitleTv.setText(BraceUtils.getCurrentDate());
        goalStepTv.setText(getResources().getString(R.string.target_step) +": " + goalStep);

        b31ProgressBar.setMaxValue(goalStep);
        b31ProgressBar.setValue(0);

        //是否支持血氧和HRV
        boolean isSupportSpo2 = (boolean) SpUtils.getParam(getmContext(),Constant.IS_SUPPORT_SPO2,false);
        homeSpo2Frame.setVisibility(isSupportSpo2?View.VISIBLE : View.GONE);
        homeHrvFrame.setVisibility(isSupportSpo2 ? View.VISIBLE : View.GONE);

        //是否支持血压
        boolean isSupportBp = (boolean) SpUtils.getParam(getmContext(),Constant.IS_SUPPORT_BP,false);
        cusBloadLin.setVisibility(isSupportBp ? View.VISIBLE : View.GONE);

    }


    private Context getmContext() {
        return mContext == null ? BaseApplication.getBaseApplication() : mContext;
    }


    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (!isVisible)
            return;
        goalStep = (int) SpUtils.getParam(getmContext(), Constant.DEVICE_SPORT_GOAL, 0);
        goalStepTv.setText(getResources().getString(R.string.target_step) +": " + goalStep);

        updatePagerData();

        if (BleConnStatus.CONNDEVICENAME == null) {
            homeConnStatusTv.setText(getResources().getString(R.string.disconnted));
            homeConnStatusTv.setTextColor(ContextCompat.getColor(getmContext(), R.color.red));
            return;
        }
        homeConnStatusTv.setText(getResources().getString(R.string.connted));
        homeConnStatusTv.setTextColor(ContextCompat.getColor(getmContext(), R.color.contents_text));
        homeDeviceMacTTv.setText("Mac: "+ BaseApplication.getBaseApplication().getBleMac());

        //保存的时间
        long currentTime = System.currentTimeMillis() / 1000;
        String tmpSaveTime = (String) SpUtils.getParam(getmContext(), "saveDate", currentTime + "");
        long diffTime = (currentTime - Long.valueOf(tmpSaveTime)) / 60;     //时间差
        if(BleConnStatus.isScannInto){  //从搜索页面进入的可以直接获取数据
            BleConnStatus.isScannInto = false;
            SpUtils.setParam(getmContext(), "saveDate", System.currentTimeMillis() / 1000 + "");
            //从设备中读取数据
            BleConnDataOperate.getBleConnDataOperate().syncUserInfoData(0, 170, 60, 25, 8000);
        }else{
            if (diffTime < 5)
                return;
            SpUtils.setParam(getmContext(), "saveDate", System.currentTimeMillis() / 1000 + "");
            //从设备中读取数据
            BleConnDataOperate.getBleConnDataOperate().getDeviceBasicData();
        }

    }


    private void updatePagerData() {
        String bleMac = BaseApplication.getBaseApplication().getBleMac();
        if (bleMac == null)
            return;
        updateStepsData(bleMac);
        updateDetailStep(bleMac);
        updateHeartData(bleMac);
        updateSleepData(bleMac);
        updateBloodData(bleMac);
        updateSpo2Data(bleMac);
        updateHrvData(bleMac);
    }

    //查询详细步数
    private void updateDetailStep(String bleMac) {
        sportList.clear();
        try {
            List<BraceCommB31Db> stepDeDbList = BraceCommDbInstance.getBraceCommDbInstance()
                    .findSavedDataForType(bleMac, BraceUtils.getCurrentDate(), Constant.DB_TYPE_SPORT);
            if (stepDeDbList == null) {
                sportMaxNumTv.setText("--");
                braceCusStepDView.setSourList(sportList);
                return;
            }

            BraceCommB31Db braceCommB31Db = stepDeDbList.get(0);
            String sportStr = braceCommB31Db.getDataSourceStr();
            List<BraceHalfHourSportBean> halfHourSportBeans = gson.fromJson(sportStr, new TypeToken<List<BraceHalfHourSportBean>>() {
            }.getType());

            Map<String, Object> sportMap = BraceUtils.setHalfDateMap();
            for (BraceHalfHourSportBean bs : halfHourSportBeans) {
                sportMap.put(bs.getTime().getColck(), bs.getStepValue());
            }
            //遍历map的key
            Set set = sportMap.keySet();
            //转换为数组
            Object[] objects = set.toArray();
            if (objects == null)
                return;
            Arrays.sort(objects);
            for (Object ob : objects) {
                sportList.add(Integer.valueOf(sportMap.get(ob) + ""));
            }
            braceCusStepDView.setSourList(sportList);
            sportMaxNumTv.setText(Collections.max(sportList) + "");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //查询hrv数据
    private void updateHrvData(final String bleMac) {
        final List<HRVOriginData> tmpHRVlist = new ArrayList<>();
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String where = "bleMac = ? and dateStr = ?";
                    List<B31HRVBean> reList = LitePal.where(where, bleMac, BraceUtils.getCurrentDate()).find(B31HRVBean.class);
                    if (reList == null || reList.isEmpty()) {
                        Message message = handler.obtainMessage();
                        message.what = HRV_CODE;
                        message.obj = tmpHRVlist;
                        handler.sendMessage(message);
                        return;
                    }
                    for (B31HRVBean hrvBean : reList) {
                        HRVOriginData hrvOriginData = gson.fromJson(hrvBean.getHrvDataStr(), HRVOriginData.class);
                        if (hrvOriginData == null)
                            return;
                        tmpHRVlist.add(hrvOriginData);

                    }

                    Message message = handler.obtainMessage();
                    message.what = HRV_CODE;
                    message.obj = tmpHRVlist;
                    handler.sendMessage(message);

                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //显示HRV的数据
    private void showHrvData(List<HRVOriginData> dataList) {
        Log.e(TAG, "----显示HRV=" + dataList.size());
        try {
//            if( dataList.size()>420)
//                return;
            List<HRVOriginData> data0to8 = getMoringData(dataList);
            HRVOriginUtil mHrvOriginUtil = new HRVOriginUtil(data0to8);
            HrvScoreUtil hrvScoreUtil = new HrvScoreUtil();
            int heartSocre = hrvScoreUtil.getSocre(dataList);
            hrvHeartSocreTv.setText(getResources().getString(R.string.heart_health_sorce) + "\n" + heartSocre);
            final List<Map<String, Float>> tenMinuteData = mHrvOriginUtil.getTenMinuteData();
            //主界面
            showHomeView(tenMinuteData);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //显示HRV的数据
    private void showHomeView(List<Map<String, Float>> tenMinuteData) {
        try {
            ChartViewUtil chartViewUtilHome = new ChartViewUtil(b31HomeHrvChart, null, true,
                    CHART_MAX_HRV, CHART_MIN_HRV, "No Data", TYPE_HRV);
            b31HomeHrvChart.getAxisLeft().removeAllLimitLines();
            b31HomeHrvChart.getAxisLeft().setDrawLabels(false);
            chartViewUtilHome.setxColor(Color.parseColor("#0e5986"));
            chartViewUtilHome.setNoDataColor(Color.parseColor("#0e5986"));
            chartViewUtilHome.drawYLable(false, 1);
            chartViewUtilHome.updateChartView(tenMinuteData);
            LineData data = b31HomeHrvChart.getData();
            if (data == null)
                return;
            LineDataSet dataSetByIndex = (LineDataSet) data.getDataSetByIndex(0);
            if (dataSetByIndex != null) {
                dataSetByIndex.setDrawFilled(false);
                dataSetByIndex.setColor(Color.parseColor("#EC1A3B"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //显示血氧的图
    private void updateSpo2View(List<Spo2hOriginData> dataList) {
        try {
            Log.e(TAG, "----------血氧展示=" + dataList.size());
//            if( dataList.size() > 420)
//                return;
            List<Spo2hOriginData> data0To8 = getSpo2MoringData(dataList);
            Spo2hOriginUtil spo2hOriginUtil = new Spo2hOriginUtil(data0To8);
            //获取处理完的血氧数据
            final List<Map<String, Float>> tenMinuteDataBreathBreak = spo2hOriginUtil.getTenMinuteData(TYPE_BEATH_BREAK);
            final List<Map<String, Float>> tenMinuteDataSpo2h = spo2hOriginUtil.getTenMinuteData(TYPE_SPO2H);
            //平均值
            int onedayDataArr[] = spo2hOriginUtil.getOnedayDataArr(TYPE_SPO2H);
            b31Spo2AveTv.setText(getResources().getString(R.string.ave_value) + "\n" + onedayDataArr[2]);
            if (getActivity() == null)
                return;
            ChartViewUtil spo2ChartViewUtilHomes = new ChartViewUtil(homeSpo2LinChartView, null, true,
                    CHART_MAX_SPO2H, CHART_MIN_SPO2H, getResources().getString(R.string.nodata), TYPE_SPO2H);
            spo2ChartViewUtilHomes.setxColor(Color.parseColor("#0e5986"));
            spo2ChartViewUtilHomes.setNoDataColor(Color.parseColor("#0e5986"));
            //更新血氧数据的图表
            spo2ChartViewUtilHomes.setBeathBreakData(tenMinuteDataBreathBreak);
            spo2ChartViewUtilHomes.updateChartView(tenMinuteDataSpo2h);
            spo2ChartViewUtilHomes.setBeathBreakData(tenMinuteDataBreathBreak);

            homeSpo2LinChartView.getAxisLeft().removeAllLimitLines();
            homeSpo2LinChartView.getAxisLeft().setDrawLabels(false);

            LineData data = homeSpo2LinChartView.getData();
            if (data == null)
                return;
            LineDataSet dataSetByIndex = (LineDataSet) data.getDataSetByIndex(0);
            if (dataSetByIndex != null) {
                dataSetByIndex.setDrawFilled(false);
                dataSetByIndex.setColor(Color.parseColor("#17AAE2"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //查询血氧数据
    private void updateSpo2Data(final String bleMac) {
        final List<Spo2hOriginData> spo2hOriginDataList = new ArrayList<>();
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String where = "bleMac = ? and dateStr = ?";
                    List<B31Spo2hBean> spo2hBeanList = LitePal.where(where, bleMac, BraceUtils.getCurrentDate()).find(B31Spo2hBean.class);
                    if (spo2hBeanList == null || spo2hBeanList.isEmpty()) {
                        Message message = handler.obtainMessage();
                        message.what = SPO2_CODE;
                        message.obj = spo2hOriginDataList;
                        handler.sendMessage(message);
                        return;
                    }
                    for (B31Spo2hBean hBean : spo2hBeanList) {
                        Spo2hOriginData spo2hOriginData = gson.fromJson(hBean.getSpo2hOriginData(), Spo2hOriginData.class);
                        if (spo2hOriginData == null)
                            return;
                        spo2hOriginDataList.add(spo2hOriginData);
                    }

                    Message message = handler.obtainMessage();
                    message.what = SPO2_CODE;
                    message.obj = spo2hOriginDataList;
                    handler.sendMessage(message);

                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询血压数据
    private void updateBloodData(String bleMac) {
        try {
            String bloodStr = BraceCommDbInstance.getBraceCommDbInstance().findSingleOrigenData(bleMac, BraceUtils.getCurrentDate(), Constant.DB_TYPE_BLOOD);
            if (bloodStr == null) {
                b30BloadValueTv.setText(getResources().getString(R.string.string_recent) +" --");
                cusBloodChart.setBpVerticalMap(resultBpMapList);
                cusBloodChart.setBpVerticalMap(new ArrayList<Map<String, Map<Integer, Integer>>>());
                return;
            }
            List<BraceHalfBpBean> bloodList = gson.fromJson(bloodStr, new TypeToken<List<BraceHalfBpBean>>() {
            }.getType());
            Message message = handler.obtainMessage();
            message.obj = bloodList;
            message.what = BLOOD_CODE;
            handler.sendMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询睡眠数据
    private void updateSleepData(String mac) {
        try {
            int deviceVersion = (int) SpUtils.getParam(getmContext(), Constant.DEVICE_VERSION_KEY, 0);

            List<BraceCommB31Db> sleepList = BraceCommDbInstance.getBraceCommDbInstance().findSavedDataForType(mac, BraceUtils.obtainAroundDate(BraceUtils.getCurrentDate(), false), deviceVersion == 0 ? Constant.DB_TYPE_GENERAL_SLEEP : Constant.DB_TYPE_PRECISION_SLEEP);
            if (sleepList == null) {
                cusSleepView.setPrecisionSleep(false);
                cusSleepView.setSleepList(new ArrayList<Integer>());
                b30StartEndTimeTv.setText("--");
                return;
            }
            BraceSleepBean braceSleepBean = gson.fromJson(sleepList.get(0).getDataSourceStr(), BraceSleepBean.class);
            //Log.e(TAG, "------睡眠=" + braceSleepBean.toString());
            String sleepLinStr = braceSleepBean.getSleepLine();
            List<Integer> sleepLt = new ArrayList<>();
            for (int i = 0; i < sleepLinStr.length(); i++) {
                int subStr = Integer.valueOf(sleepLinStr.substring(i, i + 1));
                sleepLt.add(subStr);
            }
            sleepLt.add(0, 2);
            sleepLt.add(0);
            sleepLt.add(2);
            cusSleepView.setSeekBarShow(false);
            cusSleepView.setPrecisionSleep(deviceVersion != 0);
            cusSleepView.setSleepList(sleepLt);
            b30StartEndTimeTv.setText(braceSleepBean.getSleepDown().getColck() + "-" + braceSleepBean.getSleepUp().getColck());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //心率
    private void updateHeartData(String mac) {
        try {
            List<BraceCommB31Db> heartList = BraceCommDbInstance.getBraceCommDbInstance().findSavedDataForType(mac, BraceUtils.getCurrentDate(), Constant.DB_TYPE_HEART);
            if (heartList == null) {
                cusHeartChart.setRateDataList(new ArrayList<Integer>());
                return;
            }

            List<BraceHalfHeartBean> htList = gson.fromJson(heartList.get(0)
                    .getDataSourceStr(), new TypeToken<List<BraceHalfHeartBean>>() {
            }.getType());
            Message hetMsg = handler.obtainMessage();
            hetMsg.what = HEART_CODE;
            hetMsg.obj = htList;
            handler.sendMessage(hetMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //更新总步数数据
    private void updateStepsData(String mac) {
        try {
            List<BraceCommB31Db> stepDbList = BraceCommDbInstance.getBraceCommDbInstance()
                    .findSavedDataForType(mac, BraceUtils.getCurrentDate(), Constant.DB_TYPE_SPTES);
            if (stepDbList == null)
                return;
            BraceCommB31Db setpDb = stepDbList.get(0);
            SportBasicData sportData = gson.fromJson(setpDb.getDataSourceStr(), SportBasicData.class);
            if (sportData == null)
                return;
            if (b31ProgressBar != null) {
                b31ProgressBar.setMaxValue(10000f);
                b31ProgressBar.setValue(sportData.getCurrSteps());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //显示心率图表
    private void showHeartView(List<BraceHalfHeartBean> resultHtList) {
        try {
            List<Integer> heartLt = new ArrayList<>();
            if (getActivity() == null || getActivity().isFinishing()) return;
            Map<String, Object> timeMap = BraceUtils.setHalfDateMap();
            for (BraceHalfHeartBean bHeart : resultHtList) {
                timeMap.put(bHeart.getTime().getColck(), bHeart.getRateValue());
            }

            Set set = timeMap.keySet();
            Object[] objects = set.toArray();
            if (objects == null)
                return;
            Arrays.sort(objects);
            for (Object ob : objects) {
                heartLt.add(Integer.valueOf(timeMap.get(ob) + ""));
            }
            cusHeartChart.setRateDataList(heartLt);
            lastTimeTv.setText(getResources().getString(R.string.string_recent) + resultHtList.get(resultHtList.size() - 1).getTime().getColck());
            b30HeartValueTv.setText(resultHtList.get(resultHtList.size() - 1).getRateValue() + " bpm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //展示血压数据图表
    @SuppressLint("SetTextI18n")
    private void showBloodView(List<BraceHalfBpBean> btList) {
        resultBpMapList.clear();
        try {
            for (BraceHalfBpBean bp : btList) {
                Map<String, Map<Integer, Integer>> mapMap = new HashMap<>();
                Map<Integer, Integer> mp = new HashMap<>();
                mp.put(bp.getLowValue(), bp.getHighValue());
                mapMap.put(bp.getTime().getColck(), mp);
                resultBpMapList.add(mapMap);
            }
            bloadLastTimeTv.setText(getResources().getString(R.string.string_recent) + btList.get(btList.size() - 1).getTime().getColck());
            b30BloadValueTv.setText(btList.get(btList.size() - 1).getHighValue() + "/" + btList.get(btList.size() - 1).getLowValue());
            cusBloodChart.setBpVerticalMap(resultBpMapList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (broadcastReceiver != null)
                getmContext().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //回调步数
    @Override
    public void currentCountSteps(SportBasicData sportBasicData) {
        if (b31ProgressBar != null) {
            b31ProgressBar.setMaxValue(10000f);
            b31ProgressBar.setValue(sportBasicData.getCurrSteps());
        }
        BleConnDataOperate.getBleConnDataOperate().readAllDeviceData(true);
    }

    //数据读取完了
    @Override
    public void dataReadComplete() {
        Log.e(TAG, "-------数据读取完了------");
        updatePagerData();
    }


    /**
     * 获取0点-8点之间的数据
     *
     * @param originSpo2hList
     * @return
     */
    @NonNull
    private List<Spo2hOriginData> getSpo2MoringData(List<Spo2hOriginData> originSpo2hList) {
        List<Spo2hOriginData> spo2Data = new ArrayList<>();
        try {
            if (originSpo2hList == null || originSpo2hList.isEmpty())
                return spo2Data;
            for (int i = 0; i < originSpo2hList.size(); i++) {
                Spo2hOriginData spo2hOriginData = originSpo2hList.get(i);
                if (spo2hOriginData != null && spo2hOriginData.getmTime() != null) {
                    if (spo2hOriginData.getmTime().getHMValue() < 8 * 60) {
                        spo2Data.add(spo2hOriginData);
                    }
                }
            }
            return spo2Data;
        } catch (Exception e) {
            e.printStackTrace();
            return spo2Data;
        }
    }


    /**
     * 获取0点-8点之间的数据
     *
     * @param originSpo2hList
     * @return
     */
    @NonNull
    private List<HRVOriginData> getMoringData(List<HRVOriginData> originSpo2hList) {
        List<HRVOriginData> moringData = new ArrayList<>();
        try {
            if (originSpo2hList == null || originSpo2hList.isEmpty())
                return moringData;
            for (HRVOriginData hRVOriginData : originSpo2hList) {
                if (hRVOriginData.getmTime().getHMValue() < 8 * 60) {
                    moringData.add(hRVOriginData);
                }
            }
            return moringData;
        } catch (Exception e) {
            e.printStackTrace();
            moringData.clear();
            return moringData;
        }

    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null)
                return;
            if (action.equals(Constant.DEVICE_CONNECT_ACTION)) {  //连接成功
                homeConnStatusTv.setText(getResources().getString(R.string.connted));
                homeConnStatusTv.setTextColor(ContextCompat.getColor(getmContext(), R.color.contents_text));
                homeDeviceMacTTv.setText("Mac: "+ BaseApplication.getBaseApplication().getBleMac());
                //从设备中读取数据
                BleConnDataOperate.getBleConnDataOperate().syncUserInfoData(0, 170, 60, 25, 8000);

            }
            if (action.equals(Constant.DEVICE_DISANCE_ACTION)) {  //断开连接
                homeConnStatusTv.setText(getResources().getString(R.string.disconnted));
                homeConnStatusTv.setTextColor(ContextCompat.getColor(getmContext(), R.color.red));
                homeDeviceMacTTv.setText("");
            }

            if(action.equals(Constant.B31_HRV_COMPLETE)){   //显示HRV
                String bMac = BaseApplication.getBaseApplication().getBleMac();
                if(BraceUtils.isEmpty(bMac))
                    return;
                updateHrvData(bMac);
            }

            if(action.equals(Constant.B31_SPO2_COMPLETE)){
                String bMac = BaseApplication.getBaseApplication().getBleMac();
                if(BraceUtils.isEmpty(bMac))
                    return;
                updateSpo2Data(bMac);
            }
        }
    };

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.stepViewV) {    //步数详情
            startActivity(new Intent(getmContext(), StepDetailActivity.class));
        } else if (id == R.id.CusHeartLin) { //心率详情
            startActivity(new Intent(getmContext(), HeartDetailActivity.class));
        } else if (id == R.id.cusSleepLin) {  //睡眠
            int deviceVersion = (int) SpUtils.getParam(getmContext(), Constant.DEVICE_VERSION_KEY, 0);
            startActivity(new Intent(getmContext(), deviceVersion == 3 ? SleepPrecisionDetailActivity.class : SleepDetailActivity.class));
        } else if (id == R.id.cusBloadLin) {  //血压
            startActivity(new Intent(getmContext(), BloodDetailActivity.class));
        } else if (id == R.id.b31BpOxyLin) {    //血氧
            startActivity(new Intent(getmContext(), B31BpOxyAnysisActivity.class));
        } else if (id == R.id.b31HrvView) { //hrv
            startActivity(new Intent(getmContext(), B31HrvDetailActivity.class));
        }else if(id == R.id.homeB31ManHeartImg){    //手动测量心率
            startActivity(new Intent(getmContext(), ManmualHeartActivity.class));
        }else if(id == R.id.homeB31ManBpImg){   //手动测量血压
             startActivity(new Intent(getmContext(), ManmualBloodActivity.class));
        }else if(id == R.id.homeB31ManBloadOxImg){  //手动测量血氧
            startActivity(new Intent(getmContext(), ManualSpo2Activity.class));
        }else if(id == R.id.homeB31ManFatigueImg){  //疲劳度检测
            startActivity(new Intent(getmContext(), ManmualFitActivity.class));
        }else if(id == R.id.homeB31ManRespRateImg){     //呼吸率检测
            startActivity(new Intent(getmContext(), ManmualRespiratoryRateActivity.class));
        }
    }

}