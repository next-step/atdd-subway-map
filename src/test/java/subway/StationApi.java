package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationApi {
    public static ExtractableResponse<Response> callCreateStationApi(final String stationName) {
        Map<String, String> param = new HashMap<>();
        param.put("name", stationName);
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> callRetrieveStationsApi() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> callDeleteStationByLocation(String createdLocation) {
        return RestAssured.given().log().all()
                .when().delete(createdLocation)
                .then().log().all()
                .extract();
    }
}
