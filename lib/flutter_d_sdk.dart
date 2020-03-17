import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:url_launcher/url_launcher.dart';
import 'dart:io';
import 'dart:convert' as convert;

class FlutterDSdk {
  static const MethodChannel _channel = const MethodChannel('flutter_d_sdk');

  final String action;

  FlutterDSdk({@required this.action}) : assert(action != null);

  final Completer<String> _iosCallback = Completer<String>();

  /// android 注册api
  Future<String> call({
    @required String uriString,
    Map<String, String> params,
    String downloadUrl = "https://dpl-pre.winplus.top/appdownload/walletDown",
    String appKey,
  }) async {
    if (null == params) params = {};
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
    }

    if (Platform.isIOS) {
      _channel.setMethodCallHandler((call) {
        if ("resp" != call.method) return null;
        _handleIOSCallback(call.arguments);
        return null;
      });
      _channel.invokeMethod('call', params);
      return _iosCallback.future;
    } else {
      return _channel.invokeMethod('call', params);
    }
  }

  _handleIOSCallback(dynamic arguments) {
    Map<String, dynamic> resp = {};
    if (!(arguments is String)) {
      resp['code'] = -3;
      resp['msg'] = 'failed';
      _iosCallback.complete(
          Future<String>.value(convert.jsonEncode(resp))
      );
      return;
    }

    var params = Uri.parse(arguments).queryParameters;
    resp['code'] = 0;
    resp['msg'] = 'success';
    resp['data'] = params;
    _iosCallback.complete(
        Future<String>.value(convert.jsonEncode(resp))
    );
  }
}
