package subway.acceptance.extractableResponse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.station.presentation.request.CreateStationRequest;

public class StationApiExtractableResponse {

    public static ExtractableResponse<Response> createStation(CreateStationRequest createStationRequest) {
        return RestAssured
                .given().log().all()
                .body(createStationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> selectStations() {
        return RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteStation(Long stationId) {
        return RestAssured
                .given().log().all()
                .pathParam("stationId", stationId)
                .when().delete("/stations/{stationId}")
                .then().log().all()
                .extract();
    }

}
