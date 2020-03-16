package org.dplatform;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import static org.dplatform.Constant.CANCEL_ACTION;
import static org.dplatform.Constant.KEY_RESPONSE;
import static org.dplatform.Constant.KEY_SDK_PACKAGE;
import static org.dplatform.Constant.KEY_SDK_SIGN;
import static org.dplatform.Constant.KEY_SDK_VERSION;
import static org.dplatform.Constant.RESPONSE_ACTION;
import static org.dplatform.Constant.SDK_PACKAGE;
import static org.dplatform.Constant.SDK_SIGN;
import static org.dplatform.Constant.SDK_VERSION;


public class UpExCallbackActivity extends Activity {

    private UpExchangeCallbackBroadcastReceiver mReceiver;
    private boolean isCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // fixing portrait mode problem for SDK 26 if using windowIsTranslucent = true
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        super.onCreate(savedInstanceState);

        if (!isCreatedApi()) finish();

        String packageName = UpExApiFactory.api.getDestPackageName();


        if (!Utils.isInstalled(this, packageName)) {
            if (enableCallback()) {
                UpExApiFactory.api.getRespCallback().onUninstall();
            }
        } else {

            register();

            Uri uri = UpExApiFactory.api.queryUri();
            if (null != uri) {
                try {

                    Utils.call(this, uri, new WithParameter() {
                        @Override
                        public void with(Intent intent) {
                            intent.putExtra(KEY_SDK_PACKAGE, SDK_PACKAGE);
                            intent.putExtra(KEY_SDK_VERSION, SDK_VERSION);
                            intent.putExtra(KEY_SDK_SIGN, SDK_SIGN);
                        }
                    });
                } catch (Exception e) {
                    finish();
                }
                return;
            }

        }

        finish();
    }

    private void register() {
        mReceiver = new UpExchangeCallbackBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getAction(RESPONSE_ACTION));
        intentFilter.addAction(getAction(CANCEL_ACTION));
        registerReceiver(mReceiver, intentFilter);
    }

    private final class UpExchangeCallbackBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            try {
                Utils.moveTaskToFront(context, getPackageName());
                if (null != intent && enableCallback()) {
                    if (getAction(RESPONSE_ACTION).equals(intent.getAction())) {
//                        String key = null == UpExApiFactory.api.getAppKey() ? getPackageName() : UpExApiFactory.api.getAppKey();
                        String result = intent.getStringExtra(KEY_RESPONSE);
                        System.out.println("========result===========" + result);
                        UpExApiFactory.api.getRespCallback().onRespResult(result);
//                        UpExApiFactory.api.getRespCallback().onRespResult(decrypt(key, result));
                    } else if (getAction(CANCEL_ACTION).equals(intent.getAction())) {
                        UpExApiFactory.api.getRespCallback().onCancel();
                    }
                }
            } catch (Exception e) {
                UpExResp<String> exResp = new UpExResp<>();
                exResp.code = -7;
                exResp.msg = e.getMessage();
                UpExApiFactory.api.getRespCallback().onRespResult(exResp.toString());
            } finally {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCancel) {
            if (enableCallback()) {
                UpExApiFactory.api.getRespCallback().onCancel();
            }
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isCancel = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mReceiver)
            unregisterReceiver(mReceiver);
    }

    private boolean enableCallback() {
        return isCreatedApi() && null != UpExApiFactory.api.getRespCallback();
    }

    private boolean isCreatedApi() {
        return null != UpExApiFactory.api;
    }

    private String getAction(String format) {
        return String.format(format, getPackageName());
    }
}
