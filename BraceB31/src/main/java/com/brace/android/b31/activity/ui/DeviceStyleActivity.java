package com.brace.android.b31.activity.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.brace.android.b31.BaseApplication;
import com.brace.android.b31.R;
import com.brace.android.b31.activity.BaseActivity;
import com.brace.android.b31.bean.DeviceStyleBean;
import com.brace.android.b31.constant.Constant;
import com.brace.android.b31.utils.SpUtils;
import com.brace.android.b31.view.OnDeviceStyleSelectListener;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IScreenStyleListener;
import com.veepoo.protocol.model.datas.ScreenStyleData;

import java.util.ArrayList;
import java.util.List;


/**
 * 界面风格设置
 * Created by Admin
 * Date 2019/11/27
 */
public class DeviceStyleActivity extends BaseActivity implements OnDeviceStyleSelectListener {

    private static final String TAG = "DeviceStyleActivity";

    ImageView commentackImg;
    TextView commentTitleTv;
    ListView styleListView;

    private List<DeviceStyleBean> resultList;
    private DeviceStyleAdapter deviceStyleAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_style);


        initViews();

        readDeviceData();

    }

    private void readDeviceData() {

        final int styleCount = (int) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.SP_DEVICE_STYLE_COUNT, 0);

        BaseApplication.getVPOperateManager().readScreenStyle(iBleWriteResponse, new IScreenStyleListener() {
            @Override
            public void onScreenStyleDataChange(ScreenStyleData screenStyleData) {
                resultList.clear();
                Log.e(TAG, "--------screenStyleData=" + screenStyleData.toString());
                for (int i = 0; i < styleCount; i++) {
                    DeviceStyleBean deviceStyleBean = new DeviceStyleBean();
                    deviceStyleBean.setId(i);
                    deviceStyleBean.setChecked(i == screenStyleData.getscreenStyle());
                    resultList.add(deviceStyleBean);
                }

                deviceStyleAdapter.notifyDataSetChanged();

            }
        });
    }

    private void initViews() {

        commentackImg = findViewById(R.id.commentackImg);
        commentTitleTv = findViewById(R.id.commentTitleTv);
        styleListView = findViewById(R.id.styleListView);

        commentackImg.setVisibility(View.VISIBLE);
        commentTitleTv.setText(getResources().getString(R.string.string_devices_ui));
        // int styleCount = (int) SpUtils.getParam(BaseApplication.getBaseApplication(), Constant.SP_DEVICE_STYLE_COUNT, 0);
        resultList = new ArrayList<>();

        deviceStyleAdapter = new DeviceStyleAdapter(resultList);
        styleListView.setAdapter(deviceStyleAdapter);
        deviceStyleAdapter.setOnDeviceStyleSelectListener(this);
        commentackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private IBleWriteResponse iBleWriteResponse = new IBleWriteResponse() {
        @Override
        public void onResponse(int i) {

        }
    };


    @Override
    public void onItemStyleSelect(int position) {
        Log.e(TAG, "--------setting=" + position);
        BaseApplication.getVPOperateManager().settingScreenStyle(iBleWriteResponse, new IScreenStyleListener() {
            @Override
            public void onScreenStyleDataChange(ScreenStyleData screenStyleData) {
                Log.e(TAG, "--------setting=" + screenStyleData.toString());
            }
        }, position);

    }



    private class DeviceStyleAdapter extends BaseAdapter {

        LayoutInflater layoutInflater;
        private List<DeviceStyleBean> list;

        private int selectId = -1;

        private OnDeviceStyleSelectListener onDeviceStyleSelectListener;

        public void setOnDeviceStyleSelectListener(OnDeviceStyleSelectListener onDeviceStyleSelectListener) {
            this.onDeviceStyleSelectListener = onDeviceStyleSelectListener;
        }

        public DeviceStyleAdapter(List<DeviceStyleBean> list) {
            this.list = list;
            layoutInflater = LayoutInflater.from(DeviceStyleActivity.this);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.item_device_style_layout, parent, false);
                holder.tv = convertView.findViewById(R.id.itemStyleTv);
                holder.checkBox = convertView.findViewById(R.id.itemStyleCheckBox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(position == 0 ? getResources().getString(R.string.string_default_style) : getResources().getString(R.string.style_position) + position);

            final CheckBox checkBox = holder.checkBox;
            checkBox.setChecked(list.get(position).isChecked());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!buttonView.isPressed())
                        return;
                    if (onDeviceStyleSelectListener != null)
                        onDeviceStyleSelectListener.onItemStyleSelect(position);
                    if (checkBox.isChecked()) {
                        selectId = position;
                        list.get(position).setChecked(true);
                    } else {
                        selectId = -1;
                    }

                    for (int i = 0; i < list.size(); i++) {
                        if (selectId != i) {
                            list.get(i).setChecked(false);
                        }
                    }


                    notifyDataSetChanged();
                }
            });


            return convertView;
        }


        class ViewHolder {
            TextView tv;
            CheckBox checkBox;
        }


    }
}
