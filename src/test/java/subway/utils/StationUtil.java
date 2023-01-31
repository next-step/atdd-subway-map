package subway.utils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static subway.utils.AssertUtil.*;

public class StationUtil {

    public static JsonPath createStationResultResponse(String stationName) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().body(getCreateParam(stationName)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        assertSuccessCreate(response);

        return response.body().jsonPath();
    }

    private static Map<String, String> getCreateParam(String stationName) {
        Map<String, String> param = new HashMap<>();
        param.put("name", stationName);

        return param;
    }

    public static JsonPath showStationsResultResponse() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();

        assertSuccessOk(response);

        return response.body().jsonPath();
    }

    public static void deleteStationResult(Long id) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/{id}", id)
                .then().log().all()
                .extract();

        assertSuccessNoContent(response);
    }
}
