package org.md2k.motionsenselib.device.v1.autosense;

import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselib.device.Data;
import org.md2k.motionsenselib.device.SensorType;
import org.md2k.motionsenselib.device.v1.CharacteristicsV1;

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
class CharacteristicAutoSense extends CharacteristicsV1 {
    private static final UUID CHARACTERISTICS = UUID.fromString("DA39C921-1D81-48E2-9C68-D0AE4BBD351F");
    private static final int MAX_SEQUENCE_NUMBER = 4096;
    private double[] ecgBuffer = new double[16];
    private static final double CHARACTERISTIC_FREQUENCY=25;

    CharacteristicAutoSense(boolean correctTimestamp) {
        super(CHARACTERISTIC_FREQUENCY, correctTimestamp);
    }

    @Override
    public Observable<ArrayList<Data>> listen(RxBleConnection rxBleConnection) {
        final int[] lastSequenceNumber = {-1};
        final long[] lastCorrectedTimestamp = {-1};
        return getCharacteristicListener(rxBleConnection, CHARACTERISTICS)
                .flatMap((Function<byte[], Observable<ArrayList<Data>>>) bytes -> {
                    long curTime = System.currentTimeMillis();
                    ArrayList<Data> data = new ArrayList<>();
                    int sequenceNumber = getSequenceNumber(bytes);
                    long correctedTimestamp = getTimestamp(sequenceNumber, curTime, lastSequenceNumber[0], lastCorrectedTimestamp[0], MAX_SEQUENCE_NUMBER);
                    data.add(new Data(SensorType.ACCELEROMETER, correctedTimestamp, getAccelerometer(bytes)));
                    data.add(new Data(SensorType.RESPIRATION, correctedTimestamp, getRespiration(bytes)));
                    data.add(new Data(SensorType.MOTION_RAW, curTime, getRaw(bytes)));
                    data.add(new Data(SensorType.MOTION_SEQUENCE_NUMBER, correctedTimestamp, new double[]{sequenceNumber}));
                    //32 ->                 push
                    //32, 33/34/35 ->       no push
                    //32, 37/38/39/.... ->  push       5,6,7,...
                    //33, 34/35 ->          no push
                    //33, 37/38/... ->      push        4,5,6,...
                    //34, 35 ->             no push
                    //34, 37/38/39.. ->     push        3,4,5,...
                    //35, 37/38/.. ->       push        2,3,4,...
                    if(sequenceNumber%4==0 || (lastSequenceNumber[0]!=-1 && sequenceNumber-lastSequenceNumber[0]>4-lastSequenceNumber[0]%4)){
                        for(int i=0;i<ecgBuffer.length;i++){
                            if(ecgBuffer[i]!=-1){
                                long time = lastCorrectedTimestamp[0]-10*(15-i);
                                data.add(new Data(SensorType.ECG, time, new double[]{ecgBuffer[i]}));
                                ecgBuffer[i]=-1;
                            }
                        }
                    }
                    addToECGBuffer(sequenceNumber, getECG(bytes));

                    lastCorrectedTimestamp[0] = correctedTimestamp;
                    lastSequenceNumber[0] = sequenceNumber;
                    return Observable.just(data);
                });
    }

    private double[] getAccelerometer(byte[] bytes) {
        double[] sample = new double[3];
        sample[0] = 2.0*((short) ((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff))/16384.0;
        sample[1] = 2.0*((short) ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff))/16384.0;
        sample[2] = 2.0*((short) ((bytes[4] & 0xff) << 8) | (bytes[5] & 0xff))/16384.0;
        return sample;
    }
    private void addToECGBuffer(int curSeq, double[] ecgSample) {
        switch (curSeq % 4) {
            case 0:
                ecgBuffer[0] = ecgSample[0];
                ecgBuffer[4] = ecgSample[1];
                ecgBuffer[8] = ecgSample[2];
                ecgBuffer[12] = ecgSample[3];
                break;
            case 1:
                ecgBuffer[1] = ecgSample[0];
                ecgBuffer[5] = ecgSample[1];
                ecgBuffer[9] = ecgSample[2];
                ecgBuffer[13] = ecgSample[3];
                break;
            case 2:
                ecgBuffer[2] = ecgSample[0];
                ecgBuffer[6] = ecgSample[1];
                ecgBuffer[10] = ecgSample[2];
                ecgBuffer[14] = ecgSample[3];
                break;
            case 3:
                ecgBuffer[3] = ecgSample[0];
                ecgBuffer[7] = ecgSample[1];
                ecgBuffer[11] = ecgSample[2];
                ecgBuffer[15] = ecgSample[3];
                break;
        }
    }


    private int getSequenceNumber(byte[] data) {
        return ((data[data.length-2] & 0xff) << 8) | (data[data.length-1] & 0xff);
    }
    private double[] getRespiration(byte[] data) {
        double[] sample = new double[2];
        sample[0]=((data[13] & 0xf)<<8)|(data[14] & 0xff);
        sample[1]=((data[12] & 0xff)<<4)+((data[13] & 0xf0)>>4);
        return sample;
    }
    private double[] getECG(byte[] data) {
        double[] sample=new double[4];
        sample[0]=((data[6] & 0xff)<<8) | (data[7] & 0xff);
        sample[1]=((data[8] & 0xff)<<8) | (data[9] & 0xff);
        sample[2]=((data[10] & 0xff)<<8) | (data[11] & 0xff);
        sample[3]=((data[15] & 0xff)<<8) | (data[16] & 0xff);
        return sample;
    }

}
