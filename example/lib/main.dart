import 'package:flutter/material.dart';
import 'package:motionsenselib/settings/settings.dart';

import 'main/main_page.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(home: MainPage());
  }
}
