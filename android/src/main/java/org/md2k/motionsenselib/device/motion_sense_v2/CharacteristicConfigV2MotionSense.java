package org.md2k.motionsenselib.device.motion_sense_v2;


import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselib.device.CharacteristicConfigV2;
import org.md2k.motionsenselib.settings.DeviceSettings;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

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
class CharacteristicConfigV2MotionSense extends CharacteristicConfigV2 {

    @Override
    protected Observable<RxBleConnection> setConfiguration(final RxBleConnection rxBleConnection, final DeviceSettings deviceSettings) {
        return setSensorEnableObservable(rxBleConnection, deviceSettings.isAccelerometerEnable(), deviceSettings.isGyroscopeEnable(), false, false)
                .flatMap((Function<byte[], Single<byte[]>>) bytes -> {
                    if (deviceSettings.isAccelerometerEnable() || deviceSettings.isGyroscopeEnable())
                        return setSamplingRateObservable(rxBleConnection, deviceSettings.getRawMotionSampleRate(), 0);
                    else return Single.just(new byte[0]);
                })
                .flatMap((Function<byte[], Single<byte[]>>) bytes -> {
                    if (deviceSettings.isGyroscopeEnable())
                        return setSensitivityObservable(rxBleConnection, deviceSettings.getAccelerometerSensitivity(),deviceSettings.getGyroscopeSensitivity());
                    else return Single.just(new byte[0]);
                })
                .flatMap((Function<byte[], Single<byte[]>>) bytes -> setMinimumConnectionInterval(rxBleConnection, deviceSettings.getMinConnectionInterval())).flatMapObservable((Function<byte[], ObservableSource<RxBleConnection>>) bytes -> Observable.just(rxBleConnection));
    }
}
