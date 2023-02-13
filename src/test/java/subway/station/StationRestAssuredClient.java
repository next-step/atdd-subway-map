package subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.RestAssuredClient;

import java.util.Map;

public class StationRestAssuredClient extends RestAssuredClient {
    public static ExtractableResponse<Response> createStation(Map<String, Object> requestParam) {
        return post("/stations", requestParam);
    }

    public static void deleteStation(Long id) {
        delete(String.format("/stations/%d", id));
    }

    public static ExtractableResponse<Response> listStation() {
        return get("/stations");
    }
}
