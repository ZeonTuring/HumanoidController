package com.example.max_e1controller;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.widget.Toast;

import com.example.max_e1controller.Bluetooth;
import com.example.max_e1controller.BluetoothActivity;
import com.example.max_e1controller.GlobalVariable;
import com.example.max_e1controller.R;

import java.util.ArrayList;
import java.util.UUID;

public class PairedDevice extends Fragment
{
    Activity activity;
    GlobalVariable gv;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.paired_device, container, false);
        SetView(rootView);
        return rootView;
    }

    public PairedDevice(Activity act)
    {
        activity = act;
        gv = (GlobalVariable) activity.getApplicationContext();
        gv.setActivity(activity);
    }


    private void SetView(View view)
    {
        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerViewLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);



        ArrayList<String> bluetoothDeviceName = new ArrayList<>();
        final ArrayList<String> bluetoothDeviceAddress = new ArrayList<>();
        gv.getBluetooth().GetPairedDevice(bluetoothDeviceName, bluetoothDeviceAddress);
        //recyclerViewAdapter = new RecyclerViewAdapter(bluetoothDeviceName, bluetoothDeviceAddress,activity);
        recyclerViewAdapter = new QuickAdapter<String>(bluetoothDeviceName) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.paired_device_list;
            }

            @Override
            public void convert(ViewHolder viewHolder, String data, final int position) {
                viewHolder.setText(R.id.bt_dev_name, data);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        //Toast.makeText(activity,getData(position),Toast.LENGTH_SHORT).show();
                        gv.getBluetooth().bluetoothDevice = gv.getBluetooth().bluetoothAdapter.getRemoteDevice(bluetoothDeviceAddress.get(position));
                        if (!gv.getBluetooth().isConnected)
                            gv.getBluetooth().ConnectLE();
                        else gv.getBluetooth().DisConnectLE();
                    }
                });
            }

        };
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
