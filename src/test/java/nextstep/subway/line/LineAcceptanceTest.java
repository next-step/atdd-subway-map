package nextstep.subway.line;

import static java.util.stream.Collectors.*;
import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static nextstep.subway.station.StationSteps.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 가평역;
    private StationResponse 춘천역;
    private Map<String, String> 신분당선;
    private Map<String, String> 경춘선;

    @BeforeEach
    void before() {
        강남역 = 지하철역_생성_요청(지하철역_생성("강남역")).as(StationResponse.class);
        양재역 = 지하철역_생성_요청(지하철역_생성("양재역")).as(StationResponse.class);
        가평역 = 지하철역_생성_요청(지하철역_생성("가평역")).as(StationResponse.class);
        춘천역 = 지하철역_생성_요청(지하철역_생성("춘천역")).as(StationResponse.class);

        신분당선 = 노선_생성("신분당선", "bg-red-600",
            String.valueOf(강남역.getId()), String.valueOf(양재역.getId()), "5");
        경춘선 = 노선_생성("경춘선", "bg-green-600",
            String.valueOf(가평역.getId()), String.valueOf(춘천역.getId()), "4");
    }

    @DisplayName("지하철 노선을 생성시 종점역이 추가된다.")
    @Test
    void createLine() {
        // when
        final ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_생성됨(response);
        지하철_노선_종점역_추가됨(response, Arrays.asList(강남역, 양재역));
    }

    @DisplayName("기존에 존재하는 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLine_duplicateName() {
        // given
        지하철_노선_생성_요청(신분당선);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        지하철역_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        final ExtractableResponse<Response> 신분당선_응답 = 지하철_노선_생성_요청(신분당선);
        final ExtractableResponse<Response> 경춘선_응답 = 지하철_노선_생성_요청(경춘선);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, 신분당선_응답, 경춘선_응답);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        final ExtractableResponse<Response> 신분당선_응답 = 지하철_노선_생성_요청(신분당선);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선_응답);

        // then
        지하철_노선_응답됨(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 조회한다.")
    @Test
    void getLine_notExist() {
        //given
        final String lineId = "1";

        // when
        final ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineId);

        // then
        지하철_노선_조회_실패됨(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        final ExtractableResponse<Response> 신분당선_응답 = 지하철_노선_생성_요청(신분당선);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_수정_요청(경춘선, 신분당선_응답);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 수정한다.")
    @Test
    void updateLine_notExist() {
        // given
        final String LINE_ID = "1";

        // when
        final ExtractableResponse<Response> response = 지하철_노선_수정_요청(신분당선, LINE_ID);

        // then
        지하철_노선_수정_실패됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        final ExtractableResponse<Response> 신분당선_응답 = 지하철_노선_생성_요청(신분당선);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_제거_요청(신분당선_응답);

        // then
        지하철_노선_삭제됨(response);
    }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> response) {
        final String lineId = response.jsonPath().getString("id");
        return RestAssured
            .given().log().all()
            .pathParam("lineId", lineId)
            .when().delete("/lines/{lineId}")
            .then().log().all()
            .extract();
    }

    private void 지하철_노선_수정_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(Map<String, String> lineInfo, ExtractableResponse<Response> response) {
        return 지하철_노선_수정_요청(lineInfo, response.jsonPath().getString("id"));
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(Map<String, String> lineInfo, String lineId) {
        return RestAssured
            .given().log().all()
            .pathParam("lineId", lineId)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineInfo)
            .when().put("/lines/{lineId}")
            .then().log().all()
            .extract();
    }

    private void 지하철_노선_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private void 지하철_노선_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> response) {
        return 지하철_노선_조회_요청(response.jsonPath().getString("id"));
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(String lineId) {
        return RestAssured
            .given().log().all()
            .pathParam("lineId", lineId)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{lineId}")
            .then().log().all()
            .extract();
    }

    private void 지하철_노선_목록_포함됨(
        ExtractableResponse<Response> response,
        ExtractableResponse<Response> 신분당선_응답,
        ExtractableResponse<Response> 경춘선_응답) {

        final List<ExtractableResponse<Response>> responses = Arrays.asList(신분당선_응답, 경춘선_응답);

        final List<Long> expectedLineIds = responses.stream()
            .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
            .collect(toList());
        final List<LineResponse> results = response.jsonPath().getList(".", LineResponse.class);
        final List<Long> resultLineIds = results.stream()
            .map(LineResponse::getId)
            .collect(toList());

        assertThat(resultLineIds).containsExactlyElementsOf(expectedLineIds);

        final List<LineResponse> lines = responses.stream()
            .map(it -> it.as(LineResponse.class))
            .collect(toList());

        지하철_노선_동등비교(results, lines);
    }

    private void 지하철_노선_동등비교(List<LineResponse> results, List<LineResponse> lines) {
        assertThat(results).usingRecursiveFieldByFieldElementComparator()
            .containsExactlyElementsOf(lines);
    }

    private void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotNull();
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    private void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_노선_종점역_추가됨(ExtractableResponse<Response> response, List<StationResponse> stations) {
        assertThat(response.jsonPath().getList("stations", StationResponse.class))
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyElementsOf(stations);
    }
}
