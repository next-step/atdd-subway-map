package nextstep.subway.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import org.springframework.http.MediaType;

public class LineApiCall {

    // 지하철노선 생성 요청
    public static ExtractableResponse<Response> createLine(LineRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    // 지하철노선 조회 요청
    public static ExtractableResponse<Response> getLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    // 지하철노선 수정 요청
    public static ExtractableResponse<Response> updateLine(Long id, LineUpdateRequest updateDto) {
        return RestAssured.given().log().all()
                .body(updateDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    // 지하철노선 삭제 요청
    public static ExtractableResponse<Response> deleteLine(Long id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .extract();
    }


}
