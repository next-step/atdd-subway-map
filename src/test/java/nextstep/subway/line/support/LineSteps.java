package nextstep.subway.line.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.MediaType;

public class LineSteps {

    private static final String LINE_BASE_URL = "/lines";

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String lineName, String color) {
        return RestAssured
                .given().log().all()
                .body(new LineRequest(lineName, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(LINE_BASE_URL)
                .then().log().all()
                .extract();
    }

    public static LineResponse 지하철_노선이_등록됨(String lineName, String color) {
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineName, color);
        return 지하철_노선_요청_응답값(response);
    }

    public static LineResponse 지하철_노선_요청_응답값(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long id, String lineName, String color) {
        return RestAssured
                .given().log().all()
                .body(new LineRequest(lineName, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(LINE_BASE_URL+"/"+id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_특정노선_찾기_요청(Long id) {
        return RestAssured
                .given().log().all()
                .when()
                .get(LINE_BASE_URL+"/"+id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long id) {
       return RestAssured
                .given().log().all()
                .when()
                .delete(LINE_BASE_URL+"/"+id)
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> 지하철_노선_목록_조회요청() {
      return RestAssured
                .given().log().all()
                .when()
                .get(LINE_BASE_URL)
                .then().log().all()
                .extract();
    }
}
