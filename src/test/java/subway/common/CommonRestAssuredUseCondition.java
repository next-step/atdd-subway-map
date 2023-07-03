package subway.common;

import java.util.Map;

public class CommonRestAssuredUseCondition {

    private String path;

    private String id;

    private Map<String, String> params;

    public CommonRestAssuredUseCondition(String path) {
        this.path = path;
    }

    public CommonRestAssuredUseCondition(String path, String id) {
        this.path = path;
        this.id = id;
    }

    public CommonRestAssuredUseCondition(String path, Map<String, String> params) {
        this.path = path;
        this.params = params;
    }

    public String getPath() {
        return path;
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getParams() {
        return params;
    }

}
