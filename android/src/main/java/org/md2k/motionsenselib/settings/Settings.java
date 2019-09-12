package org.md2k.motionsenselib.settings;

import java.util.ArrayList;

public class Settings {
    private boolean motionsense_enable;
    private boolean motionsense_debugEnable;
    private int motionsense_requiredDeviceNo;
    private ArrayList<DeviceSettings> motionsense_devices;
    private ArrayList<DeviceSettings> motionsense_defaultSettings;

    public boolean isMotionsense_enable() {
        return motionsense_enable;
    }

    public boolean isMotionsense_debugEnable() {
        return motionsense_debugEnable;
    }

    public int getMotionsense_requiredDeviceNo() {
        return motionsense_requiredDeviceNo;
    }

    public ArrayList<DeviceSettings> getMotionsense_devices() {
        return motionsense_devices;
    }

    public ArrayList<DeviceSettings> getMotionsense_defaultSettings() {
        return motionsense_defaultSettings;
    }
}
