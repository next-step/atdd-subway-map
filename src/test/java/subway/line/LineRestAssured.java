package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.presentation.LinePatchRequest;
import subway.line.presentation.LineRequest;

public class LineRestAssured {

    private static final String API_PATH = "/lines";

    private static final String APPLICATION_JSON_VALUE = MediaType.APPLICATION_JSON_VALUE;;

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(APPLICATION_JSON_VALUE)
                .when().post(API_PATH)
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(API_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return RestAssured.given().log().all()
                .when().get(API_PATH + "/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(LinePatchRequest linePatchRequest, Long id) {
        return RestAssured.given()
                .body(linePatchRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch(API_PATH + "/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long id) {
        LinePatchRequest linePatchRequest = new LinePatchRequest("강남 3호선", "red");

        return RestAssured.given()
                .body(linePatchRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch(API_PATH + "/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long id) {
        return RestAssured.given().log().all()
                .when().delete(API_PATH + "/" + id)
                .then().log().all()
                .extract();
    }
}
