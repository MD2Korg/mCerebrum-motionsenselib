import 'device_settings.dart';

class Settings{
  bool motionsense_enable;
  bool motionsense_debugEnable;
  int motionsense_requiredDeviceNo;
  List<DeviceSettings> motionsense_devices;
  List<DeviceSettings> motionsense_defaultSettings;
  Settings(){
    motionsense_enable=true;
    motionsense_debugEnable=false;
    motionsense_requiredDeviceNo=0;
    motionsense_devices = new List();
    motionsense_defaultSettings = new List();
  }
  Map<String, dynamic> toJson() =>
      {
        'motionsense_enable': motionsense_enable,
        'motionsense_debugEnable': motionsense_debugEnable,
        'motionsense_requiredDeviceNo':motionsense_requiredDeviceNo,
        'motionsense_devices': _encodeToJson(motionsense_devices),
        'motionsense_defaultSettings':_encodeToJson(motionsense_defaultSettings)
      };
  static List _encodeToJson(List<DeviceSettings>list){
    List jsonList = List();
    list.map((item)=>
        jsonList.add(item.toJson())
    ).toList();
    return jsonList;
  }
  factory Settings.fromJson(Map<String, dynamic> json){
    Settings s = new Settings();
    s.motionsense_enable = json['motionsense_enable']??true;
    s.motionsense_debugEnable = json['motionsense_debugEnable']??true;
    s.motionsense_requiredDeviceNo = json['motionsense_requiredDeviceNo']??0;
    var list = (json['motionsense_devices']==null?List(): json['motionsense_devices'] as List);
    if(list==null || list.length==0) s.motionsense_devices=List<DeviceSettings>();
    else
    s.motionsense_devices = list.map((i) => DeviceSettings.fromJson(i)).toList();
    list = (json['motionsense_defaultSettings']==null?List():json['motionsense_defaultSettings'] as List);
    if(list==null || list.length==0) s.motionsense_defaultSettings=List<DeviceSettings>();
    s.motionsense_defaultSettings = list.map((i) => DeviceSettings.fromJson(i)).toList();
    return s;
  }

}