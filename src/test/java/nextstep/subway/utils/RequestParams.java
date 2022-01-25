package nextstep.subway.utils;

import java.util.HashMap;
import java.util.Map;

public class RequestParams {

    private Map<String, String> params = new HashMap<>();

    public RequestParams(String key, String value) {
        params.put(key, value);
    }

    public void addParams(String key, String value) {
        params.put(key, value);
    }

    public Map<String, String> getParams() {
        return params;
    }
}
