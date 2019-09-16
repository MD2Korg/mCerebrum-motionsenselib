package org.md2k.motionsenselib.device.v2.motion_sense_hrv_plus_2_gen2;

import com.polidea.rxandroidble2.RxBleClient;

import org.md2k.motionsenselib.settings.DeviceSettings;
import org.md2k.motionsenselib.device.SensorInfo;
import org.md2k.motionsenselib.device.SensorType;
import java.util.LinkedHashMap;

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
public class MotionSenseHrvPlusGen2IG extends MotionSenseHrvPlusGen2 {

    public MotionSenseHrvPlusGen2IG(RxBleClient rxBleClient, DeviceSettings deviceSettings) {
        super(rxBleClient, deviceSettings);
    }

    @Override
    protected LinkedHashMap<SensorType, SensorInfo> createPPGSensorInfoList() {
        LinkedHashMap<SensorType, SensorInfo> sensorInfoArrayList = new LinkedHashMap<>();
        if (deviceSettings.isPpgFilterEnable()) {
            if (deviceSettings.isPpgEnable()) {
                sensorInfoArrayList.put(SensorType.PPG_FILTERED, createPPGFilteredInfo("measure the filtered value of ppg (infrared, green)", new String[]{"infrared1", "infrared2", "green1", "green2"}));
                sensorInfoArrayList.put(SensorType.PPG_DC, createPPGDcInfo("measure the dc value of ppg (infrared, green)", new String[]{"infrared1", "infrared2", "green1", "green2"}));
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
                sensorInfoArrayList.put(SensorType.PPG, createPPGInfo("measure the value of ppg (infrared, red)", new String[]{"infrared1", "infrared2", "green1", "green2"}));
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
}
