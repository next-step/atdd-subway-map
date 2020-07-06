package nextstep.subway.line.acceptance;

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

import java.time.LocalTime;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선을_생성한다;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선을_조회한다;
import static nextstep.subway.line.acceptance.step.LineStationAcceptanceStep.지하철_노선에_지하철역을_등록한다;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선에 역 등록 관련 기능")
public class LineStationAddAcceptanceTest extends AcceptanceTest {

    private ExtractableResponse<Response> createdLineResponse;
    private ExtractableResponse<Response> createdStationResponse1;
    private ExtractableResponse<Response> createdStationResponse2;
    private ExtractableResponse<Response> createdStationResponse3;

    @BeforeEach
    public void background() {
        // given
        this.createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
        this.createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        this.createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        this.createdStationResponse3 = 지하철역_등록되어_있음("선릉역");
    }

    @DisplayName("지하철 노선에 역을 등록한다.")
    @Test
    void addLineStation() {
        // when
        // 지하철_노선에_지하철역_등록_요청
        Long lineId = createdLineResponse.as(LineResponse.class).getId();
        Long stationId = createdStationResponse1.as(StationResponse.class).getId();
        ExtractableResponse<Response> response = 지하철_노선에_지하철역을_등록한다(lineId, stationId, null);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선 상세정보 조회 시 역 정보가 포함된다.")
    @Test
    void getLineWithStations() {
        // given
        // 지하철_노선에_지하철역_등록_요청
        Long lineId = createdLineResponse.as(LineResponse.class).getId();
        Long stationId = createdStationResponse1.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId, null);

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = 지하철_노선을_조회한다(lineId);

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
        Long lineId = createdLineResponse.as(LineResponse.class).getId();
        Long stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        ExtractableResponse<Response> lineStationResponse = 지하철_노선에_지하철역을_등록한다(lineId, stationId1, null);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId2, stationId1);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId3 = createdStationResponse3.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId3, stationId2);

        // then
        assertThat(lineStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = 지하철_노선을_조회한다(lineId);

        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(3);
        assertThat(lineResponse.getStations()).extracting(it -> it.getStation().getId())
                .containsExactlyElementsOf(Lists.newArrayList(1L, 2L, 3L));
    }



    @DisplayName("지하철 노선에 여러개의 역을 순서없이 등록한다.")
    @Test
    void addLineStationInAnyOrder() {
        // when
        // 지하철_노선에_지하철역_등록_요청
        Long lineId = createdLineResponse.as(LineResponse.class).getId();
        Long stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        ExtractableResponse<Response> lineStationResponse = 지하철_노선에_지하철역을_등록한다(lineId, stationId1, null);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId2, stationId1);

        // 지하철_노선에_지하철역_등록_요청
        Long stationId3 = createdStationResponse3.as(StationResponse.class).getId();
        지하철_노선에_지하철역을_등록한다(lineId, stationId3, stationId1);

        // then
        assertThat(lineStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = 지하철_노선을_조회한다(lineId);

        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(3);
        assertThat(lineResponse.getStations()).extracting(it -> it.getStation().getId())
                .containsExactlyElementsOf(Lists.newArrayList(1L, 3L, 2L));
    }

    private ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color) {
        return 지하철_노선을_생성한다(name, color, LocalTime.of(5, 30), LocalTime.of(23, 30), 5);
    }
}
