package com.brace.android.b31.adapter;

import android.content.Context;

import com.brace.android.b31.R;
import com.brace.android.b31.bean.BraceHalfHourSportBean;

import java.util.List;

/**
 * 运动详情adapter
 * Created by Admin
 * Date 2019/11/28
 */
public class SportDetailAdapter extends CommonRecyclerAdapter<BraceHalfHourSportBean> {

    public SportDetailAdapter(Context context, List<BraceHalfHourSportBean> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(MyViewHolder holder, BraceHalfHourSportBean item) {
        holder.setText(R.id.itemSportHealthyTimeTv,item.getTime().getColck());
        holder.setText(R.id.itemSportHealthyValueTv,item.getStepValue()+"");
        holder.setImageResource(R.id.itemSportHealthyTypeImg, R.mipmap.item_step_small);
    }
}
