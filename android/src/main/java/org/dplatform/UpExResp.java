package org.dplatform;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UpExResp<T> implements Serializable {
    public int code;
    public String msg;
    public T data;


    public boolean isSuccess() {
        return 0 == code;
    }

    @NotNull
    public String toString() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", data);
        return new JSONObject(map).toString();
    }
}
