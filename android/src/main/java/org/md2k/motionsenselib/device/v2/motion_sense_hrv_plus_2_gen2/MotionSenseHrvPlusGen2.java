package org.md2k.motionsenselib.device.v2.motion_sense_hrv_plus_2_gen2;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselib.device.Characteristics;
import org.md2k.motionsenselib.device.DataQuality;
import org.md2k.motionsenselib.device.DataQualityAccelerometer;
import org.md2k.motionsenselib.device.DataQualityPPG;
import org.md2k.motionsenselib.device.SensorInfo;
import org.md2k.motionsenselib.device.SensorType;
import org.md2k.motionsenselib.device.v2.motion_sense_2.MotionSenseV2;
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
class MotionSenseHrvPlusGen2 extends MotionSenseV2 {
    private CharacteristicConfigV2MotionSenseHRVPlusGen2 characteristicConfig;

    MotionSenseHrvPlusGen2(RxBleClient rxBleClient, DeviceSettings deviceSettings) {
        super(rxBleClient, deviceSettings);
        characteristicConfig = new CharacteristicConfigV2MotionSenseHRVPlusGen2();
    }
     LinkedHashMap<SensorType, SensorInfo> createPPGSensorInfoList(){
         LinkedHashMap<SensorType, SensorInfo> sensorInfoArrayList = new LinkedHashMap<>();
         if (deviceSettings.isPpgFilterEnable()) {
             if (deviceSettings.isPpgEnable()) {
                 sensorInfoArrayList.put(SensorType.PPG_FILTERED, createPPGFilteredInfo("measure the filtered value of ppg (infrared, red)", new String[]{"infrared1", "infrared2", "red1", "red2"}));
                 sensorInfoArrayList.put(SensorType.PPG_DC, createPPGDcInfo("measure the dc value of ppg (infrared, red)", new String[]{"infrared1", "infrared2", "red1", "red2"}));
             }
             if (deviceSettings.isRawPpgEnable()) {
                 sensorInfoArrayList.put(SensorType.PPG_RAW, createPPGRawInfo(18));
                 sensorInfoArrayList.put(SensorType.PPG_DC_RAW, createPPGDcRawInfo(18));
             }
             if (deviceSettings.isSequenceNumberPpgEnable()) {
                 sensorInfoArrayList.put(SensorType.PPG_SEQUENCE_NUMBER, createPPGSequenceNumberInfo(65535));
                 sensorInfoArrayList.put(SensorType.PPG_DC_SEQUENCE_NUMBER, createPPGDcSequenceNumberInfo(65535));
             }
         } else {
             if (deviceSettings.isPpgEnable()) {
                 sensorInfoArrayList.put(SensorType.PPG, createPPGInfo("measure the value of ppg (infrared, red)", new String[]{"infrared1", "infrared2", "red1", "red2"}));
             }
             if (deviceSettings.isRawPpgEnable()) {
                 sensorInfoArrayList.put(SensorType.PPG_RAW, createPPGRawInfo(14));
             }
             if (deviceSettings.isSequenceNumberPpgEnable()) {
                 sensorInfoArrayList.put(SensorType.PPG_SEQUENCE_NUMBER, createPPGSequenceNumberInfo(65535));
             }
         }
         return sensorInfoArrayList;

     }

    @Override
    protected LinkedHashMap<SensorType, SensorInfo> createSensorInfo() {
        LinkedHashMap<SensorType, SensorInfo> sensorInfoArrayList = super.createSensorInfo();
        if (deviceSettings.isMagnetometerEnable())
            sensorInfoArrayList.put(SensorType.MAGNETOMETER, createMagnetometerInfo());
        if (deviceSettings.isRawMagnetometerEnable())
            sensorInfoArrayList.put(SensorType.MAGNETOMETER_RAW, createMagnetometerRawInfo(14));
        if (deviceSettings.isSequenceNumberMagnetometerEnable())
            sensorInfoArrayList.put(SensorType.MAGNETOMETER_SEQUENCE_NUMBER, createMagnetometerSequenceNumberInfo(65535));
        sensorInfoArrayList.putAll(createPPGSensorInfoList());
        return sensorInfoArrayList;
    }

    @Override
    protected ArrayList<Characteristics> createCharacteristics() {
        ArrayList<Characteristics> characteristics = super.createCharacteristics();
        if (deviceSettings.isPpgFilterEnable() && (deviceSettings.isPpgEnable() || deviceSettings.isRawPpgEnable() || deviceSettings.isPpgDataQualityEnable() || deviceSettings.isSequenceNumberPpgEnable())) {
            characteristics.add(new CharacteristicPPGFilteredNewV2(deviceSettings.getRawPpgSampleRate()));
            characteristics.add(new CharacteristicPPGFilteredDcNewV2(deviceSettings.getRawPpgSampleRate()));
        }
        if (!deviceSettings.isPpgFilterEnable() && (deviceSettings.isPpgEnable() || deviceSettings.isRawPpgEnable() || deviceSettings.isPpgDataQualityEnable() || deviceSettings.isSequenceNumberPpgEnable())) {
            characteristics.add(new CharacteristicPpgNewV2(deviceSettings.getRawPpgSampleRate()));
        }
        if(deviceSettings.isMagnetometerEnable()|| deviceSettings.isRawMagnetometerEnable()|| deviceSettings.isSequenceNumberMagnetometerEnable()){
            characteristics.add(new CharacteristicMagnetometerV2New(deviceSettings.getRawMagnetometerSampleRate()));
        }

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

    @Override
    protected Observable<RxBleConnection> setConfiguration(RxBleConnection rxBleConnection) {
        return characteristicConfig.setConfiguration(rxBleConnection, deviceSettings);
    }

}
