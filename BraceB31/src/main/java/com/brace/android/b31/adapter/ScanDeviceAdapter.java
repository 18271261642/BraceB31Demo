package com.brace.android.b31.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brace.android.b31.R;
import com.brace.android.b31.view.OnItemClickListener;

import java.util.List;

/**
 * Created by Admin
 * Date 2019/10/30
 */
public class ScanDeviceAdapter extends RecyclerView.Adapter<ScanDeviceAdapter.ScanDeviceViewHolder>{


    private Context mContext;
    private List<BluetoothDevice> deviceList;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ScanDeviceAdapter(Context mContext, List<BluetoothDevice> deviceList) {
        this.mContext = mContext;
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public ScanDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_scan_device,viewGroup,false);
        return new ScanDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScanDeviceViewHolder scanDeviceViewHolder, int i) {
        scanDeviceViewHolder.nameTv.setText(deviceList.get(i).getName());
        scanDeviceViewHolder.macTv.setText(deviceList.get(i).getAddress());

        scanDeviceViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    int position = scanDeviceViewHolder.getLayoutPosition();
                    onItemClickListener.onIteClick(position);
                }
            }
        });
    }
	

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    class ScanDeviceViewHolder extends RecyclerView.ViewHolder{

        TextView nameTv,macTv;

        public ScanDeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.itemBleNameTv);
            macTv = itemView.findViewById(R.id.itemBleMacTv);
        }
    }


}
