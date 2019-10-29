import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class ThemeLightTeal {
  static ThemeData create(BuildContext context) {
    return new ThemeData(
      fontFamily: 'BeVietnam',
      brightness: Brightness.light,
      highlightColor: Colors.deepPurple[200],
      backgroundColor: Colors.grey[200],
      //primary colors
        primarySwatch: Colors.deepPurple,
      primaryColor: Colors.deepPurple,
      primaryColorBrightness: Brightness.light,
      primaryColorLight: Colors.deepPurple[200],
      primaryColorDark: Colors.deepPurple[700],
      dialogTheme: DialogTheme(titleTextStyle: new TextStyle(color: Colors.black, fontSize: ScreenUtil.getInstance().setSp(40))),

      accentColor: Colors.deepPurpleAccent,
      splashColor: Colors.deepPurpleAccent,
      buttonColor: Colors.deepPurpleAccent,
      appBarTheme: Theme.of(context)
          .appBarTheme
          .copyWith(color: Colors.white, brightness: Brightness.light),
      bottomAppBarTheme: new BottomAppBarTheme(
        color: Colors.black,
        elevation: 10,
      ),
      tabBarTheme: TabBarTheme(
          labelStyle:
              new TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
          labelColor: Colors.deepPurple,
          indicatorSize: TabBarIndicatorSize.label),
      chipTheme: new ChipThemeData(
          backgroundColor: Colors.grey[200],
          disabledColor: Colors.grey[200],
          selectedColor: Colors.deepPurple[200],
          secondarySelectedColor: Colors.deepPurple[200],
          labelPadding: EdgeInsets.fromLTRB(8, 0, 8, 0),
          padding: EdgeInsets.fromLTRB(0, 0, 0, 0),
          shape: StadiumBorder(side: BorderSide(color: Colors.grey[500])),
          labelStyle: TextStyle(color: Colors.black),
          secondaryLabelStyle: TextStyle(color: Colors.black),
          brightness: Brightness.light),
      buttonTheme: new ButtonThemeData(
        highlightColor: Colors.deepPurple,
        buttonColor: Colors.green,
        textTheme: ButtonTextTheme.primary,
        height: 40,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10.0),
          side: BorderSide(color: Colors.deepPurpleAccent, style: BorderStyle.solid, width: 1),
        ),
      ),
      cardTheme: new CardTheme(
        color: Colors.white,
        elevation: 4.0,
        margin: EdgeInsets.fromLTRB(2.0, 2, 2, 2),
      ),

      dividerColor: Colors.grey[200],

      textTheme: new TextTheme(
        title: new TextStyle(
            color: Colors.red,
            fontSize: ScreenUtil.getInstance().setSp(10),
            fontWeight: FontWeight.bold),

        /// For medium emphasis text that's a little smaller than [subhead].
        /// used in listtile's subtitle
        subtitle: new TextStyle(
          color: Colors.grey,
          fontSize: ScreenUtil.getInstance().setSp(34),
        ),
        body1: new TextStyle(color: Colors.black, fontSize: ScreenUtil.getInstance().setSp(40)),
        body2: new TextStyle(
            color: Colors.black,
            fontWeight: FontWeight.bold,
            fontSize: ScreenUtil.getInstance().setSp(44)),
        display1: new TextStyle(color: Colors.black,fontSize: ScreenUtil.getInstance().setSp(14)),
        display2: new TextStyle(color: Colors.black,fontSize: ScreenUtil.getInstance().setSp(14)),
        display3: new TextStyle(color: Colors.black,fontSize: ScreenUtil.getInstance().setSp(14)),
        display4: new TextStyle(color: Colors.black,fontSize: ScreenUtil.getInstance().setSp(14)),
        headline: new TextStyle(
            color: Colors.black, fontSize: ScreenUtil.getInstance().setSp(10)),

        // ListTile.title
        subhead: new TextStyle(
            color: Colors.black, fontSize: ScreenUtil.getInstance().setSp(40)),
        caption: new TextStyle(color: Colors.black, fontSize: ScreenUtil.getInstance().setSp(10)),
        button: new TextStyle(color: Colors.deepPurpleAccent, fontWeight: FontWeight.bold, fontSize: ScreenUtil.getInstance().setSp(44)),
      ),

      iconTheme: IconThemeData(color: Colors.deepPurpleAccent),
      accentIconTheme: IconThemeData(color: Colors.yellow),
    );
  }
}
