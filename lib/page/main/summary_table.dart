import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_icons/flutter_icons.dart';

import '../../motionsenselib.dart';
class DataSourceInfo {
  DataSourceInfo(this.platformType, this.platformId, this.sensorType,
      this.sensorTitle, this.dataCount, this.frequency, this.lastSample);

  final String sensorType;
  final String sensorTitle;
  final String platformType;
  final String platformId;
  final int dataCount;
  final double frequency;
  final List<double> lastSample;
}

class DataSourceInfoList extends DataTableSource {
  List<DataSourceInfo> dataSourceInfoList = new List();

  DataSourceInfoList(List devices) {
    dataSourceInfoList.clear();
    for (int i = 0; i < devices.length; i++) {
      String platformId = devices[i]["platformId"];
      String platformType = devices[i]["platformType"];
      String sensorName = devices[i]["sensorName"];
      String sensorType = devices[i]["sensorType"];
      double freq = devices[i]["frequency"];
      int count = devices[i]["count"];
//        List<double> sample = sensors[j]["lastSample"];
      DataSourceInfo a = new DataSourceInfo(platformType, platformId,
          sensorType, sensorName, count, freq, List());
      dataSourceInfoList.add(a);
    }
  }

  @override
  DataRow getRow(int index) {
    assert(index >= 0);
    if (index >= dataSourceInfoList.length) return null;
    final DataSourceInfo sensor = dataSourceInfoList[index];
    return DataRow.byIndex(index: index,
/*
        onSelectChanged: (bool selected){
          Motionsense.plot(sensor.platformType, sensor.platformId, sensor.sensorType);
          print("abc");
        },
*/
        cells: <DataCell>[
          DataCell(Text('${sensor.platformId}')),
          DataCell(Text('${sensor.sensorTitle}')),
          DataCell(Text('${sensor.dataCount}')),
          DataCell(Text('${sensor.frequency.toStringAsFixed(2)}')),
          DataCell(
              sensor.sensorType.startsWith("RAW")
                  ? SizedBox()
                  : Icon(
                MaterialCommunityIcons.getIconData("chart-bell-curve"),
                    ), onTap: () {
            if (sensor.sensorType.startsWith("RAW")) return;
//            Motionsense.plot(sensor.platformType, sensor.platformId, sensor.sensorType);
          }),
        ]);
  }

  @override
  int get rowCount => dataSourceInfoList.length;

  @override
  bool get isRowCountApproximate => false;

  @override
  int get selectedRowCount => 0;
}

class DataSourceTable extends StatelessWidget {
  final DataSourceInfoList dataSourceInfoList;

  DataSourceTable(this.dataSourceInfoList);

  @override
  Widget build(BuildContext context) {
    return dataSourceInfoList == null
        ? SizedBox()
        : SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            child: SingleChildScrollView(
                scrollDirection: Axis.vertical,
                child:  DataTable(
                  dataRowHeight: 30,
                  columnSpacing: 10,
                  horizontalMargin: 10,
                  columns: <DataColumn>[
                    DataColumn(label: Text('Device',style: Theme.of(context).textTheme.subtitle.copyWith(color: Colors.black))),
                    DataColumn(
                      label: Text('Sensor',style: Theme.of(context).textTheme.subtitle.copyWith(color: Colors.black)),
                    ),
                    DataColumn(
                      label: Text('Count',style: Theme.of(context).textTheme.subtitle.copyWith(color: Colors.black)),
                    ),
                    DataColumn(
                      label: Text('Freq',style: Theme.of(context).textTheme.subtitle.copyWith(color: Colors.black)),
                    ),
                    DataColumn(label: Text('Plot',style: Theme.of(context).textTheme.subtitle.copyWith(color: Colors.black))),
                  ],
                  rows: dataSourceInfoList.dataSourceInfoList
                      .map((itemRow) => DataRow(cells: [
                            DataCell(Text(itemRow.platformId, style: Theme.of(context).textTheme.subtitle.copyWith(color: Colors.black))),
                            DataCell(Text(itemRow.sensorTitle, style: Theme.of(context).textTheme.subtitle.copyWith(color: Colors.black))),
                            DataCell(Text(itemRow.dataCount.toString(), style: Theme.of(context).textTheme.subtitle.copyWith(color: Colors.black))),
                            DataCell(
                                Text(itemRow.frequency.toStringAsFixed(1), style: Theme.of(context).textTheme.subtitle.copyWith(color: Colors.black))),
                            DataCell(
                                itemRow.sensorType.startsWith("RAW")
                                    ? SizedBox()
                                    : Icon(
                                  MaterialCommunityIcons.getIconData("chart-timeline-variant"),
//                                        color: Colors.grey[400]
                                        color: Colors.black54,
                                      ), onTap: () async{
//    if (sensor.sensorType.startsWith("RAW")) return;
            await MotionSenseLib.plot(itemRow.platformType, itemRow.platformId, itemRow.sensorType);
                            }),
                           ]))
                      .toList(),
                )
     ));
  }
}
