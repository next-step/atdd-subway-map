package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

public class SectionStep {

    private static final String PATH = "/sections";

    public static ExtractableResponse<Response> 구간_생성_요청(String lineUri, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(ContentType.JSON)
                .when()
                .post(lineUri + PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간_삭제_요청(String lineUri, Long stationId) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .delete(lineUri + PATH + "?stationId=" + stationId)
                .then().log().all()
                .extract();
    }
}
