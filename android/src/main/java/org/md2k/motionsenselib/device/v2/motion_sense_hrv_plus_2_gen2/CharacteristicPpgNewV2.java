package org.md2k.motionsenselib.device.v2.motion_sense_hrv_plus_2_gen2;
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

import android.util.Log;

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

class CharacteristicPpgNewV2 extends CharacteristicsV2 {
    private static final UUID CHARACTERISTICS = UUID.fromString("DA39C925-1D81-48E2-9C68-D0AE4BBD351F");
    CharacteristicPpgNewV2(double frequency, boolean correctTimestamp) {
        super(frequency, correctTimestamp);
    }

    @Override
    public Observable<ArrayList<Data>> listen(RxBleConnection rxBleConnection) {
        final int[] lastSequenceNumber = {-1};
        final long[] lastCorrectedTimestamp = {-1};
        return getCharacteristicListener(rxBleConnection, CHARACTERISTICS)
                .flatMap((Function<byte[], Observable<ArrayList<Data>>>) bytes -> {
                    long curTime = System.currentTimeMillis();
                    ArrayList<Data> data = new ArrayList<>();
                    try{
                    int sequenceNumber = getSequenceNumber(bytes);
                    long correctedTimestamp = correctTimeStamp(sequenceNumber, lastSequenceNumber[0], curTime, lastCorrectedTimestamp[0]);
                    data.add(new Data(SensorType.PPG, correctedTimestamp, getPPG(bytes)));
                    data.add(new Data(SensorType.PPG_RAW, curTime, getRaw(bytes)));
                    data.add(new Data(SensorType.PPG_SEQUENCE_NUMBER, correctedTimestamp, new double[]{sequenceNumber}));
                    lastCorrectedTimestamp[0] = correctedTimestamp;
                    lastSequenceNumber[0] = sequenceNumber;
                    } catch (Exception e) {
                        MyLog.error(this.getClass().getName(), "listen()", "packet exception: " + e.getMessage());
                    }
                    return Observable.just(data);
                });
    }

    /**
     * Infra-red 1: bytes 13-11
     * Infra-red 2: bytes 10-8
     * Green/Red 1: bytes 7-5
     * Green/Red 2: bytes 4-2
     * Counter: bytes 1-0
     */
    /**
     * each ppg dc value is of type 32-bit single-precision float sent over the channels in an
     * unsigned uInt8 array, floatCast of size 4. The format is little endian. So, for example, in
     * Channel Infra-red floatCast[0] corresponds to the MSB and floatCast[3] is the LSB. The
     * counter is also in little-endian form
     */

    private double[] getPPG(byte[] bytes) {
        double[] sample = new double[4];
        sample[0] = ((bytes[0] & 0xff)<<11) | ((bytes[1] & 0xff) <<3) | ((bytes[2] & 0xe0)>>5);
        sample[1] = ((bytes[2] & 0x1f)<<14) | ((bytes[3] & 0xff) <<6) | ((bytes[4] & 0xf8)>>3);
        sample[2] = ((bytes[4] & 0x02)<<17) | ((bytes[5] & 0xff) <<9) | ((bytes[6] & 0xff)<<1) | ((bytes[7] & 0x80)>>1);
        sample[3] = ((bytes[7] & 0x7f)<<12) | ((bytes[8] & 0xff) <<4) | ((bytes[9] & 0xf0)>>4);
        return sample;
    }

    private int getSequenceNumber(byte[] data) {
        return ((data[data.length - 2] & 0xff) << 8) | (data[data.length - 1] & 0xff);
    }
}
