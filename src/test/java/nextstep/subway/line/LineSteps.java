package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.dto.SectionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.AcceptanceTest.parseIdFromResponseHeader;
import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_노선_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static void 지하철_생성한_노선_목록_조회_성공(Stream<ExtractableResponse<Response>> createResponses, ExtractableResponse<Response> getResponse) {
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedIds = createResponses.map(AcceptanceTest::parseIdFromResponseHeader)
                                                 .collect(Collectors.toList());
        List<Long> resultIds = getResponse.jsonPath().getList(".", LineResponse.class)
                .stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultIds).containsAll(expectedIds);
    }

    public static ExtractableResponse<Response> 노선_ID로_조회(Long id) {
        return RestAssured.given().log().all()
                .when()
                .get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static void 지하철_생성한_노선_조회_성공(ExtractableResponse<Response> createResponse, ExtractableResponse<Response> getResponse) {
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        Long createId = parseIdFromResponseHeader(createResponse);
        Long resultId = (getResponse.jsonPath().getObject(".", LineResponse.class)).getId();
        assertThat(resultId).isEqualTo(createId);

    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Map<String, String> params, Long id) {
        return RestAssured.given().log().all()
                .when()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .put("lines/" + id)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_수정_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(Long id) {
        return RestAssured.given().log().all()
                .when()
                .delete("lines/" + id)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_제거_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static  ExtractableResponse<Response> 지하철_노선에_구간_등록_요청(Long id, SectionRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + id + "/sections")
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선에_구간_등록_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_구간_등록_실패() {}

    public static void 지하철_구간_삭제() {}

    public static void 지하철_구간_삭제_성공() {}

    public static void 지하철_구간_삭제_실패() {}

}
