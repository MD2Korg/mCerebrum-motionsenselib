package org.md2k.motionsenselib.device.v2.motion_sense_2;

import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselib.device.Characteristics;
import org.md2k.motionsenselib.device.Data;
import org.md2k.motionsenselib.device.SensorType;
import org.md2k.motionsenselib.device.v2.CharacteristicsV2;
import org.md2k.motionsenselib.log.MyLog;

import java.util.ArrayList;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

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
class CharacteristicMotionV2 extends CharacteristicsV2 {
    private static final UUID CHARACTERISTICS = UUID.fromString("DA39C921-1D81-48E2-9C68-D0AE4BBD351F");

    private int accelerometerSensitivity;
    private int gyroscopeSensitivity;

    CharacteristicMotionV2(double frequency, boolean correctTimestamp, int accelerometerSensitivity, int gyroscopeSensitivity) {
        super(frequency, correctTimestamp);
        this.accelerometerSensitivity = accelerometerSensitivity;
        this.gyroscopeSensitivity = gyroscopeSensitivity;
    }


    @Override
    public Observable<ArrayList<Data>> listen(RxBleConnection rxBleConnection) {
        final int[] lastSequenceNumber = {-1};
        final long[] lastCorrectedTimestamp = {-1};
        return getCharacteristicListener(rxBleConnection, CHARACTERISTICS)
                .flatMap((Function<byte[], Observable<ArrayList<Data>>>) bytes -> {
                    long curTime = System.currentTimeMillis();
                    ArrayList<Data> data = new ArrayList<>();
                    try {
                        int sequenceNumber = getSequenceNumber(bytes);
                        long correctTimeStamp = correctTimeStamp(sequenceNumber, lastSequenceNumber[0], curTime, lastCorrectedTimestamp[0]);
                        data.add(new Data(SensorType.ACCELEROMETER, correctTimeStamp, getAccelerometer(bytes, accelerometerSensitivity)));
                        data.add(new Data(SensorType.GYROSCOPE, correctTimeStamp, getGyroscope(bytes, gyroscopeSensitivity)));
                        data.add(new Data(SensorType.MOTION_RAW, curTime, getRaw(bytes)));
                        data.add(new Data(SensorType.MOTION_SEQUENCE_NUMBER, correctTimeStamp, new double[]{sequenceNumber}));
                        lastCorrectedTimestamp[0] = correctTimeStamp;
                        lastSequenceNumber[0] = sequenceNumber;
                    } catch (Exception e) {
                        MyLog.error(this.getClass().getName(), "listen()", "packet exception: " + e.getMessage());
                    }
                    return Observable.just(data);
                });
    }


    private double[] getAccelerometer(byte[] bytes, double acl_sensitivity) {
        double[] sample = new double[3];
        sample[0] = ((short) ((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff)) * acl_sensitivity / 32768.0;
        sample[1] = ((short) ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff)) * acl_sensitivity / 32768.0;
        sample[2] = ((short) ((bytes[4] & 0xff) << 8) | (bytes[5] & 0xff)) * acl_sensitivity / 32768.0;
        return sample;
    }


    private double[] getGyroscope(byte[] bytes, double gyro_sensitivity) {
        double[] sample = new double[3];
        sample[0] = ((short) ((bytes[6] & 0xff) << 8) | (bytes[7] & 0xff)) * gyro_sensitivity / 32768.0;
        sample[1] = ((short) ((bytes[8] & 0xff) << 8) | (bytes[9] & 0xff)) * gyro_sensitivity / 32768.0;
        sample[2] = ((short) ((bytes[10] & 0xff) << 8) | (bytes[11] & 0xff)) * gyro_sensitivity / 32768.0;
        return sample;
    }


    private int getSequenceNumber(byte[] data) {
        return ((data[data.length - 2] & 0xff) << 8) | (data[data.length - 1] & 0xff);
    }
}
