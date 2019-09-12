import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';
import 'package:motionsenselib/settings/settings.dart';

class Motionsenselib {
  static const MethodChannel _channel = const MethodChannel('org.md2k.motionsenselib.channel');
  static const _SET_SETTINGS = 'SET_SETTINGS';
  static const _GET_SUMMARY = 'GET_SUMMARY';
  static const _BACKGROUND_SERVICE = 'BACKGROUND_SERVICE';
  static const _PLOT = 'PLOT';

  static Future<void> setSettings(Settings settings) async {
    String x = jsonEncode(settings);
    final res = await _channel.invokeMethod(_SET_SETTINGS, {"settings":x});
  }
  static Future<Map<String, dynamic>> getSummary() async {
    final res = await _channel.invokeMethod(_GET_SUMMARY);
    Map<String, dynamic> x = jsonDecode(res);
    return x;
  }
  static Future<bool> setBackgroundService(bool run) async {
    final res = await _channel.invokeMethod(_BACKGROUND_SERVICE, {"run":run});
    return res;
  }
  static Future<bool> plot(String platformType, String platformId, String sensorType) async {
    final res = await _channel.invokeMethod(_PLOT, {"platformType":platformType, "platformId": platformId,"sensorType": sensorType});
    return res;
  }
}
