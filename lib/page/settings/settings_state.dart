import 'package:equatable/equatable.dart';


abstract class SettingsState extends Equatable {
  SettingsState([List props = const []]) : super(props);
}

class EmptyState extends SettingsState {}

class LoadingState extends SettingsState {
  final String message;
  LoadingState(this.message);
}
class BluetoothDisableState extends SettingsState{}
class BluetoothUnavailableState extends SettingsState{}
class PermissionRequiredState extends SettingsState{}
class ScanStarted extends SettingsState{}
class ScanEnded extends SettingsState{}
class LoadedState extends SettingsState {}

