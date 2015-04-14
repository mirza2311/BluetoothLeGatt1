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

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    /**public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
     **/
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String DEVICE_INFORMATION= "00001800-0000-1000-8000-00805f9b34fb";
    public static String DEVICE_NAME= "00002a00-0000-1000-8000-00805f9b34fb";
    public static String DEVICE= "00002a01-0000-1000-8000-00805f9b34fb";

    public static String SENSOR_SERVICE = "7280d0b4-c973-11e4-8731-1681e6b88ec1";
    public static String BATTERY_SERVICE = "0000180f-0000-1000-8000-00805f9b34fb";

    public static String BATTERY = "00002a19-0000-1000-8000-00805f9b34fb";

    public static String GYRO = "b928ceee-ca32-11e4-8731-1681e6b88ec1";
    public static String MAG ="b928cc14-ca32-11e4-8731-1681e6b88ec1";
    public static String ACC = "b928cab6-ca32-11e4-8731-1681e6b88ec1";
    public static String BAR = "b928c85e-ca32-11e4-8731-1681e6b88ec1";
    public static String TEMP = "b928cd15-ca32-11e4-8731-1681e6b88ec1";

    static {
        attributes.put(DEVICE_INFORMATION, "Generic Access Service");
        attributes.put(DEVICE_NAME, "Device Name");
        attributes.put(DEVICE, "Device");

        attributes.put(BATTERY_SERVICE, "Battery Information Service");
        attributes.put(BATTERY, "Battery Power");

        attributes.put(SENSOR_SERVICE, "Sensor Information Service");
        attributes.put(GYRO, "Gyro Measurement");
        attributes.put(MAG, "Magnetometer Measurement");
        attributes.put(ACC, "Accelerometer Measurement");
        attributes.put(BAR, "Barometer Measurement");
        attributes.put(TEMP, "Temperature Measurement");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
