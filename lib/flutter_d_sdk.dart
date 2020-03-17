import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:url_launcher/url_launcher.dart';

class FlutterDSdk {
  static const MethodChannel _channel = const MethodChannel('flutter_d_sdk');

  final String action;

  FlutterDSdk({@required this.action}) : assert(action != null);

  /// android 注册api
  Future<String> call({
    @required String uriString,
    Map<String, String> params,
    String downloadUrl = "https://dpl-pre.winplus.top/appdownload/walletDown",
    String appKey,
  }) async {
    if (null == params) params = <String, String>{};
    // 区分当前事件类型
    params.putIfAbsent("action", () => action);
    // 被唤起的应用的scheme
    params.putIfAbsent("uriString", () => uriString);
    // Android sdk数据加密，传null
    params.putIfAbsent("appKey", () => appKey);
    // 启动scheme
    bool installed = await canLaunch(uriString);
    if (!installed) {
      await launch(downloadUrl);
      return null;
    } else {
      return _channel.invokeMethod('call', params);
    }
  }
}
