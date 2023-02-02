package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.web.request.LineCreateRequest;
import subway.web.request.LineUpdateRequest;
import subway.web.response.LineResponse;
import subway.web.response.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
class LineAcceptanceTest extends BaseAcceptance {

    StationResponse 강남역;
    StationResponse 논현역;

    @BeforeEach
    void setUpStation() {
        강남역 = createStation("강남역").as(StationResponse.class);
        논현역 = createStation("논현역").as(StationResponse.class);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선 생성")
    @Test
    void loadLine() {
        // Given && When
        LineResponse 신분당선 = 지하철_노선_생성("신분당선", 강남역, 논현역);

        // when
        ExtractableResponse<Response> actualLines = 지하철_노선_목록_조회();

        // Then
        지하철_노선_목록을_조회_할_수_있다(신분당선, actualLines);
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회 할 수 있다")
    @Test
    void loadStations() {
        // Given
        LineResponse 신분당선 = 지하철_노선_생성("신분당선", 강남역, 논현역);
        LineResponse 분당선 = 지하철_노선_생성("분당선", 강남역, 논현역);

        // When
        ExtractableResponse<Response> actualLines = 지하철_노선_목록_조회();

        // Then
        지하철_노선_목록을_조회_할_수_있다(신분당선, 분당선, actualLines);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회 할 수 있다")
    @Test
    void loadStation() {
        // Given
        LineResponse 신분당선 = 지하철_노선_생성("신분당선", 강남역, 논현역);

        // When
        ExtractableResponse<Response> actualLine = 지하철_노선_목록_조회(신분당선);

        // Then
        지하철_노선을_조회_할_수_있다(신분당선, actualLine);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선을 수정 할 수 있다")
    @Test
    void updateStation() {
        // Given
        LineResponse 신분당선 = 지하철_노선_생성("신분당선", 강남역, 논현역);

        // When
        지하철_노선_수정_요청(신분당선, "다른분당선", "bg-red-600");

        // Then
        해당_지하철_노선_정보는_수정된다(신분당선, "다른분당선", "bg-red-600");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선을 삭제 할 수 있다")
    @Test
    void deleteStation() {
        // Given
        LineResponse 신분당선 = 지하철_노선_생성("신분당선", 강남역, 논현역);

        // When
        지하철_노선_삭제_요청(신분당선);

        // Then
        해당_지하철_노선은_삭제_된다(신분당선);
    }

    private void 해당_지하철_노선은_삭제_된다(LineResponse line) {
        ExtractableResponse<Response> actualResponse = 지하철_노선_목록_조회(line);
        assertThat(actualResponse.statusCode()).as("노선을 찾지 못할 시 500에러 발생").isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 지하철_노선_삭제_요청(LineResponse line) {
        ExtractableResponse<Response> actualResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("lineId", line.getId())
            .when().delete("/lines/{lineId}")
            .then().log().all()
            .extract();

        assertThat(actualResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    private void 해당_지하철_노선_정보는_수정된다(LineResponse line, String name, String color) {
        ExtractableResponse<Response> actual = 지하철_노선_목록_조회(line);
        assertThat(actual.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual.jsonPath().getString("name")).isEqualTo(name);
        assertThat(actual.jsonPath().getString("color")).isEqualTo(color);
    }

    private void 지하철_노선_수정_요청(LineResponse 신분당선, String name, String color) {
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest(name, color);

        RestAssured.given().spec(REQUEST_SPEC).log().all()
            .body(lineUpdateRequest)
            .pathParam("lineId", 신분당선.getId())
            .when().put("/lines/{lineId}")
            .then().log().all()
            .extract();
    }


    private static void 지하철_노선_목록을_조회_할_수_있다(LineResponse line, LineResponse otherLine, ExtractableResponse<Response> actualLines) {
        assertThat(actualLines.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualLines.jsonPath().getObject("[0]", LineResponse.class)).isEqualTo(line);
        assertThat(actualLines.jsonPath().getObject("[1]", LineResponse.class)).isEqualTo(otherLine);
    }

    private static void 지하철_노선_목록을_조회_할_수_있다(LineResponse line, ExtractableResponse<Response> actualLines) {
        assertThat(actualLines.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualLines.jsonPath().getObject("[0]", LineResponse.class)).isEqualTo(line);
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured.given().spec(REQUEST_SPEC).log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    private void 지하철_노선을_조회_할_수_있다(LineResponse line, ExtractableResponse<Response> actualLine) {
        assertThat(actualLine.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualLine.jsonPath().getObject("", LineResponse.class)).isEqualTo(line);

    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회(LineResponse line) {
        return RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("lineId", line.getId())
            .when().get("/lines/{lineId}")
            .then().log().all()
            .extract();
    }

    private LineResponse 지하철_노선_생성(String lineName, StationResponse 강남역, StationResponse 논현역) {
        LineCreateRequest givenRequest = new LineCreateRequest(lineName, "bg-red-600", 강남역.getId(), 논현역.getId(), 10L);

        ExtractableResponse<Response> createResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .body(givenRequest)
            .when().post("/lines")
            .then().log().all()
            .extract();

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.jsonPath().getString("name")).isEqualTo(givenRequest.getName());
        assertThat(createResponse.jsonPath().getString("color")).isEqualTo(givenRequest.getColor());
        List<StationResponse> stations = createResponse.jsonPath().getList("stations", StationResponse.class);
        assertThat(stations).containsExactlyInAnyOrder(강남역, 논현역);

        return createResponse.as(LineResponse.class);
    }

    private static ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .body(params)
            .when().post("/stations")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

}
