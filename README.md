# flutter_d_sdk

A new Flutter plugin.

## Getting Started

### 1, 创建sdk工具类
    var dSdk = FlutterDSdk(action: "login");
    根据action区分当前执行的操作类型，如：login（登录），pay（支付）。action的值由钱包和游戏共同制定
