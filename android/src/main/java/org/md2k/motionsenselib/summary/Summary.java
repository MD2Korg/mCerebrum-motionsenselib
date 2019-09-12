package org.md2k.motionsenselib.summary;

import org.md2k.motionsenselib.device.Device;
import org.md2k.motionsenselib.device.MotionSenseManager;
import org.md2k.motionsenselib.device.SensorInfo;

import java.util.ArrayList;

public class Summary {
    private long runningTime;
    private boolean isRunning;
    private int deviceNo;
    private ArrayList<SensorSummary> sensorSummary;
    public static Summary create(){
        Summary summary = new Summary();
        summary.runningTime = MotionSenseManager.getRunningTime();
        summary.isRunning = summary.runningTime != -1;
        summary.deviceNo = MotionSenseManager.getDeviceNo();
        summary.sensorSummary = new ArrayList<>();
        ArrayList<Device> devices = MotionSenseManager.getDevices();
        for(int i=0;i<devices.size();i++){
            ArrayList<SensorInfo> sensors = devices.get(i).getSensorInfo();
            for(int j=0;j< sensors.size();j++){
                SensorSummary s = new SensorSummary();
                s.count=sensors.get(j).getCount();
                s.deviceId = devices.get(i).getDeviceSettings().getDeviceId();
                s.platformType = devices.get(i).getDeviceSettings().getPlatformType();
                s.platformId = devices.get(i).getDeviceSettings().getPlatformId();
                s.lastSampleTime = sensors.get(j).getLastSampleTime();
                s.lastSample = sensors.get(j).getLastSample();
                s.sensorType = sensors.get(j).getSensorType().name();
                s.sensorName = sensors.get(j).getTitle();
                if(sensors.get(j).getStartSampleTime()>0){
                    s.frequency = (s.count*1000.0)/(System.currentTimeMillis()-sensors.get(j).getStartSampleTime());
                }else s.frequency = 0;
                summary.sensorSummary.add(s);
            }
        }
        return summary;
    }
}
