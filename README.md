# MotionSense Devices
Service Characteristics: `0000180f-0000-1000-8000-00805f9b34fb`

## Search MotionSense Devices:

1. Match service uuid (`0000180f-0000-1000-8000-00805f9b34fb`)
2. [optional] Match device name: (`EETech_Motion`, `MotionSenseHRV`, `MotionSenseHRV+`, `MotionSense2`)

## Different types of MotionSense devices:

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

## Packet Format
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"><link type="text/css" rel="stylesheet" href="resources/sheet.css" >
<style type="text/css">.ritz .waffle a { color: inherit; }.ritz .waffle .s0{border-bottom:1px SOLID #000000;border-right:1px SOLID #000000;background-color:#ffffff;text-align:center;font-weight:bold;color:#000000;font-family:'Courier New';font-size:11pt;vertical-align:middle;white-space:normal;overflow:hidden;word-wrap:break-word;direction:ltr;padding:0px 3px 0px 3px;}.ritz .waffle .s2{background-color:#ffffff;text-align:center;color:#000000;font-family:'docs-Calibri',Arial;font-size:11pt;vertical-align:middle;white-space:normal;overflow:hidden;word-wrap:break-word;direction:ltr;padding:0px 3px 0px 3px;}.ritz .waffle .s1{border-bottom:1px SOLID #000000;border-right:1px SOLID #000000;background-color:#ffffff;text-align:center;color:#000000;font-family:'Courier New';font-size:11pt;vertical-align:middle;white-space:normal;overflow:hidden;word-wrap:break-word;direction:ltr;padding:0px 3px 0px 3px;}</style><div class="ritz grid-container" dir="ltr"><table class="waffle" cellspacing="0" cellpadding="0"><thead><tr><th class="row-header"></th><th id="752998136C0" style="width:207px" class="column-headers-background">A</th><th id="752998136C1" style="width:228px" class="column-headers-background">B</th><th class="freezebar-cell frozen-column-cell freezebar-vertical-handle"></th><th id="752998136C2" style="width:339px" class="column-headers-background">C</th><th id="752998136C3" style="width:97px" class="column-headers-background">D</th><th id="752998136C4" style="width:111px" class="column-headers-background">E</th><th id="752998136C5" style="width:206px" class="column-headers-background">F</th><th id="752998136C6" style="width:150px" class="column-headers-background">G</th><th id="752998136C7" style="width:243px" class="column-headers-background">H</th><th id="752998136C8" style="width:152px" class="column-headers-background">I</th><th id="752998136C9" style="width:182px" class="column-headers-background">J</th><th id="752998136C10" style="width:142px" class="column-headers-background">K</th><th id="752998136C11" style="width:228px" class="column-headers-background">L</th><th id="752998136C12" style="width:71px" class="column-headers-background">M</th><th id="752998136C13" style="width:211px" class="column-headers-background">N</th><th id="752998136C14" style="width:103px" class="column-headers-background">O</th><th id="752998136C15" style="width:211px" class="column-headers-background">P</th><th id="752998136C16" style="width:66px" class="column-headers-background">Q</th><th id="752998136C17" style="width:224px" class="column-headers-background">R</th><th id="752998136C18" style="width:115px" class="column-headers-background">S</th><th id="752998136C19" style="width:189px" class="column-headers-background">T</th><th id="752998136C20" style="width:177px" class="column-headers-background">U</th><th id="752998136C21" style="width:227px" class="column-headers-background">V</th><th id="752998136C22" style="width:165px" class="column-headers-background">W</th><th id="752998136C23" style="width:219px" class="column-headers-background">X</th><th id="752998136C24" style="width:173px" class="column-headers-background">Y</th><th id="752998136C25" style="width:178px" class="column-headers-background">Z</th></tr></thead><tbody><tr style='height:19px;'><th id="752998136R0" style="height: 19px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 19px;">1</div></th><td class="s0">Device</td><td class="s0" dir="ltr"><br><br>Sensor<br><br></td><td class="freezebar-cell"></td><td class="s0">Characteristics</td><td class="s0">Frequency</td><td class="s0" dir="ltr">Data Size <br>(in bytes)</td><td class="s0" dir="ltr">Config parameters</td><td class="s0">0</td><td class="s0">1</td><td class="s0">2</td><td class="s0">3</td><td class="s0">4</td><td class="s0">5</td><td class="s0">6</td><td class="s0">7</td><td class="s0">8</td><td class="s0">9</td><td class="s0">10</td><td class="s0">11</td><td class="s0">12</td><td class="s0">13</td><td class="s0">14</td><td class="s0">15</td><td class="s0">16</td><td class="s0">17</td><td class="s0">18</td><td class="s0">19</td></tr><tr><th style="height:3px" class="freezebar-cell freezebar-horizontal-handle"></th><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td><td class="freezebar-cell"></td></tr><tr style='height:19px;'><th id="752998136R1" style="height: 19px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 19px;">2</div></th><td class="s1" dir="ltr">MotionSense</td><td class="s1" dir="ltr">Acl, Gyro1,Gyro2, Seq</td><td class="freezebar-cell"></td><td class="s1" dir="ltr" rowspan="4">DA39C921-1D81-48E2-9C68-D0AE4BBD351F</td><td class="s1" dir="ltr">16</td><td class="s1" dir="ltr">20</td><td class="s1" dir="ltr" rowspan="2">acl_sensitivity=4<br>gyro_sensitivity=500</td><td class="s1" dir="ltr" colspan="2" rowspan="4">16 bit Acl X <br>2&#39;s complement form<br><br>acl_x = ((short) ((bytes[0] &amp; 0xff) &lt;&lt; 8) | (bytes[1] &amp; 0xff))*acl_sensitivity/32768.0</td><td class="s1" dir="ltr" colspan="2" rowspan="4">16 bit Accel Y <br>2&#39;s complement form<br><br>acl_y=2.0*((short) ((bytes[2] &amp; 0xff) &lt;&lt; 8) | (bytes[3] &amp; 0xff))/16384.0<br>acl_y = ((short) ((bytes[2] &amp; 0xff) &lt;&lt; 8) | (bytes[3] &amp; 0xff))*acl_sensitivity/32768.0</td><td class="s1" dir="ltr" colspan="2" rowspan="4">16 bit Accel Z <br>2&#39;s complement form<br><br>acl_z = ((short) ((bytes[4] &amp; 0xff) &lt;&lt; 8) | (bytes[5] &amp; 0xff))*acl_sensitivity/32768.0</td><td class="s1" dir="ltr" colspan="2" rowspan="3">16 bit Gyro X<br>2&#39;s complement form<br><br>gyro_x=((short) ((bytes[6] &amp; 0xff) &lt;&lt; 8) | (bytes[7] &amp; 0xff))*gyro_sensitivity/32768.0</td><td class="s1" dir="ltr" colspan="2" rowspan="3">16 bit Gyro Y<br>2&#39;s complement form<br><br>gyro_y=((short) ((bytes[8] &amp; 0xff) &lt;&lt; 8) | (bytes[9] &amp; 0xff))*gyro_sensitivity/32768.0</td><td class="s1" dir="ltr" colspan="2" rowspan="3">16 bit Gyro Z<br>2&#39;s complement form<br><br>gyro_z=((short) ((bytes[10] &amp; 0xff) &lt;&lt; 8) | (bytes[11] &amp; 0xff))*gyro_sensitivity/32768.0</td><td class="s1" dir="ltr" colspan="2">16 bit Gyro X2<br>2&#39;s complement form<br><br>gyro_x2=500.0*((short) ((bytes[12] &amp; 0xff) &lt;&lt; 8) | (bytes[13] &amp; 0xff))/32768.0</td><td class="s1" dir="ltr" colspan="2">16 bit Gyro Y2<br>2&#39;s complement form<br><br>gyro_y2=500.0*((short) ((bytes[14] &amp; 0xff) &lt;&lt; 8) | (bytes[15] &amp; 0xff))/32768.0</td><td class="s1" dir="ltr" colspan="2">16 bit Gyro Z2<br>2&#39;s complement form<br><br>gyro_z2=500.0*((short) ((bytes[16] &amp; 0xff) &lt;&lt; 8) | (bytes[17] &amp; 0xff))/32768.0</td><td class="s1" dir="ltr" colspan="2">Packet Count[16:1]<br>range: 0-65535<br><br>seq = ((data[18] &amp; 0xff) &lt;&lt; 8) | (data[19] &amp; 0xff)</td></tr><tr style='height:114px;'><th id="752998136R2" style="height: 114px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 114px;">3</div></th><td class="s1" dir="ltr">MotionSenseHRV</td><td class="s1" dir="ltr">Acl, Gyro, PPG, Seq</td><td class="freezebar-cell"></td><td class="s1" dir="ltr">16</td><td class="s1" dir="ltr">20</td><td class="s1" dir="ltr" colspan="8">PPG Red 18 bit: 12[0-7], 13[0-7],14[6-7], red = ((bytes[12] &amp; 0xff)&lt;&lt;10) | ((bytes[13] &amp; 0xff) &lt;&lt;2) | ((bytes[14] &amp; 0xc0)&gt;&gt;6)<br>PPG Green 18 bit: 14[0-5],15[0-7],16[4-7], green = ((bytes[14] &amp; 0x3f)&lt;&lt;12) | ((bytes[15] &amp; 0xff) &lt;&lt;4) | ((bytes[16] &amp; 0xf0)&gt;&gt;4)<br>PPG Infrared 18 bit: 16[0-3],17[0-7],18[2-7], infrared = ((bytes[16] &amp; 0x0f)&lt;&lt;14) | ((bytes[17] &amp; 0xff) &lt;&lt;6) | ((bytes[18] &amp; 0xfc)&gt;&gt;2)<br>Packet Count 10 bit: 18[0-1], 19[0-7], seq = ((byte[18] &amp; 0x03)&lt;&lt;8) | (byte[19] &amp; 0xff)</td></tr><tr style='height:19px;'><th id="752998136R3" style="height: 19px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 19px;">4</div></th><td class="s1" dir="ltr">MotionSense_V2<br>MotionSenseHRV_V2<br>MotionSenseHRV+_V2<br>MotionSenseHRV+Gen2</td><td class="s1" dir="ltr">Acl, Gyro, Seq</td><td class="freezebar-cell"></td><td class="s1" dir="ltr">25/50/<br>62.5/125/<br>250</td><td class="s1">14</td><td class="s1" dir="ltr">acl_sensitivity = 2/4/8/16<br>gyro_sensitivity = 250/500/1000/2000</td><td class="s1" dir="ltr" colspan="2">Packet Count[16:1]<br>range: 0-65535<br><br>seq = ((data[18] &amp; 0xff) &lt;&lt; 8) | (data[19] &amp; 0xff)</td><td class="s1"></td><td class="s1"></td><td class="s1"></td><td class="s1"></td><td class="s1"></td><td class="s1"></td></tr><tr style='height:19px;'><th id="752998136R4" style="height: 19px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 19px;">5</div></th><td class="s1">MotionSenseHRV+</td><td class="s1" dir="ltr">Acl, Quaternion, PPG, Seq</td><td class="freezebar-cell"></td><td class="s1">25</td><td class="s1">20</td><td class="s1" dir="ltr">acl_sensitivity=4<br>gyro_sensitivity=500</td><td class="s1" dir="ltr" colspan="2">16 bit Quaternion X<br>q_x= ((short)((bytes[6] &amp; 0xff) &lt;&lt; 8) | (bytes[7] &amp; 0xff))*2.0/65535.0-1</td><td class="s1" dir="ltr" colspan="2">16 bit Quaternion Y<br>q_y = ((short)((bytes[8] &amp; 0xff) &lt;&lt; 8) | (bytes[9] &amp; 0xff))*2.0/65535.0-1;</td><td class="s1" dir="ltr" colspan="2">16 bit Quaternion Z<br>q_z = ((short)((bytes[10] &amp; 0xff) &lt;&lt; 8) | (bytes[11] &amp; 0xff))*2.0/65535.0-1</td><td class="s1">HRV sensor Channel 1[18 : 11]</td><td class="s1">HRV sensor Channel 1[10 : 3]</td><td class="s1">MSB 2 Bits<br>HRV sensor Channel 1[2 : 1]<br><br>Remaining 6 Bits<br>HRV sensor Channel 2[18 : 13]</td><td class="s1">HRV sensor Channel 2[12 : 5]</td><td class="s1">MSB 4 Bits<br>HRV sensor Channel 2[4 : 1]<br><br>Remaining 4 Bits<br>HRV sensor Channel 3[18 : 15]</td><td class="s1">HRV sensor Channel 3[14 : 7]</td><td class="s1">MSB 6 Bits<br>HRV sensor Channel 3[6 : 1]<br><br>Remaining 2 Bits<br>Packet Count[10:9]</td><td class="s1">Packet Count[8:1]</td></tr><tr style='height:19px;'><th id="752998136R5" style="height: 19px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 19px;">6</div></th><td class="s1" dir="ltr">MotionSenseHRV_V2<br>MotionSenseHRV+_V2</td><td class="s1" dir="ltr">PPG, Seq</td><td class="freezebar-cell"></td><td class="s1" dir="ltr">DA39C925-1D81-48E2-9C68-D0AE4BBD351F</td><td class="s1" dir="ltr">25/50</td><td class="s1" dir="ltr">9</td><td class="s1" dir="ltr">filter=disabled</td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1"></td><td class="s1"></td><td class="s1"></td></tr><tr style='height:19px;'><th id="752998136R6" style="height: 19px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 19px;">7</div></th><td class="s1" dir="ltr">MotionSenseHRV_V2<br>MotionSenseHRV+_V2</td><td class="s1" dir="ltr">PPG, Seq</td><td class="freezebar-cell"></td><td class="s1" dir="ltr">DA39C925-1D81-48E2-9C68-D0AE4BBD351F</td><td class="s1" dir="ltr">25/50</td><td class="s1" dir="ltr">14</td><td class="s1" dir="ltr">filter=enabled</td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1"></td><td class="s1"></td><td class="s1"></td></tr><tr style='height:19px;'><th id="752998136R7" style="height: 19px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 19px;">8</div></th><td class="s1" dir="ltr">MotionSenseHRV_V2<br>MotionSenseHRV+_V2</td><td class="s1" dir="ltr">PPG_DC, Seq</td><td class="freezebar-cell"></td><td class="s1" dir="ltr">DA39C926-1D81-48E2-9C68-D0AE4BBD351F</td><td class="s1" dir="ltr">25/50</td><td class="s1" dir="ltr">14</td><td class="s1" dir="ltr">filter=enabled</td><td class="s1" dir="ltr" colspan="4">RED</td><td class="s1" dir="ltr" colspan="4">Infrared</td><td class="s1" dir="ltr" colspan="4">Green</td><td class="s1" dir="ltr" colspan="2">Packet Count[16:1]<br>range: 0-65535<br><br>seq = ((data[18] &amp; 0xff) &lt;&lt; 8) | (data[19] &amp; 0xff)</td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1"></td><td class="s1"></td><td class="s1"></td></tr><tr style='height:19px;'><th id="752998136R8" style="height: 19px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 19px;">9</div></th><td class="s1" dir="ltr">MotionSenseHRV+Gen2</td><td class="s1" dir="ltr">PPG, Seq</td><td class="freezebar-cell"></td><td class="s1" dir="ltr">DA39C925-1D81-48E2-9C68-D0AE4BBD351F</td><td class="s1" dir="ltr">25/50</td><td class="s1" dir="ltr">12</td><td class="s1" dir="ltr">filter=disabled</td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr"></td><td class="s1"></td><td class="s1"></td><td class="s1"></td></tr><tr style='height:42px;'><th id="752998136R9" style="height: 42px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 42px;">10</div></th><td class="s1" dir="ltr">MotionSenseHRV+Gen2</td><td class="s1" dir="ltr">PPG, Seq</td><td class="freezebar-cell"></td><td class="s1" dir="ltr">DA39C925-1D81-48E2-9C68-D0AE4BBD351F</td><td class="s1" dir="ltr">25/50</td><td class="s1" dir="ltr">18</td><td class="s1" dir="ltr">filter=enabled</td><td class="s1" dir="ltr" colspan="4" rowspan="2">Infrared 1</td><td class="s1" dir="ltr" colspan="4" rowspan="2">Infrared 2</td><td class="s1" dir="ltr" colspan="4" rowspan="2">Green/Red 1</td><td class="s1" dir="ltr" colspan="4" rowspan="2">Green/Red 2</td><td class="s1" dir="ltr" colspan="2" rowspan="2">Packet Count[16:1]<br>range: 0-65535<br><br>seq = ((data[16] &amp; 0xff) &lt;&lt; 8) | (data[17] &amp; 0xff)</td><td class="s1"></td><td class="s1"></td></tr><tr style='height:46px;'><th id="752998136R10" style="height: 46px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 46px;">11</div></th><td class="s1" dir="ltr">MotionSenseHRV+Gen2</td><td class="s1" dir="ltr">PPG_DC, Seq</td><td class="freezebar-cell"></td><td class="s1" dir="ltr">DA39C926-1D81-48E2-9C68-D0AE4BBD351F</td><td class="s1" dir="ltr">25/50</td><td class="s1" dir="ltr">18</td><td class="s1" dir="ltr">filter=enabled</td><td class="s1"></td><td class="s1"></td></tr><tr style='height:19px;'><th id="752998136R11" style="height: 19px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 19px;">12</div></th><td class="s1">MotionSenseHRV+</td><td class="s1" dir="ltr">Mag, Mag Sensitivity, Seq</td><td class="freezebar-cell"></td><td class="s1" rowspan="3">DA39C924-1D81-48E2-9C68-D0AE4BBD351F</td><td class="s1">12.5</td><td class="s1">17</td><td class="s1"></td><td class="s1" dir="ltr" colspan="2" rowspan="2">16 bit Mag X1<br>2&#39;s complement form<br><br>mag_x1 =((short)((bytes[0] &amp; 0xff) &lt;&lt; 8) | (bytes[1] &amp; 0xff))*((sen_x-128)*.5/128.0+1) </td><td class="s1" dir="ltr" colspan="2" rowspan="2">16 bit Mag X2<br>2&#39;s complement form<br><br>mag_x2 = ((short)((bytes[2] &amp; 0xff) &lt;&lt; 8) | (bytes[3] &amp; 0xff))*((sen_x-128)*.5/128.0+1);</td><td class="s1" dir="ltr" colspan="2" rowspan="2">16 bit Mag Y1<br>2&#39;s complement form<br><br>mag_y1 = ((short)((bytes[4] &amp; 0xff) &lt;&lt; 8) | (bytes[5] &amp; 0xff))*((sen_y-128)*.5/128.0+1)</td><td class="s1" dir="ltr" colspan="2" rowspan="2">16 bit Mag Y2<br>2&#39;s complement form<br><br>mag_y2 = ((short)((bytes[6] &amp; 0xff) &lt;&lt; 8) | (bytes[7] &amp; 0xff))*((sen_y-128)*.5/128.0+1);</td><td class="s1" dir="ltr" colspan="2" rowspan="2">16 bit Mag Z1<br>2&#39;s complement form<br><br>mag_z1 = ((short)((bytes[8] &amp; 0xff) &lt;&lt; 8) | (bytes[9] &amp; 0xff))*((sen_z-128)*.5/128.0+1)</td><td class="s1" dir="ltr" colspan="2" rowspan="2">16 bit Mag Z2<br>2&#39;s complement form<br><br>mag_z2 = ((short)((bytes[10] &amp; 0xff) &lt;&lt; 8) | (bytes[11] &amp; 0xff))*((sen_z-128)*.5/128.0+1)</td><td class="s1" dir="ltr">Mag sensitivity X<br>sen_x = bytes[12] &amp; 0xff</td><td class="s1" dir="ltr">Mag sensitivity X<br>sen_y = bytes[13] &amp; 0xff</td><td class="s1" dir="ltr">Mag sensitivity X<br>sen_z = bytes[14] &amp; 0xff</td><td class="s1" dir="ltr" colspan="2">Packet Count[16:1]<br>range: 0-65535<br><br>seq = ((data[15] &amp; 0xff) &lt;&lt; 8) | (data[16] &amp; 0xff)</td><td class="s1"></td><td class="s1"></td><td class="s1"></td></tr><tr style='height:19px;'><th id="752998136R12" style="height: 19px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 19px;">13</div></th><td class="s1" dir="ltr">MotionSenseHRV+_V2</td><td class="s1" dir="ltr">Mag, Seq</td><td class="freezebar-cell"></td><td class="s1" dir="ltr">12.5</td><td class="s1" dir="ltr">14</td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr" colspan="2">Packet Count[16:1]<br>range: 0-65535<br><br>seq = ((data[12] &amp; 0xff) &lt;&lt; 8) | (data[13] &amp; 0xff)</td><td class="s1"></td><td class="s1"></td><td class="s1"></td><td class="s1"></td><td class="s1"></td><td class="s1"></td></tr><tr style='height:19px;'><th id="752998136R13" style="height: 19px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 19px;">14</div></th><td class="s1" dir="ltr">MotionSenseHRV+_Gen2</td><td class="s1" dir="ltr">Mag, Seq</td><td class="freezebar-cell"></td><td class="s1" dir="ltr">12.5</td><td class="s1" dir="ltr">14</td><td class="s1" dir="ltr"></td><td class="s1" dir="ltr" colspan="2">16 bit Mag X1<br>2&#39;s complement form<br><br>mag_x1 =((short)((bytes[0] &amp; 0xff) &lt;&lt; 8) | (bytes[1] &amp; 0xff))*0.15</td><td class="s1" dir="ltr" colspan="2">16 bit Mag X2<br>2&#39;s complement form<br><br>mag_x2 = ((short)((bytes[2] &amp; 0xff) &lt;&lt; 8) | (bytes[3] &amp; 0xff))*0.15</td><td class="s1" dir="ltr" colspan="2">16 bit Mag Y1<br>2&#39;s complement form<br><br>mag_y1 = ((short)((bytes[4] &amp; 0xff) &lt;&lt; 8) | (bytes[5] &amp; 0xff))*0.15</td><td class="s1" dir="ltr" colspan="2">16 bit Mag Y2<br>2&#39;s complement form<br><br>mag_y2 = ((short)((bytes[6] &amp; 0xff) &lt;&lt; 8) | (bytes[7] &amp; 0xff))*0.15</td><td class="s1" dir="ltr" colspan="2">16 bit Mag Z1<br>2&#39;s complement form<br><br>mag_z1 = ((short)((bytes[8] &amp; 0xff) &lt;&lt; 8) | (bytes[9] &amp; 0xff))*0.15</td><td class="s1" dir="ltr" colspan="2">16 bit Mag Z2<br>2&#39;s complement form<br><br>mag_z2 = ((short)((bytes[10] &amp; 0xff) &lt;&lt; 8) | (bytes[11] &amp; 0xff))*0.15</td><td class="s1" dir="ltr" colspan="2">Packet Count[16:1]<br>range: 0-65535<br><br>seq = ((data[12] &amp; 0xff) &lt;&lt; 8) | (data[13] &amp; 0xff)</td><td class="s1"></td><td class="s1"></td><td class="s1"></td><td class="s1"></td><td class="s1"></td><td class="s1"></td></tr><tr style='height:19px;'><th id="752998136R14" style="height: 19px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 19px;">15</div></th><td class="s2" dir="ltr"></td><td class="s2" dir="ltr"></td><td class="freezebar-cell"></td><td class="s2"></td><td class="s2" dir="ltr"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td></tr><tr style='height:19px;'><th id="752998136R15" style="height: 19px;" class="row-headers-background"><div class="row-header-wrapper" style="line-height: 19px;">16</div></th><td class="s2" dir="ltr"></td><td class="s2" dir="ltr"></td><td class="freezebar-cell"></td><td class="s2" dir="ltr"></td><td class="s2" dir="ltr"></td><td class="s2" dir="ltr"></td><td class="s2" dir="ltr"></td><td class="s2" dir="ltr" colspan="2"></td><td class="s2" dir="ltr" colspan="2"></td><td class="s2" dir="ltr" colspan="2"></td><td class="s2" dir="ltr" colspan="2"></td><td class="s2" dir="ltr" colspan="2"></td><td class="s2" dir="ltr" colspan="2"></td><td class="s2" colspan="2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td><td class="s2"></td></tr></tbody></table></div>

[Click to get the packet format](https://docs.google.com/spreadsheets/d/e/2PACX-1vQZSFddzWZ814mPiGW7a2rSyYnR7zAWm_MlbW0UbOWpll03vRIxJydTGiPesoy6FRHQRHP01AHvnY5J/pubhtml)