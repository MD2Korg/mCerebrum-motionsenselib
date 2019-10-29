import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:motionsenselib/data/summary.dart';
import 'package:motionsenselib/page/settings/settings_page.dart';
import 'package:motionsenselib/settings/motionsense_settings.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:flutter_icons/flutter_icons.dart';

import '../../motionsenselib.dart';
import 'summary_table.dart';

class MainPage extends StatefulWidget {
  final MotionSenseSettings settings;
  final Function(String action, dynamic param) callback;
  MainPage(this.settings, this. callback);

  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  Timer _timer;
  Summary summary;

  _MainPageState();

  @override
  void initState() {
    super.initState();
    summary = Summary(null);
    setup();
  }
  Future<bool> hasPermission() async{
    if(await PermissionHandler().checkPermissionStatus(PermissionGroup.location)!=PermissionStatus.granted) return false;
    return true;
  }
  Future<void> setup() async{
    if(await hasPermission()==false){
      await PermissionHandler().requestPermissions([PermissionGroup.location]);
      if(await hasPermission()==false)
        SystemChannels.platform.invokeMethod('SystemNavigator.pop');
    }
      await MotionSenseLib.setSettings(widget.settings);
      summary = await MotionSenseLib.getSummary();
      startTimerIfRequired();
    setState(() {

    });
  }

  Future<void> startTimerIfRequired() async {
    if (_timer != null && _timer.isActive) return;
    summary = await MotionSenseLib.getSummary();
    if (summary.isRunning()) {
      _timer = new Timer.periodic(const Duration(seconds: 1), (timer) {
        MotionSenseLib.getSummary().then((onValue) {
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
                    ? Text(summary.getRunningTime(),style: Theme.of(context).textTheme.subtitle)
                    : null, //Text('stopped'),
                trailing: !summary.isRunning()
                    ? new OutlineButton(
                        onPressed: () async{
                          widget.callback("background",true);
                          await MotionSenseLib.setBackgroundService(true);
                          summary = await MotionSenseLib.getSummary();
                          startTimerIfRequired();
                          setState(() {});
                        },
                        child: Icon(MaterialCommunityIcons.getIconData("play-outline"))
                    )
                    : new OutlineButton(
                        color: Colors.red,
                        textColor: Colors.red,
                        onPressed: () async {
                          widget.callback("background",false);
                          await MotionSenseLib.setBackgroundService(false);
                          stopTimer();
                          summary = await MotionSenseLib.getSummary();
                          setState(() {});
                        },
                        child: Icon(
                          Icons.pause_circle_outline,
                          color: Colors.red,
                        )
                        )),
            ListTile(
              title: Text(
                "Settings",
              ),
              subtitle: Text("Device Configured: " +
                  widget.settings.motionSenseDevices.length.toString(), style: Theme.of(context).textTheme.subtitle,),
              trailing: new OutlineButton(
                  onPressed: () async {
                    stopTimer();
                    var res = await Navigator.push(
                        context,
                        new MaterialPageRoute(
                            builder: (BuildContext context) =>
                                new SettingsPage(widget.settings)));
                    if (res["edit"]) {
//                      widget.settings = res["settings"];
                      await MotionSenseLib.setSettings(widget.settings);
                      widget.callback("settings",widget.settings);
//                      await MotionsenselibExample.saveSettings(settings);
                      summary = await MotionSenseLib.getSummary();
                      setState(() {});
                    }
                    startTimerIfRequired();
                  },
                  child: Icon(
                      MaterialCommunityIcons.getIconData("settings-outline"),
                  )
                  ),
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
              child: new DataSourceTable(new DataSourceInfoList(summary.getDevices())),
            )
          ]),
    );
  }
}
