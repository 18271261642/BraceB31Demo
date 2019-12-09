package com.android.braceb31demo;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * adapter
 * Created by Admin
 * Date 2019/12/9
 */
public class OwnScanAdapter extends RecyclerView.Adapter<OwnScanAdapter.OwnViewHolder>{

    private List<BluetoothDevice> deviceList;
    private Context mContext;
    private OwnOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OwnOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OwnScanAdapter(List<BluetoothDevice> deviceList, Context mContext) {
        this.deviceList = deviceList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public OwnViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_own_device_layout,viewGroup,false);
        return new OwnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OwnViewHolder ownViewHolder, int i) {
        ownViewHolder.tvName.setText(deviceList.get(i).getName() == null ? "NULL":deviceList.get(i).getName());
        ownViewHolder.tvMac.setText(deviceList.get(i).getAddress());
        ownViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = ownViewHolder.getLayoutPosition();
                if(onItemClickListener != null)
                    onItemClickListener.onPositionItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    class OwnViewHolder extends RecyclerView.ViewHolder{

        TextView tvName,tvMac;

        public OwnViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.itemOwnNameTv);
            tvMac = itemView.findViewById(R.id.itemOwnMacTv);
        }
    }

    interface OwnOnItemClickListener{
        void onPositionItem(int position);
    }
}
