import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:motionsenselib/data/summary.dart';
import 'package:motionsenselib/page/settings/settings_page.dart';
import 'package:motionsenselib/settings/settings.dart';
import 'package:motionsenselib/MotionSense.dart';
import 'package:motionsenselib_example/motionsenselib_example.dart';

import 'summary_table.dart';

class MainPage extends StatefulWidget {
  MainPage();

  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  Timer _timer;
  Summary summary;
  Settings settings = new Settings();

  _MainPageState();

  @override
  void initState() {
    super.initState();
    summary = Summary(null);
    setup();
  }
  Future<void> setup() async{
    bool hasPermission = await MotionsenselibExample.permission();
    if(!hasPermission)
      SystemChannels.platform.invokeMethod('SystemNavigator.pop');
    else {
      settings = await MotionsenselibExample.readSettings();
      await MotionSense.setSettings(settings);
      summary = await MotionSense.getSummary();
      startTimerIfRequired();
    }
    setState(() {

    });
  }

  Future<void> startTimerIfRequired() async {
    if (_timer != null && _timer.isActive) return;
    summary = await MotionSense.getSummary();
    if (summary.isRunning()) {
      _timer = new Timer.periodic(const Duration(seconds: 1), (timer) {
        MotionSense.getSummary().then((onValue) {
          summary = onValue;
          setState(() {});
        });
      });
    }
  }

  void stopTimer() {
    if (_timer != null && _timer.isActive) _timer.cancel();
  }

  @override
  void dispose() {
    stopTimer();
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
            ListTile(
                title: Text(
                  "Data Collection",
                  style: TextStyle(fontSize: 16),
                ),
                subtitle: summary.isRunning()
                    ? Text(summary.getRunningTime())
                    : null, //Text('stopped'),
                trailing: !summary.isRunning()
                    ? new OutlineButton(
                        color: Colors.green,
                        shape: new RoundedRectangleBorder(
                            borderRadius: new BorderRadius.circular(10.0)),
                        textColor: Colors.green,
                        onPressed: () async{
                          await MotionsenselibExample.saveDataStart();
                          await MotionSense.setBackgroundService(true);
                          summary = await MotionSense.getSummary();
                          startTimerIfRequired();
                          setState(() {});
                        },
                        child: Icon(
                          Icons.play_circle_outline,
                          color: Colors.green,
                        ) //new Text("Delete", style: TextStyle(fontSize: 14)),
                        )
                    : new OutlineButton(
                        color: Colors.red,
                        shape: new RoundedRectangleBorder(
                            borderRadius: new BorderRadius.circular(10.0)),
                        textColor: Colors.red,
                        onPressed: () async {
                          await MotionSense.setBackgroundService(false);
                          await MotionsenselibExample.saveDataStop();
                          stopTimer();
                          summary = await MotionSense.getSummary();
                          setState(() {});
                        },
                        child: Icon(
                          Icons.pause_circle_outline,
                          color: Colors.red,
                        ) //new Text("Delete", style: TextStyle(fontSize: 14)),
                        )),
            ListTile(
//                leading: Icon(Icons.settings),
              title: Text(
                "Settings",
                style: TextStyle(fontSize: 16),
              ),
              subtitle: Text("Device Configured: " +
                  settings.motionsense_devices.length.toString()),
              trailing: new OutlineButton(
                  color: Colors.green,
                  shape: new RoundedRectangleBorder(
                      borderRadius: new BorderRadius.circular(10.0)),
                  textColor: Colors.green,
                  onPressed: () async {
                    stopTimer();
                    var res = await Navigator.push(
                        context,
                        new MaterialPageRoute(
                            builder: (BuildContext context) =>
                                new SettingsPage(settings)));
                    if (res["edit"]) {
                      settings = res["settings"];
                      await MotionSense.setSettings(settings);
                      await MotionsenselibExample.saveSettings(settings);
                      summary = await MotionSense.getSummary();
                      setState(() {});
                    }
                    startTimerIfRequired();
                  },
                  child: Icon(
                    Icons.settings,
                    color: Colors.green,
                  ) //new Text("Delete", style: TextStyle(fontSize: 14)),
                  ),

/*
                trailing: new FlatButton(
                  textColor: Colors.white,
                  color: Colors.green,
                  onPressed: () {
                    Navigator.push(
                        context,
                        new MaterialPageRoute(
                            builder: (_context) => new SettingsPage()));
                  },
                  child: new Text("Open", style: TextStyle(fontSize: 16)),
                ),
*/
            ),
            Container(
              color: Theme.of(context).highlightColor,
              child: Container(
                padding: EdgeInsets.all(16),
                child: Center(
                  child: Text(
                    "Data Summary",
                    style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                  ),
                ),
              ),
            ),
            Expanded(
              child: new DataSourceTable(summary.getDevices()),
            )
          ]),
    );
  }
}
