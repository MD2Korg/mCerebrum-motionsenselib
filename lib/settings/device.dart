import 'package:flutter_blue/flutter_blue.dart';

class Device{
  BluetoothDevice bluetoothDevice;
  String platformId;
  Device(this.bluetoothDevice,{this.platformId});
}