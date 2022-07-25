package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성;
import static nextstep.subway.station.StationAcceptanceTest.지하철역명_조회;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when 지하철 노선을 생성
        final ExtractableResponse<Response> creationResponse =
                지하철노선_생성(LINE1_NAME, LINE1_COLOR, LINE1_UP_STATION_ID, LINE1_DOWN_STATION_ID, LINE1_DISTANCE);

        // then 지하철 노석 목록 조회하여 생성한 노선 확인
        final List<String> lineNames = 지하철노선명_목록조회();
        Assertions.assertAll(
                () -> assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(lineNames).containsExactly(LINE1_NAME)
        );
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given 2개의 지하철 노선 생성
        지하철노선_생성(LINE1_NAME, LINE1_COLOR, LINE1_UP_STATION_ID, LINE1_DOWN_STATION_ID, LINE1_DISTANCE);
        지하철노선_생성(LINE2_NAME, LINE2_COLOR, LINE2_UP_STATION_ID, LINE2_DOWN_STATION_ID, LINE2_DISTANCE);

        // when 지하철 노선 목록 조회
        final ExtractableResponse<Response> linesResponse = 지하철노선_목록조회();
        final List<String> lineNames = 지하철노선명_목록조회(linesResponse);

        // then 지하철 노선 목록 조회 시 2개의 노선 조회
        Assertions.assertAll(
                () -> assertThat(linesResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineNames).isEqualTo(List.of(LINE1_NAME, LINE2_NAME))
        );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given 지하철 노선 생성
        final ExtractableResponse<Response> creationResponse =
                지하철노선_생성(LINE1_NAME, LINE1_COLOR, LINE1_UP_STATION_ID, LINE1_DOWN_STATION_ID, LINE1_DISTANCE);

        // when 지하철 노선 조회
        final Long id = 응답아이디_조회(creationResponse);
        final ExtractableResponse<Response> lineResponse = 지하철노선_조회(id);
        final String lineName = 지하철노선명_조회(lineResponse);

        // then 생성한 지하철 노선의 정보 응답 확인
        Assertions.assertAll(
                () -> assertThat(lineResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineName).isEqualTo(LINE1_NAME)
        );
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given 지하철 노선 생성
        final ExtractableResponse<Response> creationResponse =
                지하철노선_생성(LINE1_NAME, LINE1_COLOR, LINE1_UP_STATION_ID, LINE1_DOWN_STATION_ID, LINE1_DISTANCE);

        // when 생성한 지하철 노선 수정
        final Long id = 응답아이디_조회(creationResponse);
        final ExtractableResponse<Response> updateResponse = 지하철노선_수정(id, LINE2_NAME, LINE2_COLOR);

        // then 지하철 노선 정보 수정 확인
        final List<String> lineNames = 지하철노선명_목록조회();
        Assertions.assertAll(
                () -> assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineNames).isEqualTo(List.of(LINE2_NAME))
        );
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given 지하철 노선 생성
        final ExtractableResponse<Response> creationResponse =
                지하철노선_생성(LINE1_NAME, LINE1_COLOR, LINE1_UP_STATION_ID, LINE1_DOWN_STATION_ID, LINE1_DISTANCE);

        // when 생성한 지하철 노석 삭제
        final Long id = 응답아이디_조회(creationResponse);
        final ExtractableResponse<Response> deletionResponse = 지하철노선_삭제(id);

        // then 지하철 노선 정보 삭제 확인
        final List<String> lineNames = 지하철노선명_목록조회();
        Assertions.assertAll(
                () -> assertThat(deletionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(lineNames).isEmpty()
        );
    }

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void addSection() {
        // given 지하철 구간 등록
        final ExtractableResponse<Response> gangNamStationCreationResponse = 지하철역_생성(GANGNAM_STATION);
        final ExtractableResponse<Response> sindorimStationCreationResponse = 지하철역_생성(SINDORIM_STATION);
        final ExtractableResponse<Response> panGyoStationCreationResponse = 지하철역_생성(PANGYO_STATION);
        final Long gangNameStationId = 응답아이디_조회(gangNamStationCreationResponse);
        final Long sindorimStationId = 응답아이디_조회(sindorimStationCreationResponse);
        final Long panGyoStationId = 응답아이디_조회(panGyoStationCreationResponse);

        final ExtractableResponse<Response> lineCreationResponse =
                지하철노선_생성(LINE1_NAME, LINE1_COLOR, gangNameStationId, sindorimStationId, LINE1_DISTANCE);
        final Long lineId = 응답아이디_조회(lineCreationResponse);

        final ExtractableResponse<Response> sectionCreationResponse =
                지하철구간_등록(lineId, sindorimStationId, panGyoStationId);

        // when 지하철 노선 조회
        final List<String> stationNames = 지하철역명_조회();

        // then 생성한 지하철 구간의 정보 응답 확인
        Assertions.assertAll(
                () -> assertThat(sectionCreationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(stationNames).isEqualTo(List.of(GANGNAM_STATION, SINDORIM_STATION, PANGYO_STATION))
        );

    }

    @DisplayName("지하철 노선에 구간을 삭제한다.")
    @Test
    void deleteSection() {
        // given 지하철 구간 등록
        final ExtractableResponse<Response> gangNamStationCreationResponse = 지하철역_생성(GANGNAM_STATION);
        final ExtractableResponse<Response> sindorimStationCreationResponse = 지하철역_생성(SINDORIM_STATION);
        final ExtractableResponse<Response> panGyoStationCreationResponse = 지하철역_생성(PANGYO_STATION);
        final Long gangNameStationId = 응답아이디_조회(gangNamStationCreationResponse);
        final Long sindorimStationId = 응답아이디_조회(sindorimStationCreationResponse);
        final Long panGyoStationId = 응답아이디_조회(panGyoStationCreationResponse);

        final ExtractableResponse<Response> lineCreationResponse =
                지하철노선_생성(LINE1_NAME, LINE1_COLOR, gangNameStationId, sindorimStationId, LINE1_DISTANCE);
        final Long lineId = 응답아이디_조회(lineCreationResponse);

        지하철구간_등록(lineId, sindorimStationId, panGyoStationId);

        // when 지하철 구간 삭제
        final ExtractableResponse<Response> deletionResponse = 지하철구간_삭제(lineId, panGyoStationId);

        // then 지하철 구간 삭제 확인
        final List<String> stationNames = 지하철역명_조회();
        Assertions.assertAll(
                () -> assertThat(deletionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(stationNames).isEqualTo(List.of(GANGNAM_STATION, SINDORIM_STATION))
        );
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

    private String 지하철노선명_조회(final ExtractableResponse<Response> response) {
        return response.jsonPath().getObject(KEY_NAME, String.class);
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

    private ExtractableResponse<Response> 지하철노선_삭제(final Long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    private Long 응답아이디_조회(final ExtractableResponse<Response> response) {
        return response.jsonPath().getObject(KEY_ID, Long.class);
    }

    private ExtractableResponse<Response> 지하철구간_등록(
            final Long lineId, final Long upStationId, final Long downStationId
    ) {
        final Map<String, String> params = new HashMap<>();
        params.put(KEY_UP_STATION_ID, String.valueOf(upStationId));
        params.put(KEY_DOWN_STATION_ID, String.valueOf(downStationId));
        params.put(KEY_DISTANCE, String.valueOf(LINE2_DISTANCE));

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철구간_삭제(final Long lineId, final Long stationId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all()
                .extract();
    }
}
