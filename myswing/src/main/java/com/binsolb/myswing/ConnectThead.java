package com.binsolb.myswing;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;

/**
 * Created by Andrew.Jeon on 14. 3. 12.
 */
public class ConnectThead extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    BluetoothAdapter btAdapter;
    Handler mHandler;

    public ConnectThead(BluetoothDevice device, BluetoothAdapter adapter, Handler handler) {
        BluetoothSocket tmp = null;
        mmDevice = device;
        btAdapter = adapter;
        mHandler = handler;

        try {
            tmp = device.createRfcommSocketToServiceRecord(Config.MY_UUID);
        } catch (IOException e) {}
        mmSocket = tmp;
    }

    public void run() {
        btAdapter.cancelDiscovery();

        try{
            mmSocket.connect();
        } catch (IOException connectException) {
            try{
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
        mHandler.obtainMessage(Config.BLUETOOTH_SUCCESS_CONNECT, mmSocket).sendToTarget();
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {}
     }

}
