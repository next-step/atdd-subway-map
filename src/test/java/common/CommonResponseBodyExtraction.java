package common;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;

public class CommonResponseBodyExtraction {

    public static <T> List<T> getList(ExtractableResponse<Response> response, String path) {
        return response.jsonPath().getList(path);
    }

    public static <T> T getObject(ExtractableResponse<Response> response, String path,
        Class<T> objectType) {
        return response.jsonPath().getObject(path, objectType);
    }
}
