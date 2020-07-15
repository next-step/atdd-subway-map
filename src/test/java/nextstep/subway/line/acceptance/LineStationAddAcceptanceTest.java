package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.acceptance.step.LineStationAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;

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
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(lineId, null, stationId1, "4", "2");

        // then
        지하철_노선에_역_등록됨(response);
    }

    @DisplayName("지하철 노선 상세정보 조회 시 역 정보가 포함된다.")
    @Test
    void getLineWithStations() {
        // given
        지하철_노선에_지하철역_등록_요청(lineId,null, stationId1, "4", "2");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineId);

        // then
        지하철_노선_상세정보_조회_시_역_정보_포함됨(response);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서대로 등록한다.")
    @Test
    void addLineStationInOrder() {
        // when
        ExtractableResponse<Response> lineStationResponse =  지하철_노선에_지하철역_등록_요청(lineId,null, stationId1, "4", "2");

        지하철_노선에_지하철역_등록_요청(lineId, stationId1, stationId2, "4", "2");
        지하철_노선에_지하철역_등록_요청(lineId, stationId2, stationId3, "4", "2");

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
        ExtractableResponse<Response> lineStationResponse =  지하철_노선에_지하철역_등록_요청(lineId, null, stationId1, "4", "2");

        지하철_노선에_지하철역_등록_요청(lineId, stationId1, stationId2, "4", "2");
        지하철_노선에_지하철역_등록_요청(lineId, stationId1, stationId3, "4", "2");

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
        지하철_노선에_지하철역_등록_요청(lineId, null, stationId1, "4", "2");

        // when
        // 지하철_노선_이미_등록되어있는_지하철역_등록_요청
        ExtractableResponse<Response> lineStationResponse =  지하철_노선에_지하철역_등록_요청(lineId, null, stationId1, "4", "2");

        // then
        지하철_노선에_이미_존재_역_등록_실패됨(lineStationResponse);
    }

    @DisplayName("존재하지 않는 역을 등록한다.")
    @Test
    void addNonExistStationInLine() {
        // when
        // 지하철_노선_존재하지_않는_지하철역_등록_요청
        ExtractableResponse<Response> lineStationResponse = 지하철_노선에_지하철역_등록_요청(lineId, null, 4L, "4", "2");

        // then
        존재하지_않는_역_등록_실패(lineStationResponse);
    }
}
