package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.applicaion.dto.StationRequest;
import org.springframework.http.MediaType;

public class SubwayApiCaller {

    public static ExtractableResponse<Response> 지하철역_노선_등록(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        //when
        StationLineRequest request = new StationLineRequest(name,color,upStationId,downStationId,distance);
        ExtractableResponse<Response> response = RestAssured
                .given()
                    .log().all()
                    .body(request)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then()
                    .statusCode(201)
                    .log().all()
                    .extract();

        return response;
    }

    public static List<StationLineResponse> 지하철역노선_목록_조회() {
        return RestAssured
                .given()
                    .log().all()
                .when()
                    .get("/lines")
                .then()
                    .statusCode(200)
                    .log().all()
                    .extract()
                    .jsonPath().getList("$", StationLineResponse.class);
    }

    public static StationLineResponse 지하철노선_조회(String url) {
        return RestAssured
                .given()
                    .log().all()
                .when()
                    .get(url)
                .then()
                    .statusCode(200)
                    .log().all()
                    .extract()
                    .jsonPath().getObject("$", StationLineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철노선_수정(String url, String name, String color) {
        StationLineRequest request = new StationLineRequest(name,color);

        return RestAssured
                .given()
                    .log().all()
                    .body(request)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .put(url)
                .then()
                    .log().all()
                    .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_삭제(String url) {
        return RestAssured
                .given()
                    .log().all()
                .when()
                    .delete(url)
                .then()
                    .log().all()
                    .extract();
    }

    public static ExtractableResponse<Response> 지하철역_등록(String stationName) {
        StationRequest station = new StationRequest(stationName);

        return RestAssured
                .given()
                    .log().all()
                    .body(station)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then()
                    .log().all()
                    .statusCode(201)
                    .extract();
    }

    public static List<String> 지하철역_목록_조회() {
        return RestAssured
                .given()
                    .log().all()
                .when()
                    .get("/stations")
                .then()
                    .statusCode(200)
                    .log().all()
                    .extract()
                    .jsonPath().getList("name", String.class);
    }

    public static ExtractableResponse<Response> 지하철역_삭제(String url) {
        return RestAssured
                .given()
                    .log().all()
                .when()
                    .delete(url)
                .then()
                    .log().all()
                    .statusCode(204)
                    .extract();
    }
}