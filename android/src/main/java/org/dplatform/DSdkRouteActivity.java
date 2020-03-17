package org.dplatform;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class DSdkRouteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // fixing portrait mode problem for SDK 26 if using windowIsTranslucent = true
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        super.onCreate(savedInstanceState);

        if (!isCreatedApi()) finish();

        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (null != uri && enableCallback()) {
            Set<String> keys = uri.getQueryParameterNames();
            Map<String, String> params = new HashMap<>();
            if (null != keys) {
                for (String key : keys) {
                    String value = uri.getQueryParameter(key);
                    if (null != value) {
                        params.put(key, value);
                    }
                }
            }
            DSdkApiFactory.api.getRespCallback().onRespResult(new JSONObject(params).toString());
        }

        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private boolean enableCallback() {
        return isCreatedApi() && null != DSdkApiFactory.api.getRespCallback();
    }

    private boolean isCreatedApi() {
        return null != DSdkApiFactory.api;
    }
}
