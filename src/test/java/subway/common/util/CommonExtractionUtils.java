package subway.common.util;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class CommonExtractionUtils {

    private CommonExtractionUtils() {}

    /**
     * JSON에 대해 key를 통해 추출한다
     *
     * @param response
     * @return target value
     */
    public static String getValueOfKey(ExtractableResponse<Response> response, String key) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.get(key);
    }

    /**
     * List에 대해 key를 통해 추출한다
     *
     * @param response
     * @return target values
     */
    public static List<String> getValuesOfKey(ExtractableResponse<Response> response, String key) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList(key, String.class);
    }
}
