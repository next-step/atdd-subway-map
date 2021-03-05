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

    public static void 지하철_노선_생성_확인(ExtractableResponse<Response> response, HttpStatus status) {
        지하철_노선_생성_응답코드_확인(response, status);
        지하철_노선_생성_리소스_위치_확인(response);
    }

    public static void 지하철_노선_생성_응답코드_확인(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    public static void 지하철_노선_생성_리소스_위치_확인(ExtractableResponse<Response> createLineResponse) {
        assertThat(지하철_노선_URI_경로_확인(createLineResponse)).isNotBlank();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_목록_포함_확인(ExtractableResponse<Response> readLinesResponse) {
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

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(String uri) {
        return RestAssured
                .given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static String 지하철_노선_URI_경로_확인(ExtractableResponse<Response> createResponse) {
        return createResponse.header("Location");
    }

}
