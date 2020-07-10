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

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.*;
import static nextstep.subway.line.acceptance.step.LineStationAddAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;

@DisplayName("지하철 노선에 역 등록 관련 기능")
public class LineStationAddAcceptanceTest extends AcceptanceTest {

    private Long lineId;
    private Long station1Id;
    private Long station2Id;
    private Long station3Id;

    @BeforeEach
    void setup() {
        ExtractableResponse<Response> createdLineResponse = 노선_등록되어_있음("2호선");
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        lineId = createdLineResponse.as(LineResponse.class).getId();
        station1Id = createdStationResponse1.as(StationResponse.class).getId();
        station2Id = createdStationResponse2.as(StationResponse.class).getId();
        station3Id = createdStationResponse3.as(StationResponse.class).getId();
    }

    @DisplayName("지하철 노선에 역을 등록한다.")
    @Test
    void addLineStation() {
        // when
        ExtractableResponse<Response> response = 노선에_지하철역_등록_요청(lineId, null, station1Id);

        // then
        노선에_지하철역_등록됨(response);
    }

    @DisplayName("이미 등록되어 있던 역을 등록하면 등록에 실패한다.")
    @Test
    void addLineStationAlreadyAdded() {
        // given
        노선에_지하철역_등록되어_있음(lineId, station1Id);

        // when
        ExtractableResponse<Response> response = 노선에_지하철역_등록_요청(lineId, null, station1Id);

        // then
        노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("존재하지 않는 역을 등록하면 등록에 실패한다.")
    @Test
    void addLineStationNotExist() {
        // when
        ExtractableResponse<Response> response = 노선에_지하철역_등록_요청(lineId, null, 999L);

        // then
        노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("지하철 노선 상세정보 조회 시 역 정보가 포함된다.")
    @Test
    void getLineWithStations() {
        // given
        노선에_지하철역_등록되어_있음(lineId, station1Id);

        // when
        ExtractableResponse<Response> response = 노선_조회_요청(lineId);

        // then
        노선_응답됨(response);
        노선에_지하철역_포함됨(response, 1);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서대로 등록한다.")
    @Test
    void addLineStationInOrder() {
        // when
        ExtractableResponse<Response> lineStationResponse = 노선에_지하철역_등록_요청(lineId, null, station1Id);
        노선에_지하철역_등록_요청(lineId, station1Id, station2Id);
        노선에_지하철역_등록_요청(lineId, station2Id, station3Id);

        // then
        노선에_지하철역_등록됨(lineStationResponse);

        // when
        ExtractableResponse<Response> response = 노선_조회_요청(lineId);

        // then
        노선_응답됨(response);
        노선의_지하철역들의_순서가_예상과_일치함(response, Lists.newArrayList(1L, 2L, 3L));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서없이 등록한다.")
    @Test
    void addLineStationInAnyOrder() {
        // when
        ExtractableResponse<Response> lineStationResponse = 노선에_지하철역_등록_요청(lineId, null, station1Id);
        노선에_지하철역_등록_요청(lineId, station1Id, station2Id);
        노선에_지하철역_등록_요청(lineId, station1Id, station3Id);

        // then
        노선에_지하철역_등록됨(lineStationResponse);

        // when
        ExtractableResponse<Response> response = 노선_조회_요청(lineId);

        // then
        노선_응답됨(response);
        노선의_지하철역들의_순서가_예상과_일치함(response, Lists.newArrayList(1L, 3L, 2L));
    }
}
