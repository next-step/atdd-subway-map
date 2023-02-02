package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static subway.StationConstant.*;

public class StationStep {
    /*
        지하철역 생성
     */
    static ExtractableResponse<Response> createSubwayStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put(NAME_FILED, stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(STATION_URL)
                .then().log().all()
                .extract();
    }

    /*
        지하철역 목록 조회
     */
    public static ExtractableResponse<Response> getAllStationsResponse() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(STATION_URL)
                .then().log().all()
                .extract();
    }

    /*
        지하철역 삭제
     */
    public static ExtractableResponse<Response> deleteStationResponse(long id) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .pathParam(ID_FILED, id)
                .delete(STATION_ID_URL)
                .then().log().all()
                .extract();
        return response;
    }
}
