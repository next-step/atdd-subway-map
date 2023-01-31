package subway.station.util;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

import static subway.station.StationApi.STATION_NAME_KEY;

public class ExtractionUtils {

    private ExtractionUtils() {}

    public static Long getStationId(ExtractableResponse<Response> response) {
        String[] locationTokens = response.header("Location").split("/");
        String id = locationTokens[2];
        return Long.valueOf(id);
    }

    public static List<String> getStationNames(ExtractableResponse<Response> response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList(STATION_NAME_KEY, String.class);
    }
}
