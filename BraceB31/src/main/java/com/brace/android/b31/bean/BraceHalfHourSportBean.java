package com.brace.android.b31.bean;

/**
 * 步数详细数据，每半小时一条数据
 * Created by Admin
 * Date 2019/11/5
 */
public class BraceHalfHourSportBean {

    public String date;
    public CusVPTimeData time;
    public int stepValue;
    public int sportValue;
    private double disValue;
    private double calValue;

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

    public int getStepValue() {
        return stepValue;
    }

    public void setStepValue(int stepValue) {
        this.stepValue = stepValue;
    }

    public int getSportValue() {
        return sportValue;
    }

    public void setSportValue(int sportValue) {
        this.sportValue = sportValue;
    }

    public double getDisValue() {
        return disValue;
    }

    public void setDisValue(double disValue) {
        this.disValue = disValue;
    }

    public double getCalValue() {
        return calValue;
    }

    public void setCalValue(double calValue) {
        this.calValue = calValue;
    }

    @Override
    public String toString() {
        return "BraceHalfHourSportBean{" +
                "date='" + date + '\'' +
                ", time=" + time +
                ", stepValue=" + stepValue +
                ", sportValue=" + sportValue +
                ", disValue=" + disValue +
                ", calValue=" + calValue +
                '}';
    }
}
