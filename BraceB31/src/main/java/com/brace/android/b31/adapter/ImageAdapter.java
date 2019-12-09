package com.brace.android.b31.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.brace.android.b31.R;


public class ImageAdapter extends BaseAdapter {


    /**
     * 闹钟图标数组,资源ID,未选中
     */
    private final int[] alarmTypeImageList = {R.mipmap.unselected1, R.mipmap.unselected2,
            R.mipmap.unselected3, R.mipmap.unselected4, R.mipmap.unselected5, R.mipmap.unselected6,
            R.mipmap.unselected7, R.mipmap.unselected8, R.mipmap.unselected9, R.mipmap.unselected10,
            R.mipmap.unselected11, R.mipmap.unselected12, R.mipmap.unselected13, R.mipmap.unselected14,
            R.mipmap.unselected15, R.mipmap.unselected16, R.mipmap.unselected17, R.mipmap.unselected18,
            R.mipmap.unselected19, R.mipmap.unselected20};

    private Context mContext;

    public ImageAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return alarmTypeImageList.length;
    }

    @Override
    public Object getItem(int position) {
        return alarmTypeImageList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

	
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }
        // 设置GridView的显示的个子的间距
        imageView.setPadding(10, 10, 10, 10);
        imageView.setImageResource(alarmTypeImageList[position]);
        return imageView;
    }

}