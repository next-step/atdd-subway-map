package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {
    protected static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        LineRequest request = new LineRequest(name, color);

        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    protected static ExtractableResponse<Response> 지하철_노선_수정_요청(String name, String color, Long id) {
        LineRequest request = new LineRequest(name, color);

        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    protected static ExtractableResponse<Response> 지하철_노선_제거_요청(Long id) {

        return RestAssured
                .given().log().all()
                .when()
                .delete("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    protected static Long 지하철_노선_등록되어_있음(String name, String color) {

        return 지하철_노선_생성_요청(name, color).as(LineResponse.class).getId();
    }

    protected static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {

        return RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();

    }

    protected static List<Long> 지하철_노선_목록_포함됨(ExtractableResponse<Response> response) {

        return response.jsonPath().getList(".", LineResponse.class)
                .stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
    }

    protected static ExtractableResponse<Response> 지하철_노선_조회_요청(long id) {

        return RestAssured
                .given().log().all()
                .when()
                .get("/lines/{id}", id)
                .then().log().all()
                .extract();

    }

    protected static LineResponse getLineResponse(ExtractableResponse<Response> response) {

        return response.jsonPath().getObject(".", LineResponse.class);
    }

    protected static void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    protected static void 지하철_노선_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    protected static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        지하철_노선_응답됨(response);
    }

    protected static void 지하철_노선_수정됨(Long id, LineRequest updateRequest) {
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(id);
        LineResponse lineResponse = getLineResponse(response);

        assertThat(lineResponse.getId()).isEqualTo(id);
        assertThat(lineResponse.getName()).isEqualTo(updateRequest.getName());
        assertThat(lineResponse.getColor()).isEqualTo(updateRequest.getColor());
    }

    protected static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
