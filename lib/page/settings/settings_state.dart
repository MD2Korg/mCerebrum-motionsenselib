

abstract class SettingsState {
  SettingsState([List props = const []]);
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

