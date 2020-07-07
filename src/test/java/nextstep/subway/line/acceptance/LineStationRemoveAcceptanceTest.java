package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_조회_요청;
import static nextstep.subway.line.acceptance.step.LineStationAddAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineStationRemoveAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;

@DisplayName("지하철 노선에 역 제외 관련 기능")
public class LineStationRemoveAcceptanceTest extends AcceptanceTest {
    ExtractableResponse<Response> createLineResponse;
    List<ExtractableResponse<Response>> createStationResponses = new ArrayList<>();
    List<ExtractableResponse<Response>> addLineStationResponses = new ArrayList<>();

    @BeforeEach
    void background() {
        createLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN",
                LocalTime.of(5, 30), LocalTime.of(23, 30), 5);

        createStationResponses.add(지하철역_등록되어_있음("강남역"));
        createStationResponses.add(지하철역_등록되어_있음("역삼역"));
        createStationResponses.add(지하철역_등록되어_있음("선릉역"));

        addLineStationResponses.add(지하철_노선에_지하철역_등록되어_있음(createLineResponse, null, createStationResponses.get(0), 4, 2));
        addLineStationResponses.add(지하철_노선에_지하철역_등록되어_있음(createLineResponse, createStationResponses.get(0), createStationResponses.get(1), 4, 2));
        addLineStationResponses.add(지하철_노선에_지하철역_등록되어_있음(createLineResponse, createStationResponses.get(1), createStationResponses.get(2), 4, 2));
    }

    @DisplayName("지하철 노선에 등록된 마지막 지하철역을 제외한다.")
    @Test
    void removeLastLineStation() {
        // when
        ExtractableResponse<Response> removeLineStationResponse = 지하철_노선_지하철역_제외_요청(createLineResponse, addLineStationResponses.get(2));

        // then
        지하철_노선에_지하철역_제외됨(removeLineStationResponse);

        // when
        ExtractableResponse<Response> getLineResponse = 지하철_노선_조회_요청(createLineResponse);

        // then
        지하철_노선에_지하철역_제외_확인됨(getLineResponse, addLineStationResponses.get(2));
        지하철_노선에_지하철역_순서_정렬됨(getLineResponse,
                Arrays.asList(addLineStationResponses.get(0), addLineStationResponses.get(1)));
    }

    @DisplayName("지하철 노선에 등록된 중간 지하철역을 제외한다.")
    @Test
    void removeMiddleLineStation() {
        // when
        final ExtractableResponse<Response> removeLineStationResponse = 지하철_노선_지하철역_제외_요청(createLineResponse, addLineStationResponses.get(1));

        // then
        지하철_노선에_지하철역_제외됨(removeLineStationResponse);

        // when
        final ExtractableResponse<Response> getLineResponse = 지하철_노선_조회_요청(createLineResponse);

        // then
        지하철_노선에_지하철역_제외_확인됨(getLineResponse, addLineStationResponses.get(1));
        지하철_노선에_지하철역_순서_정렬됨(getLineResponse,
                Arrays.asList(addLineStationResponses.get(0), addLineStationResponses.get(2)));
    }

    @DisplayName("지하철 노선에서 등록되지 않는 역을 제외한다.")
    @Test
    void removeNotAddedLineStation() {
        // when
        final ExtractableResponse<Response> removeLineStationResponse = 지하철_노선_지하철역_제외_요청(createLineResponse, -1L);

        // then
        지하철_노선에_지하철역_제외_실패됨(removeLineStationResponse);
    }
}
