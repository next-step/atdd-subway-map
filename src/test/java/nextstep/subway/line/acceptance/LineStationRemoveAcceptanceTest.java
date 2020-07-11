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

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.노선_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.노선_조회_요청;
import static nextstep.subway.line.acceptance.step.LineStationAddAcceptanceStep.노선에_지하철역_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineStationAddAcceptanceStep.노선의_지하철역들의_순서가_예상과_일치함;
import static nextstep.subway.line.acceptance.step.LineStationRemoveAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;

@DisplayName("지하철 노선에 역 제외 관련 기능")
public class LineStationRemoveAcceptanceTest extends AcceptanceTest {

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

    @DisplayName("노선에 등록된 마지막 역을 제외한다.")
    @Test
    void removeLastLineStation() {
        // given
        노선에_지하철역_등록되어_있음(lineId, station1Id);
        노선에_지하철역_등록되어_있음(lineId, station2Id);
        노선에_지하철역_등록되어_있음(lineId, station3Id);

        // when
        ExtractableResponse<Response> removeResponse = 노선의_지하철역_제거_요청(lineId, station3Id);

        // then
        노선의_지하철역_제거됨(removeResponse);

        // when
        ExtractableResponse<Response> response = 노선_조회_요청(lineId);
        노선의_지하철역들의_순서가_예상과_일치함(response, Lists.newArrayList(1L, 2L));
    }

    @DisplayName("노선에 등록된 마지막 역을 제외한다.")
    @Test
    void removeMiddleLineStation() {
        // given
        노선에_지하철역_등록되어_있음(lineId, station1Id);
        노선에_지하철역_등록되어_있음(lineId, station2Id);
        노선에_지하철역_등록되어_있음(lineId, station3Id);

        // when
        ExtractableResponse<Response> removeResponse = 노선의_지하철역_제거_요청(lineId, station2Id);

        // then
        노선의_지하철역_제거됨(removeResponse);

        // when
        ExtractableResponse<Response> response = 노선_조회_요청(lineId);
        노선의_지하철역들의_순서가_예상과_일치함(response, Lists.newArrayList(1L, 3L));
    }

    @DisplayName("노선에 등록되지 않은 역을 제거하면 실패한다.")
    @Test
    void removeNotAddedLineStation() {
        // when
        ExtractableResponse<Response> removeResponse = 노선의_지하철역_제거_요청(lineId, 999L);

        // then
        노선의_지하철역_제거_실패됨(removeResponse);
    }
}
