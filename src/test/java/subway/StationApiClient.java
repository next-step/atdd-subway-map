package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class StationApiClient {

    private static final String ENDPOINT_STATIONS = "/stations";

    static ExtractableResponse<Response> requestCreateStation(String name) {
        StationRequest req = new StationRequest(name);

        return RestAssured.given().log().all()
                .body(req)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(ENDPOINT_STATIONS)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> requestShowStations() {
        return RestAssured.given().log().all()
                .when().get(ENDPOINT_STATIONS)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> requestDeleteStation(Long id) {
        final String ENDPOINT = ENDPOINT_STATIONS + "/" + id.toString();

        return RestAssured.given().log().all()
                .when().delete(ENDPOINT)
                .then().log().all()
                .extract();
    }
}
