package com.brace.android.b31.bean;

/**
 * 每半小时心率详细数据
 * Created by Admin
 * Date 2019/11/5
 */
public class BraceHalfHeartBean {

    private String date;
    private CusVPTimeData time;
    private int rateValue;
    private int ecgCount = 0;
    private int ppgCount = 0;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CusVPTimeData getTime() {
        return time;
    }

    public void setTime(CusVPTimeData time) {
        this.time = time;
    }

    public int getRateValue() {
        return rateValue;
    }

    public void setRateValue(int rateValue) {
        this.rateValue = rateValue;
    }

    public int getEcgCount() {
        return ecgCount;
    }

    public void setEcgCount(int ecgCount) {
        this.ecgCount = ecgCount;
    }

    public int getPpgCount() {
        return ppgCount;
    }

    public void setPpgCount(int ppgCount) {
        this.ppgCount = ppgCount;
    }

    @Override
    public String toString() {
        return "BraceHalfHeartBean{" +
                "date='" + date + '\'' +
                ", time=" + time +
                ", rateValue=" + rateValue +
                ", ecgCount=" + ecgCount +
                ", ppgCount=" + ppgCount +
                '}';
    }
}
