package org.dplatform.flutter_d_sdk;

import android.content.Context;

import org.dplatform.UpExAPI;
import org.dplatform.UpExApiCallback;
import org.dplatform.UpExApiFactory;
import org.dplatform.UpExResp;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class FlutterDSdkPlugin implements MethodChannel.MethodCallHandler {
    private Context context;

    private FlutterDSdkPlugin(Context context) {
        this.context = context;
    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
        MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_d_sdk");
        channel.setMethodCallHandler(new FlutterDSdkPlugin(registrar.activity()));
    }

    @Override
    public void onMethodCall(MethodCall methodCall, final MethodChannel.Result result) {
        try {
            if ("call".equals(methodCall.method)) {
                if (methodCall.arguments() instanceof Map) {
                    Map<String, String> map = methodCall.arguments();
                    String destPackageName = map.remove("destPackageName");
                    String uriString = map.remove("uriString");
                    String appKey = map.remove("appKey");
                    String action = map.get("action");
                    if (null == destPackageName || null == uriString || null == action) {
                        throw new IllegalArgumentException("destPackageName or uriString or action is null");
                    }
                    System.out.println("===========destPackageName===============" + destPackageName);
                    System.out.println("===========uriString===============" + uriString);
                    System.out.println("===========appKey===============" + appKey);
                    System.out.println("===========action===============" + action);
                    //创建api
                    UpExAPI api = UpExApiFactory.createUpAPI(context, destPackageName, uriString, appKey);
                    for (String key : map.keySet()) {
                        api.appendParameter(key, map.get(key));
                    }
                    //注册回调
                    api.registerRespCallback(new UpExApiCallback() {
                        @Override
                        public void onRespResult(String data) {
                            //响应结果
                            UpExResp resp = new UpExResp();
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
                            UpExResp resp = new UpExResp();
                            resp.code = -1;
                            resp.msg = "用户取消";
                            result.success(resp.toString());
                        }

                        @Override
                        public void onUninstall() {
                            //app未安装
                            UpExResp resp = new UpExResp();
                            resp.code = -2;
                            resp.msg = "app未安装";
                            result.success(resp.toString());
                        }
                    });
                    //发送请求
                    api.sendReq();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
