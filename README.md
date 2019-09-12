# MotionSense Devices
Service Characteristics: `0000180f-0000-1000-8000-00805f9b34fb`

## Search MotionSense Devices:

1. Match service uuid
2. [optional] Match device name: (`EETech_Motion`, `MotionSenseHRV`, `MotionSenseHRV+`, `MotionSense2`)

## Different types of MotionSense device:

| Device Type | Sensors | Version.Type (if exist) | Device Name | Configurable |
|---|---|---|---|---|
|MotionSense (V1)|acl+gyro|-|EETech_Motion|-|
|MotionSense (V2)|acl+gyro|0x03|MotionSense2|Yes|
|MotionSenseHRV (V1)|acl+gyro+ppg|-|MotionSenseHRV|-|
|MotionSenseHRV (V2)|acl+gyro+ppg|0x01|MotionSense2|Yes|
|MotionSenseHRV+ (V1)|acl+quaternion+ppg+magnetometer|-|MotionSenseHRV+|-
|MotionSenseHRV+ (V2)|acl+gyro+ppg+magnetometer|0x02|MotionSense2|Yes|
|MotionSenseHRV+Gen2 (Green)|acc + Gyro + GREEN PPG new hardware + magnetometer|0x05|MotionSense2|Yes|
|MotionSenseHRV+Gen2 (Red)|acc + Gyro + RED PPG new hardware + magnetometer|0x06|MotionSense2|Yes|

## Version
##### 1st Generation
Version number is not available. Use the device name to identify the type and version of the device.
##### 2nd Generation
Version number is only available for 2nd generation of MotionSense. The device is advertised as "MotionSense2".

| Characteristics (Read Only) | Data Size (bytes) | Data Format|
|---|---|---|
|`da39d600-1d81-48e2-9c68-d0ae4bbd351f`   | 4  | Major.Minor.Type.Patch <br>Ex: 4.1.1.18 (MotionSenseHRV_V2)  |

## Configuration
##### 1st Generation: (Not available)
##### 2nd Generation: 

Characteristics (read/write): `da39d650-1d81-48e2-9c68-d0ae4bbd351f`

|Command|Operation|Write Size(bytes)|Write|Returned Result Size|Returned Result|
|:---:|:---:|:---:|----|:---:|-----|
|-|previously issued valid command with zero-padding to the right |  | |10|
|0x00|Enable/disable sensors|2|0:0x00<br>1:0000gmpa <br>g=1, enable gyro<br>m=1, enable mag<br>p=1, enable ppg<br>a=1, enable acl<br><br><b>Example:</b><br>0x00 0x0B: enables gyroscope, ppg and accelerometer sensors<br>0x00 0x06: enables ppg, disables gyroscope (along with accelerometer and magnetometer)<br><br><b>Note 1:</b> In the current firmware the accelerometer cannot be independently turned on or off. The value in the accelerometer bit field mirrors the value set in Gyroscope bit automatically.<br><br><b>Note 2:</b> The magnetometer can be turned on only if the gyroscope bit is enabled. If the gyroscope bit is 0 then both the magnetometer and accelerometer cannot be turned on by setting the following bits|2|same as what is written|
|0x01| Configure ppg sensors - Controls the brightness of the LEDs  |4|<b>MotionSenseHRV+_V2 (red+ green+infrared)</b><br>0: 0x01 (config byte)<br>1:0-255 (RED)<br>2: 0-255 (Green)<br> 3: 0-255 (infrared)<br><br><b> MotionSenseHRV+_Gen2 (red+red+infrared/ green+green+infrared</b><br>0: 0x01 (config byte)<br>1:0-255 (RED/GREEN)<br>2: N/A<br> 3: 0-255 (infrared)<br><br><b>Note:</b> The recommended maximum values for Red/Green channel is 200 and Infrared is 100 expressed in decimal|4|same as what is written|
|0x02| Change sampling rate (acl/gyro, ppg)  |3|0: 0x02(config byte)<br>1: 1-5 (acl/gyro sampling rate)<br>2: 0x14/0x28 (ppg sampling rate)<br><br><b>Accelerometer/Gyroscope sampling rate:</b><br>0x05(any other value) = 25Hz<br>0x04 = 50Hz<br>0x03 = 62.5Hz<br>0x02 = 125Hz<br>0x01 = 250Hz<br><br><b>PPG sampling rate:</b><br>0x14 = 50Hz<br>0x28(any other value) = 25Hz<br><br><b>note:</b>The magnetometer sampling rate is fixed to 25Hz and the characteristic packet rate for magnetometer is 12.5 Hz such that the 2 magnetometer samples are packed in the same packet|3|same as what is written|
|0x03|  change sensitivity (acl, gyro) |3|0:0x03 (config byte)<br>1: 0-3 (gyroscope sensitivity)<br>2: 0-3 (Accelerometer sensitivity)<br><br><b>Gyroscope sensitivity:</b><br> 0x00 = ±250 dps<br>0x01(any other value) = ±500 dps<br>0x02 = ±1000 dps<br>0x03 = ±2000 dps<br><br><b>Accelerometer sensitivity:</b><br>0x00 = ±2g<br>0x01(any other value) = ±4g<br>0x02 = ±8g<br>0x03 = ±16g|3|same as what is written|
|0x04 <br>MotionSenseHRV+_V2|  Return current configuration and magnetometer sensitivity|2| 0:0x04 (config byte)<br>1:0-1 (configuration type)<br><br>0x00: return current running configuration on the same characteristic<br><br>0x01: return magnetometer sensitivity on the same characteristic|10|<b>Configuration Type: 0x00 (Current Configuration)</b><br>0: 0x00-0xFF (enable/disable sensors)<br>1: 0-255 (PPG Red)<br>2: 0-255 (PPG Green)<br>3: 0-255 (PPG infrared)<br>4: 1-5 (gyro sampling rate)<br>5: 0x14/0x28 (PPG sampling rate)<br>6: 0-3 (gyroscope sensitivity)<br>7: 0-3 (accelerometer sensitivity)<br> 8: 10-120 (min. connection interval)<br>9: 0-1 (PPG filter enabled)<br><br><b>Configuration Type: 0x01 (Magnetometer Sensitivity)</b><br>0: Hz_sensitivity<br>1: Hy_sensitivity<br>2: Hx_sensitivity<br>3-9: 0|
|0x04 <br>MotionSenseHRV+_Gen2|  Return current configuration | 1  | 0: 0x04  |10|0: 0x00-0xFF (enable/disable sensors)<br>1: 0-255 (PPG Red)<br>2: 0-255 (PPG Green)<br>3: 0-255 (PPG infrared)<br>4: 1-5 (gyro sampling rate)<br>5: 0x14/0x28 (PPG sampling rate)<br>6: 0-3 (gyroscope sensitivity)<br>7: 0-3 (accelerometer sensitivity)<br> 8: 10-120 (min. connection interval)<br>9: 0-1 (PPG filter enabled)
|0x05|Set minimum connection interval|  2 | 0:0x05 (config byte)<br>1:10-120 (min. connection interval in ms) <br><br><b>Note: </b>Minimum connection interval value should be between 10-120 ms|2|same as what is written|
|0x06|Set PPG filter| 2  |0: 0x06 (config byte)<br>1: 0-1 (enable filter)<br><br><b>Note:<br></b> Enable = 0, Disables bandpass filter in hardware and transmits raw values.<br><br>Enable = 1, Enables bandpass filter for the PPG signal. Setting this bit also enables the PPG DC characteristic, which computes the DC value of the PPG signal averaged over a window of 40 seconds|2|same as what is written|

## Default Configuration
|Device|Enable/Disable Sensor|PPG Red|PPG Green| PPG Infrared|Gyroscope Sampling rate|PPG Sampling rate|Gyroscope Sensitivity|Accelerometer Sensitivity|Min Connection Interval|PPG Filter enabled|
|---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|MotionSense|0x09|0x3F(X)|0xFF(X)|0x38(X)|5|0x28 (X)|0x01|0x01|0x0A|0|
|MotionSenseHRV|0x0B|0x3E|0x68|0x14|5|0x28|0x01|0x01|0x0A|0|
|MotionSenseHRV+|0x0F|0x3E|0x68 (X)|0x14|5|0x28|0x01|0x01|0x0A|0|
|MotionSenseHRV+ Gen2|0x0F|0x3E|0x68 (X)|0x14|5|0x28|0x01|0x01|0x0A|0|

## MotionSenseHRV+Gen2
### (Accelerometer & Gyroscope)
|Device|Characteristics|Size (in bytes)|Acl X|Acl Y|Acl Z|
|---|---|---|---|---|---|
|   |`DA39C921-1D81-48E2-9C68-D0AE4BBD351F`|14|01<br> (2's complement form)|
