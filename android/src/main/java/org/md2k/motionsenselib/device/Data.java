package org.md2k.motionsenselib.device;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.HashMap;

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
public class Data {
    private SensorType sensorType;
    private long timestamp;
    private double[] sample;

    public Data(SensorType sensorType, long timestamp, double[] sample) {
        this.sensorType = sensorType;
        this.timestamp = timestamp;
        this.sample = sample;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double[] getSample() {
        return  sample;
    }

    @NonNull
    @Override
    public String toString(){
        StringBuilder sampleStr = new StringBuilder(String.valueOf(timestamp));
            double[] s =getSample();
        for (double v : s) {
            sampleStr.append(",").append(String.valueOf(v));
        }
        return sensorType.name()+","+sampleStr;
    }
    public HashMap<String, Object> toMap(){
        HashMap<String, Object> h = new HashMap<>();
        h.put("sensorType", sensorType.name());
        h.put("timestamp", timestamp);
        h.put("sample", sample);
        return h;
    }
}
