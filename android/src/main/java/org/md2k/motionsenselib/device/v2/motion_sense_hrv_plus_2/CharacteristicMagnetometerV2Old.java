package org.md2k.motionsenselib.device.v2.motion_sense_hrv_plus_2;
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

import org.md2k.motionsenselib.device.Characteristics;
import org.md2k.motionsenselib.device.Data;
import org.md2k.motionsenselib.device.SensorType;
import org.md2k.motionsenselib.device.v2.CharacteristicsV2;
import org.md2k.motionsenselib.log.MyLog;

import java.util.ArrayList;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

class CharacteristicMagnetometerV2Old extends CharacteristicsV2 {
    private static final UUID CHARACTERISTICS = UUID.fromString("DA39C924-1D81-48E2-9C68-D0AE4BBD351F");
    private static final double CHARACTERISTIC_FREQUENCY=12.5;

    CharacteristicMagnetometerV2Old(boolean correctTimestamp) {
        super(CHARACTERISTIC_FREQUENCY, correctTimestamp);
    }


    @Override
    public Observable<ArrayList<Data>> listen(RxBleConnection rxBleConnection) {
        final long[] lastCorrectedTimestamp = {-1};
        final int[] lastSequenceNumber = {-1};
        return readSensitivity(rxBleConnection).flatMapObservable(new Function<int[], ObservableSource<? extends ArrayList<Data>>>() {
            @Override
            public ObservableSource<? extends ArrayList<Data>> apply(int[] ints) throws Exception {
                return getCharacteristicListener(rxBleConnection, CHARACTERISTICS)
                        .flatMap((Function<byte[], Observable<ArrayList<Data>>>) bytes -> {
                            long curTime = System.currentTimeMillis();
                            ArrayList<Data> data = new ArrayList<>();
                            try {
                                double[] mag1 = getMagnetometer1(bytes, ints[0], ints[1], ints[2]);
                                double[] mag2 = getMagnetometer2(bytes, ints[0], ints[1], ints[2]);
                                double[] rawLast = getRaw(bytes);
                                int sequenceNumber = getSequenceNumber(bytes);
                                double[] sequenceNumberLast = new double[]{sequenceNumber};
                                long correctedTimestamp = correctTimeStamp(sequenceNumber, lastSequenceNumber[0], curTime, lastCorrectedTimestamp[0]);
                                long correctTimestamp2 = (long) (correctedTimestamp - (1000.0 / (frequency * 2.0)));
                                data.add(new Data(SensorType.MAGNETOMETER, correctedTimestamp, mag1));
                                data.add(new Data(SensorType.MAGNETOMETER, correctTimestamp2, mag2));
                                data.add(new Data(SensorType.MAGNETOMETER_RAW, curTime, rawLast));
                                data.add(new Data(SensorType.MAGNETOMETER_SEQUENCE_NUMBER, correctedTimestamp, sequenceNumberLast));
                                lastCorrectedTimestamp[0] = correctedTimestamp;
                                lastSequenceNumber[0] = sequenceNumber;
                            } catch (Exception e) {
                                MyLog.error(this.getClass().getName(), "listen()", "packet exception: " + e.getMessage());
                            }
                            return Observable.just(data);
                        });
            }
        });
    }

    private double[] getMagnetometer1(byte[] bytes, int sensitivityX, int sensitivityY, int sensitivityZ) {
        double[] sample = new double[3];
        sample[0] = convertADCtoSI((short) ((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff), sensitivityX);
        sample[1] = convertADCtoSI((short) ((bytes[4] & 0xff) << 8) | (bytes[5] & 0xff), sensitivityY);
        sample[2] = convertADCtoSI((short) ((bytes[8] & 0xff) << 8) | (bytes[9] & 0xff), sensitivityZ);
        return sample;
    }

    private double[] getMagnetometer2(byte[] bytes, int sensitivityX, int sensitivityY, int sensitivityZ) {
        double[] sample = new double[3];
        sample[0] = convertADCtoSI((short) ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff), sensitivityX);
        sample[1] = convertADCtoSI((short) ((bytes[6] & 0xff) << 8) | (bytes[7] & 0xff), sensitivityY);
        sample[2] = convertADCtoSI((short) ((bytes[10] & 0xff) << 8) | (bytes[11] & 0xff), sensitivityZ);
        return sample;
    }

    private double convertADCtoSI(double mag, int sensitivity) {
        return mag * (0.5 * (sensitivity - 128.0) / 128.0 + 1);
    }

    private int getSequenceNumber(byte[] data) {
        return ((data[12] & 0xff) << 8) | (data[13] & 0xff);
    }

    private Single<int[]> readSensitivity(RxBleConnection rxBleConnection) {
        UUID uuid = UUID.fromString("da39d600-1d81-48e2-9c68-d0ae4bbd351f");
        return rxBleConnection.readCharacteristic(uuid).map(new Function<byte[], int[]>() {
            @Override
            public int[] apply(byte[] bytes) throws Exception {
                int[] sensitivity = new int[3];
                sensitivity[0] = (((int) bytes[2]) & 0xff);
                sensitivity[1] = (((int) bytes[1]) & 0xff);
                sensitivity[2] = (((int) bytes[0]) & 0xff);
                return sensitivity;
            }
        });
    }

}
