# flutter_d_sdk

  其他app与d平台钱包通信

## 例：游戏唤起钱包获取token实现登录游戏

### 游戏接入步骤：
#### 1， 创建sdk并指定当前操作类型

     var dSdk = FlutterDSdk(action: "login");

     注：根据action区分当前执行的操作类型，如：login（登录），pay（支付）。
         action的值由钱包和游戏共同制定。


#### 2， 唤起sdk并获取返回值

     var result = await dSdk.call(
       uriString: "up://uptest/do", 
       params: {
         'channelID': 'the channel id',
         'scheme': 'your scheme',
       },
     );

     注：uriString：当前例子指钱包app需要注册的scheme。
        downloadUrl:当前例子指钱包app的下载地址。
        appKey:加密密钥（传null即可）。
        params：传递的参数。

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
            <string>your scheme</string>
        </array>
    </dict>
</array>
```

- Info.plist配置QueriesSchemes
```
<key>LSApplicationQueriesSchemes</key>
<array>
    <string>dplatform</string>
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
