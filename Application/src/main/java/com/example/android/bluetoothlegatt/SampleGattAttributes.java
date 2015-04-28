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


    public static String MAGNETOMETER_SERVICE = "f000aa30-0451-4000-b000-000000000000";
    public static String BAROMETER_SERVICE = "f000aa40-0451-4000-b000-000000000000";
    public static String ACCELEROMETER_SERVICE = "f000aa10-0451-4000-b000-000000000000";
    public static String GYROSCOPE_SERVICE = "f000aa50-0451-4000-b000-000000000000";
    public static String TEMPERATURE_SERVICE = "f000aa00-0451-4000-b000-000000000000";
    public static String BATTERY_SERVICE = "0000180f-0000-1000-8000-00805f9b34fb";

    public static String BATTERY = "00002a19-0000-1000-8000-00805f9b34fb";

    public static String GYRO = "f000aa51-0451-4000-b000-000000000000";
    public static String MAG ="f000aa31-0451-4000-b000-000000000000";
    public static String ACC = "f000aa11-0451-4000-b000-000000000000";
    public static String BAR = "f000aa41-0451-4000-b000-000000000000";
    public static String TEMP = "f000aa01-0451-4000-b000-000000000000";

    static {
        attributes.put(DEVICE_INFORMATION, "Generic Access Service");
        attributes.put(DEVICE_NAME, "Device Name");
        attributes.put(DEVICE, "Device");

        attributes.put(BATTERY_SERVICE, "Battery Information Service");
        attributes.put(BATTERY, "Battery Power");

        attributes.put(MAGNETOMETER_SERVICE, "Magnetometer Service");
        attributes.put(BAROMETER_SERVICE, "Barometer Service");
        attributes.put(ACCELEROMETER_SERVICE, "Accelerometer Service");
        attributes.put(GYROSCOPE_SERVICE, "Gyroscope Service");
        attributes.put(TEMPERATURE_SERVICE, "Temperature Service");
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
