package subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class StationApi {
    public static ExtractableResponse<Response> 지하철역_생성(final StationRequest stationRequest) {
        return RestAssured
                .given()
                    .contentType(APPLICATION_JSON_VALUE)
                    .body(stationRequest)
                .when()
                    .post("/stations")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회() {
        return RestAssured
                .given()
                .when()
                    .get("/stations")
                .then()
                .extract();
    }

    public static List<String> 지하철역_이름_목록_조회() {
        return 지하철역_목록_조회()
                .jsonPath()
                .getList("name", String.class);
    }

    public static ExtractableResponse<Response> 지하철역_삭제(final Long id) {
        return RestAssured
                    .given()
                    .when()
                        .delete("/stations/{id}", id)
                    .then()
                    .extract();
    }


}
