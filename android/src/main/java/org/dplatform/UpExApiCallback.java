package org.dplatform;

public interface UpExApiCallback {
    /**
     * 授权结果
     */
    void onRespResult(String data);

    /**
     * 用户取消操作
     */
    void onCancel();

    /**
     * 应用未安装
     */
    void onUninstall();
}
