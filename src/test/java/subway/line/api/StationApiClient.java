package subway.line.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.dto.request.StationRequest;

public class StationApiClient {

    private static final String ENDPOINT_STATIONS = "/stations";

    public static ExtractableResponse<Response> requestCreateStation(String name) {
        StationRequest req = new StationRequest(name);

        return RestAssured.given().log().all()
                .body(req)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(ENDPOINT_STATIONS)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestShowStations() {
        return RestAssured.given().log().all()
                .when().get(ENDPOINT_STATIONS)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestDeleteStation(Long id) {
        return RestAssured.given().log().all()
                .when().delete(ENDPOINT_STATIONS + "/{stationId}", id.toString())
                .then().log().all()
                .extract();
    }
}
