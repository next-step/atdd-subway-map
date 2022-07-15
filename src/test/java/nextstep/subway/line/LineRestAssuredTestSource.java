package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class LineRestAssuredTestSource {

    public static ExtractableResponse<Response> 노선생성(final String lineName, final Long 상행역, final Long 하행역) {
        return RestAssured
                .given().log().all()
                .body(createLineParams(lineName, 상행역, 하행역))
                .contentType(ContentType.JSON)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static Map<String, Object> createLineParams(
            final String lineName,
            final Long upStationId,
            final Long downStationId) {

        final Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", "bg-red-600");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 10);
        return params;
    }

    public static ExtractableResponse<Response> 노선조회(final Long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/{id}", id)
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> 노선목록조회() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선삭제(final Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선수정(final Long id, final String lineName, final Long 상행역, final Long 하행역) {
        return RestAssured
                .given().log().all().body(createLineParams(lineName, 상행역, 하행역)).contentType(ContentType.JSON)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

}
