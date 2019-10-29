import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_icons/flutter_icons.dart';
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
              color: Theme.of(context).backgroundColor,
              child: Padding(
                padding: const EdgeInsets.all(10.0),
                child: Center(
                  child: Text("Device Info",
                      style: Theme.of(context).textTheme.body2),
                ),
              ),
            ),
            new ListTile(
              title: new Text("Device"),
              trailing: new Text(deviceSettings.getName()),
            ),
            new ListTile(
              title: new Text("ID"),
              trailing: new Text(deviceSettings.deviceId)
            ),
            new ListTile(
              title: new Text("Version"),
              trailing: new Text(deviceSettings.version),
            ),
            new ListTile(
              title: new Text("Placement"),
              trailing: new Text(deviceSettings.platformId)
            ),
            Container(
              color: Theme.of(context).backgroundColor,
              child: Padding(
                padding: const EdgeInsets.all(10.0),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
//                    Expanded(child: Container()),
              Expanded(child: Center(child: Text("Sensor Configuration",
                        style: Theme.of(context).textTheme.body2))),
              new GestureDetector(onTap: (){
                deviceSettings.setToDefault(defaultDeviceSettings);
                isEdit = true;
                setState(() {});

              },child: Icon(MaterialCommunityIcons.getIconData("undo-variant"), color: Theme.of(context).iconTheme.color,)),
/*
              new OutlineButton(
                padding: EdgeInsets.all(0),
                      shape: new RoundedRectangleBorder(
                          borderRadius: new BorderRadius.circular(2.0)),
                      onPressed: () {
                        deviceSettings.setToDefault(defaultDeviceSettings);
                        isEdit = true;
                        setState(() {});
                      },
                      child: new Text("DEFAULT",
                          style: Theme.of(context).textTheme.body2),
                    ),
*/
                  ],
                ),
              ),
            ),
            createMinConnectionIntervalWidget(),
            createAclGyroWidget(),
            createPPGWidget(),
            createMagnetometerWidget(),
/*
            ListTile(
              dense: true,
              title: GestureDetector(child:
              Text("Set to default",),
                onTap: (){
                  deviceSettings.setToDefault(defaultDeviceSettings);
                  isEdit = true;
                  setState(() {});

                },
              ),
*/
/*
              trailing: new OutlineButton(
                color: Colors.green,
                shape: new RoundedRectangleBorder(
                    borderRadius: new BorderRadius.circular(10.0)),
                onPressed: () {
                  deviceSettings.setToDefault(defaultDeviceSettings);
                  isEdit = true;
                  setState(() {});
                },
                child: new Text("DEFAULT", style: Theme.of(context).textTheme.caption),
              ),
*/
//            ),

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
        return "Acl/Gyro Sampling Rate";
    }
  }

  Widget createAclGyroWidget() {
    return Column(
      children: <Widget>[
        ListTile(

          title: Text(
            _getMotionSensorText(),
          ),
          trailing: deviceSettings.isVersion2()
              ? new DropdownButton<double>(
                  style: TextStyle(color: Colors.black),
                  items: <double>[25, 50, 62.5, 125, 250].map((double value) {
                    return new DropdownMenuItem<double>(
                      value: value,
                      child: new Text('$value Hz', style: Theme.of(context).textTheme.body1,),
                    );
                  }).toList(),
                  value: deviceSettings.characteristicMotionSampleRate,
                  onChanged: (double value) {
                    if (deviceSettings.characteristicMotionSampleRate != value) {
                      deviceSettings.setCharacteristicMotionSampleRate(value);
                      isEdit = true;
                      setState(() {});
                    }
                  },
                )
              : Text(deviceSettings.characteristicMotionSampleRate.toString() + " Hz", style: Theme.of(context).textTheme.body1,),
        ),
        deviceSettings.isVersion2()
            ? ListTile(

                title: Text(
                  "Accelerometer Sensitivity",
                ),
                trailing: new DropdownButton<int>(
                    style: TextStyle(color: Colors.black),
                    items: <int>[2, 4, 8, 16].map((int value) {
                      return new DropdownMenuItem<int>(
                        value: value,
                        child: new Text('\u00b1${value}g', style: Theme.of(context).textTheme.body1,),
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

                title: Text(
                  "Gyroscope Sensitivity",
                ),
                trailing: new DropdownButton<int>(
                  style: TextStyle(color: Colors.black),
                  items: <int>[250, 500, 1000, 2000].map((int value) {
                    return new DropdownMenuItem<int>(
                      value: value,
                      child: new Text('\u00b1$value dps', style: Theme.of(context).textTheme.body1,),
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

                title: Text(
                  "PPG Sampling Rate",
                ),
                trailing: deviceSettings.isVersion2()
                    ? new DropdownButton<double>(
                        style: TextStyle(color: Colors.black),
                        items: <double>[25, 50].map((double value) {
                          return new DropdownMenuItem<double>(
                            value: value,
                            child: new Text('$value Hz', style: Theme.of(context).textTheme.body1,),
                          );
                        }).toList(),
                        value: deviceSettings.characteristicPpgSampleRate,
                        onChanged: (double value) {
                          if (deviceSettings.characteristicPpgSampleRate != value) {
                            deviceSettings.setCharacteristicPpgSampleRate(value);
                            isEdit = true;
                            setState(() {});
                          }
                        },
                      )
                    : Text(
                        deviceSettings.characteristicPpgSampleRate.toString() + " Hz", style: Theme.of(context).textTheme.body1,
                      ),
              )
            : Container(),
        deviceSettings.isVersion2() && !deviceSettings.isMotionSense()
            ? ListTile(

                title: Text(
                  "PPG Red value (0-255)",
                  style: TextStyle(color: Colors.red),
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
                    style: TextStyle(color: Colors.red),
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

                title: Text(
                  "PPG Green brightness (0-255)",
                  style: TextStyle(color: Colors.green),
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
                    style: TextStyle(color: Colors.green),
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

                title: Text(
                  "PPG Infrared brightness (0-255)",
                  style: TextStyle(color: Colors.black45),
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
                    style: TextStyle(color: Colors.black45),
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

                title: Text(
                  "Enable PPG Filter",
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

                title: Text(
                  "Magnetometer Sampling Rate",
                ),
                trailing: Text(
                  "25 Hz",
                ),
              ),
            ],
          )
        : Container();
  }

  Widget createMinConnectionIntervalWidget() {
    return deviceSettings.isVersion2()
        ? ListTile(

            title: Text(
              "Min. Connection Interval (10-120)",
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
}
