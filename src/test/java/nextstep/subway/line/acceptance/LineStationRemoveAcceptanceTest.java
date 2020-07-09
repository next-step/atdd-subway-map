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

import java.time.LocalTime;
import java.util.Arrays;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_생성됨;
import static nextstep.subway.line.acceptance.step.LineStationAddAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.*;

@DisplayName("지하철 노선에서 역 제외 기능")
public class LineStationRemoveAcceptanceTest extends AcceptanceTest {

    private Long lineId;
    private Long stationId1;
    private Long stationId2;
    private Long stationId3;

    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음(
                "2호선",
                "GREEN",
                LocalTime.of(5, 30),
                LocalTime.of(23, 30),
                5);

        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        lineId = createdLineResponse.as(LineResponse.class).getId();
        stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        stationId3 = createdStationResponse3.as(StationResponse.class).getId();

        지하철_노선에_지하철역_등록_요청(lineId, null, stationId1, 4, 2);
        지하철_노선에_지하철역_등록_요청(lineId, stationId1, stationId2, 4, 2);
        지하철_노선에_지하철역_등록_요청(lineId, stationId2, stationId3, 4, 2);

    }

    @DisplayName("지하철 노선에 등록된 마지막 지하철역을 제외한다.")
    @Test
    void removeLastAddedStation() {
        // when
        ExtractableResponse<Response> deletedStationResponse = 지하철_노선에_지하철역_제거_요청(lineId, stationId3);
        // then
        지하철_노선에_지하철역_제외됨(deletedStationResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineId);

        // then
        지하철_노선에_지하철역_제외_확인됨(response, stationId3);
        등록된_지하철역이_정렬되어_위치됨(response, Lists.newArrayList(1L, 2L));
    }
}
