package subway.internal;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class LineApiResponseExtractor {
    public static List<String> extractNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name");
    }

    public static List<String> extractUpDownStationNames(ExtractableResponse<Response> response, String lineName) {
        String path = "find { it.name == '" + lineName + "' }.stations.name";
        return response.jsonPath().getList(path);
    }
}
