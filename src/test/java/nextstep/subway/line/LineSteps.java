package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        return RestAssured
                .given().log().all()
                .body(new LineRequest(name, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        생성된_지하철_노선_URI_경로_존재_함(response);
    }

    public static void 생성된_지하철_노선_URI_경로_존재_함(ExtractableResponse<Response> response) {
        assertThat(생성된_지하철_노선_URI_경로_확인(response)).isNotBlank();
    }

    public static String 생성된_지하철_노선_URI_경로_확인(ExtractableResponse<Response> response) {
        return response.header("Location");
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_목록_조회_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_목록_조회_결과에_2개_노선_포함_확인(ExtractableResponse<Response> readLinesResponse) {
        List<LineResponse> lines = readLinesResponse.jsonPath().getList(".", LineResponse.class);
        assertThat(lines).hasSize(2);
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(String uri) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_조회_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long id, String name, String color) {
        return RestAssured
                .given().log().all()
                .body(new LineRequest(name, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static Long 수정할_지하철_노선_ID(ExtractableResponse<Response> createResponse) {
        return createResponse.as(LineResponse.class).getId();
    }

    public static void 지하철_노선_수정_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(String uri) {
        return RestAssured
                .given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_제거_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
