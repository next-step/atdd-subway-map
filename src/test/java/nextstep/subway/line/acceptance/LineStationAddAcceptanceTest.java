package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선에 역 등록 관련 기능")
public class LineStationAddAcceptanceTest extends AcceptanceTest {
    Long lineId;
    Long stationId1;
    Long stationId2;
    Long stationId3;

    @BeforeEach
    void setup() {
        super.setUp();

        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        lineId = createdLineResponse.as(LineResponse.class).getId();
        stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        stationId3 = createdStationResponse3.as(StationResponse.class).getId();
    }

    @DisplayName("지하철 노선에 역을 등록한다.")
    @Test
    void addLineStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(null, stationId1, "4", "2");

        // then
        지하철_노선에_역_등록됨(response);
    }

    @DisplayName("지하철 노선 상세정보 조회 시 역 정보가 포함된다.")
    @Test
    void getLineWithStations() {
        // given
        지하철_노선에_지하철역_등록_요청(null, stationId1, "4", "2");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineId);

        // then
        지하철_노선_상세정보_조회_시_역_정보_포함됨(response);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서대로 등록한다.")
    @Test
    void addLineStationInOrder() {
        // when
        ExtractableResponse<Response> lineStationResponse =  지하철_노선에_지하철역_등록_요청(null, stationId1, "4", "2");

        지하철_노선에_지하철역_등록_요청(stationId1, stationId2, "4", "2");
        지하철_노선에_지하철역_등록_요청(stationId2, stationId3, "4", "2");

        // then
        지하철_노선에_역_등록됨(lineStationResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineId);

        // then
        지하철_노선에_여러개의_역이_순서대로_등록됨(response);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서없이 등록한다.")
    @Test
    void addLineStationInAnyOrder() {
        // when
        ExtractableResponse<Response> lineStationResponse =  지하철_노선에_지하철역_등록_요청(null, stationId1, "4", "2");

        지하철_노선에_지하철역_등록_요청(stationId1, stationId2, "4", "2");
        지하철_노선에_지하철역_등록_요청(stationId1, stationId3, "4", "2");

        // then
        지하철_노선에_역_등록됨(lineStationResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineId);

        // then
        지하철_노선에_여러개의_역이_순서없이_등록됨(response);
    }

    @DisplayName("이미 등록되어 있던 역을 등록한다.")
    @Test
    void addDuplicateStationInLine() {
        // given
        지하철_노선에_지하철역_등록_요청(null, stationId1, "4", "2");

        // when
        // 지하철_노선_이미_등록되어있는_지하철역_등록_요청
        ExtractableResponse<Response> lineStationResponse =  지하철_노선에_지하철역_등록_요청(null, stationId1, "4", "2");

        // then
        지하철_노선에_이미_존재_역_등록_실패됨(lineStationResponse);
    }

    @DisplayName("존재하지 않는 역을 등록한다.")
    @Test
    void addNonExistStationInLine() {
        // when
        // 지하철_노선_존재하지_않는_지하철역_등록_요청
        ExtractableResponse<Response> lineStationResponse = 지하철_노선에_지하철역_등록_요청(null, 4L, "4", "2");

        // then
        // 지하철 노선에 지하철역 등록 실패됨
        존재하지_않는_역_등록_실패(lineStationResponse);
    }

    private ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(Long preStationId, Long stationId1, String distance, String duration) {
        Map<String, String> params = createLineStationRequestParams(preStationId, stationId1, distance, duration);

        return createLineStation(params, lineId);
    }

    private void 지하철_노선에_역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(Long lineId) {
        return readLinesStation(lineId);
    }

    private void 지하철_노선_상세정보_조회_시_역_정보_포함됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(1);
    }

    private void 지하철_노선에_여러개의_역이_순서대로_등록됨(ExtractableResponse<Response> response) {
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(3);
        List<Long> stationIds = lineResponse.getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, 2L, 3L));
    }

    private void 지하철_노선에_여러개의_역이_순서없이_등록됨(ExtractableResponse<Response> response) {
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(3);
        List<Long> stationIds = lineResponse.getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, 3L, 2L));
    }

    private void 지하철_노선에_이미_존재_역_등록_실패됨(ExtractableResponse<Response> lineStationResponse) {
        assertThat(lineStationResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(lineStationResponse.body().asString()).contains("지하철 역이 해당 노선에 이미 등록되어 있습니다");
    }
    private void 존재하지_않는_역_등록_실패(ExtractableResponse<Response> lineStationResponse) {
        assertThat(lineStationResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(lineStationResponse.body().asString()).contains("존재하지 않는 역은 등록이 불가능 합니다.");
    }

    private ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", "5");

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines").
                then().
                log().all().
                extract();
    }

    private Map<String, String> createLineStationRequestParams(Long preStationId, Long stationId, String distance, String duration) {
        Map<String, String> params = new HashMap<>();

        params.put("preStationId", preStationId + "");
        params.put("stationId", stationId + "");
        params.put("distance", distance);
        params.put("duration", duration);

        return params;
    }

    private ExtractableResponse<Response> createLineStation(Map<String, String> params, Long lineId) {
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();
    }

    private ExtractableResponse<Response> readLinesStation(Long lineId) {
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines/{lineId}", lineId).
                then().
                log().all().
                extract();
    }
}
