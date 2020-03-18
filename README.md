# flutter_d_sdk

  接入方（APP）向授权方（APP）发起操作请求，如 游戏应用A 向 钱包应用B 发起登录、支付、获取个人信息请求等。
  
  ![flow](https://github.com/hrxiang/flutter_d_sdk/blob/master/flow.png)

### Android接入配置（必要）：

- AndroidManifest添加以下配置，并修改Scheme
```
   <activity
       android:name="org.dplatform.DSdkRouteActivity"
       android:configChanges="keyboardHidden|orientation|screenSize"
       android:exported="true"
       android:theme="@android:style/Theme.Translucent.NoTitleBar" >
       <intent-filter>
           <action android:name="android.intent.action.VIEW"/>
           <category android:name="android.intent.category.DEFAULT"/>
           <category android:name="android.intent.category.BROWSABLE"/>
           <data
              android:scheme="你的scheme"
              android:host="你的host"
               android:path="你的path"/>
           <data/>
       </intent-filter>
   </activity>
```

### iOS接入配置（必要）：

- Info.plist配置你的Scheme
```
<key>CFBundleURLTypes</key>
<array>
    <dict>
    <key>CFBundleTypeRole</key>
    <string>Editor</string>
    <key>CFBundleURLSchemes</key>
        <array>
            <string>你的scheme</string>
        </array>
    </dict>
</array>
```

- Info.plist配置QueriesSchemes
```
<key>LSApplicationQueriesSchemes</key>
<array>
    <string>授权方scheme</string>
</array>
```

- AppDelegate.swift配置SDK回调
```
添加如下代码：
override func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
    if let controller = window.rootViewController as? FlutterViewController {
        FlutterMethodChannel(
            name: "flutter_d_sdk",
            binaryMessenger: controller.binaryMessenger
        ).invokeMethod(
            "resp",
            arguments: url.absoluteString.removingPercentEncoding
        )
    }
    return true
}
```

### 接入方发起授权请求：
#### 1， 创建SDK并指定当前操作类型
```
var dSdk = FlutterDSdk(action: "aciton-name");

注：
aciton-name 事件名称，由接入方和授权方协定。
```


#### 2， 发起授权请求并获取返回值
```
var result = await dSdk.call(
    uriString: "scheme://a-platform/", 
    params: {
      'param1': 'value1',
      'param2': 'value2',
    },
);

注：
uriString[必填]：[授权方scheme]://平台标识，有授权方定义；
params[必填]：键值对参数，由双方协定；
downloadUrl[可选]：授权方下载页面，当授权方APP未安装时，跳转至此链接页面；
appKey[可选]：加密密钥（传null即可）；
result：授权方返回信息，其类型由授权方决定。
```
