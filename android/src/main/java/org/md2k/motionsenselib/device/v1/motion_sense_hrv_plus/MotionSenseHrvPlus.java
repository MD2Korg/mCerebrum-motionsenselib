package org.md2k.motionsenselib.device.v1.motion_sense_hrv_plus;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselib.device.CharacteristicBatteryV1V2;
import org.md2k.motionsenselib.device.Characteristics;
import org.md2k.motionsenselib.device.DataQuality;
import org.md2k.motionsenselib.device.DataQualityAccelerometer;
import org.md2k.motionsenselib.device.DataQualityPPG;
import org.md2k.motionsenselib.device.Device;
import org.md2k.motionsenselib.settings.DeviceSettings;
import org.md2k.motionsenselib.device.SensorInfo;
import org.md2k.motionsenselib.device.SensorType;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import io.reactivex.Observable;


/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class MotionSenseHrvPlus extends Device {

    public MotionSenseHrvPlus(RxBleClient rxBleClient, DeviceSettings deviceSettings) {
        super(rxBleClient, deviceSettings);
    }
    @Override
    protected Observable<RxBleConnection> setConfiguration(RxBleConnection rxBleConnection) {
        return Observable.just(rxBleConnection);
    }

    @Override
    protected LinkedHashMap<SensorType, SensorInfo> createSensorInfo() {
        LinkedHashMap<SensorType, SensorInfo> sensorInfoArrayList = new LinkedHashMap<>();
        if(deviceSettings.isAccelerometerEnable()) sensorInfoArrayList.put(SensorType.ACCELEROMETER, createAccelerometerInfo());
        if(deviceSettings.isQuaternionEnable()) sensorInfoArrayList.put(SensorType.QUATERNION, createQuaternionInfo());
        if(deviceSettings.isSequenceNumberMotionEnable()) sensorInfoArrayList.put(SensorType.MOTION_SEQUENCE_NUMBER, createMotionSequenceNumberInfo(1023));
        if(deviceSettings.isRawMotionEnable()) sensorInfoArrayList.put(SensorType.MOTION_RAW, createMotionRawInfo(20));
        if(deviceSettings.isAccelerometerDataQualityEnable()) sensorInfoArrayList.put(SensorType.ACCELEROMETER_DATA_QUALITY, createAccelerometerDataQualityInfo());
        if(deviceSettings.isBatteryEnable()) sensorInfoArrayList.put(SensorType.BATTERY, createBatteryInfo());
        if(deviceSettings.isPpgEnable()) sensorInfoArrayList.put(SensorType.PPG, createPPGInfo("measure the value of ppg (red, infrared, green)", new String[]{"red", "infrared", "green"}));
        if(deviceSettings.isPpgDataQualityEnable()) sensorInfoArrayList.put(SensorType.PPG_DATA_QUALITY, createPPGDataQualityInfo());
        if(deviceSettings.isMagnetometerEnable()) sensorInfoArrayList.put(SensorType.MAGNETOMETER, createMagnetometerInfo());
        if(deviceSettings.isMagnetometerSensitivityEnable()) sensorInfoArrayList.put(SensorType.MAGNETOMETER_SENSITIVITY, createMagnetometerSensitivityInfo());
        if(deviceSettings.isSequenceNumberMagnetometerEnable()) sensorInfoArrayList.put(SensorType.MAGNETOMETER_SEQUENCE_NUMBER, createMagnetometerSequenceNumberInfo(1023));
        if(deviceSettings.isRawMagnetometerEnable()) sensorInfoArrayList.put(SensorType.MAGNETOMETER_RAW, createMagnetometerRawInfo(17));
        return sensorInfoArrayList;
    }

    @Override
    protected ArrayList<Characteristics> createCharacteristics() {
        ArrayList<Characteristics> characteristics = new ArrayList<>();
        if (deviceSettings.isAccelerometerEnable() || deviceSettings.isQuaternionEnable() || deviceSettings.isRawMotionEnable() || deviceSettings.isSequenceNumberMotionEnable() || deviceSettings.isAccelerometerDataQualityEnable() || deviceSettings.isPpgEnable() || deviceSettings.isPpgDataQualityEnable())
            characteristics.add(new CharacteristicMotion(deviceSettings.getRawMotionSampleRate()));
        if (deviceSettings.isBatteryEnable())
            characteristics.add(new CharacteristicBatteryV1V2());
        if(deviceSettings.isMagnetometerEnable() || deviceSettings.isMagnetometerSensitivityEnable() || deviceSettings.isRawMagnetometerEnable()||deviceSettings.isSequenceNumberMagnetometerEnable())
            characteristics.add(new CharacteristicMagnetometer(deviceSettings.getRawMagnetometerSampleRate()));
        return characteristics;
    }

    @Override
    protected ArrayList<DataQuality> createDataQualities() {
        ArrayList<DataQuality> dataQualities = new ArrayList<>();
        if (deviceSettings.isAccelerometerDataQualityEnable())
            dataQualities.add(new DataQualityAccelerometer());
        if(deviceSettings.isPpgDataQualityEnable())
            dataQualities.add(new DataQualityPPG());
        return dataQualities;
    }
}
