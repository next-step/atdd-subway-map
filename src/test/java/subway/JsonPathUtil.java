package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.presentation.line.dto.response.LineResponse;

import java.util.List;

public class JsonPathUtil {

    public static LineResponse getLineResponse(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("$", LineResponse.class);
    }

    public static List<LineResponse> getLineResponses(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("$", LineResponse.class);
    }

    public static Long getId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }
}
