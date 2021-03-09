package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class LineSupport {

    public Long 지하철_노선_등록되어_있음(String name, String color) {
        return 지하철_노선_등록요청(name, color).as(LineResponse.class).getId();
    }

    public ExtractableResponse<Response> 지하철_노선_목록조회요청() {

        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();
    }

    public ExtractableResponse<Response> 지하철_노선_조회요청(Long lineId) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/{lineId}", lineId)
                .then().log().all().extract();
    }

    public ExtractableResponse<Response> 지하철_노선_등록요청(String name, String color) {
        LineRequest request = new LineRequest(name, color);

        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all().extract();
    }

    public ExtractableResponse<Response> 지하철_노선_수정요청(String name, String color, Long lineId) {
        LineRequest request = new LineRequest(name, color);

        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/{lineId}", lineId)
                .then().log().all().extract();
    }

    public ExtractableResponse<Response> 지하철_노선_삭제요청(Long lineId) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all().extract();
    }
}
