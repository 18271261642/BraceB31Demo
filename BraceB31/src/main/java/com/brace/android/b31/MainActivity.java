package com.brace.android.b31;


import android.os.Bundle;
import android.util.Log;

import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.bean.BraceCommB31Db;
import com.brace.android.b31.bean.BraceCommDbInstance;
import com.brace.android.b31.constant.Constant;
import com.google.gson.Gson;

import java.util.List;


public class MainActivity extends BaseActivity {
	

    private static final String TAG = "MainActivity";

    String bleMac ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bleMac = BaseApplication.getBaseApplication().getBleMac();

    }



    private void findDbData() {
        List<BraceCommB31Db> list = BraceCommDbInstance.getBraceCommDbInstance().findSavedDataForType(bleMac,"2019-11-06",Constant.DB_TYPE_SPORT);

        Log.e(TAG,"----------查询数据-="+new Gson().toJson(list));
    }
}
