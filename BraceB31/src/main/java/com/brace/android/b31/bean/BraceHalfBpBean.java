package com.brace.android.b31.bean;

/**
 * 每半小时血压数据
 * Created by Admin
 * Date 2019/11/5
 */
public class BraceHalfBpBean {
	

    private String date;
    private CusVPTimeData time;
    private int highValue;
    private int lowValue;

    public BraceHalfBpBean() {
    }

    public BraceHalfBpBean(String date, CusVPTimeData time, int highValue, int lowValue) {
        this.date = date;
        this.time = time;
        this.highValue = highValue;
        this.lowValue = lowValue;
    }

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

    public int getHighValue() {
        return highValue;
    }

    public void setHighValue(int highValue) {
        this.highValue = highValue;
    }

    public int getLowValue() {
        return lowValue;
    }

    public void setLowValue(int lowValue) {
        this.lowValue = lowValue;
    }

    @Override
    public String toString() {
        return "BraceHalfBpBean{" +
                "date='" + date + '\'' +
                ", time=" + time +
                ", highValue=" + highValue +
                ", lowValue=" + lowValue +
                '}';
    }
}
