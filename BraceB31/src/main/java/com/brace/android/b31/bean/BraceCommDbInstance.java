package com.brace.android.b31.bean;

import android.util.Log;

import org.litepal.LitePal;

import java.util.List;

/**
 * 处理从手环中读取的数据保存和修改
 * Created by Admin
 * Date 2019/11/5
 */
public class BraceCommDbInstance {

    private static final String TAG = "BraceCommDbInstance";


    private static volatile BraceCommDbInstance braceCommDbInstance;

    private BraceCommDbInstance() {

    }


    public static synchronized BraceCommDbInstance getBraceCommDbInstance(){
        if(braceCommDbInstance == null){
            synchronized (BraceCommDbInstance.class){
                braceCommDbInstance = new BraceCommDbInstance();
            }
        }
        return braceCommDbInstance;
    }


    /**
     * 保存数据，每天的数据只有一条
     * @param braceCommB31Db
     */
    public void saveDataSoreceData(BraceCommB31Db braceCommB31Db){
        try {
            String rtc = braceCommB31Db.getRtcStr();
            String address = braceCommB31Db.getAddressStr();
            String typeStr = braceCommB31Db.getType();
            boolean isSaved = braceCommB31Db.saveOrUpdate("rtcStr = ? and addressStr = ? and type = ?",
                    rtc,address,typeStr);
            Log.e(TAG,"------isSaved="+isSaved);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 根据条件查询数据
     * @param mac Mac地址
     * @param dayStr 日期 yyyy-MM-dd格式
     * @param type  类型
     * @return
     */
    public List<BraceCommB31Db> findSavedDataForType(String mac, String dayStr, String type){
        try {
            String whereStr = "rtcStr = ? and addressStr = ? and type = ?";
            List<BraceCommB31Db> dbList = LitePal.where(whereStr,dayStr,mac,type).find(BraceCommB31Db.class);
            return dbList == null || dbList.isEmpty() ? null : dbList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询单条数据
     * @param mac
     * @param dayStr
     * @param type
     * @return
     */
    public String findSingleOrigenData(String mac,String dayStr,String type){
        try {
            String whereStr = "rtcStr = ? and addressStr = ? and type = ?";
            List<BraceCommB31Db> dbList = LitePal.where(whereStr,dayStr,mac,type).find(BraceCommB31Db.class);
            return dbList == null || dbList.isEmpty() ? null : dbList.get(0).getDataSourceStr();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
