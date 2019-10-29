package org.md2k.motionsenselib.device.v1;
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


import org.md2k.motionsenselib.device.Characteristics;

public abstract class CharacteristicsV1 extends Characteristics {
    public CharacteristicsV1(double frequency, boolean correctTimestamp){
        super(frequency, correctTimestamp);
    }
    protected long getTimestamp(int curSequence, long curTimestamp, int lastSequenceNumber, long lastTimestamp, int maxLimit) {
        if(!correctTimestamp) return curTimestamp;
        if (lastSequenceNumber == -1)
            return curTimestamp;
        int diff = (curSequence - lastSequenceNumber + maxLimit) % maxLimit;
        long predictedTimestamp = (long) (lastTimestamp + (1000.0 * diff) / frequency);
        if (curTimestamp < predictedTimestamp || curTimestamp - predictedTimestamp > 2000)
            predictedTimestamp = curTimestamp;
        return predictedTimestamp;
    }
}
