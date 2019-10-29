package org.md2k.motionsenselib.settings;


public class DeviceSettings {
    private String platformType;
    private String platformId;
    private String deviceId;
    private String version;

    private boolean correctTimestamp;
    private double characteristicMotionSampleRate;
    private double characteristicPpgSampleRate;

    private boolean accelerometerEnable;
    private int accelerometerSensitivity;
    private boolean gyroscopeEnable;
    private int gyroscopeSensitivity;
    private boolean quaternionEnable;
    private boolean sequenceNumberMotionEnable;
    private boolean rawMotionEnable;
    private boolean ppgEnable;
    private int ppgRed;
    private int ppgGreen;
    private int ppgInfrared;
    private boolean ppgFilterEnable;
    private boolean rawPpgEnable;
    private boolean sequenceNumberPpgEnable;
    private boolean magnetometerEnable;
    private boolean magnetometerSensitivityEnable;
    private boolean sequenceNumberMagnetometerEnable;
    private boolean rawMagnetometerEnable;
    private boolean respirationEnable;
    private boolean ecgEnable;
    private boolean batteryEnable;


    private boolean accelerometerDataQualityEnable;
    private boolean ppgDataQualityEnable;
    private boolean respirationDataQualityEnable;
    private boolean ecgDataQualityEnable;

    private int minConnectionInterval;

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

    public boolean isCorrectTimestamp() {
        return correctTimestamp;
    }

    public double getCharacteristicMotionSampleRate() {
        return characteristicMotionSampleRate;
    }

    public double getCharacteristicPpgSampleRate() {
        return characteristicPpgSampleRate;
    }

    public boolean isAccelerometerEnable() {
        return accelerometerEnable;
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

    public int getGyroscopeSensitivity() {
        return gyroscopeSensitivity;
    }

    public boolean isQuaternionEnable() {
        return quaternionEnable;
    }

    public boolean isSequenceNumberMotionEnable() {
        return sequenceNumberMotionEnable;
    }

    public boolean isRawMotionEnable() {
        return rawMotionEnable;
    }

    public boolean isBatteryEnable() {
        return batteryEnable;
    }

    public boolean isPpgEnable() {
        return ppgEnable;
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

    public boolean isSequenceNumberPpgEnable() {
        return sequenceNumberPpgEnable;
    }

    public boolean isMagnetometerEnable() {
        return magnetometerEnable;
    }

    public boolean isMagnetometerSensitivityEnable() {
        return magnetometerSensitivityEnable;
    }

    public boolean isSequenceNumberMagnetometerEnable() {
        return sequenceNumberMagnetometerEnable;
    }

    public boolean isRawMagnetometerEnable() {
        return rawMagnetometerEnable;
    }

    public int getMinConnectionInterval() {
        return minConnectionInterval;
    }

    public boolean isRespirationEnable() {
        return respirationEnable;
    }

    public boolean isEcgEnable() {
        return ecgEnable;
    }

    public boolean isRespirationDataQualityEnable() {
        return respirationDataQualityEnable;
    }

    public boolean isEcgDataQualityEnable() {
        return ecgDataQualityEnable;
    }
}