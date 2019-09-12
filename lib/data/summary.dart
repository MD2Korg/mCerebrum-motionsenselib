class Summary{

  Map<String, dynamic> data;
  Summary(this.data);

  bool isRunning(){
    if(data==null || !data.containsKey("isRunning")) return false;
    return data["isRunning"];
  }
  int getDeviceNo(){
    if(data==null || !data.containsKey("deviceNo")) return 0;
    return data["deviceNo"];

  }
  String getRunningTime(){
    if(data==null || !data.containsKey("runningTime")) return '';
    int runningTime = data["runningTime"];
    runningTime = (runningTime/1000).round();
    int sec = runningTime%60;
    runningTime = (runningTime/60).round();
    int min = runningTime%60;
    int hour = (runningTime/60).round();
    String res = (hour.toString().padLeft(2, '0'))+':'+(min.toString().padLeft(2, '0'))+":"+(sec.toString().padLeft(2, '0'));
    return res;
  }
  List getDevices(){
    if(data==null || !data.containsKey("sensorSummary")) return new List();
    return data["sensorSummary"];
  }
}