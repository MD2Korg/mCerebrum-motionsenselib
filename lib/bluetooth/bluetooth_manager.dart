import 'dart:async';
import 'dart:io';

import 'package:android_intent/android_intent.dart';
import 'package:flutter_blue/flutter_blue.dart';
import 'package:motionsenselib/settings/device.dart';
import 'package:motionsenselib/settings/device_settings.dart';
import 'package:motionsenselib/settings/motionsense_settings.dart';

class BluetoothManager {
  bool _scanning=false;
  StreamSubscription _scanSubscription;
  List<Device> scanList = new List();
  MotionSenseSettings settings;

  BluetoothManager(this.settings);

  Stream<BluetoothState> getBluetoothState() {
    return FlutterBlue.instance.state;
  }

  Future<bool> isBluetoothOn() async {
    return await FlutterBlue.instance.isOn;
  }

  Future<bool> isAvailable() {
    return FlutterBlue.instance.isAvailable;
  }
  bool isScanning(){
    return _scanning;
  }
  bool isExist(Device device){
    for(int i=0;i<settings.motionSenseDevices.length;i++){
      if(settings.motionSenseDevices[i].platformId==device.platformId) return true;
    }
    return false;

  }

  void startScan(Function myFunc) async{
    if(_scanning==true) return null;
    scanList.clear();

    _scanning=true;
    Guid g = new Guid("0000180f-0000-1000-8000-00805f9b34fb");
    _scanSubscription = FlutterBlue.instance.scan(withServices: [g]).listen((scanResult) {
      for(int i=0;i<scanList.length;i++){
        if(scanList[i].bluetoothDevice.id.toString().toLowerCase()==scanResult.device.id.toString().toLowerCase())
          return;
      }
      for(int i=0;i<settings.motionSenseDevices.length;i++){
        if(scanResult.device.id.toString().toLowerCase()==settings.motionSenseDevices[i].deviceId.toLowerCase())
          return;
      }
      scanList.add(new Device(scanResult.device));
      myFunc(scanList);
    });
  }

  Future<void> stopScan() async{
    await FlutterBlue.instance.stopScan();
    if(_scanSubscription!=null) {
      await _scanSubscription.cancel();
      _scanSubscription=null;
    }
    _scanning = false;
  }
  List<DeviceSettings> getConfiguredDevices(){
    return settings.motionSenseDevices;
  }
  Future<void> addDevice(Device device) async{
    DeviceSettings deviceSettings;
    switch(device.bluetoothDevice.name){
      case "Autosense": deviceSettings = new DeviceSettings( "AUTOSENSE_BLE", device.platformId, device.bluetoothDevice.id.toString(), "1.0.0.0");break;
      case "EETech_Motion": deviceSettings=new DeviceSettings("MOTION_SENSE", device.platformId, device.bluetoothDevice.id.toString(), "1.0.3.0");break;
      case "MotionSenseHRV": deviceSettings=new DeviceSettings("MOTION_SENSE_HRV", device.platformId, device.bluetoothDevice.id.toString(), "1.0.1.0");break;
      case "MotionSenseHRV+": deviceSettings=new DeviceSettings("MOTION_SENSE_HRV_PLUS", device.platformId, device.bluetoothDevice.id.toString(), "1.0.2.0");break;
      case "MotionSense2": deviceSettings=await _addDeviceForV2(device);break;
    }
    settings.motionSenseDevices.add(deviceSettings);
    for(int i=0;i<scanList.length;i++){
      if(scanList[i].bluetoothDevice.id.toString().toLowerCase()==device.bluetoothDevice.id.toString().toLowerCase()){
        scanList.removeAt(i);
        break;
      }
    }
  }
  Future<DeviceSettings> _addDeviceForV2(Device device) async{
    DeviceSettings deviceSettings;
    List<int> version=await getVersion(device);
    String versionStr = version[0].toString();
    for(int i=1;i<version.length;i++){
      versionStr+="."+version[i].toString();
    }
    switch(version[2]){
      case 1: deviceSettings=new DeviceSettings("MOTION_SENSE_V2", device.platformId, device.bluetoothDevice.id.toString(), versionStr);break;
      case 2: deviceSettings=new DeviceSettings("MOTION_SENSE_HRV_V2", device.platformId, device.bluetoothDevice.id.toString(), versionStr);
      break;
      case 2: deviceSettings=new DeviceSettings("MOTION_SENSE_HRV_PLUS_V2", device.platformId, device.bluetoothDevice.id.toString(), versionStr);break;
      case 4: deviceSettings=new DeviceSettings("MOTION_SENSE_HRV_PLUS_GEN2_RED", device.platformId, device.bluetoothDevice.id.toString(), versionStr);break;
      case 5: deviceSettings=new DeviceSettings("MOTION_SENSE_HRV_PLUS_GEN2_GREEN", device.platformId, device.bluetoothDevice.id.toString(), versionStr);break;
      case 6: deviceSettings=new DeviceSettings("MOTION_SENSE_HRV_PLUS_GEN2_RED", device.platformId, device.bluetoothDevice.id.toString(), versionStr);break;
    }
    return deviceSettings;
  }

  Future<List<int>> getVersion(Device device) async{
    List<int> version;
    await device.bluetoothDevice.connect();
    List<BluetoothService> services = await device.bluetoothDevice.discoverServices();
    for(int i=0;i<services.length;i++){
      List<BluetoothCharacteristic> ch= services[i].characteristics;
      for(int j=0;j<ch.length;j++){
        if(ch[j].uuid.toString().toLowerCase()=="da39d600-1d81-48e2-9c68-d0ae4bbd351f"){
          version = await ch[j].read();
        }
      }
    }
    await device.bluetoothDevice.disconnect();
    return version;
  }
  Future<void> enableGPS() async{
    final AndroidIntent intent = new AndroidIntent(
      action: 'android.settings.LOCATION_SOURCE_SETTINGS',
    );
    await intent.launch();
  }

  Future<void> enableBluetooth() async {
    if (Platform.isAndroid) {
      // This will trigger a dialog ("Application wants to turn on Bluetooth, do you want to allow it?")
      const intent = AndroidIntent(
        action: 'android.bluetooth.adapter.action.REQUEST_ENABLE',
      );
      await intent.launch();

      // We wait 5 seconds - fixed.
      // This is a tricky bit, because if the user waits on the aforementioned dialog for more than 5 seconds, we still have a disabled bluetooth adapter below.
      // Also can not wait forever, so prepare your UI to switch back to a "un-connected" status.
      await Future.delayed(Duration(seconds: 5));
    }
  }

  void deleteDevice(String deviceId) {
    for(int i=0;i<settings.motionSenseDevices.length;i++){
      if(settings.motionSenseDevices[i].deviceId==deviceId) {
        settings.motionSenseDevices.removeAt(i);
        return;
      }
    }
  }
}
