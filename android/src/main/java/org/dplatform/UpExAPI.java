package org.dplatform;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.lang.ref.WeakReference;

import static org.dplatform.Constant.RESPONSE_ACTION;

public final class UpExAPI {
    private WeakReference<Context> reference;
    private UpExApiCallback callback;
    private Uri.Builder builder;
    private String appKey;

    UpExAPI(WeakReference<Context> reference, String packageName, String uri, String appKey) {
        this.reference = reference;
        this.appKey = appKey;
        this.builder = Uri.parse(uri).buildUpon()
                .appendQueryParameter("packageName", packageName)
                .appendQueryParameter("callbackUri", RESPONSE_ACTION);
    }


    /**
     * @param key   参数键
     * @param value 参数值
     */
    public void appendParameter(String key, String value) {
        builder.appendQueryParameter(key, value);
    }

    /**
     * 授权结果回调
     *
     * @param callback 回调监听
     */
    public void registerRespCallback(UpExApiCallback callback) {
        this.callback = callback;
    }

    /**
     * 唤起第三方授权界面
     */
    public void sendReq() {
        Context cxt = getContext();
        if (null != cxt) {
            cxt.startActivity(new Intent(cxt, UpExCallbackActivity.class));
        }
    }

    Uri queryUri() {
        return builder.build();
    }

    /**
     * 加密密钥
     **/
    String getAppKey() {
        return appKey;
    }

    UpExApiCallback getRespCallback() {
        return callback;
    }

    private Context getContext() {
        return reference.get();
    }
}
