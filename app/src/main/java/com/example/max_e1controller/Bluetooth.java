package com.example.max_e1controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Bluetooth{

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket;
    BluetoothManager bluetoothManager;
    BluetoothGattCallback bluetoothGattCallback;
    BluetoothGatt bluetoothGatt;
    BluetoothGattService bluetoothGattService;
    BluetoothGattCharacteristic bluetoothGattCharacteristic;
    BluetoothGattCharacteristic bluetoothGattReadCharacteristic;

    boolean isConnected = false;

    private static final String TAG = "TAG";
    Activity activity;
    //UUID service_uuid = UUID.fromString("00001800-0000-1000-8000-00805F9B34FB");//00001800
    UUID service_uuid = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    //UUID character_uuid = UUID.fromString("00002A00-0000-1000-8000-00805F9B34FB");
    UUID character_uuid = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");
    UUID character_read_uuid = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E");
    static public String byte2Hex(byte[] b) {
        String result = "";
        for (int i=0 ; i<b.length ; i++)
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        return result;
    }
    public Bluetooth(final Activity act)
    {
        activity = act;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
            Toast.makeText(activity,"Bluetooth Adatper Not Found!",Toast.LENGTH_LONG).show();
        else if (!bluetoothAdapter.isEnabled())
        {
            while (!bluetoothAdapter.isEnabled())
            {
                try {
                    bluetoothAdapter.enable();

                }
                catch(Exception e){}
            }
            Toast.makeText(activity, "Bluetooth Enabled",Toast.LENGTH_SHORT).show();
        }

        if (!activity.getPackageManager().hasSystemFeature(activity.getPackageManager().FEATURE_BLUETOOTH_LE))
            Toast.makeText(activity,"You gg la!",Toast.LENGTH_SHORT).show();
        bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (status == BluetoothGatt.GATT_FAILURE)
                {
                    Toast.makeText(activity, "not connected!",Toast.LENGTH_SHORT).show();
                }
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        bluetoothGatt.discoverServices();
                        Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show();
                    }
                    if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        Toast.makeText(activity, "Disconnected!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status)
            {
                super.onServicesDiscovered(gatt, status);
                bluetoothGattService = gatt.getService(service_uuid);
                if (bluetoothGattService != null)
                    isConnected = true;
                bluetoothGattReadCharacteristic = bluetoothGattService.getCharacteristic(character_read_uuid);
                bluetoothGatt.setCharacteristicNotification(bluetoothGattReadCharacteristic, true);
                List<BluetoothGattDescriptor> descriptorList = bluetoothGattReadCharacteristic.getDescriptors();
                if (descriptorList != null && descriptorList.size() > 0)
                    for (BluetoothGattDescriptor descriptor : descriptorList)
                    {
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        bluetoothGatt.writeDescriptor(descriptor);
                    }

            }

            @Override
            public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status)
            {
//                if (status == BluetoothGatt.GATT_SUCCESS)
//                {
//                    Log.d(TAG,"read data: " + characteristic.getValue());
//                }
            }

            @Override
            public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status)
            {
                super.onCharacteristicWrite(gatt, characteristic, status);
//                if (status == BluetoothGatt.GATT_SUCCESS && characteristic.equals(bluetoothGattCharacteristic))
//                {
//                    Log.d(TAG,"write data: " + byte2Hex(characteristic.getValue()));
//                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
            {
                  super.onCharacteristicChanged(gatt, characteristic);
//                if(characteristic.equals(bluetoothGattReadCharacteristic))
//                {
//                    Log.d(TAG,"read data: " + new String(characteristic.getValue()));
//                }
            }
        };


    }

    public void GetPairedDevice(ArrayList<String> devName, ArrayList<String> devAddress)
    {
        devName.clear();
        devAddress.clear();
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        if (!pairedDevice.isEmpty())
        {
            for (BluetoothDevice device : pairedDevice)
            {
                devName.add(device.getName());
                devAddress.add(device.getAddress());
            }
            for (int i=0 ; i <devName.size()-1;i++)
            {

            }
        }
    }

    public void setActivity(Activity act)
    {
        activity = act;
    }


    synchronized  public void ReadDataLE()
    {
        bluetoothGattService = bluetoothGatt.getService(service_uuid);
        bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(character_read_uuid);
        bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
    }


    synchronized public boolean SendDataLE(byte[] data)
    {
        if (bluetoothGatt == null)
        {
            Toast.makeText(activity,"null",Toast.LENGTH_SHORT);
            return false;
        }
        if (bluetoothGattService == null)
            bluetoothGattService = bluetoothGatt.getService(service_uuid);
        if (bluetoothGattCharacteristic == null)
            bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(character_uuid);

        bluetoothGattCharacteristic.setValue(data);
        bluetoothGattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
        return bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
    }

    public void Connect()
    {
        try {
            bluetoothSocket.connect();
            Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
                Toast.makeText(activity, bluetoothDevice.getAddress()+"Failed", Toast.LENGTH_SHORT).show();
            }

    }

    public void ConnectLE()
    {
        try
        {
            bluetoothGatt = bluetoothDevice.connectGatt(activity,false,bluetoothGattCallback);
            isConnected = true;
        }
        catch(Exception e)
        {
            Toast.makeText(activity,"failed",Toast.LENGTH_SHORT).show();
            isConnected = false;
        }
    }

    boolean Disconnect()
    {
        try {
            Thread.sleep(500);
            bluetoothSocket.close();
            Toast.makeText(activity, "Disconnected", Toast.LENGTH_SHORT).show();
            return true;
        }
        catch (Exception e) {
            Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    boolean DisConnectLE()
    {
        if (bluetoothGatt == null)
        {
            return true;
        }
        try
        {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            isConnected = false;
            return true;
        }
        catch (Exception e)
        {
            isConnected = true;
            return false;
        }

    }




}
