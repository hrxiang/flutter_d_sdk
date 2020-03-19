import 'package:flutter/material.dart';
import 'package:flutter_d_sdk/flutter_d_sdk.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var token = 'None';
  var pay = "None";


  final TextEditingController _controller = TextEditingController();

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
            Text('登录结果: $token'),
            RaisedButton(
              child: Text('去登录 Login'),
              onPressed: () {
                //
                FlutterDSdk(action: "login").call(
                  uriString: "dplatform://dplatform.org",
                  params: {
                    'channelID': '10000144',
                    'scheme': 'xyttylusdt',
                    'appName': '四川麻将',
                  },
                ).then((val) {
                  setState(() {
                    token = val;
                  });
                });
                //
              },
            ),

            SizedBox(height: 50.0),

            Text('支付结果: $pay'),
            TextFormField(
              decoration: InputDecoration(hintText: '请输入订单号'),
              controller: _controller,
            ),
            RaisedButton(
              child: Text('去充值 Pay'),
              onPressed: () {

                if (_controller.text?.isEmpty ?? true) {
                  setState(() {
                    pay = '⚠️请输入订单号️⚠️';
                  });
                  return;
                }

                //
                FlutterDSdk(action: "pay").call(
                  uriString: "dplatform://dplatform.org",
                  params: {
                    'orderSn': _controller.text,
                  }
                ).then((val) {
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
