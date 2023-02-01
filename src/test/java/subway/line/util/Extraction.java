package subway.line.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class Extraction {

    public static final String LOCATION = "Location";

    private Extraction() {}

    public static Long getLineId(ExtractableResponse<Response> response) {
        String[] locationTokens = response.header(LOCATION).split("/");
        String id = locationTokens[2];
        return Long.valueOf(id);
    }
}
