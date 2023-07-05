package subway.helper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SubwayStationHelper {

    public static final String STATION_API_URL = "/stations";

    public static ExtractableResponse<Response> 지하철_역_생성_요청(String stationName) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                    .body(parameter)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post(STATION_API_URL)
                .then().log().all()
                .extract();

        return response;
    }
}