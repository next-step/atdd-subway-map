package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationAcceptanceRequest {
    private static final String STATION_PATH = "/stations";

    public static Map<String, String> createStationBody(String name) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        return body;
    }

    public static ExtractableResponse<Response> 지하철_역_생성_요청(Map<String, String> stationBody) {
        return RestAssured.given().log().all()
                .body(stationBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(STATION_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_역_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(STATION_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_역_제거_요청(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }
}
