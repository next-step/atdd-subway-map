package subway.internal;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class StationApiResponseExtractor {
    public static class Single {
        public static Long extractId(ExtractableResponse<Response> response) {
            return response.jsonPath().getLong("id");
        }
    }

    public static List<String> extractNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }
}
