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
            Text('response: $token'),
            RaisedButton(
              child: Text('去登陆 Login'),
              onPressed: () {
                //
                FlutterDSdk(action: "login").call(
                  uriString: "dplatform://dplatform.org",
                  params: {
                    'channelID': '10000144',
                    'scheme': 'xyttylusdt',
                    'appName': 'Testapp',
                  },
                ).then((val) {
                  setState(() {
                    token = val;
                  });
                });
                //
              },
            ),
            Text('response: $pay'),
            RaisedButton(
              child: Text('去充值 Pay'),
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
