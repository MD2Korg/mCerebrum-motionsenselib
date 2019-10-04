import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';
import 'package:motionsenselib/settings/motionsense_settings.dart';

class MotionsenselibExample {
  static const MethodChannel _channel = const MethodChannel('org.md2k.motionsenselib_example.channel');
  static const _READ_SETTINGS = 'READ_SETTINGS';
  static const _SAVE_SETTINGS = 'SAVE_SETTINGS';
  static const _SAVE_DATA_START = 'SAVE_DATA_START';
  static const _SAVE_DATA_STOP = 'SAVE_DATA_STOP';


  static Future<bool> saveSettings(MotionSenseSettings settings) async {
    String x = jsonEncode(settings);
    return await _channel.invokeMethod(_SAVE_SETTINGS, {"settings":x});
  }
  static Future<MotionSenseSettings> readSettings() async {
    final String res = await _channel.invokeMethod(_READ_SETTINGS);
    if(res==null || res.length==0) return MotionSenseSettings();
    else {
      Map<String, dynamic> x = jsonDecode(res);
      return MotionSenseSettings.fromJson(x);
    }
  }
  static Future<bool> saveDataStart() async {
    return await _channel.invokeMethod(_SAVE_DATA_START);
  }
  static Future<bool> saveDataStop() async {
    return await _channel.invokeMethod(_SAVE_DATA_STOP);
  }
}
