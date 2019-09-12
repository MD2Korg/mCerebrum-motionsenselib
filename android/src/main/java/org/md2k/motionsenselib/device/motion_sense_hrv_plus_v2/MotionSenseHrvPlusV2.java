package org.md2k.motionsenselib.device.motion_sense_hrv_plus_v2;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselib.device.CharacteristicMagnetometerV2;
import org.md2k.motionsenselib.device.Characteristics;
import org.md2k.motionsenselib.settings.DeviceSettings;
import org.md2k.motionsenselib.device.SensorInfo;
import org.md2k.motionsenselib.device.SensorType;
import org.md2k.motionsenselib.device.motion_sense_hrv_v2.MotionSenseHrvV2;

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
public class MotionSenseHrvPlusV2 extends MotionSenseHrvV2 {
    private CharacteristicConfigV2MotionSenseHrvPlus characteristicConfig;

    public MotionSenseHrvPlusV2(RxBleClient rxBleClient, DeviceSettings deviceSettings) {
        super(rxBleClient, deviceSettings);
        characteristicConfig = new CharacteristicConfigV2MotionSenseHrvPlus();
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
        return sensorInfoArrayList;
    }

    @Override
    protected ArrayList<Characteristics> createCharacteristics() {
        ArrayList<Characteristics> characteristics = super.createCharacteristics();
        if(deviceSettings.isMagnetometerEnable()|| deviceSettings.isRawMagnetometerEnable()|| deviceSettings.isSequenceNumberMagnetometerEnable()){
            characteristics.add(new CharacteristicMagnetometerV2(deviceSettings.getRawMagnetometerSampleRate()));
        }

        return characteristics;
    }

    @Override
    protected Observable<RxBleConnection> setConfiguration(RxBleConnection rxBleConnection) {
        return characteristicConfig.setConfiguration(rxBleConnection, deviceSettings);
    }

}
