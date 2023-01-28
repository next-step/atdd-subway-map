package utils;

import java.util.HashMap;
import java.util.Map;

public class JsonBodyParam {
    String key;
    String value;

    public JsonBodyParam(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Map<String, String> toMap() {
        Map<String, String> params = new HashMap<>();
        params.put(key, value);
        return params;
    }
}
