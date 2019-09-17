import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:motionsenselib/settings/device_settings.dart';

class SettingsDetailsPage extends StatefulWidget {
  final DeviceSettings deviceSettings;
  final DeviceSettings defaultDeviceSettings;

  SettingsDetailsPage(this.deviceSettings, {this.defaultDeviceSettings});

  @override
  _SettingsDetailsPageState createState() =>
      _SettingsDetailsPageState(deviceSettings, defaultDeviceSettings);
}

class _SettingsDetailsPageState extends State<SettingsDetailsPage> {
  DeviceSettings deviceSettings;
  DeviceSettings defaultDeviceSettings;
  bool isEdit = false;

  _SettingsDetailsPageState(this.deviceSettings, this.defaultDeviceSettings);

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        Navigator.pop(
            context, {"deviceSettings": deviceSettings, "edit": isEdit});
        return Future.value(false);
      },
      child: new Scaffold(
        appBar: AppBar(elevation: 4.0, title: Text("Sensor Settings")),
        body: bodyData(context),
      ),
    );
  }

  Widget bodyData(BuildContext context) {
    return Container(
      height: double.infinity,
      child: SingleChildScrollView(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Container(
              color: Theme.of(context).highlightColor,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Center(
                  child: Text("Device Info",
                      style: Theme.of(context).textTheme.subtitle),
                ),
              ),
            ),
            new ListTile(
              dense: true,
              title: new Text("Device", style: TextStyle(fontSize: 12)),
              trailing: new Text(deviceSettings.name,
                  style: TextStyle(fontSize: 12, color: Colors.green)),
            ),
            new ListTile(
              dense: true,
              title: new Text("ID", style: TextStyle(fontSize: 12)),
              trailing: new Text(deviceSettings.deviceId,
                  style: TextStyle(fontSize: 12, color: Colors.green)),
            ),
            new ListTile(
              dense: true,
              title: new Text("Version", style: TextStyle(fontSize: 12)),
              trailing: new Text(deviceSettings.version,
                  style: TextStyle(fontSize: 12, color: Colors.green)),
            ),
            new ListTile(
              dense: true,
              title: new Text("Placement", style: TextStyle(fontSize: 12)),
              trailing: new Text(deviceSettings.platformId,
                  style: TextStyle(fontSize: 12, color: Colors.green)),
            ),
            Container(
              color: Theme.of(context).highlightColor,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Center(
                  child: Text("Sensor Configuration",
                      style: Theme.of(context).textTheme.subtitle),
                ),
              ),
            ),
            ListTile(
              dense: true,
              leading: Text(
                "Set Configuration",
                style: TextStyle(fontSize: 12),
              ),
              trailing: new OutlineButton(
                color: Colors.green,
                shape: new RoundedRectangleBorder(
                    borderRadius: new BorderRadius.circular(10.0)),
                onPressed: () {
                  deviceSettings.setToDefault(defaultDeviceSettings);
                  isEdit = true;
                  setState(() {});
                },
                child: new Text("Default", style: TextStyle(fontSize: 12)),
              ),
            ),
            createMinConnectionIntervalWidget(),
            createAclGyroWidget(),
            createPPGWidget(),
            createMagnetometerWidget(),
//            createSaveRawData(),
          ],
        ),
      ),
    );
  }

  String _getMotionSensorText() {
    switch (deviceSettings.platformType) {
      case "MOTION_SENSE_HRV":
        return "Acl/Gyro/PPG Sampling Rate";
      case "MOTION_SENSE_HRV_PLUS":
        return "Acl/Quaternion/PPG Sampling Rate";
      default:
        return "Accelerometer/Gyroscope Sampling Rate";
    }
  }

  Widget createAclGyroWidget() {
    return Column(
      children: <Widget>[
        ListTile(
          dense: true,
          leading: Text(
            _getMotionSensorText(),
            style: TextStyle(fontSize: 12),
          ),
          trailing: deviceSettings.isVersion2()
              ? new DropdownButton<double>(
                  style: TextStyle(color: Colors.black, fontSize: 12),
                  items: <double>[25, 50, 62.5, 125, 250].map((double value) {
                    return new DropdownMenuItem<double>(
                      value: value,
                      child: new Text('$value Hz'),
                    );
                  }).toList(),
                  value: deviceSettings.accelerometerSampleRate,
                  onChanged: (double value) {
                    if (deviceSettings.accelerometerSampleRate != value) {
                      deviceSettings.setAccelerometerSampleRate(value);
                      isEdit = true;
                      setState(() {});
                    }
                  },
                )
              : Text(deviceSettings.accelerometerSampleRate.toString() + " Hz",
                  style: TextStyle(fontSize: 12)),
        ),
        deviceSettings.isVersion2()
            ? ListTile(
                dense: true,
                leading: Text(
                  "Accelerometer Sensitivity",
                  style: TextStyle(fontSize: 12),
                ),
                trailing: new DropdownButton<int>(
                    style: TextStyle(color: Colors.black, fontSize: 12),
                    items: <int>[2, 4, 8, 16].map((int value) {
                      return new DropdownMenuItem<int>(
                        value: value,
                        child: new Text('\u00b1${value}g'),
                      );
                    }).toList(),
                    value: deviceSettings.accelerometerSensitivity,
                    onChanged: (int value) {
                      if (deviceSettings.accelerometerSensitivity != value) {
                        deviceSettings.setAccelerometerSensitivity(value);
                        isEdit = true;
                        setState(() {});
                      }
                    }),
              )
            : Container(),
        deviceSettings.isVersion2()
            ? ListTile(
                dense: true,
                leading: Text(
                  "Gyroscope Sensitivity",
                  style: TextStyle(fontSize: 12),
                ),
                trailing: new DropdownButton<int>(
                  style: TextStyle(color: Colors.black, fontSize: 12),
                  items: <int>[250, 500, 1000, 2000].map((int value) {
                    return new DropdownMenuItem<int>(
                      value: value,
                      child: new Text('\u00b1$value dps'),
                    );
                  }).toList(),
                  value: deviceSettings.gyroscopeSensitivity,
                  onChanged: (int value) {
                    if (deviceSettings.gyroscopeSensitivity != value) {
                      deviceSettings.setGyroscopeSensitivity(value);
                      isEdit = true;
                      setState(() {});
                    }
                  },
                ),
              )
            : Container(),
      ],
    );
  }

  Widget createPPGWidget() {
    return Column(
      children: <Widget>[
        deviceSettings.isVersion2() && !deviceSettings.isMotionSense()
            ? ListTile(
                dense: true,
                leading: Text(
                  "PPG Sampling Rate",
                  style: TextStyle(fontSize: 12),
                ),
                trailing: deviceSettings.isVersion2()
                    ? new DropdownButton<double>(
                        style: TextStyle(color: Colors.black, fontSize: 12),
                        items: <double>[25, 50].map((double value) {
                          return new DropdownMenuItem<double>(
                            value: value,
                            child: new Text('$value Hz'),
                          );
                        }).toList(),
                        value: deviceSettings.ppgSampleRate,
                        onChanged: (double value) {
                          if (deviceSettings.ppgSampleRate != value) {
                            deviceSettings.setPpgSampleRate(value);
                            isEdit = true;
                            setState(() {});
                          }
                        },
                      )
                    : Text(
                        deviceSettings.ppgSampleRate.toString() + " Hz",
                        style: TextStyle(fontSize: 12),
                      ),
              )
            : Container(),
        deviceSettings.isVersion2() && !deviceSettings.isMotionSense()
            ? ListTile(
                dense: true,
                title: Text(
                  "PPG Red value",
                  style: TextStyle(fontSize: 12, color: Colors.red),
                ),
                subtitle: Text(
                  "range: 0-255",
                  style: TextStyle(fontSize: 10, color: Colors.red),
                ),
                trailing: new Container(
                  width: 60.0,
                  child: new TextField(
                    controller: new TextEditingController.fromValue(
                        new TextEditingValue(
                            text: deviceSettings.ppgRed.toString(),
                            selection: new TextSelection.collapsed(
                                offset:
                                    deviceSettings.ppgRed.toString().length))),
                    textAlign: TextAlign.center,
                    style: TextStyle(fontSize: 12, color: Colors.red),
                    keyboardType: TextInputType.number,
                    onChanged: (value) {
                      int v = 0;
                      if (value != null && value.length != 0)
                        v = double.parse(value).round();
                      if (v < 0) v = 0;
                      if (v > 255) v = 255;
                      if (deviceSettings.ppgRed != v) {
                        deviceSettings.setPpgRed(v);
                        isEdit = true;
//                        setState(() {});
                      }
                    },
                    onSubmitted: (value) {
                      int v = 0;
                      if (value != null && value.length != 0)
                        v = double.parse(value).round();
                      if (v < 0) v = 0;
                      if (v > 255) v = 255;
                      if (deviceSettings.ppgRed != v) {
                        deviceSettings.setPpgRed(v);
                        isEdit = true;
                        setState(() {});
                      }
                    },
                  ),
                ),
              )
            : Container(),
        deviceSettings.isVersion2() && !deviceSettings.isMotionSense()
            ? ListTile(
                dense: true,
                title: Text(
                  "PPG Green brightness",
                  style: TextStyle(fontSize: 12, color: Colors.green),
                ),
                subtitle: Text(
                  "range: 0-255",
                  style: TextStyle(fontSize: 10, color: Colors.green),
                ),
                trailing: new Container(
                  width: 60.0,
                  child: new TextField(
                    controller: new TextEditingController.fromValue(
                        new TextEditingValue(
                            text: deviceSettings.ppgGreen.toString(),
                            selection: new TextSelection.collapsed(
                                offset: deviceSettings.ppgGreen
                                    .toString()
                                    .length))),
                    textAlign: TextAlign.center,
                    style: TextStyle(fontSize: 12, color: Colors.green),
                    keyboardType: TextInputType.number,
                    onChanged: (value) {
                      int v = 0;
                      if (value != null && value.length != 0)
                        v = double.parse(value).round();
                      if (v < 0) v = 0;
                      if (v > 255) v = 255;
                      if (deviceSettings.ppgGreen != v) {
                        deviceSettings.setPpgGreen(v);
                        isEdit = true;
//                        setState(() {});
                      }
                    },
                    onSubmitted: (value) {
                      int v = 0;
                      if (value != null && value.length != 0)
                        v = double.parse(value).round();
                      if (v < 0) v = 0;
                      if (v > 255) v = 255;
                      if (deviceSettings.ppgGreen != v) {
                        deviceSettings.setPpgGreen(v);
                        isEdit = true;
                        setState(() {});
                      }
                    },
                  ),
                ),
              )
            : Container(),
        deviceSettings.isVersion2() && !deviceSettings.isMotionSense()
            ? ListTile(
                dense: true,
                title: Text(
                  "PPG Infrared brightness",
                  style: TextStyle(fontSize: 12, color: Colors.black45),
                ),
                subtitle: Text(
                  "range: 0-255",
                  style: TextStyle(fontSize: 10, color: Colors.black45),
                ),
                trailing: new Container(
                  width: 60.0,
                  child: new TextField(
                    controller: new TextEditingController.fromValue(
                        new TextEditingValue(
                            text: deviceSettings.ppgInfrared.toString(),
                            selection: new TextSelection.collapsed(
                                offset: deviceSettings.ppgInfrared
                                    .toString()
                                    .length))),
                    textAlign: TextAlign.center,
                    style: TextStyle(fontSize: 12, color: Colors.black45),
                    keyboardType: TextInputType.number,
                    onChanged: (value) {
                      int v = 0;
                      if (value != null && value.length != 0)
                        v = double.parse(value).round();
                      if (v < 0) v = 0;
                      if (v > 255) v = 255;
                      if (deviceSettings.ppgInfrared != v) {
                        deviceSettings.setPpgInfrared(v);
                        isEdit = true;
//                        setState(() {});
                      }
                    },
                    onSubmitted: (value) {
                      int v = 0;
                      if (value != null && value.length != 0)
                        v = double.parse(value).round();
                      if (v < 0) v = 0;
                      if (v > 255) v = 255;
                      if (deviceSettings.ppgInfrared != v) {
                        deviceSettings.setPpgInfrared(v);
                        isEdit = true;
                        setState(() {});
                      }
                    },
                  ),
                ),
              )
            : Container(),
        deviceSettings.isVersion2() && !deviceSettings.isMotionSense()
            ? ListTile(
                dense: true,
                leading: Text(
                  "Enable PPG Filter",
                  style: TextStyle(fontSize: 12),
                ),
                trailing: Checkbox(
                  value: deviceSettings.ppgFilterEnable,
                  onChanged: (bool newValue) {
                    if (deviceSettings.ppgFilterEnable != newValue) {
                      deviceSettings.setPpgFilterEnable(newValue);
                      isEdit = true;
                      setState(() {});
                    }
                  },
                ))
            : Container(),
      ],
    );
  }

  Widget createMagnetometerWidget() {
    return deviceSettings.isMotionSenseHRVPlus() ||
            deviceSettings.isMotionSenseHRVPlusGen2()
        ? Column(
            children: <Widget>[
              ListTile(
                dense: true,
                leading: Text(
                  "Magnetometer Sampling Rate",
                  style: TextStyle(fontSize: 12),
                ),
                trailing: Text(
                    deviceSettings.magnetometerSampleRate.toString() + " Hz",
                    style: TextStyle(fontSize: 12)),
              ),
            ],
          )
        : Container();
  }

  Widget createMinConnectionIntervalWidget() {
    return deviceSettings.isVersion2()
        ? ListTile(
            dense: true,
            title: Text(
              "Min. Connection Interval",
              style: TextStyle(fontSize: 12),
            ),
            subtitle: Text(
              "range: 10-120",
              style: TextStyle(fontSize: 10),
            ),
            trailing: new Container(
              width: 60.0,
              child: new TextField(
                controller: new TextEditingController.fromValue(
                    new TextEditingValue(
                        text: deviceSettings.minConnectionInterval.toString(),
                        selection: new TextSelection.collapsed(
                            offset: deviceSettings.minConnectionInterval
                                .toString()
                                .length))),
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 12),
                keyboardType: TextInputType.number,
                onSubmitted: (value) {
                  int v = 10;
                  if (value != null && value.length != 0) v = int.parse(value);
                  if (v < 10) v = 10;
                  if (v > 120) v = 120;
                  if (deviceSettings.minConnectionInterval != v) {
                    deviceSettings.setMinConnectionInterval(v);
                    isEdit = true;
                    setState(() {});
                  }
                },
              ),
            ),
          )
        : Container();
  }

/*
  Widget createSaveRawData() {
    return ListTile(
      dense: true,
      leading: Text(
        "Save Raw Data & Sequence Number",
        style: TextStyle(fontSize: 12),
      ),
*/
/*
      trailing: Checkbox(
        value: this.deviceSettings.getDeviceValue(id, "saveRaw") == "true"
            ? true
            : false,
        onChanged: (bool newValue) {
          this.deviceSettings.setDeviceValue(
              id, "saveRaw", newValue.toString());
          setState(() {});
        },
      ),
*/ /*

    );
  }
*/
}
