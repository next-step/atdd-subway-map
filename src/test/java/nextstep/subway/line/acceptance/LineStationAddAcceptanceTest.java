package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.*;
import static nextstep.subway.line.acceptance.step.LineStationAddAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;

@DisplayName("지하철 노선에 역 등록 관련 기능")
public class LineStationAddAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선에 역을 등록한다.")
    @Test
    void addLineStation() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN",
                LocalTime.of(5, 30), LocalTime.of(23, 30), 5);
        ExtractableResponse<Response> createdStationResponse = 지하철역_등록되어_있음("강남역");

        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(createdLineResponse, null, createdStationResponse, 4, 2);

        // then
        지하철_노선에_지하철역_등록됨(response);
    }

    @DisplayName("지하철 노선 상세정보 조회 시 역 정보가 포함된다.")
    @Test
    void getLineWithStations() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN",
                LocalTime.of(5, 30), LocalTime.of(23, 30), 5);
        ExtractableResponse<Response> createdStationResponse = 지하철역_등록되어_있음("강남역");

        지하철_노선에_지하철역_등록_요청(createdLineResponse, null, createdStationResponse, 4, 2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createdLineResponse);

        // then
        지하철_노선_응답됨(response);
        지하철_노선_상세정보_조회_시_역_정보_포함됨(response, Arrays.asList(createdStationResponse));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서대로 등록한다.")
    @Test
    void addLineStationInOrder() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN",
                LocalTime.of(5, 30), LocalTime.of(23, 30), 5);
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        // when
        ExtractableResponse<Response> lineStationResponse = 지하철_노선에_지하철역_등록_요청(createdLineResponse, null, createdStationResponse1, 4, 2);
        지하철_노선에_지하철역_등록_요청(createdLineResponse, createdStationResponse1, createdStationResponse2, 4, 2);
        지하철_노선에_지하철역_등록_요청(createdLineResponse, createdStationResponse2, createdStationResponse3, 4, 2);

        // then
        지하철_노선에_지하철역_등록됨(lineStationResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createdLineResponse);

        // then
        지하철_노선_응답됨(response);
        지하철_노선_상세정보_조회_시_역_정보_포함됨(response, Arrays.asList(createdStationResponse1, createdStationResponse2, createdStationResponse3));
        지하철_노선에_지하철역_순서대로_등록됨(response, Arrays.asList(createdStationResponse1, createdStationResponse2, createdStationResponse3));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서없이 등록한다.")
    @Test
    void addLineStationInAnyOrder() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN",
                LocalTime.of(5, 30), LocalTime.of(23, 30), 5);
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        // when
        ExtractableResponse<Response> lineStationResponse = 지하철_노선에_지하철역_등록_요청(createdLineResponse, null, createdStationResponse1, 4, 2);
        지하철_노선에_지하철역_등록_요청(createdLineResponse, createdStationResponse1, createdStationResponse2,4, 2);
        지하철_노선에_지하철역_등록_요청(createdLineResponse, createdStationResponse1, createdStationResponse3,4, 2);

        // then
        지하철_노선에_지하철역_등록됨(lineStationResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createdLineResponse);

        // then
        지하철_노선_응답됨(response);
        지하철_노선_상세정보_조회_시_역_정보_포함됨(response, Arrays.asList(createdStationResponse1, createdStationResponse2, createdStationResponse3));
        지하철_노선에_지하철역_순서대로_등록됨(response, Arrays.asList(createdStationResponse1, createdStationResponse3, createdStationResponse2));
    }

    @DisplayName("이미 등록되어 있던 역을 등록한다.")
    @Test
    void addAlreadyAddedLineStation() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN",
                LocalTime.of(5, 30), LocalTime.of(23, 30), 5);
        ExtractableResponse<Response> createdStationResponse = 지하철역_등록되어_있음("강남역");
        지하철_노선에_지하철역_등록되어_있음(createdLineResponse, null, createdStationResponse, 4, 2);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(createdLineResponse, null, createdStationResponse, 4, 2);

        // then
        지하철_노선에_지하철역_등록_중복_등록으로_실패됨(response);
    }

    @DisplayName("존재하지 않는 역을 등록한다")
    @Test
    void addNotExistingStationAsLineStation() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN",
                LocalTime.of(5, 30), LocalTime.of(23, 30), 5);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(createdLineResponse, null, 1L, 4, 2);

        // then
        지하철_노선에_지하철역_등록_존재하지않아_실패됨(response);
    }
}
