import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:motionsenselib/page/main/main_page.dart';
import 'package:motionsenselib/settings/motionsense_settings.dart';
import 'package:motionsenselib_example/motionsenselib_example.dart';
import 'package:permission_handler/permission_handler.dart';

import 'ThemeLightTeal.dart';

void main() async {
  MotionSenseSettings settings;
  if(await PermissionHandler().checkPermissionStatus(PermissionGroup.storage)!=PermissionStatus.granted)
  await PermissionHandler().requestPermissions([PermissionGroup.storage]);
  if(await PermissionHandler().checkPermissionStatus(PermissionGroup.storage)!=PermissionStatus.granted) {
    SystemChannels.platform.invokeMethod('SystemNavigator.pop');
    return;
  }

  settings = await MotionsenselibExample.readSettings();
  StreamSubscription<dynamic> streamSubscription;
  void callback(String action, dynamic param) async {
    switch (action) {
      case "background":
        if (param == true) {
          await MotionsenselibExample.saveDataStart();
/*
          streamSubscription = MotionSenseLib.listenSensorData.listen((onData){
            print("onData"+onData.toString());
          }, onDone: (){
            print("onDone");
          }, onError: (dynamic error){
            print("onError="+error.toString());
          });
*/
        }
        else {
          await MotionsenselibExample.saveDataStop();
          if (streamSubscription != null)
            streamSubscription.cancel();
        }
        break;
      case "settings":
        await MotionsenselibExample.saveSettings(param);
        break;
    }
  }

  runApp(MaterialApp(
      builder: (context, child){
        ScreenUtil(width: 1080, height: 1980, allowFontScaling: true).init(context);
        return new Theme(data: ThemeLightTeal.create(context), child: child);
      },
      home: MainPage(settings, callback)));
}
