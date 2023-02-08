package subway.station.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class StationExtraction {

    private StationExtraction() {}

    public static Long getStationId(ExtractableResponse<Response> response) {
        String[] locationTokens = response.header("Location").split("/");
        String id = locationTokens[2];
        return Long.valueOf(id);
    }
}
