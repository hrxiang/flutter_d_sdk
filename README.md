# flutter_d_sdk

  其他app与d平台钱包通信

## 例：游戏唤起钱包获取token实现登录游戏

### 游戏接入步骤：
#### 1， 创建sdk并指定当前操作类型

     var dSdk = FlutterDSdk(action: "login");

     注：根据action区分当前执行的操作类型，如：login（登录），pay（支付）。
         action的值由钱包和游戏共同制定。


#### 2， 唤起sdk并获取返回值

     var result = await dSdk.call(uriString: "up://uptest/do");

     注：uriString：当前例子指钱包app需要注册的scheme。
        downloadUrl:当前例子指钱包app的下载地址。
        appKey:加密密钥（传null即可）。
        params：传递的参数。

### 钱包回传数据：

    Android：
              Intent intent = activity.getIntent();
                if (null != intent) {
                     Uri uri = intent.getData();
                     if (null != uri) {
                         String action = uri.getQueryParameter("action");
                         String packageName = uri.getQueryParameter("packageName");
                         String callbackUri = uri.getQueryParameter("callbackUri");
                         String filterAction = String.format(callbackUri, packageName);
                         Intent intent = new Intent();
                         intent.setAction(filterAction);
                          //回传的数据
                         Map<String, String> params = new HashMap<>();
                         if ("login".equals(action)) {
                             params.put("token", "202003171122");
                         } else if ("pay".equals(action)) {
                             params.put("money", "100000000");
                         }
                         //"d_sdk_resp"这个key是固定的不要修改，value是json字符串
                         intent.putExtra("d_sdk_resp", new JSONObject(params).toString());
                         sendBroadcast(intent);
                         //启动钱包的方式不会影响已启动的钱包的操作流程，是两个独立的程序。
                         //关闭当前页面，如果启动了多个页面才拿到数据，则需要关闭所有启动过的所有页面。
                         //因为当前页面与游戏在同一栈中，如果不关闭，则回不到游戏。
                         finish();
                     }
               }

              注：uri的值只能在注册了scheme的activity才能获取。
    iOS: