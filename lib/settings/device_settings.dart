class DeviceSettings {
  final String platformType;
  final String platformId;
  final String deviceId;
  final String version;
  bool _correctTimestamp;
  double _characteristicMotionSampleRate;
  double _characteristicPpgSampleRate;

  bool _accelerometerEnable;
  int _accelerometerSensitivity;
  bool _gyroscopeEnable;
  int _gyroscopeSensitivity;
  bool _quaternionEnable;
  bool _sequenceNumberMotionEnable;
  bool _rawMotionEnable;
  bool _ppgEnable;
  int _ppgRed;
  int _ppgGreen;
  int _ppgInfrared;
  bool _ppgFilterEnable;
  bool _rawPpgEnable;
  bool _sequenceNumberPpgEnable;
  bool _magnetometerEnable;
  bool _magnetometerSensitivityEnable;
  bool _sequenceNumberMagnetometerEnable;
  bool _rawMagnetometerEnable;
  bool _respirationEnable;
  bool _ecgEnable;
  bool _batteryEnable;

  bool _accelerometerDataQualityEnable;
  bool _ppgDataQualityEnable;
  bool _respirationDataQualityEnable;
  bool _ecgDataQualityEnable;
  int _minConnectionInterval;


  factory DeviceSettings.fromJson(Map<String, dynamic> json) {
    DeviceSettings d = DeviceSettings(json["platformType"], json["platformId"],
        json["deviceId"], json["version"]);
    d._correctTimestamp = json["correctTimestamp"];
    d._characteristicMotionSampleRate = json["characteristicMotionSampleRate"];
    d._characteristicPpgSampleRate = json["characteristicPpgSampleRate"];

    d._accelerometerEnable = json['accelerometerEnable'];
    d._accelerometerSensitivity = json['accelerometerSensitivity'];
    d._gyroscopeEnable = json['gyroscopeEnable'];
    d._gyroscopeSensitivity = json['gyroscopeSensitivity'];
    d._quaternionEnable = json['quaternionEnable'];
    d._sequenceNumberMotionEnable = json['sequenceNumberMotionEnable'];
    d._rawMotionEnable = json['rawMotionEnable'];
    d._ppgEnable = json['ppgEnable'];
    d._ppgRed = json['ppgRed'];
    d._ppgGreen = json['ppgGreen'];
    d._ppgInfrared = json['ppgInfrared'];
    d._ppgFilterEnable = json['ppgFilterEnable'];
    d._rawPpgEnable = json['rawPpgEnable'];
    d._sequenceNumberPpgEnable = json['sequenceNumberPpgEnable'];
    d._magnetometerEnable = json['magnetometerEnable'];
    d._magnetometerSensitivityEnable = json['magnetometerSensitivityEnable'];
    d._sequenceNumberMagnetometerEnable =
        json['sequenceNumberMagnetometerEnable'];
    d._rawMagnetometerEnable = json['rawMagnetometerEnable'];
    d._respirationEnable = json['respirationEnable'];
    d._ecgEnable = json['ecgEnable'];
    d._batteryEnable = json['batteryEnable'];

    d._accelerometerDataQualityEnable = json['accelerometerDataQualityEnable'];
    d._ppgDataQualityEnable = json['ppgDataQualityEnable'];
    d._respirationDataQualityEnable = json['respirationDataQualityEnable'];
    d._ecgDataQualityEnable = json['ecgDataQualityEnable'];

    d._minConnectionInterval = json['minConnectionInterval'];

    return d;
  }

  Map<String, dynamic> toJson() => {
        'platformType': platformType,
        'platformId': platformId,
        'deviceId': deviceId,
        'version': version,
        'correctTimestamp':_correctTimestamp,
    'characteristicMotionSampleRate':_characteristicMotionSampleRate,
    'characteristicPpgSampleRate':_characteristicPpgSampleRate,
        'accelerometerEnable': _accelerometerEnable,
        'accelerometerSensitivity': _accelerometerSensitivity,
        'gyroscopeEnable': _gyroscopeEnable,
        'gyroscopeSensitivity': _gyroscopeSensitivity,
        'quaternionEnable': _quaternionEnable,
        'sequenceNumberMotionEnable': _sequenceNumberMotionEnable,
        'rawMotionEnable': _rawMotionEnable,
        'batteryEnable': _batteryEnable,
        'ppgEnable': _ppgEnable,
        'ppgRed': _ppgRed,
        'ppgGreen': _ppgGreen,
        'ppgInfrared': _ppgInfrared,
        'ppgFilterEnable': _ppgFilterEnable,
        'rawPpgEnable': _rawPpgEnable,
        'sequenceNumberPpgEnable': _sequenceNumberPpgEnable,
        'magnetometerEnable': _magnetometerEnable,
        'magnetometerSensitivityEnable': _magnetometerSensitivityEnable,
        'sequenceNumberMagnetometerEnable': _sequenceNumberMagnetometerEnable,
        'rawMagnetometerEnable': _rawMagnetometerEnable,
        'respirationEnable': _respirationEnable,
        'ecgEnable': _ecgEnable,
        'accelerometerDataQualityEnable': _accelerometerDataQualityEnable,
        'ppgDataQualityEnable': _ppgDataQualityEnable,
        'respirationDataQualityEnable': _respirationDataQualityEnable,
        'ecgDataQualityEnable': _ecgDataQualityEnable,
        'minConnectionInterval': _minConnectionInterval,
      };

  DeviceSettings(
      this.platformType, this.platformId, this.deviceId, this.version,
      {DeviceSettings defaultSetting}) {
    setToDefault(defaultSetting);
  }

  void setToDefault(DeviceSettings defaultSettings) {
    switch (platformType) {
      case "AUTOSENSE_BLE":
        _setToDefaultAutoSense(defaultSettings);
        break;
      case "MOTION_SENSE":
        _setToDefaultMotionSense(defaultSettings);
        break;
      case "MOTION_SENSE_HRV":
        _setToDefaultMotionSenseHRV(defaultSettings);
        break;
      case "MOTION_SENSE_HRV_PLUS":
        _setToDefaultMotionSenseHRVPlus(defaultSettings);
        break;
      case "MOTION_SENSE_V2":
        _setToDefaultMotionSenseV2(defaultSettings);
        break;
      case "MOTION_SENSE_HRV_V2":
        _setToDefaultMotionSenseHRVV2(defaultSettings);
        break;
      case "MOTION_SENSE_HRV_PLUS_V2":
        _setToDefaultMotionSenseHRVPlusV2(defaultSettings);
        break;

      case "MOTION_SENSE_HRV_PLUS_GEN2_GREEN":
        _setToDefaultMotionSenseHRVPlusV2(defaultSettings);
        break;
      case "MOTION_SENSE_HRV_PLUS_GEN2_RED":
        _setToDefaultMotionSenseHRVPlusV2(defaultSettings);
        break;
    }
  }

  void _setToDefaultAutoSense(DeviceSettings defaultSettings) {
    this._correctTimestamp=defaultSettings?._correctTimestamp??true;
    this._accelerometerEnable = defaultSettings?._accelerometerEnable ?? true;
    this._respirationEnable = defaultSettings?._respirationEnable ?? true;
    this._ecgEnable = defaultSettings?._ecgEnable ?? true;
    this._sequenceNumberMotionEnable =
        defaultSettings?._sequenceNumberMotionEnable ?? true;
    this._rawMotionEnable = defaultSettings?._rawMotionEnable ?? true;
    this._batteryEnable = defaultSettings?._batteryEnable ?? true;
    this._accelerometerDataQualityEnable =
        defaultSettings?._accelerometerDataQualityEnable ?? true;
    this._respirationDataQualityEnable =
        defaultSettings?._respirationDataQualityEnable ?? true;
    this._ecgDataQualityEnable =
        defaultSettings?._ecgDataQualityEnable ?? true;
  }

  void _setToDefaultMotionSense(DeviceSettings defaultSettings) {
    this._correctTimestamp=defaultSettings?._correctTimestamp??true;
    this._accelerometerEnable = defaultSettings?._accelerometerEnable ?? true;
    this._gyroscopeEnable = defaultSettings?._gyroscopeEnable ?? true;
    this._sequenceNumberMotionEnable =
        defaultSettings?._sequenceNumberMotionEnable ?? true;
    this._rawMotionEnable = defaultSettings?._rawMotionEnable ?? true;
    this._batteryEnable = defaultSettings?._batteryEnable ?? true;
    this._accelerometerDataQualityEnable =
        defaultSettings?._accelerometerDataQualityEnable ?? true;
  }

  void _setToDefaultMotionSenseHRV(DeviceSettings defaultSettings) {
    _setToDefaultMotionSense(defaultSettings);
    this._ppgEnable = defaultSettings?._ppgEnable ?? true;
    this._ppgDataQualityEnable = defaultSettings?._ppgDataQualityEnable ?? true;
  }

  void _setToDefaultMotionSenseHRVPlus(DeviceSettings defaultSettings) {
    this._correctTimestamp=defaultSettings?._correctTimestamp??true;
    this._accelerometerEnable = defaultSettings?._accelerometerEnable ?? true;
    this._accelerometerDataQualityEnable =
        defaultSettings?._accelerometerDataQualityEnable ?? true;

    this._quaternionEnable = defaultSettings?._quaternionEnable ?? true;

    this._sequenceNumberMotionEnable =
        defaultSettings?._sequenceNumberMotionEnable ?? true;
    this._rawMotionEnable = defaultSettings?._rawMotionEnable ?? true;
    this._batteryEnable = defaultSettings?._batteryEnable ?? true;
    this._ppgEnable = defaultSettings?._ppgEnable ?? true;
    this._ppgDataQualityEnable = defaultSettings?._ppgDataQualityEnable ?? true;

    this._magnetometerEnable = defaultSettings?._magnetometerEnable ?? true;
    this._magnetometerSensitivityEnable =
        defaultSettings?._magnetometerSensitivityEnable ?? true;
    this._sequenceNumberMagnetometerEnable =
        defaultSettings?._sequenceNumberMagnetometerEnable ?? true;
    this._rawMagnetometerEnable =
        defaultSettings?._rawMagnetometerEnable ?? true;
  }

  void _setToDefaultMotionSenseV2(DeviceSettings d) {
    this._correctTimestamp=d?._correctTimestamp??true;
    this._characteristicMotionSampleRate = d?._characteristicMotionSampleRate ?? 25;
    this._accelerometerEnable = d?._accelerometerEnable ?? true;
    this._accelerometerSensitivity = d?._accelerometerSensitivity ?? 4;
    this._accelerometerDataQualityEnable =
        d?._accelerometerDataQualityEnable ?? true;

    this._gyroscopeEnable = d?._gyroscopeEnable ?? true;
    this._gyroscopeSensitivity = d?._gyroscopeSensitivity ?? 500;

    this._sequenceNumberMotionEnable = d?._sequenceNumberMotionEnable ?? true;
    this._rawMotionEnable = d?._rawMotionEnable ?? true;

    this._batteryEnable = d?._batteryEnable ?? true;
    this._minConnectionInterval = d?._minConnectionInterval ?? 10;
  }

  void _setToDefaultMotionSenseHRVV2(DeviceSettings d) {
    _setToDefaultMotionSenseV2(d);
    this._characteristicPpgSampleRate = d?._characteristicPpgSampleRate ?? 25;
    this._ppgEnable = d?._ppgEnable ?? true;
    this._ppgRed = d?._ppgRed ?? 0x3E;
    this._ppgGreen = d?._ppgGreen ?? 0x68;
    this._ppgInfrared = d?._ppgInfrared ?? 0x14;
    this._ppgFilterEnable = d?._ppgFilterEnable ?? false;
    this._sequenceNumberPpgEnable = d?._sequenceNumberPpgEnable ?? true;
    this._rawPpgEnable = d?._rawPpgEnable ?? true;
    this._ppgDataQualityEnable = d?._ppgDataQualityEnable ?? true;
  }

  void _setToDefaultMotionSenseHRVPlusV2(DeviceSettings d) {
    _setToDefaultMotionSenseHRVV2(d);
    this._magnetometerEnable = d?._magnetometerEnable ?? true;
    this._magnetometerSensitivityEnable =
        d?._magnetometerSensitivityEnable ?? true;
    this._sequenceNumberMagnetometerEnable =
        d?._sequenceNumberMagnetometerEnable ?? true;
    this._rawMagnetometerEnable = d?._rawMagnetometerEnable ?? true;
  }

  bool isVersion2() {
    if (platformType == "MOTION_SENSE" ||
        platformType == "MOTION_SENSE_HRV" ||
        platformType == "MOTION_SENSE_HRV_PLUS")
      return false;
    else
      return true;
  }

  bool isMotionSense() {
    if (platformType == "MOTION_SENSE" || platformType == "MOTION_SENSE_V2")
      return true;
    else
      return false;
  }

  bool isMotionSenseHRV() {
    if (platformType == "MOTION_SENSE_HRV" ||
        platformType == "MOTION_SENSE_HRV_V2")
      return true;
    else
      return false;
  }

  bool isMotionSenseHRVPlus() {
    if (platformType == "MOTION_SENSE_HRV_PLUS" ||
        platformType == "MOTION_SENSE_HRV_PLUS_V2")
      return true;
    else
      return false;
  }

  bool isMotionSenseHRVPlusGen2() {
    if (platformType == "MOTION_SENSE_HRV_PLUS_GEN2_GREEN" ||
        platformType == "MOTION_SENSE_HRV_PLUS_GEN2_RED")
      return true;
    else
      return false;
  }

  setCharacteristicMotionSampleRate(double sampleRate) {
    _characteristicMotionSampleRate = sampleRate;
  }

  void setCharacteristicPpgSampleRate(double value) {
    _characteristicPpgSampleRate = value;
  }

  setAccelerometerSensitivity(int value) {
    _accelerometerSensitivity = value;
  }

  setGyroscopeSensitivity(int value) {
    _gyroscopeSensitivity = value;
  }

  bool get correctTimestamp => _correctTimestamp;


  int get accelerometerSensitivity => _accelerometerSensitivity;

  int get gyroscopeSensitivity => _gyroscopeSensitivity;

  bool get gyroscopeEnable => _gyroscopeEnable;

  int get minConnectionInterval => _minConnectionInterval;


  bool get rawMagnetometerEnable => _rawMagnetometerEnable;

  bool get sequenceNumberMagnetometerEnable =>
      _sequenceNumberMagnetometerEnable;

  bool get magnetometerEnable => _magnetometerEnable;

  bool get sequenceNumberPpgEnable => _sequenceNumberPpgEnable;

  bool get rawPpgEnable => _rawPpgEnable;

  bool get ppgFilterEnable => _ppgFilterEnable;

  int get ppgInfrared => _ppgInfrared;

  int get ppgGreen => _ppgGreen;

  int get ppgRed => _ppgRed;

  bool get ppgEnable => _ppgEnable;

  bool get batteryEnable => _batteryEnable;

  bool get rawMotionEnable => _rawMotionEnable;

  bool get sequenceNumberMotionEnable => _sequenceNumberMotionEnable;

  bool get quaternionEnable => _quaternionEnable;

  bool get accelerometerEnable => _accelerometerEnable;

  void setMinConnectionInterval(int value) {
    _minConnectionInterval = value;
  }

  void setPpgRed(int v) {
    _ppgRed = v;
  }

  void setPpgGreen(int v) {
    _ppgGreen = v;
  }

  void setPpgInfrared(int v) {
    _ppgInfrared = v;
  }

  void setPpgFilterEnable(bool newValue) {
    _ppgFilterEnable = newValue;
  }

  String getName() {
    switch (platformType) {
      case "AUTOSENSE_BLE":
        return "AutoSense";
      case "MOTION_SENSE":
        return "MotionSense";
      case "MOTION_SENSE_HRV":
        return "MotionSenseHRV";
      case "MOTION_SENSE_HRV_PLUS":
        return "MotionSenseHRV";
      case "MOTION_SENSE_V2":
        return "MotionSense_V2";
      case "MOTION_SENSE_HRV_V2":
        return "MotionSenseHRV_V2";
      case "MOTION_SENSE_HRV_PLUS_V2":
        return "MotionSenseHRV+_V2";
      case "MOTION_SENSE_HRV_PLUS_GEN2_RED":
        return "MotionSenseHRV+Gen2 (Red)";
      case "MOTION_SENSE_HRV_PLUS_GEN2_GREEN":
        return "MotionSenseHRV+Gen2 (Green)";
      case "MOTION_SENSE_HRV_PLUS_GEN2_RED":
        return "MotionSenseHRV+Gen2 (Red)";
      default:
        return "Unknown";
    }
  }

  double get characteristicMotionSampleRate => _characteristicMotionSampleRate;

  bool get ecgDataQualityEnable => _ecgDataQualityEnable;

  bool get respirationDataQualityEnable => _respirationDataQualityEnable;

  bool get ppgDataQualityEnable => _ppgDataQualityEnable;

  bool get accelerometerDataQualityEnable => _accelerometerDataQualityEnable;

  bool get ecgEnable => _ecgEnable;

  bool get respirationEnable => _respirationEnable;

  bool get magnetometerSensitivityEnable => _magnetometerSensitivityEnable;

  double get characteristicPpgSampleRate => _characteristicPpgSampleRate;
}
