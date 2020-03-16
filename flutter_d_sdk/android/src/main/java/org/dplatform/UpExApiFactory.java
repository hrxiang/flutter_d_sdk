package org.dplatform;

import android.content.Context;

import java.lang.ref.WeakReference;


public class UpExApiFactory {
    private UpExApiFactory() {
    }

    static UpExAPI api;

    /**
     * @param context         上下文
     * @param destPackageName 被唤起app的包名
     * @param uriString       scheme
     * @param appKey          密钥
     * @return UpExAPI
     */
    public static synchronized UpExAPI createUpAPI(Context context, String destPackageName, String uriString, String appKey) {
        if (null == context) {
            throw new IllegalStateException("context is null!");
        } else if (null == destPackageName) {
            throw new IllegalStateException("destPackageName is null!");
        } else if (null == uriString) {
            throw new IllegalStateException("uriString is null!");
        }
        return api = new UpExAPI(new WeakReference<>(context), context.getPackageName(), destPackageName, uriString, appKey);
    }
}
