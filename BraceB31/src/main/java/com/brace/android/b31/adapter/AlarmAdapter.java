package com.brace.android.b31.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.brace.android.b31.R;
import com.brace.android.b31.utils.BraceUtils;
import com.veepoo.protocol.model.settings.Alarm2Setting;

import java.util.List;

/**
 * B30手环闹钟列表适配器
 */
public class AlarmAdapter extends BaseAdapter {

    private List<Alarm2Setting> dataList;
    private Context mContext;
    /**
     * 闹钟开关回调事件实例
     */
    private AlarmCheckChange mCallBack;

    private LayoutInflater layoutInflater;


    /**
     *

     * 闹钟图标数组,资源ID
     */
    private final int[] alarmTypeImageList = {R.mipmap.selected1, R.mipmap.selected2,
            R.mipmap.selected3, R.mipmap.selected4, R.mipmap.selected5, R.mipmap.selected6,
            R.mipmap.selected7, R.mipmap.selected8, R.mipmap.selected9, R.mipmap.selected10,
            R.mipmap.selected11, R.mipmap.selected12, R.mipmap.selected13, R.mipmap.selected14,
            R.mipmap.selected15, R.mipmap.selected16, R.mipmap.selected17, R.mipmap.selected18,
            R.mipmap.selected19, R.mipmap.selected20};

    public AlarmAdapter(Context context, List<Alarm2Setting> data) {
        mContext = context;
        dataList = data;
        layoutInflater =  LayoutInflater.from(mContext);
    }

    /**
     * 设置闹钟开关回调事件实例
     */
    public void setChangeCallBack(AlarmCheckChange callBack) {
        this.mCallBack = callBack;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_alarm_layout, viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.iv_alarm_type = view.findViewById(R.id.iv_alarm_type);
            viewHolder.tv_time = view.findViewById(R.id.tv_alarm_time);
            viewHolder.date = view.findViewById(R.id.tv_alarm_date);
            viewHolder.check = view.findViewById(R.id.alarmToggleBtn);
            //viewHolder.singleImg = view.findViewById(R.id.singleAlarmImg);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Alarm2Setting setting = dataList.get(position);
        int hour = setting.getAlarmHour();
        final int minute = setting.getAlarmMinute();
        String hourStr = hour < 10 ? "0" + hour : "" + hour;
        String minuteStr = minute < 10 ? "0" + minute : "" + minute;
        String showTime = hourStr + ":" + minuteStr;
        viewHolder.tv_time.setText(showTime);

        if(setting.getRepeatStatus().equals("0000000") && !setting.getUnRepeatDate().equals("0000-00-00")){ //单次的闹钟
            viewHolder.check.setVisibility(View.GONE);
            //viewHolder.singleImg.setVisibility(View.VISIBLE);
        }else{
            //viewHolder.singleImg.setVisibility(View.GONE);
            viewHolder.check.setVisibility(View.VISIBLE);
            viewHolder.check.setChecked(dataList.get(position).isOpen());
        }
        viewHolder.date.setText(BraceUtils.obtainAlarmDate(mContext, setting.getRepeatStatus()));
        int sceneIndex = setting.getScene() > 0 && setting.getScene() <= 20 ? setting.getScene()
                - 1 : 0;// 做个预防
        viewHolder.iv_alarm_type.setImageResource(alarmTypeImageList[sceneIndex]);


        viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!buttonView.isPressed())
                    return;
                if(mCallBack != null){
                    mCallBack.onCheckChange(position);
                }
            }
        });
        return view;
    }

    private class ViewHolder {
        ImageView iv_alarm_type;
        TextView tv_time, date;
        ToggleButton check;
    }

    /**
     * 闹钟开关回调事件
     */
    public interface AlarmCheckChange {
        void onCheckChange(int position);
    }

}
