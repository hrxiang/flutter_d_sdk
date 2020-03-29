import 'package:flutter/material.dart';
import 'package:flutter_d_sdk/flutter_d_sdk.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var token = 'None';
  var pay = 'None';

  final TextEditingController _controllerToken = TextEditingController();
  final TextEditingController _controllerOrder = TextEditingController();

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
            TextFormField(
              decoration: InputDecoration(
                hintText: '对接方平台Token（仅Lite版登录填写）',
              ),
              controller: _controllerToken,
            ),
            RaisedButton(
              child: Text('去登录 Login'),
              onPressed: () {
                
                var action = 'login';

                var params = {
                  'channelID': '10013057',
                  'scheme': 'xyttylusdt',
                  'appName': '四川麻将',
                };

                if (!(_controllerToken.text?.isEmpty ?? true)) {
                  /// Lite模式，反向授权带入接入方的平台Token.
                  action = 'auth';
                  params['token'] = _controllerToken.text;
                }

                FlutterDSdk(action: action).call(
                  //uriString: 'dplatform://dplatform.org',
                  uriString: 'org.dplatform.lite.resp://dplatform.org',
                  params: params,
                ).then((val) {
                  setState(() {
                    token = val;
                  });
                });
              },
            ),

            SizedBox(height: 50.0),

            Text('支付结果: $pay'),
            TextFormField(
              decoration: InputDecoration(hintText: '请输入订单号'),
              controller: _controllerOrder,
            ),
            RaisedButton(
              child: Text('去充值 Pay'),
              onPressed: () {

                if (_controllerOrder.text?.isEmpty ?? true) {
                  setState(() {
                    pay = '⚠️请输入订单号️⚠️';
                  });
                  return;
                }

                var params = {
                  'orderSn': _controllerOrder.text,
                  'scheme': 'xyttylusdt',
                };

                if (!(_controllerToken.text?.isEmpty ?? true)) {
                  /// Lite模式，反向授权带入接入方的平台Token.
                  params['token'] = _controllerToken.text;
                }

                FlutterDSdk(action: 'pay').call(
                  //uriString: 'dplatform://dplatform.org',
                  uriString: 'org.dplatform.lite.req://dplatform.org',
                  params: params,
                ).then((val) {
                  setState(() {
                    pay = val;
                  });
                });
              },
            ),
          ],
        )),
      ),
    );
  }
}
