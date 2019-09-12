class DeviceSettings {
  String name;
  String platformType;
  String platformId;
  String deviceId;
  String version;

  bool _accelerometerEnable;
  double _accelerometerSampleRate;
  String _accelerometerWriteType;
  int _accelerometerSensitivity;
  bool _accelerometerDataQualityEnable;

  bool _gyroscopeEnable;
  double _gyroscopeSampleRate;
  String _gyroscopeWriteType;
  int _gyroscopeSensitivity;

  bool _quaternionEnable;
  double _quaternionSampleRate;
  String _quaternionWriteType;

  bool _sequenceNumberMotionEnable;
  double _sequenceNumberMotionSampleRate;
  String _sequenceNumberMotionWriteType;

  bool _rawMotionEnable;
  double _rawMotionSampleRate;
  String _rawMotionWriteType;

  bool _batteryEnable;
  double _batterySampleRate;
  String _batteryWriteType;

  bool _ppgEnable;
  double _ppgSampleRate;
  String _ppgWriteType;
  int _ppgRed;
  int _ppgGreen;
  int _ppgInfrared;
  bool _ppgFilterEnable;
  bool _rawPpgEnable;
  double _rawPpgSampleRate;
  String _rawPpgWriteType;
  bool _sequenceNumberPpgEnable;
  double _sequenceNumberPpgSampleRate;
  String _sequenceNumberPpgWriteType;
  bool _ppgDataQualityEnable;

  bool _magnetometerEnable;
  double _magnetometerSampleRate;
  String _magnetometerWriteType;
  bool _magnetometerSensitivityEnable;
  double _magnetometerSensitivitySampleRate;
  String _magnetometerSensitivityWriteType;
/*
  double _magnetometerXSensitivity;
  double _magnetometerYSensitivity;
  double _magnetometerZSensitivity;
*/
  bool _sequenceNumberMagnetometerEnable;
  double _sequenceNumberMagnetometerSampleRate;
  String _sequenceNumberMagnetometerWriteType;
  bool _rawMagnetometerEnable;
  double _rawMagnetometerSampleRate;
  String _rawMagnetometerWriteType;

  int _minConnectionInterval;
  factory DeviceSettings.fromJson(Map<String, dynamic> json){
    DeviceSettings d = DeviceSettings(json["name"],json["platformType"], json["platformId"], json["deviceId"], json["version"]);
    d._accelerometerEnable=json['accelerometerEnable'];
    d._accelerometerSampleRate=json['accelerometerSampleRate'];
    d._accelerometerWriteType=json['accelerometerWriteType'];
    d._accelerometerSensitivity=json['accelerometerSensitivity'];
    d._accelerometerDataQualityEnable=json['accelerometerDataQualityEnable'];

    d._gyroscopeEnable=json['gyroscopeEnable'];
    d._gyroscopeSampleRate=json['gyroscopeSampleRate'];
    d._gyroscopeWriteType=json['gyroscopeWriteType'];
    d._gyroscopeSensitivity=json['gyroscopeSensitivity'];

    d._quaternionEnable=json['quaternionEnable'];
    d._quaternionSampleRate=json['quaternionSampleRate'];
    d._quaternionWriteType=json['quaternionWriteType'];

    d._sequenceNumberMotionEnable=json['sequenceNumberMotionEnable'];
    d._sequenceNumberMotionSampleRate=json['sequenceNumberMotionSampleRate'];
    d._sequenceNumberMotionWriteType=json['sequenceNumberMotionWriteType'];

    d._rawMotionEnable=json['rawMotionEnable'];
    d._rawMotionSampleRate=json['rawMotionSampleRate'];
    d._rawMotionWriteType=json['rawMotionWriteType'];

    d._batteryEnable=json['batteryEnable'];
    d._batterySampleRate=json['batterySampleRate'];
    d._batteryWriteType=json['batteryWriteType'];

    d._ppgEnable=json['ppgEnable'];
    d._ppgSampleRate=json['ppgSampleRate'];
    d._ppgWriteType=json['ppgWriteType'];
    d._ppgRed=json['ppgRed'];
    d._ppgGreen=json['ppgGreen'];
    d._ppgInfrared=json['ppgInfrared'];
    d._ppgFilterEnable=json['ppgFilterEnable'];
    d._rawPpgEnable=json['rawPpgEnable'];
    d._rawPpgSampleRate=json['rawPpgSampleRate'];
    d._rawPpgWriteType=json['rawPpgWriteType'];
    d._sequenceNumberPpgEnable=json['sequenceNumberPpgEnable'];
    d._sequenceNumberPpgSampleRate=json['sequenceNumberPpgSampleRate'];
    d._sequenceNumberPpgWriteType=json['sequenceNumberPpgWriteType'];
    d._ppgDataQualityEnable=json['ppgDataQualityEnable'];

    d._magnetometerEnable=json['magnetometerEnable'];
    d._magnetometerSampleRate=json['magnetometerSampleRate'];
    d._magnetometerWriteType=json['magnetometerWriteType'];
    d._magnetometerSensitivityEnable=json['magnetometerSensitivityEnable'];
    d._magnetometerSensitivitySampleRate=json['magnetometerSensitivitySampleRate'];
    d._magnetometerSensitivityWriteType=json['magnetometerSensitivityWriteType'];
    d._sequenceNumberMagnetometerEnable=json['sequenceNumberMagnetometerEnable'];
    d._sequenceNumberMagnetometerSampleRate=json['sequenceNumberMagnetometerSampleRate'];
    d._sequenceNumberMagnetometerWriteType=json['sequenceNumberMagnetometerWriteType'];
    d._rawMagnetometerEnable=json['rawMagnetometerEnable'];
    d._rawMagnetometerSampleRate=json['rawMagnetometerSampleRate'];
    d._rawMagnetometerWriteType=json['rawMagnetometerWriteType'];
    d._minConnectionInterval=json['minConnectionInterval'];

    return d;
  }

  Map<String, dynamic> toJson() => {
        'name': name,
        'platformType': platformType,
        'platformId': platformId,
        'deviceId': deviceId,
        'version': version,
        'accelerometerEnable': _accelerometerEnable,
        'accelerometerSampleRate': _accelerometerSampleRate,
        'accelerometerWriteType': _accelerometerWriteType,
        'accelerometerSensitivity': _accelerometerSensitivity,
        'accelerometerDataQualityEnable': _accelerometerDataQualityEnable,
        'gyroscopeEnable': _gyroscopeEnable,
        'gyroscopeSampleRate': _gyroscopeSampleRate,
        'gyroscopeWriteType': _gyroscopeWriteType,
        'gyroscopeSensitivity': _gyroscopeSensitivity,
        'quaternionEnable': _quaternionEnable,
        'quaternionSampleRate': _quaternionSampleRate,
        'quaternionWriteType': _quaternionWriteType,
        'sequenceNumberMotionEnable': _sequenceNumberMotionEnable,
        'sequenceNumberMotionSampleRate': _sequenceNumberMotionSampleRate,
        'sequenceNumberMotionWriteType': _sequenceNumberMotionWriteType,
        'rawMotionEnable': _rawMotionEnable,
        'rawMotionSampleRate': _rawMotionSampleRate,
        'rawMotionWriteType': _rawMotionWriteType,
        'batteryEnable': _batteryEnable,
        'batterySampleRate': _batterySampleRate,
        'batteryWriteType': _batteryWriteType,
        'ppgEnable': _ppgEnable,
        'ppgSampleRate': _ppgSampleRate,
        'ppgWriteType': _ppgWriteType,
        'ppgRed': _ppgRed,
        'ppgGreen': _ppgGreen,
        'ppgInfrared': _ppgInfrared,
        'ppgFilterEnable': _ppgFilterEnable,
        'rawPpgEnable': _rawPpgEnable,
        'rawPpgSampleRate': _rawPpgSampleRate,
        'rawPpgWriteType': _rawPpgWriteType,
        'sequenceNumberPpgEnable': _sequenceNumberPpgEnable,
        'sequenceNumberPpgSampleRate': _sequenceNumberPpgSampleRate,
        'sequenceNumberPpgWriteType': _sequenceNumberPpgWriteType,
        'ppgDataQualityEnable': _ppgDataQualityEnable,
        'magnetometerEnable': _magnetometerEnable,
        'magnetometerSampleRate': _magnetometerSampleRate,
        'magnetometerWriteType': _magnetometerWriteType,
        'magnetometerSensitivityEnable': _magnetometerSensitivityEnable,
        'magnetometerSensitivitySampleRate': _magnetometerSensitivitySampleRate,
        'magnetometerSensitivityWriteType': _magnetometerSensitivityWriteType,
        'sequenceNumberMagnetometerEnable': _sequenceNumberMagnetometerEnable,
        'sequenceNumberMagnetometerSampleRate':
            _sequenceNumberMagnetometerSampleRate,
        'sequenceNumberMagnetometerWriteType':
            _sequenceNumberMagnetometerWriteType,
        'rawMagnetometerEnable': _rawMagnetometerEnable,
        'rawMagnetometerSampleRate': _rawMagnetometerSampleRate,
        'rawMagnetometerWriteType': _rawMagnetometerWriteType,
        'minConnectionInterval': _minConnectionInterval,
      };

  DeviceSettings(this.name, this.platformType, this.platformId, this.deviceId,
      this.version,
      {DeviceSettings defaultSetting}) {
    setToDefault(defaultSetting);
  }

  void setToDefault(DeviceSettings defaultSettings) {
    switch (platformType) {
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

  void _setToDefaultMotionSense(DeviceSettings defaultSettings) {
    this._accelerometerEnable = defaultSettings?._accelerometerEnable ?? true;
    this._accelerometerSampleRate =
        defaultSettings?._accelerometerSampleRate ?? 16;
    this._accelerometerWriteType =
        defaultSettings?._accelerometerWriteType ?? "FIXED";
    this._accelerometerSensitivity =
        defaultSettings?._accelerometerSensitivity ?? 4;
    this._accelerometerDataQualityEnable =
        defaultSettings?._accelerometerDataQualityEnable ?? true;
    this._gyroscopeEnable = defaultSettings?._gyroscopeEnable ?? true;
    this._gyroscopeSampleRate = defaultSettings?._gyroscopeSampleRate ?? 32;
    this._gyroscopeWriteType = defaultSettings?._gyroscopeWriteType ?? "FIXED";
    this._gyroscopeSensitivity = defaultSettings?._gyroscopeSensitivity ?? 500;
    this._sequenceNumberMotionEnable =
        defaultSettings?._sequenceNumberMotionEnable ?? true;
    this._sequenceNumberMotionSampleRate =
        defaultSettings?._sequenceNumberMotionSampleRate ?? 16;
    this._sequenceNumberMotionWriteType =
        defaultSettings?._sequenceNumberMotionWriteType ?? "FIXED";
    this._rawMotionEnable = defaultSettings?._rawMotionEnable ?? true;
    this._rawMotionSampleRate = defaultSettings?._rawMotionSampleRate ?? 16;
    this._rawMotionWriteType =
        defaultSettings?._rawMotionWriteType ?? "AS_RECEIVED";
    this._batteryEnable = defaultSettings?._batteryEnable ?? true;
    this._batterySampleRate = defaultSettings?._batterySampleRate ?? 1;
    this._batteryWriteType =
        defaultSettings?._batteryWriteType ?? "AS_RECEIVED";
  }

  void _setToDefaultMotionSenseHRV(DeviceSettings defaultSettings) {
    _setToDefaultMotionSense(defaultSettings);
    this._gyroscopeSampleRate = defaultSettings?._gyroscopeSampleRate ?? 16;
    this._ppgEnable = defaultSettings?._ppgEnable ?? true;
    this._ppgSampleRate = defaultSettings?._ppgSampleRate ?? 16;
    this._ppgWriteType = defaultSettings?._ppgWriteType ?? "FIXED";
    this._ppgDataQualityEnable = defaultSettings?._ppgDataQualityEnable ?? true;
  }

  void _setToDefaultMotionSenseHRVPlus(DeviceSettings defaultSettings) {
    this._accelerometerEnable = defaultSettings?._accelerometerEnable ?? true;
    this._accelerometerSampleRate =
        defaultSettings?._accelerometerSampleRate ?? 25;
    this._accelerometerWriteType =
        defaultSettings?._accelerometerWriteType ?? "FIXED";
    this._accelerometerSensitivity =
        defaultSettings?._accelerometerSensitivity ?? 4;
    this._accelerometerDataQualityEnable =
        defaultSettings?._accelerometerDataQualityEnable ?? true;

    this._quaternionEnable = defaultSettings?._quaternionEnable ?? true;
    this._quaternionSampleRate = defaultSettings?._quaternionSampleRate ?? 25;
    this._quaternionWriteType =
        defaultSettings?._quaternionWriteType ?? "FIXED";

    this._sequenceNumberMotionEnable =
        defaultSettings?._sequenceNumberMotionEnable ?? true;
    this._sequenceNumberMotionSampleRate =
        defaultSettings?._sequenceNumberMotionSampleRate ?? 25;
    this._sequenceNumberMotionWriteType =
        defaultSettings?._sequenceNumberMotionWriteType ?? "FIXED";
    this._rawMotionEnable = defaultSettings?._rawMotionEnable ?? true;
    this._rawMotionSampleRate = defaultSettings?._rawMotionSampleRate ?? 25;
    this._rawMotionWriteType =
        defaultSettings?._rawMotionWriteType ?? "AS_RECEIVED";
    this._batteryEnable = defaultSettings?._batteryEnable ?? true;
    this._batterySampleRate = defaultSettings?._batterySampleRate ?? 1;
    this._batteryWriteType =
        defaultSettings?._batteryWriteType ?? "AS_RECEIVED";

    this._ppgEnable = defaultSettings?._ppgEnable ?? true;
    this._ppgSampleRate = defaultSettings?._ppgSampleRate ?? 25;
    this._ppgWriteType = defaultSettings?._ppgWriteType ?? "FIXED";
    this._ppgDataQualityEnable = defaultSettings?._ppgDataQualityEnable ?? true;

    this._magnetometerEnable = defaultSettings?._magnetometerEnable ?? true;
    this._magnetometerSampleRate =
        defaultSettings?._magnetometerSampleRate ?? 25;
    this._magnetometerWriteType =
        defaultSettings?._magnetometerWriteType ?? "FIXED";
    this._magnetometerSensitivityEnable =
        defaultSettings?._magnetometerSensitivityEnable ?? true;
    this._magnetometerSensitivitySampleRate =
        defaultSettings?._magnetometerSensitivitySampleRate ?? 12.5;
    this._magnetometerSensitivityWriteType =
        defaultSettings?._magnetometerSensitivityWriteType ?? "FIXED";
    this._sequenceNumberMagnetometerEnable =
        defaultSettings?._sequenceNumberMagnetometerEnable ?? true;
    this._sequenceNumberMagnetometerSampleRate =
        defaultSettings?._sequenceNumberMagnetometerSampleRate ?? 12.5;
    this._sequenceNumberMagnetometerWriteType =
        defaultSettings?._sequenceNumberMagnetometerWriteType ?? "FIXED";
    this._rawMagnetometerEnable =
        defaultSettings?._rawMagnetometerEnable ?? true;
    this._rawMagnetometerSampleRate =
        defaultSettings?._rawMagnetometerSampleRate ?? 12.5;
    this._rawMagnetometerWriteType =
        defaultSettings?._rawMagnetometerWriteType ?? "AS_RECEIVED";
  }

  void _setToDefaultMotionSenseV2(DeviceSettings d) {
    this._accelerometerEnable = d?._accelerometerEnable ?? true;
    this._accelerometerSampleRate = d?._accelerometerSampleRate ?? 25;
    this._accelerometerWriteType = d?._accelerometerWriteType ?? "FIXED";
    this._accelerometerSensitivity = d?._accelerometerSensitivity ?? 4;
    this._accelerometerDataQualityEnable =
        d?._accelerometerDataQualityEnable ?? true;

    this._gyroscopeEnable = d?._gyroscopeEnable ?? true;
    this._gyroscopeSampleRate = d?._gyroscopeSampleRate ?? 25;
    this._gyroscopeWriteType = d?._gyroscopeWriteType ?? "FIXED";
    this._gyroscopeSensitivity = d?._gyroscopeSensitivity ?? 500;

    this._sequenceNumberMotionEnable = d?._sequenceNumberMotionEnable ?? true;
    this._sequenceNumberMotionSampleRate =
        d?._sequenceNumberMotionSampleRate ?? 25;
    this._sequenceNumberMotionWriteType =
        d?._sequenceNumberMotionWriteType ?? "FIXED";
    this._rawMotionEnable = d?._rawMotionEnable ?? true;
    this._rawMotionSampleRate = d?._rawMotionSampleRate ?? 25;
    this._rawMotionWriteType = d?._rawMotionWriteType ?? "AS_RECEIVED";

    this._batteryEnable = d?._batteryEnable ?? true;
    this._batterySampleRate = d?._batterySampleRate ?? 1;
    this._batteryWriteType = d?._batteryWriteType ?? "AS_RECEIVED";
    this._minConnectionInterval = d?._minConnectionInterval ?? 10;
  }

  void _setToDefaultMotionSenseHRVV2(DeviceSettings d) {
    _setToDefaultMotionSenseV2(d);
    this._ppgEnable = d?._ppgEnable ?? true;
    this._ppgSampleRate = d?._ppgSampleRate ?? 25;
    this._ppgWriteType = d?._ppgWriteType ?? "FIXED";
    this._ppgRed = d?._ppgRed ?? 0x3E;
    this._ppgGreen = d?._ppgGreen ?? 0x68;
    this._ppgInfrared = d?._ppgInfrared ?? 0x14;
    this._ppgFilterEnable = d?._ppgFilterEnable ?? false;
    this._sequenceNumberPpgEnable = d?._sequenceNumberPpgEnable ?? true;
    this._sequenceNumberPpgSampleRate = d?._sequenceNumberPpgSampleRate ?? 25;
    this._sequenceNumberPpgWriteType =
        d?._sequenceNumberPpgWriteType ?? "FIXED";
    this._rawPpgEnable = d?._rawPpgEnable ?? true;
    this._rawPpgSampleRate = d?._ppgSampleRate ?? 25;
    this._rawPpgWriteType = d?._ppgWriteType ?? "AS_RECEIVED";
    this._ppgDataQualityEnable = d?._ppgDataQualityEnable ?? true;
  }

  void _setToDefaultMotionSenseHRVPlusV2(DeviceSettings d) {
    _setToDefaultMotionSenseHRVV2(d);
    this._magnetometerEnable = d?._magnetometerEnable ?? true;
    this._magnetometerSampleRate = d?._magnetometerSampleRate ?? 25;
    this._magnetometerWriteType = d?._magnetometerWriteType ?? "FIXED";
    this._magnetometerSensitivityEnable =
        d?._magnetometerSensitivityEnable ?? true;
    this._magnetometerSensitivitySampleRate =
        d?._magnetometerSensitivitySampleRate ?? 25;
    this._magnetometerSensitivityWriteType =
        d?._magnetometerSensitivityWriteType ?? "FIXED";
    this._sequenceNumberMagnetometerEnable =
        d?._sequenceNumberMagnetometerEnable ?? true;
    this._sequenceNumberMagnetometerSampleRate =
        d?._sequenceNumberMagnetometerSampleRate ?? 25;
    this._sequenceNumberMagnetometerWriteType =
        d?._sequenceNumberMagnetometerWriteType ?? "FIXED";
    this._rawMagnetometerEnable = d?._rawMagnetometerEnable ?? true;
    this._rawMagnetometerSampleRate = d?._rawMagnetometerSampleRate ?? 12.5;
    this._rawMagnetometerWriteType =
        d?._rawMagnetometerWriteType ?? "AS_RECEIVED";
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

  double get accelerometerSampleRate => _accelerometerSampleRate;

  setAccelerometerSampleRate(double sampleRate) {
    _accelerometerSampleRate = sampleRate;
    _gyroscopeSampleRate = sampleRate;
    _sequenceNumberMotionSampleRate = sampleRate;
    _rawMotionSampleRate = sampleRate;
  }

  void setPpgSampleRate(double value) {
    _ppgSampleRate = value;
    _sequenceNumberPpgSampleRate = value;
    _rawPpgSampleRate = value;
  }

  setAccelerometerSensitivity(int value) {
    _accelerometerSensitivity = value;
  }

  setGyroscopeSensitivity(int value) {
    _gyroscopeSensitivity = value;
  }

  double get rawMotionSampleRate => _rawMotionSampleRate;

  double get sequenceNumberMotionSampleRate => _sequenceNumberMotionSampleRate;

  double get gyroscopeSampleRate => _gyroscopeSampleRate;

  int get accelerometerSensitivity => _accelerometerSensitivity;

  int get gyroscopeSensitivity => _gyroscopeSensitivity;

  String get gyroscopeWriteType => _gyroscopeWriteType;

  bool get gyroscopeEnable => _gyroscopeEnable;

  String get accelerometerWriteType => _accelerometerWriteType;

  int get minConnectionInterval => _minConnectionInterval;

  String get rawMagnetometerWriteType => _rawMagnetometerWriteType;

  double get rawMagnetometerSampleRate => _rawMagnetometerSampleRate;

  bool get rawMagnetometerEnable => _rawMagnetometerEnable;

  String get sequenceNumberMagnetometerWriteType =>
      _sequenceNumberMagnetometerWriteType;

  double get sequenceNumberMagnetometerSampleRate =>
      _sequenceNumberMagnetometerSampleRate;

  bool get sequenceNumberMagnetometerEnable =>
      _sequenceNumberMagnetometerEnable;

  String get magnetometerSensitivityWriteType =>
      _magnetometerSensitivityWriteType;

  double get magnetometerSensitivitySampleRate =>
      _magnetometerSensitivitySampleRate;

  String get magnetometerWriteType => _magnetometerWriteType;

  double get magnetometerSampleRate => _magnetometerSampleRate;

  bool get magnetometerEnable => _magnetometerEnable;

  String get sequenceNumberPpgWriteType => _sequenceNumberPpgWriteType;

  double get sequenceNumberPpgSampleRate => _sequenceNumberPpgSampleRate;

  bool get sequenceNumberPpgEnable => _sequenceNumberPpgEnable;

  String get rawPpgWriteType => _rawPpgWriteType;

  double get rawPpgSampleRate => _rawPpgSampleRate;

  bool get rawPpgEnable => _rawPpgEnable;

  bool get ppgFilterEnable => _ppgFilterEnable;

  int get ppgInfrared => _ppgInfrared;

  int get ppgGreen => _ppgGreen;

  int get ppgRed => _ppgRed;

  String get ppgWriteType => _ppgWriteType;

  double get ppgSampleRate => _ppgSampleRate;

  bool get ppgEnable => _ppgEnable;

  String get batteryWriteType => _batteryWriteType;

  double get batterySampleRate => _batterySampleRate;

  bool get batteryEnable => _batteryEnable;

  String get rawMotionWriteType => _rawMotionWriteType;

  bool get rawMotionEnable => _rawMotionEnable;

  String get sequenceNumberMotionWriteType => _sequenceNumberMotionWriteType;

  bool get sequenceNumberMotionEnable => _sequenceNumberMotionEnable;

  String get quaternionWriteType => _quaternionWriteType;

  double get quaternionSampleRate => _quaternionSampleRate;

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
}
