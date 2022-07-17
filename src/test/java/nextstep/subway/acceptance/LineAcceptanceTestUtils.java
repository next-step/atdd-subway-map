package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import org.springframework.http.MediaType;

public class LineAcceptanceTestUtils {

    void 지하철_노선_삭제(int id) {
        RestAssured.given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all();
    }

    ExtractableResponse<Response> 지하철_노선_수정(int id, LineRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();
    }


    ExtractableResponse<Response> 지하철_노선_조회(int id) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> 지하철_노선_생성(LineRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
