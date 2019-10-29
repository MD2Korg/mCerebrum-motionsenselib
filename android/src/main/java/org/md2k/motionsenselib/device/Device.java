package org.md2k.motionsenselib.device;

import android.util.Log;

import androidx.annotation.NonNull;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;

import org.md2k.motionsenselib.device.v1.motion_sense.MotionSense;
import org.md2k.motionsenselib.device.v1.motion_sense_hrv.MotionSenseHrv;
import org.md2k.motionsenselib.device.v1.motion_sense_hrv_plus.MotionSenseHrvPlus;
import org.md2k.motionsenselib.device.v2.motion_sense_hrv_plus_2.MotionSenseHrvPlusV2;
import org.md2k.motionsenselib.device.v2.motion_sense_hrv_plus_2_gen2.MotionSenseHrvPlusGen2IG;
import org.md2k.motionsenselib.device.v2.motion_sense_hrv_plus_2_gen2.MotionSenseHrvPlusGen2IR;
import org.md2k.motionsenselib.device.v2.motion_sense_hrv_2.MotionSenseHrvV2;
import org.md2k.motionsenselib.device.v2.motion_sense_2.MotionSenseV2;
import org.md2k.motionsenselib.log.MyLog;
import org.md2k.motionsenselib.settings.DeviceSettings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;


/**
 * Copyright (c) 2015, The University of Memphis, MD2K Center
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p/>
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * <p/>
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p/>
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
public abstract class Device {
    private LinkedHashMap<SensorType, SensorInfo> sensorInfoArrayList;
    protected DeviceSettings deviceSettings;
    private ArrayList<DataQuality> dataQualities;
    private HashSet<ReceiveCallback> receiveCallbacks;
    private Disposable connectionDisposable;
    private Disposable connectionStatusDisposable;
    private RxBleClient rxBleClient;
    private Disposable dataQualityDisposable;
    private boolean isStarted;

    protected abstract Observable<RxBleConnection> setConfiguration(RxBleConnection rxBleConnection);

    private Observable<ArrayList<Data>> getSensingObservable(RxBleConnection rxBleConnection) {
        ArrayList<Characteristics> characteristics = createCharacteristics();
        if (characteristics.size() == 0) return Observable.empty();
        @SuppressWarnings("unchecked")
        Observable<ArrayList<Data>>[] observables = new Observable[characteristics.size()];
        for (int i = 0; i < characteristics.size(); i++) {
            observables[i] = (characteristics.get(i).listen(rxBleConnection));
        }
        return Observable.mergeArray(observables);
    }

    private Observable<Data> getDataQualityObservable() {
        dataQualities = createDataQualities();
        if (dataQualities == null || dataQualities.size() == 0) return Observable.empty();
        @SuppressWarnings("unchecked")
        Observable<Data>[] observables = new Observable[dataQualities.size()];
        for (int i = 0; i < dataQualities.size(); i++)
            observables[i] = dataQualities.get(i).getObservable();
        return Observable.mergeArray(observables);
    }

    abstract protected LinkedHashMap<SensorType, SensorInfo> createSensorInfo();

    abstract protected ArrayList<Characteristics> createCharacteristics();

    abstract protected ArrayList<DataQuality> createDataQualities();


    public ArrayList<SensorInfo> getSensorInfo() {
        ArrayList<SensorInfo> list = new ArrayList<>();
        for (Map.Entry<SensorType, SensorInfo> s : sensorInfoArrayList.entrySet()) {
            list.add(s.getValue());
        }
        return list;
    }

    public SensorInfo getSensorInfo(SensorType sensorType) {
        return sensorInfoArrayList.get(sensorType);
    }

    static Device create(RxBleClient rxBleClient, DeviceSettings deviceSettings) {
        switch (deviceSettings.getPlatformType()) {
            case "MOTION_SENSE":
                return new MotionSense(rxBleClient, deviceSettings);
            case "MOTION_SENSE_HRV":
                return new MotionSenseHrv(rxBleClient, deviceSettings);
            case "MOTION_SENSE_HRV_PLUS":
                return new MotionSenseHrvPlus(rxBleClient, deviceSettings);
            case "MOTION_SENSE_V2":
                return new MotionSenseV2(rxBleClient, deviceSettings);
            case "MOTION_SENSE_HRV_V2":
                return new MotionSenseHrvV2(rxBleClient, deviceSettings);
            case "MOTION_SENSE_HRV_PLUS_V2":
                return new MotionSenseHrvPlusV2(rxBleClient, deviceSettings);
            case "MOTION_SENSE_HRV_PLUS_GEN2_GREEN":
                return new MotionSenseHrvPlusGen2IG(rxBleClient, deviceSettings);
            case "MOTION_SENSE_HRV_PLUS_GEN2_RED":
                return new MotionSenseHrvPlusGen2IR(rxBleClient, deviceSettings);
        }
        return null;
    }

    public DeviceSettings getDeviceSettings() {
        return deviceSettings;
    }

    protected Device(RxBleClient rxBleClient, DeviceSettings deviceSettings) {
        this.deviceSettings = deviceSettings;
        this.rxBleClient = rxBleClient;
        this.sensorInfoArrayList = createSensorInfo();
        receiveCallbacks = new HashSet<>();
    }


    private void startDataQuality() {
        MyLog.info(this.getClass().getName(), "startDataQuality()", "deviceId = " + deviceSettings.getDeviceId());

        dataQualityDisposable = getDataQualityObservable().map(new Function<Data, ArrayList<Data>>() {
            @Override
            public ArrayList<Data> apply(Data data) throws Exception {
                ArrayList<Data> l = new ArrayList<>();
                l.add(data);
                return l;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onConnectionReceived, this::onConnectionFailure);
    }

    private void connect() {
        RxBleDevice bleDevice = rxBleClient.getBleDevice(deviceSettings.getDeviceId());
        MyLog.info(this.getClass().getName(), "connect()", "deviceId = " + deviceSettings.getDeviceId() + " connectionStatus=" + bleDevice.getConnectionState().name());
        if (bleDevice.getConnectionState() != RxBleConnection.RxBleConnectionState.DISCONNECTED)
            return;
        connectionDisposable = Observable.timer(3, TimeUnit.SECONDS).flatMap((Function<Long, ObservableSource<RxBleConnection>>) aLong -> bleDevice.establishConnection(false)).map(new Function<RxBleConnection, RxBleConnection>() {
            @Override
            public RxBleConnection apply(RxBleConnection rxBleConnection) throws Exception {
                MyLog.info(this.getClass().getName(), "connect()", "deviceId = "+deviceSettings.getDeviceId()+" connectionStatus=CONNECTED");
                return rxBleConnection;
            }
        }).flatMap((Function<RxBleConnection, ObservableSource<RxBleConnection>>) this::setConfiguration).flatMap((Function<RxBleConnection, Observable<ArrayList<Data>>>) this::getSensingObservable)
                .buffer(500, TimeUnit.MILLISECONDS)
                .map(new Function<List<ArrayList<Data>>, ArrayList<Data>>() {
                    @Override
                    public ArrayList<Data> apply(List<ArrayList<Data>> data) throws Exception {
                        ArrayList<Data> arrayList = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++)
                            arrayList.addAll(data.get(i));
                        return arrayList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onConnectionReceived, this::onConnectionFailure);
    }

    private void initialize() {
        for (Map.Entry<SensorType, SensorInfo> s : sensorInfoArrayList.entrySet()) {
            s.getValue().setCount(0);
            s.getValue().setLastSample(null);
            s.getValue().setLastSampleTime(-1);
            s.getValue().setStartSampleTime(-1);

        }
    }
    private void connectionStatusListener(){
        RxBleDevice bleDevice = rxBleClient.getBleDevice(deviceSettings.getDeviceId());
        connectionStatusDisposable = Observable.merge(Observable.just(bleDevice.getConnectionState()), bleDevice.observeConnectionStateChanges()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxBleConnectionState -> {
                    MyLog.info(this.getClass().getName(), "connectionStatusListener()", "deviceId = "+deviceSettings.getDeviceId()+" connectionStatus = "+rxBleConnectionState.name());

                    if (rxBleConnectionState == RxBleConnection.RxBleConnectionState.DISCONNECTED) {
                        reconnect();
                    }

                }, throwable -> {
                    MyLog.info(this.getClass().getName(), "connectionStatusListener()", "deviceId = "+deviceSettings.getDeviceId()+" Exception:"+throwable.getMessage());
                });

    }

    void start() {
        MyLog.info(this.getClass().getName(), "start()", "deviceId = " + deviceSettings.getDeviceId() + " isStarted=" + isStarted);
        if (isStarted) return;
        isStarted = true;
        initialize();
        startDataQuality();
        connectionStatusListener();
    }

    private void onConnectionReceived(ArrayList<Data> d) {
        Data data;

        for (int x = 0; x < d.size(); x++) {
            data = d.get(x);
            for (int i = 0; i < dataQualities.size(); i++) {
                dataQualities.get(i).addSample(data);
            }
            SensorInfo sensorInfo = sensorInfoArrayList.get(data.getSensorType());
            if (sensorInfo != null) {
                sensorInfo.setCount(sensorInfo.getCount() + 1);
                sensorInfo.setLastSample(data.getSample());
                sensorInfo.setLastSampleTime(data.getTimestamp());
                if (sensorInfo.getStartSampleTime() <= 0)
                    sensorInfo.setStartSampleTime(data.getTimestamp());
            }
        }
        for (ReceiveCallback receiveCallback : receiveCallbacks) {
            try {
                receiveCallback.onReceive(d);
            } catch (Exception e) {
                MyLog.error(this.getClass().getName(), "onConnectionReceived()", "deviceId = " + deviceSettings.getDeviceId() + " Exception=" +e.getMessage());
                receiveCallbacks.remove(receiveCallback);
            }
        }
    }
    private void onConnectionFailure(Throwable throwable) {
        MyLog.error(this.getClass().getName(), "onConnectionFailure()", "deviceId = " + deviceSettings.getDeviceId() + " Exception=" +throwable.toString());
/*
        if (dataQualityDisposable != null && !dataQualityDisposable.isDisposed())
            dataQualityDisposable.dispose();
        dataQualityDisposable = null;
        startDataQuality();
        reconnect();
*/
    }

    private void reconnect() {
        RxBleDevice bleDevice = rxBleClient.getBleDevice(deviceSettings.getDeviceId());
        MyLog.info(this.getClass().getName(), "reconnect()", "deviceId = " + deviceSettings.getDeviceId() + " connectionStatus=" + bleDevice.getConnectionState().name());
        if (bleDevice.getConnectionState() != RxBleConnection.RxBleConnectionState.DISCONNECTED)
            disconnect();
        else if (bleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.DISCONNECTED)
            connect();
        else
            MyLog.error(this.getClass().getName(), "reconnect()", "deviceId = " + deviceSettings.getDeviceId() + " connectionStatus=" + bleDevice.getConnectionState().name()+" nothing to do");
    }


    private void disconnect() {
        RxBleDevice bleDevice = rxBleClient.getBleDevice(deviceSettings.getDeviceId());
        MyLog.info(this.getClass().getName(), "disconnect()", "deviceId = " + deviceSettings.getDeviceId() + "connectionStatus=" + bleDevice.getConnectionState().name());
        if (bleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.DISCONNECTED)
            return;
        if (connectionDisposable == null) return;
        try {
            if (!connectionDisposable.isDisposed()) {
                connectionDisposable.dispose();
            }
        } catch (Exception e) {
            MyLog.error(this.getClass().getName(), "disconnect()", "deviceId = " + deviceSettings.getDeviceId() + " Exception = " + e.getMessage());
        }
        connectionDisposable = null;
    }

    public void addListener(@NonNull ReceiveCallback receiveCallback) {
        if (receiveCallbacks.contains(receiveCallback)) return;
        receiveCallbacks.add(receiveCallback);
    }

    public void removeListener(@NonNull ReceiveCallback receiveCallback) {
        receiveCallbacks.remove(receiveCallback);
    }

    void stop() {
        MyLog.info(this.getClass().getName(), "stop()", "deviceId = " + deviceSettings.getDeviceId() + " isStarted = "+isStarted);

        isStarted = false;
        if (connectionStatusDisposable != null && !connectionStatusDisposable.isDisposed()) {
            connectionStatusDisposable.dispose();
        }
        connectionStatusDisposable = null;
        if (dataQualityDisposable != null && !dataQualityDisposable.isDisposed())
            dataQualityDisposable.dispose();
        dataQualityDisposable = null;
        disconnect();
    }

    protected SensorInfo createAccelerometerInfo() {
        return SensorInfo.builder().setSensorType(SensorType.ACCELEROMETER).setTitle("Accelerometer").setDescription("measures the 3 axes acceleration (x,y,z) applied to the device in g").setFields(new Field[]{new Field("x", "x", "contains x axis acceleration"), new Field("y", "y", "contains y axis acceleration"), new Field("z", "z", "contains z axis acceleration")}).setUnit("g").setRange(-4, 4).build();
    }

    protected SensorInfo createAccelerometerDataQualityInfo() {
        return SensorInfo.builder().setSensorType(SensorType.ACCELEROMETER_DATA_QUALITY).setTitle("DataQuality (Acl)").setDescription("measures the data quality of accelerometer values: [0=GOOD, 1=NO_DATA, 2=NOT_WORN, 3=LOOSE_ATTACHMENT]").setFields(new Field[]{new Field("data_quality", "Data Quality (Acl)", "measures the data quality of accelerometer values: [0=GOOD, 1=NO_DATA, 2=NOT_WORN, 3=LOOSE_ATTACHMENT]")}).setUnit("number").setRange(0, 3).build();
    }

    protected SensorInfo createGyroscopeInfo() {
        return SensorInfo.builder().setSensorType(SensorType.GYROSCOPE).setTitle("Gyroscope").setDescription("measure the rate of rotation around the device's local X, Y and Z axis in degree/second").setFields(new Field[]{new Field("x", "x", "contains rotation in x axis"), new Field("y", "y", "contains rotation in y axis"), new Field("z", "z", "contains rotation in z axis")}).setUnit("degree/second").setRange(-1000, 1000).build();
    }

    protected SensorInfo createQuaternionInfo() {
        return SensorInfo.builder().setSensorType(SensorType.QUATERNION).setTitle("Quaternion").setDescription("measure the rate of rotation around the device\'s local X, Y and Z axis").setFields(new Field[]{new Field("x", "x", "contains rotation in x axis "), new Field("y", "y", "contains rotation in y axis"), new Field("z", "z", "contains rotation in z axis")}).setUnit("degree/second").setRange(-1, 1).build();
    }

    protected SensorInfo createMotionSequenceNumberInfo(int maxValue) {
        return SensorInfo.builder().setSensorType(SensorType.MOTION_SEQUENCE_NUMBER).setTitle("Sequence(M)").setDescription("sequence number of the motion packet").setFields(new Field[]{new Field("sequence_number", "Sequence Number (Motion)", "contains sequence number of the motion packet")}).setUnit("number").setRange(0, maxValue).build();
    }

    protected SensorInfo createMotionRawInfo(int length) {
        return SensorInfo.builder().setSensorType(SensorType.MOTION_RAW).setTitle("Raw (M)").setDescription("raw byte array from motion characteristics").setFields(fill(length)).setUnit("number").setRange(0, 255).build();
    }

    protected SensorInfo createBatteryInfo() {
        return SensorInfo.builder().setSensorType(SensorType.BATTERY).setTitle("Battery").setDescription("current battery level in percentage").setFields(new Field[]{new Field("battery", "Battery Level", "current battery level")}).setUnit("percentage").setRange(0, 100).build();
    }

    protected SensorInfo createMagnetometerInfo() {
        return SensorInfo.builder().setSensorType(SensorType.MAGNETOMETER).setTitle("Magnetometer").setDescription("measure magnetic field in the X, Y and Z axis in micro tesla").setFields(new Field[]{new Field("x", "x", "contains x axis magnetic field"), new Field("y", "y", "contains y axis magnetic field"), new Field("z", "z", "contains z axis magnetic field")}).setUnit("micro tesla").setRange(-400, 400).build();
    }

    protected SensorInfo createMagnetometerSensitivityInfo() {
        return SensorInfo.builder().setSensorType(SensorType.MAGNETOMETER_SENSITIVITY).setTitle("Mag. Sensitivity").setDescription("measure sensitivity of the magnetic field in m").setFields(new Field[]{new Field("x", "x", "contains magnetic sensitivity in x axis"), new Field("y", "y", "contains magnetic sensitivity in y axis"), new Field("z", "z", "contains magnetic sensitivity in z axis")}).setUnit("micro tesla").setRange(-400, 400).build();
    }

    protected SensorInfo createMagnetometerSequenceNumberInfo(int maxValue) {
        return SensorInfo.builder().setSensorType(SensorType.MAGNETOMETER_SEQUENCE_NUMBER).setTitle("Sequence(Mag)").setDescription("sequence number of the magnetometer packet").setFields(new Field[]{new Field("sequence_number", "Sequence Number (Mag)", "contains sequence number of the magnetometer packet")}).setUnit("number").setRange(0, maxValue).build();
    }

    protected SensorInfo createMagnetometerRawInfo(int length) {
        return SensorInfo.builder().setSensorType(SensorType.MAGNETOMETER_RAW).setTitle("Raw (Mag)").setDescription("raw byte array from magnetometer characteristics").setFields(fill(length)).setUnit("number").setRange(0, 255).build();
    }

    protected SensorInfo createPPGInfo(String description, String[] fields) {

        return SensorInfo.builder().setSensorType(SensorType.PPG).setTitle("Ppg").setDescription(description).setFields(createField(fields)).setUnit("number").setRange(0, 250000).build();
    }

    protected SensorInfo createPPGFilteredInfo(String description, String[] fields) {
        return SensorInfo.builder().setSensorType(SensorType.PPG_FILTERED).setTitle("Ppg Filtered").setDescription(description).setFields(createField(fields)).setUnit("number").setRange(-250, 250).build();
    }

    protected SensorInfo createPPGDataQualityInfo() {
        return SensorInfo.builder().setSensorType(SensorType.PPG_DATA_QUALITY).setTitle("DataQuality (ppg)").setDescription("measures the data quality of ppg: [0=GOOD, 1=NO_DATA, 2=NOT_WORN, 3=LOOSE_ATTACHMENT]").setFields(new Field[]{new Field("data_quality", "Data Quality (ppg)", "measures the data quality of ppg values: [0=GOOD, 1=NO_DATA, 2=NOT_WORN, 3=LOOSE_ATTACHMENT]")}).setUnit("number").setRange(0, 3).build();
    }

    protected SensorInfo createPPGSequenceNumberInfo(int maxValue) {
        return SensorInfo.builder().setSensorType(SensorType.PPG_SEQUENCE_NUMBER).setTitle("Sequence(ppg)").setDescription("sequence number of the ppg packet").setFields(new Field[]{new Field("sequence_number", "Sequence Number (PPG)", "contains sequence number of the ppg packet")}).setUnit("number").setRange(0, maxValue).build();
    }

    protected SensorInfo createPPGRawInfo(int length) {
        return SensorInfo.builder().setSensorType(SensorType.PPG_RAW).setTitle("Raw (ppg)").setDescription("raw byte array from ppg characteristics").setFields(fill(length)).setUnit("number").setRange(0, 255).build();
    }

    protected SensorInfo createPPGDcInfo(String description, String[] fields) {
        return SensorInfo.builder().setSensorType(SensorType.PPG_DC).setTitle("Ppg DC").setDescription(description).setFields(createField(fields)).setUnit("number").setRange(-500, 500).build();
    }

    protected SensorInfo createPPGDcSequenceNumberInfo(int length) {
        return SensorInfo.builder().setSensorType(SensorType.PPG_DC_SEQUENCE_NUMBER).setTitle("Sequence (ppg dc)").setDescription("sequence number of the ppg dc packet").setFields(new Field[]{new Field("sequence_number", "Sequence Number (ppg dc)", "contains sequence number of the ppg dc packet")}).setUnit("number").setRange(0, length).build();
    }

    protected SensorInfo createPPGDcRawInfo(int length) {
        return SensorInfo.builder().setSensorType(SensorType.PPG_DC_RAW).setTitle("Raw (PPG DC)").setDescription("raw byte array from ppg dc characteristics").setFields(fill(length)).setUnit("number").setRange(0, 255).build();
    }
    protected SensorInfo createRespirationInfo() {
        return SensorInfo.builder().setSensorType(SensorType.RESPIRATION).setTitle("Respiration").setDescription("measures respiration").setFields(new Field[]{new Field("respiration", "Respiration", "Contains respiration value"),new Field("baseline", "Baseline", "Contains respiration baseline value")}).setUnit("number").setRange(-5000,5000).build();
    }
    protected SensorInfo createEcgInfo() {
        return SensorInfo.builder().setSensorType(SensorType.ECG).setTitle("ECG").setDescription("measures ecg").setFields(new Field[]{new Field("ecg", "ECG", "Contains ecg value")}).setUnit("number").setRange(-5000,5000).build();
    }

    private Field[] createField(String[] f) {
        Field[] filled = new Field[f.length];
        for (int i = 0; i < f.length; i++) {
            filled[i] = new Field(f[i], f[i], f[i]);
        }
        return filled;
    }

    private Field[] fill(int length) {
        Field[] filled = new Field[length];
        for (int i = 0; i < length; i++) {
            filled[i] = new Field("byte_" + i, "Byte " + i, "Byte " + i);
        }
        return filled;
    }

}
