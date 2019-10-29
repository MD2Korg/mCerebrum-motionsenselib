package org.md2k.motionsenselib.device;


import org.md2k.motionsenselib.log.MyLog;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
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
public class DataQualityECG extends DataQuality {
    private final static float MAGNITUDE_VARIANCE_THRESHOLD = (float) 0.0025;   //this threshold comes from the data we collect by placing the wrist sensor on table. It compares with the wrist accelerometer on-body from participant #11 (smoking pilot study)
    private static final int DELAY = 3000;
    private ArrayList<Double> samples=new ArrayList<>();

    @Override
    protected Observable<Data> getObservable(){
        long timestamp = System.currentTimeMillis();

        return Observable.interval(DELAY-timestamp%DELAY, DELAY, TimeUnit.MILLISECONDS).timeInterval().map(longTimed -> {
            DataQualityType dataQualityType = getStatus();
            samples.clear();
            return new Data(SensorType.ECG_DATA_QUALITY, (System.currentTimeMillis()/DELAY)*DELAY, new double[]{dataQualityType.getValue()});
        });
    }

    @Override
    protected void addSample(Data data){
        try {
            if (data.getSensorType() == SensorType.ECG) {
                double[] res = data.getSample();
                samples.add(res[0]);
            }
        }catch (Exception e){
            MyLog.error(this.getClass().getName(), "addSample()","Exception="+e.getMessage());
        }
    }
    private synchronized DataQualityType getStatus() {
            if(samples==null || samples.size()==0) return DataQualityType.NO_DATA;
            double sd =getStdDev(samples);
            if (sd < MAGNITUDE_VARIANCE_THRESHOLD)
                return DataQualityType.NOT_WORN;
            else return DataQualityType.GOOD;
    }

    private double getMean(ArrayList<Double> data) {
        double sum = 0.0;
        for (double a : data)
            sum += a;
        return sum / data.size();
    }

    private double getVariance(ArrayList<Double> data) {
        double mean = getMean(data);
        double temp = 0;
        for (double a : data)
            temp += (mean - a) * (mean - a);
        return temp / data.size();
    }

    private double getStdDev(ArrayList<Double> data) {
        return Math.sqrt(getVariance(data));
    }
}