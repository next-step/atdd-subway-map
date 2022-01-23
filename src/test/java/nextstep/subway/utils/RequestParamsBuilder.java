package nextstep.subway.utils;

import java.util.HashMap;
import java.util.Map;

public class RequestParamsBuilder<V> {
    private Map<String, V> params;

    private RequestParamsBuilder(Map<String, V> params) {
        this.params = params;
    }

    public static <V> RequestParamsBuilder<V> builder() {
        return new RequestParamsBuilder<>(new HashMap<>());
    }

    public RequestParamsBuilder<V> addParam(String key, V value) {
        params.put(key, value);
        return this;
    }

    public Map<String, V> build() {
        return params;
    }
}
