package com.brace.android.b31.adapter;

import android.content.Context;

import com.brace.android.b31.R;
import com.brace.android.b31.bean.BraceHalfBpBean;

import java.util.List;

/**
 * 血氧详情adapter
 * Created by Admin
 * Date 2019/11/28
 */
public class BloodDetailAdapter extends CommonRecyclerAdapter<BraceHalfBpBean> {


    public BloodDetailAdapter(Context context, List<BraceHalfBpBean> data, int layoutId) {
        super(context, data, layoutId);
    }
	

    @Override
    public void convert(MyViewHolder holder, BraceHalfBpBean item) {
        holder.setText(R.id.itemSportHealthyTimeTv,item.getTime().getColck());
        holder.setText(R.id.itemSportHealthyValueTv,item.getHighValue()+"/"+item.getLowValue());
        int hightValue = item.getHighValue();
        if (hightValue<=120){
            holder.setImageResource(R.id.itemSportHealthyTypeImg, R.mipmap.icon_blood_type2);
        }else if (hightValue<=140){
            holder.setImageResource(R.id.itemSportHealthyTypeImg, R.mipmap.icon_blood_type4);
        }else if (hightValue <=150){
            holder.setImageResource(R.id.itemSportHealthyTypeImg, R.mipmap.icon_blood_type3);
        }else{
            holder.setImageResource(R.id.itemSportHealthyTypeImg, R.mipmap.icon_blood_type5);
        }

    }
}
