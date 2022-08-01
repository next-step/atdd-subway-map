package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        final ExtractableResponse<Response> creationResponse = 지하철노선_생성(LINE_NAME1);

        지하철노선이_생성됨(creationResponse);
        지하철노선명_목록조회(LINE_NAME1);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        지하철노선_생성(LINE_NAME1);
        지하철노선_생성(LINE_NAME2);

        final ExtractableResponse<Response> linesResponse = 지하철노선_목록조회();

        지하철노선명_목록이_조회됨(linesResponse);
        지하철노선명_목록조회(LINE_NAME1, LINE_NAME2);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        final ExtractableResponse<Response> creationResponse = 지하철노선_생성(LINE_NAME1);

        final ExtractableResponse<Response> lineResponse = 지하철노선_조회(creationResponse);

        지하철노선이_조회됨(lineResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        final ExtractableResponse<Response> creationResponse = 지하철노선_생성(LINE_NAME1);

        final Long id = 응답아이디_조회(creationResponse);
        final ExtractableResponse<Response> updateResponse = 지하철노선_수정(id, LINE_NAME2, LINE_COLOR2);

        지하철노선이_수정됨(updateResponse);
        지하철노선명_목록조회(LINE_NAME2);
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        final ExtractableResponse<Response> creationResponse = 지하철노선_생성(LINE_NAME1);

        final Long id = 응답아이디_조회(creationResponse);
        final ExtractableResponse<Response> deletionResponse = 지하철노선_삭제(id);

        지하철노선이_삭제됨(deletionResponse);
        지하철노선명_목록조회();
    }

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void addSection() {
        final ExtractableResponse<Response> sectionCreationResponse = 지하철구간_등록();

        지하철구간이_등록됨(sectionCreationResponse);
    }

    @DisplayName("지하철 노선에 구간을 삭제한다.")
    @Test
    void deleteSection() {
        final ExtractableResponse<Response> sectionCreationResponse = 지하철구간_등록();

        final ExtractableResponse<Response> deletionResponse = 지하철구간_삭제(sectionCreationResponse);

        지하철구간이_삭제됨(deletionResponse);
    }

    @DisplayName("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우 역을 삭제할 수 없다.")
    @Test
    void validateSectionSize() {
        final ExtractableResponse<Response> sectionCreationResponse = 지하철노선_생성(LINE_NAME1);

        final ExtractableResponse<Response> deletionResponse = 지하철구간_삭제(sectionCreationResponse);

        지하철구간_삭제_실패함(deletionResponse);
    }

    @DisplayName("지하철 노선에 등록된 하행 종점역만 제거할 수 있다.")
    @Test
    void validateDeleteableStation() {
        final ExtractableResponse<Response> sectionCreationResponse = 지하철구간_등록();

        final ExtractableResponse<Response> deletionResponse = 지하철_상행역_삭제(sectionCreationResponse);

        지하철구간_삭제_실패함(deletionResponse);
    }

    private ExtractableResponse<Response> 지하철노선_생성(final String lineName) {
        final ExtractableResponse<Response> gangNamStationCreationResponse = 지하철역_생성(GANGNAM_STATION);
        final ExtractableResponse<Response> sindorimStationCreationResponse = 지하철역_생성(SINDORIM_STATION);
        final Long gangNameStationId = 응답아이디_조회(gangNamStationCreationResponse);
        final Long sindorimStationId = 응답아이디_조회(sindorimStationCreationResponse);
        return 지하철노선_생성(lineName, LINE_COLOR1, gangNameStationId, sindorimStationId, LINE_DISTANCE1);
    }

    private ExtractableResponse<Response> 지하철노선_생성(
            final String name,
            final String color,
            final Long upStationId,
            final Long downStationId,
            final Long distance
    ) {
        final Map<String, String> params = new HashMap<>();
        params.put(KEY_NAME, name);
        params.put(KEY_COLOR, color);
        params.put(KEY_UP_STATION_ID, String.valueOf(upStationId));
        params.put(KEY_DOWN_STATION_ID, String.valueOf(downStationId));
        params.put(KEY_DISTANCE, String.valueOf(distance));

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private void 지하철노선이_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 지하철노선_목록조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private List<String> 지하철노선명_목록조회(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList(KEY_NAME, String.class);
    }

    private void 지하철노선명_목록조회(final String... line) {
        final List<String> lines = List.of(line);
        assertThat(lines).isEqualTo(지하철노선명_목록조회());
    }

    private List<String> 지하철노선명_목록조회() {
        final ExtractableResponse<Response> lineResponse = 지하철노선_목록조회();
        return 지하철노선명_목록조회(lineResponse);
    }

    private ExtractableResponse<Response> 지하철노선_조회(final Long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선_조회(final ExtractableResponse<Response> response) {
        final Long id = 응답아이디_조회(response);
        return 지하철노선_조회(id);
    }

    private void 지하철노선이_조회됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철노선명_목록이_조회됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 지하철노선_수정(final Long id, final String name, final String color) {
        final Map<String, String> params = new HashMap<>();
        params.put(KEY_NAME, name);
        params.put(KEY_COLOR, color);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    private void 지하철노선이_수정됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 지하철노선_삭제(final Long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    private void 지하철노선이_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Long 응답아이디_조회(final ExtractableResponse<Response> response) {
        return response.jsonPath().getObject(KEY_ID, Long.class);
    }

    private ExtractableResponse<Response> 지하철구간_등록() {
        final ExtractableResponse<Response> lineCreationResponse = 지하철노선_생성(LINE_NAME1);

        final Long lineId = 응답아이디_조회(lineCreationResponse);

        final List<StationResponse> stationResponses = 응답역_조회(lineCreationResponse);
        final StationResponse lastStationResponse = stationResponses.get(stationResponses.size() - 1);

        final ExtractableResponse<Response> pangyoStationCreationResponse = 지하철역_생성(PANGYO_STATION);
        final Long pangyoStationId = 응답아이디_조회(pangyoStationCreationResponse);

        return 지하철구간_등록(lineId, lastStationResponse.getId(), pangyoStationId);
    }

    private List<StationResponse> 응답역_조회(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList(KEY_STATIONS, StationResponse.class);
    }

    private ExtractableResponse<Response> 지하철구간_등록(
            final Long lineId, final Long upStationId, final Long downStationId
    ) {
        final Map<String, String> params = new HashMap<>();
        params.put(KEY_UP_STATION_ID, String.valueOf(upStationId));
        params.put(KEY_DOWN_STATION_ID, String.valueOf(downStationId));
        params.put(KEY_DISTANCE, String.valueOf(LINE_DISTANCE2));

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    private void 지하철구간이_등록됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 지하철구간_삭제(final ExtractableResponse<Response> response) {
        final Long lineId = 응답아이디_조회(response);

        final List<StationResponse> stationResponses = 응답역_조회(response);
        final StationResponse lastStationResponse = stationResponses.get(stationResponses.size() - 1);

        return 지하철구간_삭제(lineId, lastStationResponse.getId());
    }

    private ExtractableResponse<Response> 지하철_상행역_삭제(final ExtractableResponse<Response> response) {
        final Long lineId = 응답아이디_조회(response);

        final List<StationResponse> stationResponses = 응답역_조회(response);
        final StationResponse firstStationResponse = stationResponses.get(0);

        return 지하철구간_삭제(lineId, firstStationResponse.getId());
    }

    private ExtractableResponse<Response> 지하철구간_삭제(final Long lineId, final Long stationId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all()
                .extract();
    }

    private void 지하철구간이_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 지하철구간_삭제_실패함(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
