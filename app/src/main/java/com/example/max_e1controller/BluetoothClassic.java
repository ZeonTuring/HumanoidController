package com.example.max_e1controller;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.max_e1controller.Bluetooth;
import com.example.max_e1controller.BluetoothActivity;
import com.example.max_e1controller.R;

public class BluetoothClassic extends Fragment
{
    BluetoothActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bluetooth_classic, container, false);
        return rootView;
    }

    public BluetoothClassic(BluetoothActivity act)
    {
        activity = act;
    }

}
