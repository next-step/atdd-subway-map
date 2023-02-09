package subway.Acceptance.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.LineSectionRequest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.Acceptance.line.LineAcceptanceFixture.당당선_수정_요청;
import static subway.Acceptance.line.LineAcceptanceFixture.신분당선_생성_요청;

public class LineAcceptanceStep {

    public static ExtractableResponse<Response> 노선_생성_요청(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> 노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_조회_요청(ExtractableResponse<Response> response) {
        return RestAssured.given()
                .pathParam("id", response.jsonPath().getLong("id"))
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_수정_요청(ExtractableResponse<Response> response) {
        return RestAssured.given()
                .pathParam("id", response.jsonPath().getLong("id"))
                .log().all()
                .body(당당선_수정_요청)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_삭제_요청(ExtractableResponse<Response> response) {
        return RestAssured.given()
                .pathParam("id", response.jsonPath().getLong("id"))
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_구간_등록_요청(ExtractableResponse<Response> response, LineSectionRequest lineSectionRequest) {
        return RestAssured.given()
                .pathParam("id", response.jsonPath().getLong("id"))
                .log().all()
                .body(lineSectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }

    public static void 노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 노선_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> it.jsonPath().getLong("id"))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList("id", Long.class);

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static void 노선_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> listResponse = 노선_목록_조회_요청();

        // then
        assertThat(listResponse.jsonPath().getList("name", String.class)).doesNotContain(신분당선_생성_요청.getName());
    }

    public static void 노선_구간_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 노선_구간_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
