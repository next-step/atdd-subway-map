package subway.helper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SubwayLineHelper {

    public static final String SUBWAY_LINE_API_URL = "/subway-lines";
    public static final Map<String, Object> SUBWAY_LIEN_PARAMETERS_1 = Map.of("name", "신분당선"
            , "color", "bg-red-600"
            , "upStationId", 1, "downStationId", 2
            , "distance", 10);
    public static final Map<String, Object> SUBWAY_LIEN_PARAMETERS_2 = Map.of("name", "분당선"
            , "color", "bg-green-600"
            , "upStationId", 3, "downStationId"
            , 4, "distance", 10);

    public static ExtractableResponse<Response> 지하철_노션_요청(Map<String, Object> parameters) {
        ExtractableResponse<Response> createSubwayLineApiResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(parameters)
                .when().log().all()
                .post(SubwayLineHelper.SUBWAY_LINE_API_URL)
                .then().log().all()
                .extract();

        return createSubwayLineApiResponse;
    }

    public static ExtractableResponse<Response> 지하철_노션에_지하철_역_요청(String stationName) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                    .body(parameter)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post(SubwayStationHelper.STATION_API_URL)
                .then().log().all()
                .extract();

        return response;
    }
}