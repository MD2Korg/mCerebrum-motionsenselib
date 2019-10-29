package org.md2k.motionsenselib.device.v1.motion_sense_hrv_plus;
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

import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselib.device.Data;
import org.md2k.motionsenselib.device.SensorType;
import org.md2k.motionsenselib.device.v1.CharacteristicsV1;
import org.md2k.motionsenselib.log.MyLog;

import java.util.ArrayList;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

class CharacteristicMagnetometer extends CharacteristicsV1 {
    private static final UUID CHARACTERISTICS = UUID.fromString("da39c924-1d81-48e2-9c68-d0ae4bbd351f");
    private static final int MAX_SEQUENCE_NUMBER = 1024;
    private static final double CHARACTERISTIC_FREQUENCY=12.5;

    CharacteristicMagnetometer(boolean correctTimestamp) {
        super(CHARACTERISTIC_FREQUENCY, correctTimestamp);
    }

    @Override
    public Observable<ArrayList<Data>> listen(RxBleConnection rxBleConnection) {
        final int[] lastSequenceNumber = {-1};
        final long[] lastCorrectedTimestamp = {-1};
        return getCharacteristicListener(rxBleConnection, CHARACTERISTICS)
                .flatMap((Function<byte[], Observable<ArrayList<Data>>>) bytes -> {
                    long curTime = System.currentTimeMillis();
                    ArrayList<Data> data = new ArrayList<Data>();

                    try {
                        int sequenceNumber = getSequenceNumber(bytes);
                        long timestamp = getTimestamp(sequenceNumber / 2, curTime, lastSequenceNumber[0], lastCorrectedTimestamp[0], MAX_SEQUENCE_NUMBER / 2);
                        data.add(new Data(SensorType.MAGNETOMETER, timestamp - (long) (1000.0 / (frequency * 2)), getMagnetometer1(bytes)));
                        data.add(new Data(SensorType.MAGNETOMETER, timestamp, getMagnetometer2(bytes)));
                        data.add(new Data(SensorType.MAGNETOMETER_SENSITIVITY, timestamp, getSensitivity(bytes)));
                        data.add(new Data(SensorType.MAGNETOMETER_RAW, curTime, getRaw(bytes)));
                        data.add(new Data(SensorType.MAGNETOMETER_SEQUENCE_NUMBER, timestamp, new double[]{sequenceNumber}));
                        lastCorrectedTimestamp[0] = timestamp;
                        lastSequenceNumber[0] = sequenceNumber;
                    } catch (Exception e) {
                        MyLog.error(this.getClass().getName(), "listen()", "packet exception: " + e.getMessage());
                    }
                    return Observable.just(data);
                });
    }

    private double[] getMagnetometer1(byte[] bytes) {
        double[] sample = new double[3];
        double[] sensitivity = getSensitivity(bytes);
        double sen_x = sensitivity[0];
        double sen_y = sensitivity[1];
        double sen_z = sensitivity[2];
        sample[0] = ((short) ((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff)) * ((sen_x - 128) * .5 / 128.0 + 1);
        sample[1] = ((short) ((bytes[4] & 0xff) << 8) | (bytes[5] & 0xff)) * ((sen_y - 128) * .5 / 128.0 + 1);
        sample[2] = ((short) ((bytes[8] & 0xff) << 8) | (bytes[9] & 0xff)) * ((sen_z - 128) * .5 / 128.0 + 1);
        return sample;
    }

    private double[] getMagnetometer2(byte[] bytes) {
        double[] sample = new double[3];
        double[] sensitivity = getSensitivity(bytes);
        double sen_x = sensitivity[0];
        double sen_y = sensitivity[1];
        double sen_z = sensitivity[2];
        sample[0] = ((short) ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff)) * ((sen_x - 128) * .5 / 128.0 + 1);
        sample[1] = ((short) ((bytes[6] & 0xff) << 8) | (bytes[7] & 0xff)) * ((sen_y - 128) * .5 / 128.0 + 1);
        sample[2] = ((short) ((bytes[10] & 0xff) << 8) | (bytes[11] & 0xff)) * ((sen_z - 128) * .5 / 128.0 + 1);
        return sample;
    }

    private double[] getSensitivity(byte[] bytes) {
        double[] sample = new double[3];
        sample[0] = bytes[12] & 0xff;
        sample[1] = bytes[13] & 0xff;
        sample[2] = bytes[14] & 0xff;
        return sample;
    }

    private int getSequenceNumber(byte[] data) {
        return ((data[data.length-2] & 0x03) << 8) | (data[data.length-1] & 0xff);
    }
}
