import 'package:flutter/material.dart';
import 'package:flutter_d_sdk/flutter_d_sdk.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var token = 'Unknown';
  var pay = "Unknown";

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
            child: Column(
          children: <Widget>[
            Text('token: $token'),
            RaisedButton(
              child: Text('去登陆'),
              onPressed: () {
                //
                FlutterDSdk(action: "login")
                    .call(uriString: "up://uptest/do")
                    .then((val) {
                  setState(() {
                    token = val;
                  });
                });
                //
              },
            ),
            Text('充值结果: $pay'),
            RaisedButton(
              child: Text('去充值'),
              onPressed: () {
                //
                FlutterDSdk(action: "pay")
                    .call(uriString: "up://uptest/do")
                    .then((val) {
                  setState(() {
                    pay = val;
                  });
                });
                //
              },
            ),
          ],
        )),
      ),
    );
  }
}
