package com.brace.android.b31.bean;


import org.litepal.crud.LitePalSupport;

/**
 * Created by Admin
 * Date 2019/11/5
 */
public class BraceCommB31Db extends LitePalSupport {


    /**
     * 日期 yyyy-MM-dd格式
     */
    private String rtcStr;

    /**
     * mac地址
     */
    private String addressStr;

    /**
     * 类型
     */
    private String type;


    /*
    是否已经上传设备的数据；0-未上传；1已上传
     */
    private int isUploadStatus;

    public int getIsUploadStatus() {
        return isUploadStatus;
    }

    public void setIsUploadStatus(int isUploadStatus) {
        this.isUploadStatus = isUploadStatus;
    }

    /**
     * 数据，转换为json格式
     */
    private String dataSourceStr;


    public String getRtcStr() {
        return rtcStr;
    }

    public void setRtcStr(String rtcStr) {
        this.rtcStr = rtcStr;
    }

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {
        this.addressStr = addressStr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataSourceStr() {
        return dataSourceStr;
    }

    public void setDataSourceStr(String dataSourceStr) {
        this.dataSourceStr = dataSourceStr;
    }


}
