package subway.line.util;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

import static subway.line.LineApi.LINE_NAME_KEY;

public class ExtractionUtils {

    private ExtractionUtils() {}

    public static List<String> getLineNames(ExtractableResponse<Response> response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList(LINE_NAME_KEY, String.class);
    }
}
