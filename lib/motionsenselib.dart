import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';
import 'package:motionsenselib/settings/motionsense_settings.dart';

import 'data/summary.dart';

class MotionSenseLib {
  static const MethodChannel _channel = const MethodChannel('org.md2k.motionsenselib.channel');
  static const EventChannel _sensorDataEventChannel = EventChannel('org.md2k.motionsenselib.eventchannel.sensordata');
  static const _SET_SETTINGS = 'SET_SETTINGS';
  static const _GET_SUMMARY = 'GET_SUMMARY';
  static const _BACKGROUND_SERVICE = 'BACKGROUND_SERVICE';
  static const _SENSOR_INFO = "SENSOR_INFO";
  static const _PLOT = 'PLOT';

  static Future<void> setSettings(MotionSenseSettings settings) async {
    String x = jsonEncode(settings);
    return _channel.invokeMethod(_SET_SETTINGS, {"settings":x});
  }
  static Future<List> getSensorInfo(String deviceId) async {
    final res = await _channel.invokeMethod(_SENSOR_INFO, {"deviceId": deviceId});
    return jsonDecode(res) as List;
  }
  static Future<Summary> getSummary() async {
    final res = await _channel.invokeMethod(_GET_SUMMARY);
    Map<String, dynamic> x = jsonDecode(res);
    return Summary(x);
  }
  static Future<bool> setBackgroundService(bool run) async {
    final res = await _channel.invokeMethod(_BACKGROUND_SERVICE, {"run":run});
    return res;
  }

  static Future<bool> plot(String platformType, String platformId, String sensorType) async {
    final res = await _channel.invokeMethod(_PLOT, {"platformType":platformType, "platformId": platformId,"sensorType": sensorType});
    return res;
  }
  static Stream<String> _events;

  static Stream<String> get listenSensorData {
    if (_events == null) {
      _events = _sensorDataEventChannel
          .receiveBroadcastStream()
          .map(
              (dynamic event) {
                print("event");
            return "a";
          });
    }
    return _events;
  }

}
