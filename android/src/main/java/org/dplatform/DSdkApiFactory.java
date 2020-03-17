package org.dplatform;

import android.content.Context;

import java.lang.ref.WeakReference;


public class DSdkApiFactory {
    private DSdkApiFactory() {
    }

    static DSdkAPI api;

    /**
     * @param context   上下文
     * @param uriString scheme
     * @param appKey    密钥
     * @return DSdkAPI
     */
    public static synchronized DSdkAPI createUpAPI(Context context, String uriString, String appKey) {
        if (null == context) {
            throw new IllegalStateException("context is null!");
        } else if (null == uriString) {
            throw new IllegalStateException("uriString is null!");
        }
        return api = new DSdkAPI(new WeakReference<>(context), context.getPackageName(), uriString, appKey);
    }
}
