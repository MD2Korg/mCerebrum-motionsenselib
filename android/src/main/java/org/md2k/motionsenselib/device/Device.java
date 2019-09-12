package org.md2k.motionsenselib.device;

import android.util.Log;

import androidx.annotation.NonNull;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;

import org.md2k.motionsenselib.MSConstants;
import org.md2k.motionsenselib.device.motion_sense.MotionSense;
import org.md2k.motionsenselib.device.motion_sense_hrv.MotionSenseHrv;
import org.md2k.motionsenselib.device.motion_sense_hrv_plus.MotionSenseHrvPlus;
import org.md2k.motionsenselib.device.motion_sense_hrv_plus_2.MotionSenseHrvPlusV2;
import org.md2k.motionsenselib.device.motion_sense_hrv_plus_2_gen2.MotionSenseHrvPlusGen2;
import org.md2k.motionsenselib.device.motion_sense_hrv_plus_2_gen2.MotionSenseHrvPlusGen2IR;
import org.md2k.motionsenselib.device.motion_sense_hrv_2.MotionSenseHrvV2;
import org.md2k.motionsenselib.device.motion_sense_2.MotionSenseV2;
import org.md2k.motionsenselib.settings.DeviceSettings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
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

    private Observable<Data> getSensingObservable(RxBleConnection rxBleConnection) {
        ArrayList<Characteristics> characteristics = createCharacteristics();
        if (characteristics.size() == 0) return Observable.empty();
        @SuppressWarnings("unchecked")
        Observable<Data>[] observables = new Observable[characteristics.size()];
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
        switch(deviceSettings.getPlatformType()){
            case "MOTION_SENSE": return new MotionSense(rxBleClient, deviceSettings);
            case "MOTION_SENSE_HRV":return new MotionSenseHrv(rxBleClient, deviceSettings);
            case "MOTION_SENSE_HRV_PLUS": return new MotionSenseHrvPlus(rxBleClient, deviceSettings);
                case "MOTION_SENSE_V2":
                    return new MotionSenseV2(rxBleClient, deviceSettings);
                case "MOTION_SENSE_HRV_V2":
                    return new MotionSenseHrvV2(rxBleClient, deviceSettings);
                case "MOTION_SENSE_HRV_PLUS_V2":
                    return new MotionSenseHrvPlusV2(rxBleClient, deviceSettings);
                case "MOTION_SENSE_HRV_PLUS_GEN2_GREEN":
                    return new MotionSenseHrvPlusGen2(rxBleClient, deviceSettings);
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
        dataQualityDisposable = getDataQualityObservable()
                .subscribe(this::onConnectionReceived, this::onConnectionFailure);
    }

    private void connect() {
        RxBleDevice bleDevice = rxBleClient.getBleDevice(deviceSettings.getDeviceId());
        if(MSConstants.DEBUG) Log.e("error", "connect (start)=>  state = "+bleDevice.getConnectionState().name());
        if(bleDevice.getConnectionState()!= RxBleConnection.RxBleConnectionState.DISCONNECTED) return;
        connectionDisposable = Observable.timer(3, TimeUnit.SECONDS).flatMap((Function<Long, ObservableSource<RxBleConnection>>) aLong -> bleDevice.establishConnection(false)).flatMap((Function<RxBleConnection, ObservableSource<RxBleConnection>>) this::setConfiguration).flatMap((Function<RxBleConnection, Observable<Data>>) this::getSensingObservable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onConnectionReceived, this::onConnectionFailure);
    }
    private void initialize(){
        for(Map.Entry<SensorType, SensorInfo> s:sensorInfoArrayList.entrySet()){
            s.getValue().setCount(0);
            s.getValue().setLastSample(null);
            s.getValue().setLastSampleTime(-1);
            s.getValue().setStartSampleTime(-1);

        }
    }

    void start() {
        if (isStarted) return;
        isStarted = true;
        initialize();
        startDataQuality();
        RxBleDevice bleDevice = rxBleClient.getBleDevice(deviceSettings.getDeviceId());
        connectionStatusDisposable = Observable.merge(Observable.just(bleDevice.getConnectionState()), bleDevice.observeConnectionStateChanges()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxBleConnectionState -> {
                    if(MSConstants.DEBUG) Log.e("error", "ConnectionStatusListener: (onChange): status = " + rxBleConnectionState.toString());
                    if (rxBleConnectionState == RxBleConnection.RxBleConnectionState.DISCONNECTED) {
                        reconnect();
                    }
                }, throwable -> {
                    if(MSConstants.DEBUG) Log.d("abc", "error=" + throwable.toString());

                });
    }

    private void onConnectionReceived(Data data) {
//        data.setDeviceInfo(deviceInfo);
        for (int i = 0; i < dataQualities.size(); i++) {
            dataQualities.get(i).addSample(data);
        }
        SensorInfo sensorInfo = sensorInfoArrayList.get(data.getSensorType());
        if(sensorInfo!=null) {
            sensorInfo.setCount(sensorInfo.getCount() + 1);
            sensorInfo.setLastSample(data.getSample());
            sensorInfo.setLastSampleTime(data.getTimestamp());
            if(sensorInfo.getStartSampleTime()==0) sensorInfo.setStartSampleTime(data.getTimestamp());
            else
            if(MSConstants.DEBUG) Log.d("info",sensorInfo.getSensorType()+" "+(double)(sensorInfo.getCount())*1000.0/(sensorInfo.getLastSampleTime()-sensorInfo.getStartSampleTime()));
        }

        for (ReceiveCallback receiveCallback : receiveCallbacks) {
            try {
                receiveCallback.onReceive(data);
            } catch (Exception e) {
                if(MSConstants.DEBUG) Log.e("abc","onConnectionReceive: exception can't send data="+e.getMessage());
                receiveCallbacks.remove(receiveCallback);
            }
        }
    }

    private void reconnect() {
        RxBleDevice bleDevice = rxBleClient.getBleDevice(deviceSettings.getDeviceId());
        if(MSConstants.DEBUG) Log.e("error", "reconnect (start)=>  state = "+bleDevice.getConnectionState().name());
        if(bleDevice.getConnectionState()!= RxBleConnection.RxBleConnectionState.DISCONNECTED)
            disconnect();
        else if(bleDevice.getConnectionState()== RxBleConnection.RxBleConnectionState.DISCONNECTED)
            connect();
        else if(MSConstants.DEBUG) Log.e("error", "inside reconnect => ble is not disconnected. state = "+bleDevice.getConnectionState().name());
    }

    private void onConnectionFailure(Throwable throwable) {
        if(MSConstants.DEBUG) Log.e("error", "onConnectionFailure: " + throwable.getMessage());
        reconnect();
    }

    private void disconnect() {
        if(MSConstants.DEBUG) Log.d("abc", "disconnecting...");
        RxBleDevice bleDevice = rxBleClient.getBleDevice(deviceSettings.getDeviceId());
        if(MSConstants.DEBUG) Log.e("error", "disconnect (start)=>  state = "+bleDevice.getConnectionState().name());
        if(bleDevice.getConnectionState()== RxBleConnection.RxBleConnectionState.DISCONNECTED)
            return;
        if(connectionDisposable==null) return;
        try {
            if (!connectionDisposable.isDisposed()) {
                connectionDisposable.dispose();
            }
        }catch (Exception e){
            if(MSConstants.DEBUG) Log.e("abc", connectionDisposable.isDisposed() ?"not disposed": "null message=" + e.getMessage());
        }
        connectionDisposable = null;
        if(MSConstants.DEBUG) Log.e("error", "disconnect (end)=>  state = "+bleDevice.getConnectionState().name());
    }

    public void addListener(@NonNull ReceiveCallback receiveCallback) {
        if (receiveCallbacks.contains(receiveCallback)) return;
        receiveCallbacks.add(receiveCallback);
    }

    public void removeListener(@NonNull ReceiveCallback receiveCallback) {
        receiveCallbacks.remove(receiveCallback);
    }

    void stop() {
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
        return SensorInfo.builder().setSensorType(SensorType.ACCELEROMETER).setTitle("Accelerometer").setDescription("measures the 3 axes acceleration (x,y,z) applied to the device in g").setFields(new String[]{"X", "Y", "Z"}).setUnit("g").setRange(-4, 4).build();
    }

    protected SensorInfo createAccelerometerDataQualityInfo() {
        return SensorInfo.builder().setSensorType(SensorType.ACCELEROMETER_DATA_QUALITY).setTitle("DataQuality (Acl)").setDescription("measures the data quality of accelerometer values: [0=GOOD, 1=NO_DATA, 2=NOT_WORN, 3=LOOSE_ATTACHMENT]").setFields(new String[]{"DATA_QUALITY"}).setUnit("number").setRange(0, 3).build();
    }

    protected SensorInfo createGyroscopeInfo() {
        return SensorInfo.builder().setSensorType(SensorType.GYROSCOPE).setTitle("Gyroscope").setDescription("measure the rate of rotation around the device's local X, Y and Z axis in degree/second").setFields(new String[]{"X", "Y", "Z"}).setUnit("degree/second").setRange(-1000, 1000).build();
    }

    protected SensorInfo createQuaternionInfo() {
        return SensorInfo.builder().setSensorType(SensorType.QUATERNION).setTitle("Quaternion").setDescription("measure the rate of rotation around the device\'s local X, Y and Z axis").setFields(new String[]{"X", "Y", "Z"}).setUnit("degree/second").setRange(-1, 1).build();
    }

    protected SensorInfo createMotionSequenceNumberInfo(int maxValue) {
        return SensorInfo.builder().setSensorType(SensorType.MOTION_SEQUENCE_NUMBER).setTitle("Sequence(M)").setDescription("sequence number of the motion packet").setFields(new String[]{"SEQ"}).setUnit("number").setRange(0, maxValue).build();
    }

    protected SensorInfo createMotionRawInfo(int length) {
        return SensorInfo.builder().setSensorType(SensorType.MOTION_RAW).setTitle("Raw (M)").setDescription("raw byte array from motion characteristics").setFields(fill(length)).setUnit("number").setRange(0, 255).build();
    }

    protected SensorInfo createBatteryInfo() {
        return SensorInfo.builder().setSensorType(SensorType.BATTERY).setTitle("Battery").setDescription("current battery level in percentage").setFields(new String[]{"LEVEL"}).setUnit("percentage").setRange(0, 100).build();
    }

    protected SensorInfo createMagnetometerInfo() {
        return SensorInfo.builder().setSensorType(SensorType.MAGNETOMETER).setTitle("Magnetometer").setDescription("measure magnetic field in the X, Y and Z axis in micro tesla").setFields(new String[]{"X", "Y", "Z"}).setUnit("micro tesla").setRange(-400, 400).build();
    }

    protected SensorInfo createMagnetometerSensitivityInfo() {
        return SensorInfo.builder().setSensorType(SensorType.MAGNETOMETER_SENSITIVITY).setTitle("Mag. Sensitivity").setDescription("measure sensitivity of the magnetic field in m").setFields(new String[]{"X", "Y", "Z"}).setUnit("micro tesla").setRange(-400, 400).build();
    }

    protected SensorInfo createMagnetometerSequenceNumberInfo(int maxValue) {
        return SensorInfo.builder().setSensorType(SensorType.MAGNETOMETER_SEQUENCE_NUMBER).setTitle("Sequence(Mag)").setDescription("sequence number of the magnetometer packet").setFields(new String[]{"SEC"}).setUnit("number").setRange(0, maxValue).build();
    }

    protected SensorInfo createMagnetometerRawInfo(int length) {
        return SensorInfo.builder().setSensorType(SensorType.MAGNETOMETER_RAW).setTitle("Raw (Mag)").setDescription("raw byte array from magnetometer characteristics").setFields(fill(length)).setUnit("number").setRange(0, 255).build();
    }

    protected SensorInfo createPPGInfo(String description, String[] fields) {
        return SensorInfo.builder().setSensorType(SensorType.PPG).setTitle("Ppg").setDescription(description).setFields(fields).setUnit("number").setRange(0, 250000).build();
    }

    protected SensorInfo createPPGFilteredInfo(String description, String[] fields) {
        return SensorInfo.builder().setSensorType(SensorType.PPG_FILTERED).setTitle("Ppg Filtered").setDescription(description).setFields(fields).setUnit("number").setRange(-250, 250).build();
    }

    protected SensorInfo createPPGDataQualityInfo() {
        return SensorInfo.builder().setSensorType(SensorType.PPG_DATA_QUALITY).setTitle("DataQuality (ppg)").setDescription("measures the data quality of ppg: [0=GOOD, 1=NO_DATA, 2=NOT_WORN, 3=LOOSE_ATTACHMENT]").setFields(new String[]{"DATA_QUALITY"}).setUnit("number").setRange(0, 3).build();
    }

    protected SensorInfo createPPGSequenceNumberInfo(int maxValue) {
        return SensorInfo.builder().setSensorType(SensorType.PPG_SEQUENCE_NUMBER).setTitle("Sequence(ppg)").setDescription("sequence number of the ppg packet").setFields(new String[]{"SEQ"}).setUnit("number").setRange(0, maxValue).build();
    }

    protected SensorInfo createPPGRawInfo(int length) {
        return SensorInfo.builder().setSensorType(SensorType.PPG_RAW).setTitle("Raw (ppg)").setDescription("raw byte array from ppg characteristics").setFields(fill(length)).setUnit("number").setRange(0, 255).build();
    }

    protected SensorInfo createPPGDcInfo(String description, String[] fields) {
        return SensorInfo.builder().setSensorType(SensorType.PPG_DC).setTitle("Ppg DC").setDescription(description).setFields(fields).setUnit("number").setRange(-500, 500).build();
    }

    protected SensorInfo createPPGDcSequenceNumberInfo(int length) {
        return SensorInfo.builder().setSensorType(SensorType.PPG_DC_SEQUENCE_NUMBER).setTitle("Sequence (ppg dc)").setDescription("sequence number of the ppg dc packet").setFields(new String[]{"SEQ"}).setUnit("number").setRange(0, length).build();
    }

    protected SensorInfo createPPGDcRawInfo(int length) {
        return SensorInfo.builder().setSensorType(SensorType.PPG_DC_RAW).setTitle("Raw (PPG DC)").setDescription("raw byte array from ppg dc characteristics").setFields(fill(length)).setUnit("number").setRange(0, 255).build();
    }

    private String[] fill(int length) {
        String[] filled = new String[length];
        for (int i = 0; i < length; i++) {
            filled[i] = "C" + i;
        }
        return filled;
    }

}
