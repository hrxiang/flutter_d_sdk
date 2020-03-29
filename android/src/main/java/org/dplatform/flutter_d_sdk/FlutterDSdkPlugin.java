package org.dplatform.flutter_d_sdk;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.dplatform.DSdkAPI;
import org.dplatform.DSdkApiCallback;
import org.dplatform.DSdkApiFactory;
import org.dplatform.DSdkResp;
import org.dplatform.Utils;
import org.dplatform.WithParameter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

import static org.dplatform.Constant.KEY_SDK_PACKAGE;
import static org.dplatform.Constant.KEY_SDK_SIGN;
import static org.dplatform.Constant.KEY_SDK_VERSION;
import static org.dplatform.Constant.SDK_PACKAGE;
import static org.dplatform.Constant.SDK_SIGN;
import static org.dplatform.Constant.SDK_VERSION;

public class FlutterDSdkPlugin implements MethodChannel.MethodCallHandler {
    private Activity activity;

    private FlutterDSdkPlugin(Activity activity) {
        this.activity = activity;
    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
        MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_d_sdk");
        channel.setMethodCallHandler(new FlutterDSdkPlugin(registrar.activity()));

        //收到scheme请求
        Uri uri = Utils.getUri(registrar.activity());
        if (null != uri) {
            String channelName = "flutter_d_sdk";
            ActivityInfo info = null;
            try {
                info = registrar.activity().getPackageManager().getActivityInfo(registrar.activity().getComponentName(), PackageManager.GET_META_DATA);
                channelName = info.metaData.getString("flutter_channel");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            MethodChannel callChannel = new MethodChannel(registrar.messenger(), channelName);
            callChannel.invokeMethod("call", uri.toString());
        }
    }

    @Override
    public void onMethodCall(MethodCall methodCall, final MethodChannel.Result result) {
        try {
            if ("call".equals(methodCall.method)) {
                if (methodCall.arguments() instanceof Map) {
                    Map<String, String> map = methodCall.arguments();
                    String uriString = map.remove("uriString");
                    String appKey = map.remove("appKey");
                    String intentType = map.remove("intentType");
                    //创建api
                    DSdkAPI api = DSdkApiFactory.createUpAPI(activity, uriString, appKey);
//                    for (String key : map.keySet()) {
//                        api.appendParameter(key, map.get(key));
//                    }
                    api.appendParameter("param", new JSONObject(map).toString());
                    //注册回调
                    api.registerRespCallback(new DSdkApiCallback() {
                        @Override
                        public void onRespResult(String data) {
                            //响应结果
                            DSdkResp resp = new DSdkResp();
                            resp.code = 0;
                            resp.msg = "success";
                            try {
                                resp.data = new JSONObject(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            result.success(resp.toString());
                        }

                        @Override
                        public void onCancel() {
                            //被用户取消
                            DSdkResp resp = new DSdkResp();
                            resp.code = -1;
                            resp.msg = "canceled";
                            result.success(resp.toString());
                        }

                        @Override
                        public void onUninstall() {
                            //app未安装
                            DSdkResp resp = new DSdkResp();
                            resp.code = -2;
                            resp.msg = "app uninstalled";
                            result.success(resp.toString());
                        }
                    });
                    //发送请求
                    if ("broadcast".equals(intentType)) {
                        //通过方式广播方式传递值
                        api.sendReq();
                    } else {
                        //通过启动activity方式传递值
                        Uri uri = api.queryUri();
                        if (null != uri) {
                            System.out.println("uri:" + uri.toString());
                            try {
                                Utils.call(activity, uri, new WithParameter() {
                                    @Override
                                    public void with(Intent intent) {
                                        intent.setAction(Intent.ACTION_VIEW);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra(KEY_SDK_PACKAGE, SDK_PACKAGE);
                                        intent.putExtra(KEY_SDK_VERSION, SDK_VERSION);
                                        intent.putExtra(KEY_SDK_SIGN, SDK_SIGN);
                                    }
                                });
                            } catch (Exception e) {
                                DSdkResp<String> exResp = new DSdkResp<>();
                                exResp.code = -3;
                                exResp.msg = e.getMessage();
                                result.success(exResp.toString());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
