package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import org.springframework.http.MediaType;

public class LineAcceptanceRequest {
    private final static String LINE_PATH = "/lines";

    static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post(LINE_PATH)
                .then().log().all().extract();
    }

    static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(LINE_PATH)
                .then().log().all().extract();
    }

    static ExtractableResponse<Response> 지하철_노선_조회_요청(Long lineId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(LINE_PATH + "/{id}", lineId)
                .then().log().all().extract();
    }

    static ExtractableResponse<Response> 지하철_노선_수정_요청(Long lineId, LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().put(LINE_PATH + "/{id}", lineId)
                .then().log().all().extract();
    }

    static ExtractableResponse<Response> 지하철_노선_제거_요청(Long lineId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(LINE_PATH + "/{id}", lineId)
                .then().log().all().extract();
    }
}
