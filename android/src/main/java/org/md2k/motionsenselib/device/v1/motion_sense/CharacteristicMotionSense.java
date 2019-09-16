package org.md2k.motionsenselib.device.v1.motion_sense;

import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselib.device.Characteristics;
import org.md2k.motionsenselib.device.Data;
import org.md2k.motionsenselib.device.SensorType;
import org.md2k.motionsenselib.device.v1.CharacteristicsV1;

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
class CharacteristicMotionSense extends CharacteristicsV1 {
    private static final UUID CHARACTERISTICS = UUID.fromString("DA39C921-1D81-48E2-9C68-D0AE4BBD351F");
    private static final int MAX_SEQUENCE_NUMBER = 65536;
    private double frequency;

    CharacteristicMotionSense(double frequency) {
        this.frequency = frequency;
    }

    @Override
    public Observable<Data> listen(RxBleConnection rxBleConnection) {
        final int[] lastSequenceNumber = {-1};
        final long[] lastCorrectedTimestamp = {-1};
        return getCharacteristicListener(rxBleConnection, CHARACTERISTICS)
                .flatMap((Function<byte[], Observable<Data>>) bytes -> {
                    long curTime = System.currentTimeMillis();
                    Data[] data = new Data[5];
                    int sequenceNumber = getSequenceNumber(bytes);
                    long correctedTimestamp = correctTimeStamp(sequenceNumber, curTime, lastSequenceNumber[0], lastCorrectedTimestamp[0], frequency, MAX_SEQUENCE_NUMBER);
                    data[0] = new Data(SensorType.ACCELEROMETER, correctedTimestamp, getAccelerometer(bytes));
                    data[1] = new Data(SensorType.GYROSCOPE, correctedTimestamp - (long) (1000.0 / (frequency * 2)), getGyroscope1(bytes));
                    data[2] = new Data(SensorType.GYROSCOPE, correctedTimestamp, getGyroscope2(bytes));
                    data[3] = new Data(SensorType.MOTION_RAW, curTime, getRaw(bytes));
                    data[4] = new Data(SensorType.MOTION_SEQUENCE_NUMBER, correctedTimestamp, new double[]{sequenceNumber});
                    lastCorrectedTimestamp[0] = correctedTimestamp;
                    lastSequenceNumber[0] = sequenceNumber;
                    return Observable.fromArray(data);
                });
    }

    private double[] getAccelerometer(byte[] bytes) {
        double[] sample = new double[3];
        sample[0] = 2.0*((short) ((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff))/16384.0;
        sample[1] = 2.0*((short) ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff))/16384.0;
        sample[2] = 2.0*((short) ((bytes[4] & 0xff) << 8) | (bytes[5] & 0xff))/16384.0;
        return sample;
    }


    private double[] getGyroscope1(byte[] bytes) {
        double[] sample = new double[3];
        sample[0] = 500.0*((short) ((bytes[6] & 0xff) << 8) | (bytes[7] & 0xff))/32768.0;
        sample[1] = 500.0*((short) ((bytes[8] & 0xff) << 8) | (bytes[9] & 0xff))/32768.0;
        sample[2] = 500.0*((short) ((bytes[10] & 0xff) << 8) | (bytes[11] & 0xff))/32768.0;
        return sample;
    }

    private double[] getGyroscope2(byte[] bytes) {
        double[] sample = new double[3];
        sample[0] = 500.0*((short) ((bytes[12] & 0xff) << 8) | (bytes[13] & 0xff))/32768.0;
        sample[1] = 500.0*((short) ((bytes[14] & 0xff) << 8) | (bytes[15] & 0xff))/32768.0;
        sample[2] = 500.0*((short) ((bytes[16] & 0xff) << 8) | (bytes[17] & 0xff))/32768.0;
        return sample;
    }

    private int getSequenceNumber(byte[] data) {
        return ((data[data.length-2] & 0xff) << 8) | (data[data.length-1] & 0xff);
    }

}
