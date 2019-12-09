package com.brace.android.b31.bean;

/**
 * Created by Admin
 * Date 2019/11/5
 */
public class SportBasicData {


    //当前汇总步数
    private int currSteps;

    //卡路里
    private double currKcal;

    //距离
    private double currDisance;

    public SportBasicData(int currSteps, double currKcal, double currDisance) {
        this.currSteps = currSteps;
        this.currKcal = currKcal;
        this.currDisance = currDisance;
    }


    public int getCurrSteps() {
        return currSteps;
    }

    public void setCurrSteps(int currSteps) {
        this.currSteps = currSteps;
    }

    public double getCurrKcal() {
        return currKcal;
    }

    public void setCurrKcal(double currKcal) {
        this.currKcal = currKcal;
    }

    public double getCurrDisance() {
        return currDisance;
    }

    public void setCurrDisance(double currDisance) {
        this.currDisance = currDisance;
    }

    @Override
    public String toString() {
        return "SportBasicData{" +
                "currSteps=" + currSteps +
                ", currKcal=" + currKcal +
                ", currDisance=" + currDisance +
                '}';
    }
}
