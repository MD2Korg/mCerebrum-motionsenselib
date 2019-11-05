import 'dart:async';
import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_blue/flutter_blue.dart';
import 'package:flutter_icons/flutter_icons.dart';
import 'package:location_permissions/location_permissions.dart' as lp;
import 'package:motionsenselib/bluetooth/bluetooth_manager.dart';
import 'package:motionsenselib/data/summary.dart';
import 'package:motionsenselib/page/settings/settings_page.dart';
import 'package:motionsenselib/settings/motionsense_settings.dart';
import 'package:permission_handler/permission_handler.dart';

import '../../motionsenselib.dart';
import 'summary_table.dart';

class MainPage extends StatefulWidget {
  final MotionSenseSettings settings;
  final Function(String action, dynamic param) callback;

  MainPage(this.settings, this.callback);

  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  Timer _timer;
  Summary summary;
  int sensorState = 0;
  BluetoothManager bluetoothManager;

  _MainPageState();

  @override
  void initState() {
    super.initState();

    summary = Summary(null);
    setup();
  }


  Widget _showStatus() {
    return new Column(children: <Widget>[
      sensorState == 1 || sensorState == 3
          ? new Container(
              margin: EdgeInsets.only(bottom: 1),
              color: Colors.redAccent,
              child: new ListTile(
                title: new Text(
                  'Please enable bluetooth',
                  style: Theme.of(context).primaryTextTheme.subhead,
                ),
                trailing: Platform.isIOS
                    ? Container()
                    : FlatButton(
                        textColor: Colors.white,
//                              borderSide: BorderSide(color: Colors.white),
                        child: Text("Enable"),
                        onPressed: () {
                          bluetoothManager.enableBluetooth();
                        },
                      ),
              ))
          : Container(),
      sensorState == 2 || sensorState == 3
          ? new Container(
              margin: EdgeInsets.only(bottom: 1),
              color: Colors.redAccent,
              child: new ListTile(
                title: new Text(
                  'Please enable gps',
                  style: Theme.of(context).primaryTextTheme.subhead,
                ),
                trailing: Platform.isIOS
                    ? Container()
                    : FlatButton(
                        textColor: Colors.white,
                        child: Text(
                          "Enable",
                          textAlign: TextAlign.right,
                        ),
                        onPressed: () {
                          bluetoothManager.enableGPS();
                        },
                      ),
              ))
          : Container(),
    ]);
  }



  Future<int> checkSensorStatus() async{
    lp.LocationPermissions locationPermissions = new lp.LocationPermissions();
    bluetoothManager = new BluetoothManager(widget.settings);
    bool bluetoothOn = await bluetoothManager.isBluetoothOn();
    bool locationOn = await locationPermissions.checkServiceStatus()==lp.ServiceStatus.enabled;
    if(bluetoothOn && locationOn) return 0;
    else if(!bluetoothOn && locationOn) return 1;
    else if(bluetoothOn && !locationOn) return 2;
    else return 3;
  }


  Future<void> setup() async {
    if (await PermissionHandler()
            .checkPermissionStatus(PermissionGroup.location) ==
        PermissionStatus.denied) {
      await PermissionHandler().requestPermissions([PermissionGroup.location]);
      PermissionStatus x = await PermissionHandler().checkPermissionStatus(PermissionGroup.location);
      if (x == PermissionStatus.denied)
        SystemChannels.platform.invokeMethod('SystemNavigator.pop');
    }
    summary = await MotionSenseLib.getSummary();
    if (!summary.isRunning()) await MotionSenseLib.setSettings(widget.settings);
    _startTimer();
    setState(() {});
  }

  Future<void> _startTimer() async {
    if (_timer != null && _timer.isActive) return;
    _timer = new Timer.periodic(const Duration(seconds: 1), (timer) async {
      summary = await MotionSenseLib.getSummary();
      sensorState = await checkSensorStatus();
        setState(() {});
    });
  }

  void _stopTimer() {
    if (_timer != null && _timer.isActive) _timer.cancel();
  }

  @override
  void dispose() {
    _stopTimer();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('MotionSense'),
      ),
      body: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            _showStatus(),
            Container(
              color: Theme.of(context).backgroundColor,
              child: Container(
                padding: EdgeInsets.all(10),
                child: Center(
                  child: Text(
                    "Activity",
                    style: Theme.of(context).textTheme.body2,
                  ),
                ),
              ),
            ),
            ListTile(
                title: Text(
                  "Data Collection",
                ),
                subtitle: summary.isRunning()
                    ? Text(summary.getRunningTime(),
                        style: Theme.of(context).textTheme.subtitle)
                    : null, //Text('stopped'),
                trailing: !summary.isRunning()
                    ? new OutlineButton(
                        onPressed: () async {
                          widget.callback("background", true);
                          await MotionSenseLib.setBackgroundService(true);
                          summary = await MotionSenseLib.getSummary();
                          setState(() {});
                        },
                        child: Icon(
                            MaterialCommunityIcons.getIconData("play-outline")))
                    : new OutlineButton(
                        color: Colors.red,
                        textColor: Colors.red,
                        onPressed: () async {
                          widget.callback("background", false);
                          await MotionSenseLib.setBackgroundService(false);
                          summary = await MotionSenseLib.getSummary();
                          setState(() {});
                        },
                        child: Icon(
                          Icons.pause_circle_outline,
                          color: Colors.red,
                        ))),
            ListTile(
              title: Text(
                "Settings",
              ),
              subtitle: Text(
                "Device Configured: " +
                    widget.settings.motionSenseDevices.length.toString(),
                style: Theme.of(context).textTheme.subtitle,
              ),
              trailing: new OutlineButton(
                  onPressed: () async {
                    var res = await Navigator.push(
                        context,
                        new MaterialPageRoute(
                            builder: (BuildContext context) =>
                                new SettingsPage(widget.settings)));
                    if (res["edit"]) {
//                      widget.settings = res["settings"];
                      await MotionSenseLib.setSettings(widget.settings);
                      widget.callback("settings", widget.settings);
//                      await MotionsenselibExample.saveSettings(settings);
                      summary = await MotionSenseLib.getSummary();
                      setState(() {});
                    }
                  },
                  child: Icon(
                    MaterialCommunityIcons.getIconData("settings-outline"),
                  )),
            ),
            Container(
              color: Theme.of(context).backgroundColor,
              child: Container(
                padding: EdgeInsets.all(10),
                child: Center(
                  child: Text(
                    "Data Summary",
                    style: Theme.of(context).textTheme.body2,
                  ),
                ),
              ),
            ),
            Expanded(
              child: new DataSourceTable(
                  new DataSourceInfoList(summary.getDevices())),
            )
          ]),
    );
  }
}
