import 'device_settings.dart';

class MotionSenseSettings{
  bool motionSenseEnable;
  bool motionSenseDebugEnable;
  int motionSenseRequiredDeviceNo;
  List<DeviceSettings> motionSenseDevices;
  List<DeviceSettings> motionSenseDefaultSettings;
  MotionSenseSettings(){
    motionSenseEnable=true;
    motionSenseDebugEnable=false;
    motionSenseRequiredDeviceNo=0;
    motionSenseDevices = new List();
    motionSenseDefaultSettings = new List();
  }
  Map<String, dynamic> toJson() =>
      {
        'motionsense_enable': motionSenseEnable,
        'motionsense_debugEnable': motionSenseDebugEnable,
        'motionsense_requiredDeviceNo':motionSenseRequiredDeviceNo,
        'motionsense_devices': _encodeToJson(motionSenseDevices),
        'motionsense_defaultSettings':_encodeToJson(motionSenseDefaultSettings)
      };
  static List _encodeToJson(List<DeviceSettings>list){
    List jsonList = List();
    list.map((item)=>
        jsonList.add(item.toJson())
    ).toList();
    return jsonList;
  }
  factory MotionSenseSettings.fromJson(Map<String, dynamic> json){
    MotionSenseSettings s = new MotionSenseSettings();

    s.motionSenseEnable = json['motionsense_enable']??true;
    s.motionSenseDebugEnable = json['motionsense_debugEnable']??true;
    if(json['motionsense_requiredDeviceNo']!=null) {
      int x = json['motionsense_requiredDeviceNo'];
      s.motionSenseRequiredDeviceNo = json['motionsense_requiredDeviceNo'];
    }
    List list;
    if(json['motionsense_devices']==null)
      list = new List<Map<String, dynamic>>();
    else list = json['motionsense_devices'] as List;
    if(list==null || list.length==0) s.motionSenseDevices=new List<DeviceSettings>();
    else {
      s.motionSenseDevices=new List<DeviceSettings>();
      for(int i =0;i<list.length;i++){
        s.motionSenseDevices.add(DeviceSettings.fromJson(list[i]));
      }
    }

    List list1;
    if(json['motionsense_defaultSettings']==null)
      list1=new List<Map<String, dynamic>>();
    else
    list1 = json['motionsense_defaultSettings'] as List;
    if(list1==null || list1.length==0) s.motionSenseDefaultSettings=List<DeviceSettings>();
    else {
      s.motionSenseDefaultSettings=new List<DeviceSettings>();
      for(int i =0;i<list1.length;i++){
        s.motionSenseDefaultSettings.add(DeviceSettings.fromJson(list1[i]));
      }
    }

    return s;
  }

}