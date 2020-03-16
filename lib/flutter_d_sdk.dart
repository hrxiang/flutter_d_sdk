import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class FlutterDSdk {
  static const MethodChannel _channel = const MethodChannel('flutter_d_sdk');

  final String action;

  FlutterDSdk({@required this.action});

  /// android 注册api
  Future<String> call({
    @required String uriString,
    @required String destPackageName,
    String appKey,
    Map<String, String> params,
  }) async {
    if (null == params) params = <String, String>{};
    params.putIfAbsent("action", () => action);
    //被唤起的应用的包名
    params.putIfAbsent("destPackageName", () => destPackageName);
    //被唤起的应用的scheme
    params.putIfAbsent("uriString", () => uriString);
    //Android sdk数据加密，传null
    params.putIfAbsent("appKey", () => appKey);
    return _channel.invokeMethod('call', params);
  }
}
