package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class StationRequestCollection {

    public static ExtractableResponse<Response> 지하철역_전체_조회() {
        return RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_생성(String stationName) {
        return RestAssured.given().log().all()
                .body(Map.of("name", stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_삭제(long stationId) {
        return RestAssured
                .given().log().all()
                .when().delete("/stations/{stationId}", stationId)
                .then().log().all()
                .extract();
    }

    public static Long 성수역_생성() {
        return 지하철역_생성("성수역").jsonPath().getLong("id");
    }
}
