import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:flutter_blue/flutter_blue.dart';
import 'package:location_permissions/location_permissions.dart';
import 'package:motionsenselib/bluetooth/bluetooth_manager.dart';
import 'package:motionsenselib/settings/device.dart';
import 'package:motionsenselib/settings/device_settings.dart';
import 'package:motionsenselib/settings/settings.dart';

import 'settings_event.dart';
import 'settings_state.dart';

class SettingsBloc extends Bloc<SettingsEvent, SettingsState> {
  final _scanStreamController = StreamController<List<Device>>();

  StreamSink<List<Device>> get scanSink => _scanStreamController.sink;

  Stream<List<Device>> get streamScan => _scanStreamController.stream;

  final _configuredStreamController = StreamController<List<DeviceSettings>>();

  StreamSink<List<DeviceSettings>> get configuredSink =>
      _configuredStreamController.sink;

  Stream<List<DeviceSettings>> get streamConfigured =>
      _configuredStreamController.stream;

  final _statusStreamController = StreamController<int>();

  StreamSink<int> get statusSink => _statusStreamController.sink;

  Stream<int> get streamStatus => _statusStreamController.stream;

  SettingsBloc(Settings settings){
    bluetoothManager = new BluetoothManager(settings);
  }
  Settings settings(){
    return bluetoothManager.settings;
  }

  BluetoothManager bluetoothManager;

  bool isScanning() {
    return bluetoothManager.isScanning();
  }

  @override
  SettingsState get initialState => EmptyState();

  void statusListener() async {
    LocationPermissions locationPermissions = new LocationPermissions();

    bluetoothManager.getBluetoothState().listen((bluetoothStatus) {
      locationPermissions.checkServiceStatus().then((gpsStatus) {
        if (bluetoothStatus == BluetoothState.on &&
            gpsStatus == ServiceStatus.enabled) {
          statusSink.add(0);
          dispatch(StartScanEvent());
        } else if (bluetoothStatus != BluetoothState.on &&
            gpsStatus == ServiceStatus.enabled) {
          statusSink.add(1);
          dispatch(StopScanEvent());
        } else if (bluetoothStatus == BluetoothState.on &&
            gpsStatus != ServiceStatus.enabled) {
          statusSink.add(2);
          dispatch(StopScanEvent());
        } else {
          statusSink.add(3);
          dispatch(StopScanEvent());
        }
      });
    });
    locationPermissions.serviceStatus.listen((gpsStatus) {
      bluetoothManager.isBluetoothOn().then((bluetoothStatus) {
        if (bluetoothStatus == true && gpsStatus == ServiceStatus.enabled) {
          statusSink.add(0);
          dispatch(StartScanEvent());
        } else if (bluetoothStatus != true &&
            gpsStatus == ServiceStatus.enabled) {
          statusSink.add(1);
          dispatch(StopScanEvent());
        } else if (bluetoothStatus == true &&
            gpsStatus != ServiceStatus.enabled) {
          statusSink.add(2);
          dispatch(StopScanEvent());
        } else {
          statusSink.add(3);
          dispatch(StopScanEvent());
        }
      });
    });
  }

  @override
  Stream<SettingsState> mapEventToState(SettingsEvent event) async* {

    print("settings_bloc: event = " + event.toString());
    if (event is InitEvent) {
      PermissionStatus p =
          await new LocationPermissions().checkPermissionStatus();
      if (p == PermissionStatus.granted) {
        statusListener();
        configuredSink.add(bluetoothManager.getConfiguredDevices());
      } else {
        yield PermissionRequiredState();
      }
    } else if (event is EnableBluetoothEvent) {
      bluetoothManager.enableBluetooth();
    } else if (event is EnableGPSEvent) {
      bluetoothManager.enableGPS();
    } else if (event is StartScanEvent) {
      bool res = await bluetoothManager.isAvailable();
      if (!res) {
        yield BluetoothUnavailableState();
      } else {
        await _startScan();
        yield ScanStarted();
      }
    } else if (event is StopScanEvent) {
      if (bluetoothManager.isScanning()) await bluetoothManager.stopScan();
      yield ScanEnded();
    } else if (event is AddDeviceEvent) {
      yield LoadingState("Adding Device...");
      await Future.delayed(const Duration(milliseconds: 100), () => "1");

      await bluetoothManager.addDevice(event.device);
      configuredSink.add(bluetoothManager.getConfiguredDevices());
      scanSink.add(bluetoothManager.scanList);
      yield LoadedState();
      print("abc");
    } else if (event is DeleteDeviceEvent) {
      bluetoothManager.deleteDevice(event.deviceId);
      configuredSink.add(bluetoothManager.getConfiguredDevices());
      scanSink.add(bluetoothManager.scanList);
      yield LoadedState();
    }
  }
  Future<void> _startScan() async{
    if(await bluetoothManager.isBluetoothOn()==false) return null;
    LocationPermissions locationPermissions = new LocationPermissions();
    if(await locationPermissions.checkServiceStatus()!=ServiceStatus.enabled) return null;
    if(bluetoothManager.isScanning()) return null;
      bluetoothManager.startScan((List<Device> devices) {
        scanSink.add(devices);
      });
  }

  Future<void> dispose() async{
    _scanStreamController.close();
    await bluetoothManager.stopScan();
    super.dispose();
  }

  bool isConfigured(Device device) {
    return bluetoothManager.isExist(device);
  }
}
