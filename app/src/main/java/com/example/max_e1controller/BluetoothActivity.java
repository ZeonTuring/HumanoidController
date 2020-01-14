package com.example.max_e1controller;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import android.content.Context;
import android.content.Intent;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class BluetoothActivity extends AppCompatActivity
{
    GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), BluetoothActivity.this);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        gv = (GlobalVariable) getApplicationContext();
    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.putExtra("bluetooth_connection",gv.getBluetooth().isConnected);
        BluetoothActivity.this.setResult(1, intent);
        BluetoothActivity.this.finish();
        super.onBackPressed();
    }
}