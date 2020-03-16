import 'package:flutter/material.dart';
import 'package:flutter_d_sdk/flutter_d_sdk.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var result = 'Unknown';
  var dSdk = FlutterDSdk(action: "login");

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
            Text('result: $result'),
            RaisedButton(
              child: Text('call'),
              onPressed: () {
                //
                dSdk
                    .call(
                        uriString: "up://uptest/do",
                        destPackageName: "com.example.myapplication")
                    .then((val) {
                  setState(() {
                    result = val;
                  });
                });
                //
              },
            )
          ],
        )),
      ),
    );
  }
}
