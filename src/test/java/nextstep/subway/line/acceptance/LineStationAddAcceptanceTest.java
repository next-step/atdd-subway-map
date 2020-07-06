package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_조회_요청;
import static nextstep.subway.line.acceptance.step.LineStationAddAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;

@DisplayName("지하철 노선에 역 등록 관련 기능")
public class LineStationAddAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선에 역을 등록한다.")
    @Test
    void addLineStation() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN", LocalTime.of(5, 30), LocalTime.of(23, 30), 5);
        ExtractableResponse<Response> createdStationResponse = 지하철역_등록되어_있음("강남역");

        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(createdLineResponse, createdStationResponse, "");

        // then
        지하철_노선에_지하철역_등록됨(response);
    }


    @DisplayName("지하철 노선 상세정보 조회 시 역 정보가 포함된다.")
    @Test
    void getLineWithStations() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN", LocalTime.of(5, 30), LocalTime.of(23, 30), 5);
        ExtractableResponse<Response> createdStationResponse = 지하철역_등록되어_있음("강남역");

        지하철_노선에_지하철역_등록_요청(createdLineResponse, createdStationResponse, "");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createdLineResponse);

        // then
        지하철_노선_상세정보_응답됨(response, 1);
    }


    @DisplayName("지하철 노선에 여러개의 역을 순서대로 등록한다.")
    @Test
    void addLineStationInOrder() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN", LocalTime.of(5, 30), LocalTime.of(23, 30), 5);
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        // when
        Long stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        Long stationId2 = createdStationResponse2.as(StationResponse.class).getId();

        지하철_노선에_지하철역_등록_요청(createdLineResponse, createdStationResponse1, "");
        지하철_노선에_지하철역_등록_요청(createdLineResponse, createdStationResponse2, String.valueOf(stationId1));
        ExtractableResponse<Response> lineStationResponse = 지하철_노선에_지하철역_등록_요청(createdLineResponse, createdStationResponse3, String.valueOf(stationId2));

        // then
        지하철_노선에_지하철역_등록됨(lineStationResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineStationResponse);

        지하철_노선_상세정보_응답됨(response, 3);
        등록된_지하철_역이_주어진_위치에_위치됨(response, Lists.newArrayList(1L, 2L, 3L));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서없이 등록한다.")
    @Test
    void addLineStationInAnyOrder() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN", LocalTime.of(5, 30), LocalTime.of(23, 30), 5);
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        // when
        Long stationId1 = createdStationResponse1.as(LineResponse.class).getId();

        지하철_노선에_지하철역_등록_요청(createdLineResponse, createdStationResponse1, "");
        지하철_노선에_지하철역_등록_요청(createdLineResponse, createdStationResponse2, String.valueOf(stationId1));
        ExtractableResponse<Response> lineStationResponse = 지하철_노선에_지하철역_등록_요청(createdLineResponse, createdStationResponse3, String.valueOf(stationId1));

        // then
        지하철_노선에_지하철역_등록됨(lineStationResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineStationResponse);

        지하철_노선_상세정보_응답됨(response, 3);
        등록된_지하철_역이_주어진_위치에_위치됨(response, Lists.newArrayList(1L, 3L, 2L));
    }


}
