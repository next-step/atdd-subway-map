package subway.internal;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class LineApiResponseExtractor {
    public static class Single {
        public static Long extractId(ExtractableResponse<Response> response) {
            return response.jsonPath().getLong("id");
        }

        public static String extractName(ExtractableResponse<Response> response) {
            return response.jsonPath().getString("name");
        }

        public static String extractColor(ExtractableResponse<Response> response) {
            return response.jsonPath().getString("color");
        }

        public static List<String> extractUpDownStationNames(ExtractableResponse<Response> response) {
            return response.jsonPath().getList("stations.name");
        }
    }

    public static List<String> extractNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name");
    }

    public static List<String> extractUpDownStationNames(ExtractableResponse<Response> response, String lineName) {
        String path = "find { it.name == '" + lineName + "' }.stations.name";
        return response.jsonPath().getList(path);
    }
}
