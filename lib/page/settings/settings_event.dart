import 'package:equatable/equatable.dart';
import 'package:motionsenselib/settings/device.dart';

abstract class SettingsEvent extends Equatable {
  SettingsEvent([List props = const []]) : super(props);
}
class InitEvent extends SettingsEvent{}
class EnableGPSEvent extends SettingsEvent{}
class EnableBluetoothEvent extends SettingsEvent{}
class StartScanEvent extends SettingsEvent {}
class StopScanEvent extends SettingsEvent {}
class AddDeviceEvent extends SettingsEvent{
  final Device device;
  AddDeviceEvent(this.device);
}
class DeleteDeviceEvent extends SettingsEvent{
  final String deviceId;
  DeleteDeviceEvent(this.deviceId);
}
