package org.md2k.motionsenselib.device.v2.motion_sense_2;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselib.device.CharacteristicBatteryV1V2;
import org.md2k.motionsenselib.device.Characteristics;
import org.md2k.motionsenselib.device.DataQuality;
import org.md2k.motionsenselib.device.DataQualityAccelerometer;
import org.md2k.motionsenselib.device.Device;
import org.md2k.motionsenselib.device.SensorInfo;
import org.md2k.motionsenselib.device.SensorType;
import org.md2k.motionsenselib.settings.DeviceSettings;

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
public class MotionSenseV2 extends Device {
    private CharacteristicConfigV2MotionSense characteristicConfig;

    public MotionSenseV2(RxBleClient rxBleClient, DeviceSettings deviceSettings) {
        super(rxBleClient, deviceSettings);
        characteristicConfig = new CharacteristicConfigV2MotionSense();
    }

    @Override
    protected LinkedHashMap<SensorType, SensorInfo> createSensorInfo() {
        LinkedHashMap<SensorType, SensorInfo> sensorInfoArrayList = new LinkedHashMap<>();
        if (deviceSettings.isAccelerometerEnable())
            sensorInfoArrayList.put(SensorType.ACCELEROMETER, createAccelerometerInfo());
        if (deviceSettings.isAccelerometerDataQualityEnable())
            sensorInfoArrayList.put(SensorType.ACCELEROMETER_DATA_QUALITY, createAccelerometerDataQualityInfo());
        if (deviceSettings.isGyroscopeEnable())
            sensorInfoArrayList.put(SensorType.GYROSCOPE, createGyroscopeInfo());
        if (deviceSettings.isSequenceNumberMotionEnable())
            sensorInfoArrayList.put(SensorType.MOTION_SEQUENCE_NUMBER, createMotionSequenceNumberInfo(65535));
        if (deviceSettings.isRawMotionEnable())
            sensorInfoArrayList.put(SensorType.MOTION_RAW, createMotionRawInfo(14));
        if (deviceSettings.isBatteryEnable())
            sensorInfoArrayList.put(SensorType.BATTERY, createBatteryInfo());
        return sensorInfoArrayList;
    }

    @Override
    protected ArrayList<Characteristics> createCharacteristics() {
        ArrayList<Characteristics> characteristics = new ArrayList<>();
        if (deviceSettings.isAccelerometerEnable() || deviceSettings.isGyroscopeEnable() || deviceSettings.isRawMotionEnable() || deviceSettings.isSequenceNumberMotionEnable() || deviceSettings.isAccelerometerDataQualityEnable())
            characteristics.add(new CharacteristicMotionV2(deviceSettings.getRawMotionSampleRate(), deviceSettings.getAccelerometerSensitivity(), deviceSettings.getGyroscopeSensitivity()));
        if (deviceSettings.isBatteryEnable())
            characteristics.add(new CharacteristicBatteryV1V2());

        return characteristics;
    }
    @Override
    protected ArrayList<DataQuality> createDataQualities() {
        ArrayList<DataQuality> dataQualities = new ArrayList<>();
        if (deviceSettings.isAccelerometerDataQualityEnable())
            dataQualities.add(new DataQualityAccelerometer());
        return dataQualities;
    }
    @Override
    protected Observable<RxBleConnection> setConfiguration(RxBleConnection rxBleConnection) {
        return characteristicConfig.setConfiguration(rxBleConnection, deviceSettings);
    }
}
