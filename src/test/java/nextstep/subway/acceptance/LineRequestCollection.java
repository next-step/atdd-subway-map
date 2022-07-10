package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;

import static nextstep.subway.acceptance.StationRequestCollection.지하철역_생성;

public class LineRequestCollection {

    public static int 지하철_단일_노선_삭제(long lineId) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract()
                .statusCode();
    }

    public static int 지하철_노선_수정(long lineId, String name, String color) {
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("name", name);
        requestParam.put("color", color);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParam)
                .when().put("/lines/{lineId}", lineId)
                .then().log().all()
                .extract()
                .statusCode();
    }

    public static ExtractableResponse<Response> 지하철_단일_노선_조회(long lineId) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성(String lineName, String color, Long upStationId, Long downStationId, Integer distance) {
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("name", lineName);
        requestParam.put("color", color);
        requestParam.put("upStationId", upStationId.toString());
        requestParam.put("downStationId", downStationId.toString());
        requestParam.put("distance", distance.toString());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParam)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성(String lineName, String color) {
        Long upStationId = 지하철역_생성("강남역").jsonPath().getLong("id");
        Long downStationId = 지하철역_생성("건대입구역").jsonPath().getLong("id");
        return 지하철_노선_생성(lineName, color, upStationId, downStationId, 10);
    }
}
