package com.brace.android.b31.bean;

import com.veepoo.protocol.model.datas.InsomniaTimeData;

import java.util.List;

/**
 * Created by Admin
 * Date 2019/11/6
 */
public class BracePrecisionSleepBean extends BraceSleepBean {

    //睡眠标志
    private int sleepTag;
    //起夜得分
    private int getUpScore;
    //深睡夜得分
    private int deepScore;
    //睡眠效率得分，起夜到深睡的效率得分
    private int sleepEfficiencyScore;
    //入睡效率得分，从开始睡眠到第一次进入深睡的效率
    private int fallAsleepScore;
    //睡眠时长得分
    private int sleepTimeScore;
    //退出睡眠的模式
    private int exitSleepMode;
    //深浅睡眠模式
    private int deepAndLightMode;
    //其他睡眠时间长，单位分钟--快速眼动时长
    private int otherDuration;
    //第一次进入深睡时间
    private int firstDeepDuration;
    //起夜总时长，单位分钟
    private int getUpDuration;
    //起夜到深睡时间的平均值
    private int getUpToDeepAve;
    //曲线上一个点代表的时间，单位秒，现在是60s
    private int onePointDuration;
    //睡眠类型
    private int accurateType;
    //失眠标志
    private int insomniaTag;
    //失眠得分
    private int insomniaScore;
    //失眠次数
    private int insomniaTimes;
    //失眠长度
    private int insomniaLength;
    //失眠列表
    List<InsomniaTimeData> insomniaBeanList;
    //失眠开始时间
    private String startInsomniaTime;
    //失眠结束时间
    private String stopInsomniaTime;
    //通过睡眠曲线3的个数来计算,单位是分钟
    private int insomniaDuration;
    //睡眠原始字符串
    private String sleepSourceStr;
    // 上一段标志位，默认为0(表示肯定没有上一段)，如果为1，则表示有上一段睡眠,否则表示没有上一段睡眠
    private int laster;
    // 下一段睡眠标志位，默认为255(表示不确定是否有下一段)，如果为1，则表示有下一段睡眠,否则表示没有下一段睡眠
    private int next;


    public int getSleepTag() {
        return sleepTag;
    }

    public void setSleepTag(int sleepTag) {
        this.sleepTag = sleepTag;
    }

    public int getGetUpScore() {
        return getUpScore;
    }

    public void setGetUpScore(int getUpScore) {
        this.getUpScore = getUpScore;
    }

    public int getDeepScore() {
        return deepScore;
    }

    public void setDeepScore(int deepScore) {
        this.deepScore = deepScore;
    }

    public int getSleepEfficiencyScore() {
        return sleepEfficiencyScore;
    }

    public void setSleepEfficiencyScore(int sleepEfficiencyScore) {
        this.sleepEfficiencyScore = sleepEfficiencyScore;
    }

    public int getFallAsleepScore() {
        return fallAsleepScore;
    }

    public void setFallAsleepScore(int fallAsleepScore) {
        this.fallAsleepScore = fallAsleepScore;
    }

    public int getSleepTimeScore() {
        return sleepTimeScore;
    }

    public void setSleepTimeScore(int sleepTimeScore) {
        this.sleepTimeScore = sleepTimeScore;
    }

    public int getExitSleepMode() {
        return exitSleepMode;
    }

    public void setExitSleepMode(int exitSleepMode) {
        this.exitSleepMode = exitSleepMode;
    }

    public int getDeepAndLightMode() {
        return deepAndLightMode;
    }

    public void setDeepAndLightMode(int deepAndLightMode) {
        this.deepAndLightMode = deepAndLightMode;
    }

    public int getOtherDuration() {
        return otherDuration;
    }

    public void setOtherDuration(int otherDuration) {
        this.otherDuration = otherDuration;
    }

    public int getFirstDeepDuration() {
        return firstDeepDuration;
    }

    public void setFirstDeepDuration(int firstDeepDuration) {
        this.firstDeepDuration = firstDeepDuration;
    }

    public int getGetUpDuration() {
        return getUpDuration;
    }

    public void setGetUpDuration(int getUpDuration) {
        this.getUpDuration = getUpDuration;
    }

    public int getGetUpToDeepAve() {
        return getUpToDeepAve;
    }

    public void setGetUpToDeepAve(int getUpToDeepAve) {
        this.getUpToDeepAve = getUpToDeepAve;
    }

    public int getOnePointDuration() {
        return onePointDuration;
    }

    public void setOnePointDuration(int onePointDuration) {
        this.onePointDuration = onePointDuration;
    }

    public int getAccurateType() {
        return accurateType;
    }

    public void setAccurateType(int accurateType) {
        this.accurateType = accurateType;
    }

    public int getInsomniaTag() {
        return insomniaTag;
    }

    public void setInsomniaTag(int insomniaTag) {
        this.insomniaTag = insomniaTag;
    }

    public int getInsomniaScore() {
        return insomniaScore;
    }

    public void setInsomniaScore(int insomniaScore) {
        this.insomniaScore = insomniaScore;
    }

    public int getInsomniaTimes() {
        return insomniaTimes;
    }

    public void setInsomniaTimes(int insomniaTimes) {
        this.insomniaTimes = insomniaTimes;
    }

    public int getInsomniaLength() {
        return insomniaLength;
    }

    public void setInsomniaLength(int insomniaLength) {
        this.insomniaLength = insomniaLength;
    }

    public List<InsomniaTimeData> getInsomniaBeanList() {
        return insomniaBeanList;
    }

    public void setInsomniaBeanList(List<InsomniaTimeData> insomniaBeanList) {
        this.insomniaBeanList = insomniaBeanList;
    }

    public String getStartInsomniaTime() {
        return startInsomniaTime;
    }

    public void setStartInsomniaTime(String startInsomniaTime) {
        this.startInsomniaTime = startInsomniaTime;
    }

    public String getStopInsomniaTime() {
        return stopInsomniaTime;
    }

    public void setStopInsomniaTime(String stopInsomniaTime) {
        this.stopInsomniaTime = stopInsomniaTime;
    }

    public int getInsomniaDuration() {
        return insomniaDuration;
    }

    public void setInsomniaDuration(int insomniaDuration) {
        this.insomniaDuration = insomniaDuration;
    }

    public String getSleepSourceStr() {
        return sleepSourceStr;
    }

    public void setSleepSourceStr(String sleepSourceStr) {
        this.sleepSourceStr = sleepSourceStr;
    }

    public int getLaster() {
        return laster;
    }

    public void setLaster(int laster) {
        this.laster = laster;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "BracePrecisionSleepBean{" +
                "sleepTag=" + sleepTag +
                ", getUpScore=" + getUpScore +
                ", deepScore=" + deepScore +
                ", sleepEfficiencyScore=" + sleepEfficiencyScore +
                ", fallAsleepScore=" + fallAsleepScore +
                ", sleepTimeScore=" + sleepTimeScore +
                ", exitSleepMode=" + exitSleepMode +
                ", deepAndLightMode=" + deepAndLightMode +
                ", otherDuration=" + otherDuration +
                ", firstDeepDuration=" + firstDeepDuration +
                ", getUpDuration=" + getUpDuration +
                ", getUpToDeepAve=" + getUpToDeepAve +
                ", onePointDuration=" + onePointDuration +
                ", accurateType=" + accurateType +
                ", insomniaTag=" + insomniaTag +
                ", insomniaScore=" + insomniaScore +
                ", insomniaTimes=" + insomniaTimes +
                ", insomniaLength=" + insomniaLength +
                ", insomniaBeanList=" + insomniaBeanList +
                ", startInsomniaTime='" + startInsomniaTime + '\'' +
                ", stopInsomniaTime='" + stopInsomniaTime + '\'' +
                ", insomniaDuration=" + insomniaDuration +
                ", sleepSourceStr='" + sleepSourceStr + '\'' +
                ", laster=" + laster +
                ", next=" + next +
                '}';
    }
}
