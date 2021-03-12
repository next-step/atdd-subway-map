package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class StationSteps {

    public static Map<String, String> createStationInputHelper(final String stationName) {
        return Map.of("name", stationName);
    }

    public static ExtractableResponse< Response > createStationHelper(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteStationHelper(final String uri) {
        return  RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .   extract();
    }

    public static ExtractableResponse<Response> getStationsHelper(){
        return RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }
}
