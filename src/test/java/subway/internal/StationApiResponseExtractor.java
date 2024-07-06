package subway.internal;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationApiResponseExtractor {
    public static Long extractId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }
}
