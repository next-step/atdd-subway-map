package utils;

public class JsonBodyParams {
    String key;
    String value;

    public JsonBodyParams(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
