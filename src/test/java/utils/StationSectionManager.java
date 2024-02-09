package utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.section.StationSectionRequest;

/**
 * 지하철역 구간 관리자 유틸 클래스
 */
public class StationSectionManager {

    public static ExtractableResponse<Response> save(long lineId, StationSectionRequest stationSectionRequest) {
        return RestAssured
                .given()
                .body(stationSectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static void saveFailure(long lineId, StationSectionRequest stationSectionRequest) {
        RestAssured
                .given()
                .body(stationSectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void remove(long lineId, long stationId) {
        RestAssured
                .given()
                .when().delete("lines/{lineId}/sections/{stationId}", lineId, stationId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    public static void removeFailure(long lineId, long stationId) {
        RestAssured
                .given()
                .when().delete("lines/{lineId}/sections/{stationId}", lineId, stationId)
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
