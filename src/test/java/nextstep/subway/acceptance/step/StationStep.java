package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

public class StationStep {

    private static final String PATH = "/stations";

    public static ExtractableResponse<Response> 역_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(ContentType.JSON)
                .when()
                .post(PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 역_목록_조회_요청() {
        return RestAssured.given().log().all()
                .accept(ContentType.JSON)
                .when()
                .get(PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 역_삭제_요청(String uri) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .delete(uri)
                .then().log().all()
                .extract();
    }
}
