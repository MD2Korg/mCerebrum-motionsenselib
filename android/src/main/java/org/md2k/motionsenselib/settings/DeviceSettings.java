package org.md2k.motionsenselib.settings;


public class DeviceSettings {
    private String name;
    private String platformType;
    private String platformId;
    private String deviceId;
    private String version;

    private boolean accelerometerEnable;
    private double accelerometerSampleRate;
    private String accelerometerWriteType;
    private int accelerometerSensitivity;
    private boolean accelerometerDataQualityEnable;

    private boolean gyroscopeEnable;
    private double gyroscopeSampleRate;
    private String gyroscopeWriteType;
    private int gyroscopeSensitivity;

    private boolean quaternionEnable;
    private double quaternionSampleRate;
    private String quaternionWriteType;

    private boolean sequenceNumberMotionEnable;
    private double sequenceNumberMotionSampleRate;
    private String sequenceNumberMotionWriteType;
    private boolean rawMotionEnable;
    private double rawMotionSampleRate;
    private String rawMotionWriteType;

    private boolean batteryEnable;
    private double batterySampleRate;
    private String batteryWriteType;

    private boolean ppgEnable;
    private double ppgSampleRate;
    private String ppgWriteType;
    private int ppgRed;
    private int ppgGreen;
    private int ppgInfrared;
    private boolean ppgFilterEnable;
    private boolean rawPpgEnable;
    private double rawPpgSampleRate;
    private String rawPpgWriteType;
    private boolean sequenceNumberPpgEnable;
    private double sequenceNumberPpgSampleRate;
    private String sequenceNumberPpgWriteType;
    private boolean ppgDataQualityEnable;


    private boolean magnetometerEnable;
    private double magnetometerSampleRate;
    private String magnetometerWriteType;
    private boolean magnetometerSensitivityEnable;
    private double magnetometerSensitivitySampleRate;
    private String magnetometerSensitivityWriteType;
    private boolean sequenceNumberMagnetometerEnable;
    private double sequenceNumberMagnetometerSampleRate;
    private String sequenceNumberMagnetometerWriteType;
    private boolean rawMagnetometerEnable;
    private double rawMagnetometerSampleRate;
    private String rawMagnetometerWriteType;

    private int minConnectionInterval;

    public String getName() {
        return name;
    }

    public String getPlatformType() {
        return platformType;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getVersion() {
        return version;
    }

    public boolean isAccelerometerEnable() {
        return accelerometerEnable;
    }

    public double getAccelerometerSampleRate() {
        return accelerometerSampleRate;
    }

    public String getAccelerometerWriteType() {
        return accelerometerWriteType;
    }

    public int getAccelerometerSensitivity() {
        return accelerometerSensitivity;
    }

    public boolean isAccelerometerDataQualityEnable() {
        return accelerometerDataQualityEnable;
    }

    public boolean isGyroscopeEnable() {
        return gyroscopeEnable;
    }

    public double getGyroscopeSampleRate() {
        return gyroscopeSampleRate;
    }

    public String getGyroscopeWriteType() {
        return gyroscopeWriteType;
    }

    public int getGyroscopeSensitivity() {
        return gyroscopeSensitivity;
    }

    public boolean isQuaternionEnable() {
        return quaternionEnable;
    }

    public double getQuaternionSampleRate() {
        return quaternionSampleRate;
    }

    public String getQuaternionWriteType() {
        return quaternionWriteType;
    }

    public boolean isSequenceNumberMotionEnable() {
        return sequenceNumberMotionEnable;
    }

    public double getSequenceNumberMotionSampleRate() {
        return sequenceNumberMotionSampleRate;
    }

    public String getSequenceNumberMotionWriteType() {
        return sequenceNumberMotionWriteType;
    }

    public boolean isRawMotionEnable() {
        return rawMotionEnable;
    }

    public double getRawMotionSampleRate() {
        return rawMotionSampleRate;
    }

    public String getRawMotionWriteType() {
        return rawMotionWriteType;
    }

    public boolean isBatteryEnable() {
        return batteryEnable;
    }

    public double getBatterySampleRate() {
        return batterySampleRate;
    }

    public String getBatteryWriteType() {
        return batteryWriteType;
    }

    public boolean isPpgEnable() {
        return ppgEnable;
    }

    public double getPpgSampleRate() {
        return ppgSampleRate;
    }

    public String getPpgWriteType() {
        return ppgWriteType;
    }

    public int getPpgRed() {
        return ppgRed;
    }

    public int getPpgGreen() {
        return ppgGreen;
    }

    public int getPpgInfrared() {
        return ppgInfrared;
    }

    public boolean isPpgFilterEnable() {
        return ppgFilterEnable;
    }

    public boolean isPpgDataQualityEnable() {
        return ppgDataQualityEnable;
    }

    public boolean isRawPpgEnable() {
        return rawPpgEnable;
    }

    public double getRawPpgSampleRate() {
        return rawPpgSampleRate;
    }

    public String getRawPpgWriteType() {
        return rawPpgWriteType;
    }

    public boolean isSequenceNumberPpgEnable() {
        return sequenceNumberPpgEnable;
    }

    public double getSequenceNumberPpgSampleRate() {
        return sequenceNumberPpgSampleRate;
    }

    public String getSequenceNumberPpgWriteType() {
        return sequenceNumberPpgWriteType;
    }

    public boolean isMagnetometerEnable() {
        return magnetometerEnable;
    }

    public double getMagnetometerSampleRate() {
        return magnetometerSampleRate;
    }

    public String getMagnetometerWriteType() {
        return magnetometerWriteType;
    }

    public boolean isMagnetometerSensitivityEnable() {
        return magnetometerSensitivityEnable;
    }

    public double getMagnetometerSensitivitySampleRate() {
        return magnetometerSensitivitySampleRate;
    }

    public String getMagnetometerSensitivityWriteType() {
        return magnetometerSensitivityWriteType;
    }

    public boolean isSequenceNumberMagnetometerEnable() {
        return sequenceNumberMagnetometerEnable;
    }

    public double getSequenceNumberMagnetometerSampleRate() {
        return sequenceNumberMagnetometerSampleRate;
    }

    public String getSequenceNumberMagnetometerWriteType() {
        return sequenceNumberMagnetometerWriteType;
    }

    public boolean isRawMagnetometerEnable() {
        return rawMagnetometerEnable;
    }

    public double getRawMagnetometerSampleRate() {
        return rawMagnetometerSampleRate;
    }

    public String getRawMagnetometerWriteType() {
        return rawMagnetometerWriteType;
    }

    public int getMinConnectionInterval() {
        return minConnectionInterval;
    }

}
