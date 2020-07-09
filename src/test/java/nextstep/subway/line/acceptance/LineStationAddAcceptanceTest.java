package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.*;
import static nextstep.subway.line.acceptance.step.LineStationAddAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;

@DisplayName("지하철 노선에 역 등록 관련 기능")
public class LineStationAddAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선에 역을 등록한다.")
    @Test
    void addLineStation() {
        // given
        ExtractableResponse<Response> createdLineResponse = 노선_등록되어_있음("2호선");
        ExtractableResponse<Response> createdStationResponse = 지하철역_등록되어_있음("강남역");

        // when
        Long lineId = createdLineResponse.as(LineResponse.class).getId();
        Long stationId = createdStationResponse.as(StationResponse.class).getId();
        ExtractableResponse<Response> response = 노선에_지하철역_등록_요청(lineId, null, stationId);

        // then
        노선에_목록_등록됨(response);
    }

    @DisplayName("지하철 노선 상세정보 조회 시 역 정보가 포함된다.")
    @Test
    void getLineWithStations() {
        // given
        ExtractableResponse<Response> createdLineResponse = 노선_등록되어_있음("2호선");
        ExtractableResponse<Response> createdStationResponse = 지하철역_등록되어_있음("강남역");

        Long lineId = createdLineResponse.as(LineResponse.class).getId();
        Long stationId = createdStationResponse.as(StationResponse.class).getId();
        노선에_지하철역_등록되어_있음(lineId, stationId);

        // when
        ExtractableResponse<Response> response = 노선_조회_요청(lineId);

        // then
        노선_응답됨(response);
        노선에_지하철역_포함됨(response, 1);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서대로 등록한다.")
    @Test
    void addLineStationInOrder() {
        // given
        ExtractableResponse<Response> createdLineResponse = 노선_등록되어_있음("2호선");
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        // when
        Long lineId = createdLineResponse.as(LineResponse.class).getId();
        Long stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        ExtractableResponse<Response> lineStationResponse = 노선에_지하철역_등록_요청(lineId, null, stationId1);

        Long stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        노선에_지하철역_등록_요청(lineId, stationId1, stationId2);

        Long stationId3 = createdStationResponse3.as(StationResponse.class).getId();
        노선에_지하철역_등록_요청(lineId, stationId2, stationId3);

        // then
        노선에_목록_등록됨(lineStationResponse);

        // when
        ExtractableResponse<Response> response = 노선_조회_요청(lineId);

        // then
        노선_응답됨(response);
        노선의_지하철역들의_순서가_예상과_일치함(response, Lists.newArrayList(1L, 2L, 3L));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서없이 등록한다.")
    @Test
    void addLineStationInAnyOrder() {
        // given
        ExtractableResponse<Response> createdLineResponse = 노선_등록되어_있음("2호선");
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        // when
        Long lineId = createdLineResponse.as(LineResponse.class).getId();
        Long stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        ExtractableResponse<Response> lineStationResponse = 노선에_지하철역_등록_요청(lineId, null, stationId1);

        Long stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        노선에_지하철역_등록_요청(lineId, stationId1, stationId2);

        Long stationId3 = createdStationResponse3.as(StationResponse.class).getId();
        노선에_지하철역_등록_요청(lineId, stationId1, stationId3);

        // then
        노선에_목록_등록됨(lineStationResponse);

        // when
        ExtractableResponse<Response> response = 노선_조회_요청(lineId);

        // then
        노선_응답됨(response);
        노선의_지하철역들의_순서가_예상과_일치함(response, Lists.newArrayList(1L, 3L, 2L));
    }
}
