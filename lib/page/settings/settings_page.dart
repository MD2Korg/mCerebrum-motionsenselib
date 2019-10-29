import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:location_permissions/location_permissions.dart';
import 'package:motionsenselib/settings/device.dart';
import 'package:motionsenselib/settings/device_settings.dart';
import 'package:motionsenselib/settings/motionsense_settings.dart';
import 'package:motionsenselib/page/settings/settings_state.dart';
import 'package:motionsenselib/page/settings_details/settings_details_page.dart';

import '../progress_dialog.dart';
import 'settings_bloc.dart';
import 'settings_event.dart';

class SettingsPage extends StatefulWidget {
  final MotionSenseSettings settings;

  SettingsPage(this.settings);

  @override
  _SettingsPageState createState() => _SettingsPageState();
}

class _SettingsPageState extends State<SettingsPage> {
  SettingsBloc _settingsBloc;
  ProgressDialog _progressDialog;
  bool isEdit = false;

  void onDone() {
  }

  Widget _showStatus(SettingsState state) {
    return StreamBuilder(
        stream: _settingsBloc.streamStatus,
        builder: (context, snapshot) {
          return new Column(children: <Widget>[
            snapshot.data == 1 || snapshot.data == 3
                ? new Container(
                    margin: EdgeInsets.only(bottom: 1),
                    color: Colors.redAccent,
                    child: new ListTile(
                      title: new Text(
                        'Please enable bluetooth',
                        style: Theme.of(context).primaryTextTheme.subhead,
                      ),
                      trailing: Platform.isIOS
                          ? Container()
                          : FlatButton(
                              textColor: Colors.white,
//                              borderSide: BorderSide(color: Colors.white),
                              child: Text("Enable"),
                              onPressed: () {
                                _settingsBloc.dispatch(EnableBluetoothEvent());
                              },
                            ),
                    ))
                : Container(),
            snapshot.data == 2 || snapshot.data == 3
                ? new Container(
                    margin: EdgeInsets.only(bottom: 1),
                    color: Colors.redAccent,
                    child: new ListTile(
                      title: new Text(
                        'Please enable gps',
                        style: Theme.of(context).primaryTextTheme.subhead,
                      ),
                      trailing: Platform.isIOS
                          ? Container()
                          : FlatButton(
                              textColor: Colors.white,
                              child: Text(
                                "Enable",
                                textAlign: TextAlign.right,
                              ),
                              onPressed: () {
                                _settingsBloc.dispatch(EnableGPSEvent());
                              },
                            ),
                    ))
                : Container(),
          ]);
        });
  }
  BuildContext myContext;
  @override
  void initState() {
    super.initState();
    _settingsBloc = new SettingsBloc(widget.settings);
    _settingsBloc.dispatch(InitEvent());
    _progressDialog = new ProgressDialog(context,
        type: ProgressDialogType.Normal, isDismissible: true);
    myContext = context;
  }

  @override
  void dispose() {
    super.dispose();
    _settingsBloc.dispose();
  }

  List<Widget> listAvailableDevices(
      BuildContext context, List<Device> devices) {
    List<Widget> list = new List();
    for (int i = 0; i < devices.length; i++) {
      List<String> ll = new List();
      if(devices[i].bluetoothDevice.name=="Autosense"){
        ll.add("CHEST");
      }else {
        ll.add("LEFT_WRIST");
        ll.add("RIGHT_WRIST");
      }
      ll.add("OTHER");
      Widget l = PopupMenuButton<String>(
        elevation: 3.2,
        offset: Offset(MediaQuery.of(context).size.width/10,0),
        onSelected: (String value) async {
          if (value == "OTHER") {
            devices[i].platformId = await _asyncInputDialog();
            if(devices[i].platformId=='') return;
          }
          else devices[i].platformId = value;
          if (_settingsBloc.isConfigured(devices[i])) {
            final snackBar =
            SnackBar(content: Text("Error: Duplicate sensor placement"));
            Scaffold.of(context).showSnackBar(snackBar);
          } else {
            isEdit = true;
            _settingsBloc.dispatch(AddDeviceEvent(devices[i]));
          }
        },
        child: new ListTile(
            title: new Text(
              devices[i].bluetoothDevice.name,
            ),
            subtitle: new Text(
              devices[i].bluetoothDevice.id.toString(), style: Theme.of(context).textTheme.subtitle,
            ),
          trailing: Icon(Icons.add, color: Theme.of(context).iconTheme.color,),
        ),
        /*new Text("Add",
              style:
              TextStyle(color: Colors.green, fontWeight: FontWeight.bold)),*/
        itemBuilder: (BuildContext context) {
          return ll.map((String choice) {
            return PopupMenuItem<String>(
              value: choice,
              child: Text(choice,),
            );
          }).toList();
        },
      );
      list.add(l);
    }
    return list;
  }

  List<Widget> listConfiguredDevices(
      BuildContext context, List<DeviceSettings> deviceSettings) {
    List<String> ll = new List();
    ll.add("Sensor Details");
    ll.add("Remove");
    List<Widget> list = new List();
    for (int i = 0; i < deviceSettings.length; i++) {
      ListTile l = new ListTile(
        title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: <Widget>[
            new Text(
              deviceSettings[i].getName(),
            ),
            new Text(
              deviceSettings[i].platformId,
              style: TextStyle(color: Theme.of(context).accentColor),
            )
          ],
        ),
        subtitle: new Text(
          deviceSettings[i].deviceId, style: Theme.of(context).textTheme.subtitle
        ),
        trailing: PopupMenuButton<String>(
          elevation: 3.2,
          onSelected: (String value) async {
            if (value == "Remove") {
              isEdit = true;
              _settingsBloc
                  .dispatch(DeleteDeviceEvent(deviceSettings[i].deviceId));
            } else {
              var res = await Navigator.push(
                  context,
                  new MaterialPageRoute(
                      builder: (_context) =>
                          SettingsDetailsPage(deviceSettings[i])));
              if (res["edit"] == true) {
                isEdit = true;
                deviceSettings[i] = res["deviceSettings"];
              }
            }
          },
          child: Icon(Icons.more_vert, color: Colors.grey),
          itemBuilder: (BuildContext context) {
            return ll.map((String choice) {
              return PopupMenuItem<String>(
                value: choice,
                child: Text(choice),
              );
            }).toList();
          },
        ),
      );
      list.add(l);
    }
    return list;
  }

  @override
  Widget build(BuildContext context) {

    return WillPopScope(
      onWillPop: () async {
        MotionSenseSettings s = _settingsBloc.settings();
        print(s.motionSenseDevices.length.toString());
        Navigator.pop(context, {"settings":s, "edit":isEdit});
        return Future.value(false);
      },
      child: Scaffold(
        body: BlocListener(
          bloc: _settingsBloc,
          listener: (BuildContext context, SettingsState state) async {
            if (state is PermissionRequiredState) {
              await new LocationPermissions().requestPermissions();
              _settingsBloc.dispatch(InitEvent());
            } else if (state is LoadingState) {
              _progressDialog.show();
            } else {
              _progressDialog.hide();
            }
          },
          child: BlocBuilder(
            bloc: _settingsBloc,
            builder: (BuildContext context, SettingsState state) {
              return new Scaffold(
                  appBar: AppBar(
                      elevation: 4.0,
                      title: Text(
                        "MotionSense Settings",
                      )),
                  body: new Column(children: <Widget>[
                    _showStatus(state),
                    new Image.asset('assets/motionsense.jpg',
                        package: 'motionsenselib',
                        width: double.infinity,
                        height: 150.0,
                        fit: BoxFit.fill),
                    Container(
                      color: Theme.of(context).backgroundColor,
                      child: Padding(
                        padding: const EdgeInsets.all(10.0),
                        child: Center(
                          child: Text("Configured Devices", style: Theme.of(context).textTheme.body2,),
                        ),
                      ),
                    ),
                    StreamBuilder(
                        stream: _settingsBloc.streamConfigured,
                        builder: (context, snapshot) {
                          return snapshot.data == null ||
                                  snapshot.data.length == 0
                              ? Container(
                                  padding: EdgeInsets.all(16),
                                  child: Center(
                                    child: Text(
                                      "Not configured yet",
                                      style: TextStyle(
                                          fontStyle: FontStyle.italic,
                                          color: Colors.grey,),
                                    ),
                                  ))
                              : new Column(
                                  children: listConfiguredDevices(
                                      context, snapshot.data),
                                );
                        }),
                    Container(
                      padding: EdgeInsets.all(10),
                      color: Theme.of(context).backgroundColor,
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: <Widget>[
//                          Expanded(child: Container()),
                          Expanded(
                            child: Center(
                              child: Text("Available Devices",style: Theme.of(context).textTheme.body2),
                            ),
                          ),
                          _settingsBloc.isScanning()
                              ?
                                  GestureDetector(
                                  child: Container(
                                      alignment: Alignment.centerRight,
                                      child: SizedBox(
                                          height: 16,
                                          width: 16,
                                          child: CircularProgressIndicator(
                                            backgroundColor: Colors.white,
                                            strokeWidth: 2,
                                          ))),
                                  onTap: () {
                                    _settingsBloc.dispatch(StopScanEvent());
                                  },
                                )
                              :  GestureDetector(
                                    child: Text(
                                      "Scan",
                                      style: Theme.of(context).textTheme.body2.copyWith(color: Theme.of(context).primaryColorLight),
                                      textAlign: TextAlign.right,
                                    ),
                                    onTap: () {
                                      _settingsBloc.dispatch(StartScanEvent());
                                    },
                                )
                        ],
                      ),
                    ),
                    StreamBuilder(
                        stream: _settingsBloc.streamScan,
                        builder: (context, snapshot) {
                          return snapshot.data == null
                              ? Container()
                              : Expanded(
                                  child: new ListView(
                                    children: listAvailableDevices(
                                        context, snapshot.data),
                                  ),
                                );
                        }),
                  ]));
            },
          ),
        ),
      ),
    );
  }

  Future<String> _asyncInputDialog() async {
    return showDialog<String>(
      context: myContext,
      barrierDismissible: false,
      // dialog is dismissible with a tap on the barrier
      builder: (BuildContext context) {
        String selected = '';
        return AlertDialog(
          title: Text('Enter Sensor Placement',),
          content: new Row(
            children: <Widget>[
              new Expanded(
                  child: new TextField(
                    autofocus: true,
                    decoration: new InputDecoration(labelText: 'Sensor Placement'),
                    onChanged: (value) {
                      selected = value;
                    },
                  ))
            ],
          ),
          actions: <Widget>[
            FlatButton(
              child: Text('Cancel'),
              onPressed: () {
                Navigator.of(myContext).pop('');
              },
            ),
            FlatButton(
              child: Text('Ok'),
              onPressed: () {
                Navigator.of(myContext).pop(selected);
              },
            ),
          ],
        );
      },
    );
  }

}

