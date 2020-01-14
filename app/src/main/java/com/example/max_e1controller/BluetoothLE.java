package com.example.max_e1controller;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.max_e1controller.BluetoothActivity;
import com.example.max_e1controller.R;

public class BluetoothLE extends Fragment
{
    BluetoothActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bluetooth_le, container, false);
        return rootView;
    }

    public BluetoothLE(BluetoothActivity act)
    {
        activity = act;
    }
}
