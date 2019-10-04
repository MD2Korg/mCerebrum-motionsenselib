import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:motionsenselib/page/main/main_page.dart';
import 'package:motionsenselib/settings/motionsense_settings.dart';
import 'package:motionsenselib_example/motionsenselib_example.dart';
import 'package:permission_handler/permission_handler.dart';

void main() async {
  MotionSenseSettings settings;
  if(await PermissionHandler().checkPermissionStatus(PermissionGroup.storage)!=PermissionStatus.granted)
  await PermissionHandler().requestPermissions([PermissionGroup.storage]);
  if(await PermissionHandler().checkPermissionStatus(PermissionGroup.storage)!=PermissionStatus.granted) {
    SystemChannels.platform.invokeMethod('SystemNavigator.pop');
    return;
  }

  settings = await MotionsenselibExample.readSettings();
  void callback(String action, dynamic param) async {
    switch (action) {
      case "background":
        if (param == true)
          await MotionsenselibExample.saveDataStart();
        else
          await MotionsenselibExample.saveDataStop();
        break;
      case "settings":
        await MotionsenselibExample.saveSettings(param);
        break;
    }
  }

  runApp(MaterialApp(home: MainPage(settings, callback)));
}
