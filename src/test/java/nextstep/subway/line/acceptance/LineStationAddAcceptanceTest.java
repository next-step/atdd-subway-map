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
        // 지하철_노선에_지하철역_등록_요청
        Map<String, String> params = createLineStationRequestParams(null, stationId1, "4", "2");
        ExtractableResponse<Response> response = createLineStation(params, lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선 상세정보 조회 시 역 정보가 포함된다.")
    @Test
    void getLineWithStations() {
        // 지하철_노선에_지하철역_등록_요청
        Map<String, String> params = createLineStationRequestParams(null, stationId1, "4", "2");
        createLineStation(params, lineId);

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = readLinesStation(lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(1);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서대로 등록한다.")
    @Test
    void addLineStationInOrder() {
        // when
        // 지하철_노선에_지하철역_등록_요청
        Map<String, String> params = createLineStationRequestParams(null, stationId1, "4", "2");
        ExtractableResponse<Response> lineStationResponse = createLineStation(params, lineId);

        // 지하철_노선에_지하철역_등록_요청
        params = createLineStationRequestParams(stationId1, stationId2, "4", "2");
        createLineStation(params, lineId);

        // 지하철_노선에_지하철역_등록_요청
        params = createLineStationRequestParams(stationId2, stationId3, "4", "2");
        createLineStation(params, lineId);

        // then
        assertThat(lineStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = readLinesStation(lineId);

        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(3);
        List<Long> stationIds = lineResponse.getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, 2L, 3L));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서없이 등록한다.")
    @Test
    void addLineStationInAnyOrder() {
        // when
        // 지하철_노선에_지하철역_등록_요청
        Map<String, String> params = createLineStationRequestParams(null, stationId1, "4", "2");
        ExtractableResponse<Response> lineStationResponse = createLineStation(params, lineId);

        // 지하철_노선에_지하철역_등록_요청
        params = createLineStationRequestParams(stationId1, stationId2, "4", "2");
        createLineStation(params, lineId);

        // 지하철_노선에_지하철역_등록_요청
        params = createLineStationRequestParams(stationId1, stationId3, "4", "2");
        createLineStation(params, lineId);

        // then
        assertThat(lineStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = readLinesStation(lineId);

        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(3);
        List<Long> stationIds = lineResponse.getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, 3L, 2L));
    }

    @DisplayName("이미 등록되어 있던 역을 등록한다.")
    @Test
    void addDuplicateStationInLine() {
        // given
        // 지하철_노선에_지하철역_등록_요청
        Map<String, String> params = createLineStationRequestParams(null, stationId1, "4", "2");
        createLineStation(params, lineId);

        // when
        // 지하철_노선_이미_등록되어있는_지하철역_등록_요청
        params = createLineStationRequestParams(null, stationId1, "4", "2");
        ExtractableResponse<Response> lineStationResponse = createLineStation(params, lineId);

        // then
        // 지하철 노선에 지하철역 등록 실패됨
        assertThat(lineStationResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(lineStationResponse.body().asString()).contains("DuplicateStationInLineException");
    }

    @DisplayName("존재하지 않는 역을 등록한다.")
    @Test
    void addNonExistStationInLine() {
        // when
        // 지하철_노선_존재하지_않는_지하철역_등록_요청
        Map<String, String> params = createLineStationRequestParams(null, 4L, "4", "2");
        ExtractableResponse<Response> lineStationResponse = createLineStation(params, lineId);

        // then
        // 지하철 노선에 지하철역 등록 실패됨
        assertThat(lineStationResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(lineStationResponse.body().asString()).contains("NonExistStationInLineException");
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
        params.put("distance", "4");
        params.put("duration", "2");

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
