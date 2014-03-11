package com.binsolb.myswing;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Andrew.Jeon on 14. 3. 12.
 */
public class ConnectedThread extends Thread {
    final String TAG = "ConnectedThread";
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    Handler mHandler;

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mmSocket = socket;
        mHandler = handler;

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

//    StringBuffer sbb = new StringBuffer();

    public void run() {
        byte[] buffer;
        int bytes;

        while (true) {
            try {
                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                buffer = new byte[1024];
                bytes = mmInStream.read(buffer);
                mHandler.obtainMessage(Config.BLUETOOTH_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    }

    public void write(String income) {
        try {
            mmOutStream.write(income.getBytes());
            for (int i = 0; i < income.getBytes().length; i++)
                Log.v("outStream" + Integer.toString(i), Character.toString((char) (Integer.parseInt(Byte.toString(income.getBytes()[i])))));
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
        }

    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }
}

