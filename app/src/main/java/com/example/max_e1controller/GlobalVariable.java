package com.example.max_e1controller;

import android.app.Activity;
import android.app.Application;

import com.example.max_e1controller.Bluetooth;

public class GlobalVariable extends Application
{
    private Bluetooth bluetooth;

    Bluetooth getBluetooth()
    {
        return bluetooth;
    }

    void setActivity(Activity act)
    {
        bluetooth.setActivity(act);
    }

    void setBluetooth(Bluetooth bt)
    {
        bluetooth = bt;
    }
}
