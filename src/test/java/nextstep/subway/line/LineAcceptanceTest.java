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

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private static final String LINE1_NAME = "1호선";
    private static final String LINE1_COLOR = "red";
    private static final Long LINE1_UP_STATION_ID = 1L;
    private static final Long LINE1_DOWN_STATION_ID = 3L;
    private static final Long LINE1_DISTANCE = 10L;

    private static final String LINE2_NAME = "2호선";
    private static final Long LINE2_UP_STATION_ID = 2L;
    private static final String LINE2_COLOR = "blue";
    private static final Long LINE2_DOWN_STATION_ID = 4L;
    private static final Long LINE2_DISTANCE = 20L;

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COLOR = "color";
    private static final String KEY_UP_STATION_ID = "upStationId";
    private static final String KEY_DOWN_STATION_ID = "downStationId";
    private static final String KEY_DISTANCE = "distance";

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
}
