# flutter_d_sdk

A new Flutter plugin.

## Getting Started

### 例：游戏唤起钱包获取token实现登录游戏

### 1, 创建sdk工具类

    var dSdk = FlutterDSdk(action: "login");

    注：根据action区分当前执行的操作类型，如：login（登录），pay（支付）。
    action的值由钱包和游戏共同制定。


### 2, 唤起sdk并获取返回值
    var result = await dSdk.call(uriString: "up://uptest/do",destPackageName: "com.example.myapplication");
######## uriString：被唤起的app注册的scheme。
######## destPackageName：被唤起app的包名（Android使用）。
######## appKey:加密密钥（传null即可）。
