package org.md2k.motionsenselib.device;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.polidea.rxandroidble2.LogConstants;
import com.polidea.rxandroidble2.LogOptions;
import com.polidea.rxandroidble2.RxBleClient;

import org.md2k.motionsenselib.device.v1.motion_sense.MotionSense;
import org.md2k.motionsenselib.log.MyLog;
import org.md2k.motionsenselib.settings.DeviceSettings;

import java.util.ArrayList;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;

public class MotionSenseManager {
    private static MotionSenseManager instance;
    private RxBleClient rxBleClient;
    private ArrayList<Device> devices;
    private long startTimestamp;

    public static void init(@NonNull Context context) {
        if (instance == null) {
            instance = new MotionSenseManager(context);
        }
    }

    private MotionSenseManager(Context context) {
        devices = new ArrayList<>();
        rxBleClient = RxBleClient.create(context);
        startTimestamp=-1;
        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
            MyLog.error(this.getClass().getCanonicalName(), "MotionSenseManager(Context context)","Undeliverable exception received "+e.getMessage());
        });
    }

    public static Device addDevice(@NonNull DeviceSettings deviceSettings) {
        for (int i = 0; i < instance.devices.size(); i++) {
            if (deviceSettings.getDeviceId().equals(instance.devices.get(i).deviceSettings.getDeviceId())) {
                return instance.devices.get(i);
            }
        }
        Device d = Device.create(instance.rxBleClient, deviceSettings);
        instance.devices.add(d);
        return d;
    }

    public static Device getDevice(@NonNull String deviceId) {
        for (int i = 0; i < instance.devices.size(); i++) {
            if (instance.devices.get(i).getDeviceSettings().getDeviceId().equals(deviceId))
                return instance.devices.get(i);
        }
        return null;
    }

    public static Device getDevice(@NonNull String platformType, String platformId) {
        for (int i = 0; i < instance.devices.size(); i++) {
            if (instance.devices.get(i).getDeviceSettings().getPlatformType().equals(platformType) && instance.devices.get(i).getDeviceSettings().getPlatformId().equals(platformId))
                return instance.devices.get(i);
        }
        return null;
    }

    public static int getDeviceNo() {
        return instance.devices.size();
    }

    public static void removeDevices() {
        for (int i = 0; i < instance.devices.size(); i++) {
            instance.devices.get(i).stop();
        }
        instance.devices.clear();
    }

    public static void removeDevice(@NonNull String deviceId) {
        int index = -1;
        for (int i = 0; i < instance.devices.size(); i++) {
            if (deviceId.equals(instance.devices.get(i).deviceSettings.getDeviceId())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            instance.devices.get(index).stop();
            instance.devices.remove(index);
        }
    }

    public static ArrayList<Device> getDevices() {
        return instance.devices;
    }

    public static void start() {
        MyLog.info(instance.getClass().getName(), "start()", "starting...deviceNo = "+instance.devices.size());
        if(instance.devices.size()==0){
            instance.startTimestamp=-1;
            return;
        }
        instance.startTimestamp = System.currentTimeMillis();
        for (int i = 0; i < instance.devices.size(); i++) {
            instance.devices.get(i).start();
        }
    }

    public static void stop() {
        MyLog.info(instance.getClass().getName(), "stop()", "stopping...deviceNo = "+instance.devices.size());
        instance.startTimestamp = -1;
        for (int i = 0; i < instance.devices.size(); i++) {
            instance.devices.get(i).stop();
        }
    }

    public static long getRunningTime() {
        if(instance.startTimestamp==-1) return -1;
        return System.currentTimeMillis()-instance.startTimestamp;
    }
}
