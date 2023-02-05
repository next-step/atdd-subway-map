package subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.utils.RestAssuredClient;

import java.util.Map;

public class StationRestAssuredClient {
    public static ExtractableResponse<Response> createStation(Map<String, Object> requestParam) {
        return RestAssuredClient.post("/stations", requestParam);
    }

    public static void deleteStation(Long id) {
        RestAssuredClient.delete(String.format("/stations/%d", id));
    }

    public static ExtractableResponse<Response> listStation() {
        return RestAssuredClient.get("/stations");
    }
}
