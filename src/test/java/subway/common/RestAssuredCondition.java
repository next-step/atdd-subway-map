package subway.common;

import java.util.Map;

public class RestAssuredCondition {

    private String path;

    private Map<String, String> params;

    public RestAssuredCondition(String path) {
        this.path = path;
    }

    public RestAssuredCondition(String path, Map<String, String> params) {
        this.path = path;
        this.params = params;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }

}
