import 'package:motionsenselib/settings/settings.dart';

import 'data/summary.dart';
import 'motionsenselib.dart';

class MotionSense {
  static Future<void> setSettings(Settings settings) async {
    return Motionsenselib.setSettings(settings);
  }

  static Future<Summary> getSummary() async {
    Map<String, dynamic> res = await Motionsenselib.getSummary();
    return Summary(res);
  }

  static Future<bool> setBackgroundService(bool run) async {
    return await Motionsenselib.setBackgroundService(run);
  }

  static Future<bool> plot(String platformType, String platformId,
      String sensorType) async {
    return await Motionsenselib.plot(platformType, platformId, sensorType);
  }
}