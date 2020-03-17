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


public class DSdkCallbackActivity extends Activity {

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

//        if (!Utils.isInstalled(this, packageName)) {
//            if (enableCallback()) {
//                DSdkApiFactory.api.getRespCallback().onUninstall();
//            }
//        } else {
//        }

        register();

        Uri uri = DSdkApiFactory.api.queryUri();
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
                        String result = intent.getStringExtra(KEY_RESPONSE);
                        DSdkApiFactory.api.getRespCallback().onRespResult(result);
                    } else if (getAction(CANCEL_ACTION).equals(intent.getAction())) {
                        DSdkApiFactory.api.getRespCallback().onCancel();
                    }
                }
            } catch (Exception e) {
                DSdkResp<String> exResp = new DSdkResp<>();
                exResp.code = -3;
                exResp.msg = e.getMessage();
                DSdkApiFactory.api.getRespCallback().onRespResult(exResp.toString());
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
                DSdkApiFactory.api.getRespCallback().onCancel();
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
        return isCreatedApi() && null != DSdkApiFactory.api.getRespCallback();
    }

    private boolean isCreatedApi() {
        return null != DSdkApiFactory.api;
    }

    private String getAction(String format) {
        return String.format(format, getPackageName());
    }
}
