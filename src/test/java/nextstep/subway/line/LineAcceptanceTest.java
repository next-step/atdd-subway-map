package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private final String LINE_NAME_1 = "1호선";
    private final String LINE_NAME_2 = "2호선";
    private final String COLOR_1 = "blue";
    private final String COLOR_2 = "green";

    private LineRequest firstRequest;
    private LineRequest secondRequest;

    @BeforeEach
    void before() {
        firstRequest = new LineRequest(LINE_NAME_1, COLOR_1);
        secondRequest = new LineRequest(LINE_NAME_2, COLOR_2);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {

        // when
        ExtractableResponse<Response> response = 노선_생성_요청(firstRequest);

        // then
        // 지하철_노선_생성됨
        노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        노선_등록되어_있음(secondRequest);

        // when
        ExtractableResponse<Response> response = 노선_생성_요청(secondRequest);

        // then
        노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> firstMockLine = 노선_등록되어_있음(firstRequest);
        ExtractableResponse<Response> secondMockLine = 노선_등록되어_있음(secondRequest);

        /// when
        ExtractableResponse<Response> response = 노선_목록_조회_요청();

        // then
        노선_목록_응답됨(response);
        노선_목록_포함됨(response, Arrays.asList(firstMockLine, secondMockLine));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> mockLine = 노선_등록되어_있음(firstRequest);

        // when
        ExtractableResponse<Response> response = 노선_조회_요청(mockLine);

        // then
        노선_응답됨(response, mockLine);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> mockLine = 노선_등록되어_있음(firstRequest);

        // when
        ExtractableResponse<Response> response = 노선_수정_요청(mockLine, secondRequest);

        // then
        노선_수정됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> mockLine = 노선_등록되어_있음(firstRequest);

        // when
        ExtractableResponse<Response> response = 노선_제거_요청(mockLine);

        // then
        노선_삭제됨(response);
    }


    private ExtractableResponse<Response> 노선_생성_요청(LineRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().
                        extract();
    }

    private ExtractableResponse<Response> 노선_목록_조회_요청() {
        return 노선_목록_조회_요청("/lines");
    }

    private ExtractableResponse<Response> 노선_목록_조회_요청(String uri) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 노선_조회_요청(ExtractableResponse<Response> mockLine) {
        String uri = mockLine.header("Location");

        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 노선_수정_요청(ExtractableResponse<Response> response, LineRequest params) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 노선_제거_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 노선_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }


    private void 노선_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private void 노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 노선_응답됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createdResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    private void 노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 노선_등록되어_있음(LineRequest params) {
        return 노선_생성_요청(params);
    }

    private void 노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
