package subway.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.station.StationRequest;

public class StationFixture {
    public static ExtractableResponse<Response> createStation(String name) {
        return RestAssured
            .given()
            .body(new StationRequest(name))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then()
            .extract();
    }

    public static ExtractableResponse<Response> getStations() {
        return RestAssured
            .given()
            .when()
            .get("/stations")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }
}
