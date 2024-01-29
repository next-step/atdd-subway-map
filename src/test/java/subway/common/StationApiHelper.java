package subway.common;

import core.RestAssuredHelper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

public class StationApiHelper {
    public static final String STATION_API_PATH = "/stations";

    public static ExtractableResponse<Response> createStation(final String name) {
        return RestAssuredHelper.post(STATION_API_PATH, Map.of("name", name));
    }

    public static ExtractableResponse<Response> fetchStations() {
        return RestAssuredHelper.get(STATION_API_PATH);
    }

    public static ExtractableResponse<Response> removeStation(final Long stationId) {
        return RestAssuredHelper.deleteById(STATION_API_PATH, stationId);
    }

}
