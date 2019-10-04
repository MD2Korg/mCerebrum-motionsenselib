// Copyright 2016 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:flutter/material.dart';

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
                      Icons.multiline_chart,
                      color: Colors.green,
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
                    DataColumn(label: const Text('Device')),
                    DataColumn(
                      label: const Text('Sensor'),
                    ),
                    DataColumn(
                      label: const Text('Count'),
                    ),
                    DataColumn(
                      label: const Text('Freq'),
                    ),
                    DataColumn(label: const Text('Plot')),
                  ],
                  rows: dataSourceInfoList.dataSourceInfoList
                      .map((itemRow) => DataRow(cells: [
                            DataCell(Text(itemRow.platformId)),
                            DataCell(Text(itemRow.sensorTitle)),
                            DataCell(Text(itemRow.dataCount.toString())),
                            DataCell(
                                Text(itemRow.frequency.toStringAsFixed(1))),
                            DataCell(
                                itemRow.sensorType.startsWith("RAW")
                                    ? SizedBox()
                                    : Icon(
                                        Icons.insert_chart,
                                        color: Colors.blue,
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
