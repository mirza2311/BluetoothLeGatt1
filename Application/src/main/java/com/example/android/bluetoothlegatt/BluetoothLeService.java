/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothlegatt;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.android.TCP_IP.ClientActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;
    private ClientActivity c = new ClientActivity();
    private Socket socket;
    private DataOutputStream outputToClient;

    private String IP ="192.168.1.95"  ;
    private int PORT = 8000;
    private boolean connected;
    private DataOutputStream dao;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    /** public final static UUID UUID_HEART_RATE_MEASUREMENT =
     UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);**/
    public final static UUID UUID_GYRO =
            UUID.fromString(SampleGattAttributes.GYRO);
    public final static UUID UUID_MAG =
            UUID.fromString(SampleGattAttributes.MAG);
    public final static UUID UUID_ACC =
            UUID.fromString(SampleGattAttributes.ACC);
    public final static UUID UUID_BAR =
            UUID.fromString(SampleGattAttributes.BAR);
    public final static UUID UUID_TEMP =
            UUID.fromString(SampleGattAttributes.TEMP);
    public final static UUID UUID_BATTERY =
            UUID.fromString(SampleGattAttributes.BATTERY);
    public final static UUID UUID_DEVICE_NAME =
            UUID.fromString(SampleGattAttributes.DEVICE_NAME);
    public final static UUID UUID_DEVICE=
            UUID.fromString(SampleGattAttributes.DEVICE);

    public BluetoothLeService() {
        new Thread(new ClientThread()).start();
    }

    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {


                //System.out.println("Lenght is : "+c.getPortNumber());
                // System.out.println("Lenght is : "+c.getPortNumber2());
                //System.out.println("kolikocina je: "+c.getKoliko());
                //IP = c.getPortNumber();
                if (IP != null){
                    //  PORT = Integer.parseInt(c.getPortNumber2());
                    InetAddress serverAddr = InetAddress.getByName(IP);

                    socket = new Socket(serverAddr, PORT);
                }


            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }



    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // byte[] d = characteristic.getValue();
        //writeBytesToSocket(d);
        if (UUID_GYRO.equals(characteristic.getUuid())) {
            // MyDB d = new MyDB(this);

            final byte[] data = characteristic.getValue();
            double[] V = getValues(data);

            double Z = V[0] * 0.00875;
            double Y = V[1] * 0.00875;
            double X = V[2] * 0.00875;
            DecimalFormat dX = new DecimalFormat("###0.000");

            if (data != null && data.length > 0) {
                if (data != null && data.length > 0) {
                    if (socket != null){
                        writeIntToSocket(3);
                        writeBytesToSocket(data);
                    }
                    intent.putExtra(EXTRA_DATA, "X: " + dX.format(X) + "\n" + " Y: " + dX.format(Y) + "\n" + " Z: " + dX.format(Z));

                }
            }
        } else if (UUID_MAG.equals(characteristic.getUuid())) {
            //  MyDB d = new MyDB(this);

            final byte[] data = characteristic.getValue();
            double[] V = getValues(data);
            double Z = V[0] * 0.00016;
            double Y = V[1] * 0.00016;
            double X = V[2] * 0.00016;
            DecimalFormat dX = new DecimalFormat("###0.0000");

            if (data != null && data.length > 0) {
                if (data != null && data.length > 0) {
                    if (socket != null){
                        writeIntToSocket(2);
                        writeBytesToSocket(data);
                    }
                    intent.putExtra(EXTRA_DATA, "X: " + dX.format(X) + "\n" + " Y: " + dX.format(Y) + "\n" + " Z: " + dX.format(Z));
                }
            }
        } else if (UUID_ACC.equals(characteristic.getUuid())) {
            //MyDB d = new MyDB(this);

            final byte[] data = characteristic.getValue();

            System.out.println("lenght: " +data.length);
            // final byte[] sensor = new byte[1];
            //sensor[] = 1;
            double[] V = getValues(data);
            double Z = V[0] * 0.000061;
            double Y = V[1] * 0.000061;
            double X = V[2] * 0.000061;

            DecimalFormat dX = new DecimalFormat("###0.000000");
            if (data != null && data.length > 0) {
                if (data != null && data.length > 0) {
                    if (socket != null){

                        writeIntToSocket(1);
                        writeBytesToSocket(data);
                    }

                    intent.putExtra(EXTRA_DATA, "X: " + dX.format(X) + "\n" + " Y: " + dX.format(Y) + "\n" + " Z: " + dX.format(Z));
                }
            }
        } else if (UUID_BAR.equals(characteristic.getUuid())) {
            // MyDB d = new MyDB(this);

            final byte[] data = characteristic.getValue();
            byte[] bar = new byte[3];
            bar[0] = data[2];
            bar[1] = data[1];
            bar[2] = data[0];
            if (bar != null && bar.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(bar.length);
                for(byte byteChar : bar)
                    stringBuilder.append(String.format("%02X ", byteChar));


                double x = ((data[2] * 65536 ) + (data[1] * 256) + data[0]) ;

                double X = x / 4096;


                double meter = X / 98.04;
                DecimalFormat dX = new DecimalFormat("###0.000");

                    intent.putExtra(EXTRA_DATA, dX.format(X) + " hPa " + dX.format(meter) + " m");
                    if (socket != null){
                        writeIntToSocket(4);
                        writeBytesToSocket(data);


                    }


            }
        }else  if (UUID_BATTERY.equals(characteristic.getUuid())) {



            //MyDB d = new MyDB(this);
            final byte[] data = characteristic.getValue();
            int procent  = data[0];
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));

                intent.putExtra(EXTRA_DATA, procent+ " %");
                if (socket != null){
                    writeIntToSocket(6);
                    writeBytesToSocket(data);


                }
            }
        }else if (UUID_TEMP.equals(characteristic.getUuid())) {


            //MyDB d = new MyDB(this);
            final byte[] data = characteristic.getValue();
            System.out.println("LEngth:" +data.length);
            double t = byteArrayToDouble(data);
            System.out.println("TEMP " + t);



            if (data != null && data.length > 0) {
                if (socket != null){
                    writeIntToSocket(5);
                    writeBytesToSocket(data);
                }

                intent.putExtra(EXTRA_DATA, t + " C");
            }

        }else if (UUID_DEVICE_NAME.equals(characteristic.getUuid())) {
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                String s = new String(data);
                intent.putExtra(EXTRA_DATA,s);
            }
        }

        sendBroadcast(intent);
    }

    public double byteArrayToDouble(byte[] b) {
        double value;


        int temp =( (b[1] * 256) + b[0]);
        System.out.println("TEMP1 : " + temp);
        value = (42.5 + (temp / 480));
        return value;
    }

    public double[] getValues(byte[] data){
        double [] values = new double[3];
        byte[] low = new byte[3];
        byte[] high = new byte[3];
        byte[] lowHigh = new byte[6];

        int c = 0;
        int a = 0;
        int b =0 ;

        for (int i = 0; i < data.length;i++){
            if (i % 2 == 0){
                low[c++] = data[i];

            }else if (i % 2 != 0){
                high[a++] = data[i];
            }
        }


        for(int i = 2; i >= 0 ; i--){
            lowHigh[b++] = high[i];
            lowHigh[b++] = low[i];
        }

        int x = (lowHigh[0] *256 )+ lowHigh[1];
        int y = (lowHigh[2] * 256) + lowHigh[3];
        int z = (lowHigh[4] * 256) + lowHigh[5];

        values[0] = x;
        values[1] = y;
        values[2] = z;

        return values;
    }

    private void writeBytesToSocket(byte[] data) {
        try {
            dao = new DataOutputStream(socket.getOutputStream());
            dao.write(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeIntToSocket(int data) {
        try {
            dao = new DataOutputStream(socket.getOutputStream());
            dao.write(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        /**BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
         UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
         descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
         mBluetoothGatt.writeDescriptor(descriptor);
         **/
        // This is specific to Heart Rate Measurement.
        if ((!UUID_BATTERY.equals(characteristic.getUuid())) && (!UUID_DEVICE_NAME.equals(characteristic.getUuid())) && (!UUID_DEVICE.equals(characteristic.getUuid()))) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }
}