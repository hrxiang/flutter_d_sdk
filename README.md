# flutter_d_sdk

  其他app与d平台钱包通信

## 例：游戏唤起钱包获取token实现登录游戏

### 1, 创建sdk并指定当前操作类型

    var dSdk = FlutterDSdk(action: "login");

    注：根据action区分当前执行的操作类型，如：login（登录），pay（支付）。
       action的值由钱包和游戏共同制定。


### 2, 唤起sdk并获取返回值

    var result = await dSdk.call(uriString: "up://uptest/do");

    注：uriString：当前例子指钱包app需要注册的scheme。
       downloadUrl:当前例子指钱包app的下载地址。
       appKey:加密密钥（传null即可）。
       params：传递的参数。
