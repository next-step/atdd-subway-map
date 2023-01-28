package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonBodyParams {
    private List<JsonBodyParam> bodyParams;

    public  JsonBodyParams(List<JsonBodyParam> params) {
       this.bodyParams = params;
    }

    public Map<String, String> toMap() {
        Map<String, String> params = new HashMap<>();
        for (JsonBodyParam bodyParam : bodyParams) {
            params.put(bodyParam.getKey(), bodyParam.getValue());
        }
        return params;
    }
}
