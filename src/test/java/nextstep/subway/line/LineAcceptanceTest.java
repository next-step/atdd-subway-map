package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static nextstep.subway.line.LineAcceptanceAssertion.*;
import static nextstep.subway.line.LineAcceptanceRequest.*;
import static nextstep.subway.station.StationAcceptanceRequest.createStationBody;
import static nextstep.subway.station.StationAcceptanceRequest.지하철_역_생성_요청;


@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 상행역;
    private StationResponse 하행역;
    private StationResponse 신규역;

    @BeforeEach
    public void initialize() {
        // given
        상행역 = 지하철_역_생성_요청(createStationBody("상행역")).as(StationResponse.class);
        하행역 = 지하철_역_생성_요청(createStationBody("하행역")).as(StationResponse.class);
        신규역 = 지하철_역_생성_요청(createStationBody("신규역")).as(StationResponse.class);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        Map<String, String> lineBody = createLineBody("1호선", "blue", 상행역.getId(), 하행역.getId(), 10);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineBody);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성하면 실패한다.")
    @Test
    void createStationWithDuplicateName() {
        Map<String, String> lineBody1 = createLineBody("1호선", "blue", 상행역.getId(), 하행역.getId(), 10);
        Map<String, String> lineBody2 = createLineBody("1호선", "green", 하행역.getId(), 신규역.getId(), 23);

        // given
        지하철_노선_생성_요청(lineBody1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineBody2);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        Map<String, String> lineBody1 = createLineBody("1호선", "blue", 상행역.getId(), 하행역.getId(), 10);
        Map<String, String> lineBody2 = createLineBody("2호선", "green", 하행역.getId(), 신규역.getId(), 23);

        // given
        ExtractableResponse<Response> line1Response = 지하철_노선_생성_요청(lineBody1);
        ExtractableResponse<Response> line2Response = 지하철_노선_생성_요청(lineBody2);

        // when
        ExtractableResponse<Response> linesResponse = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(linesResponse);
        지하철_노선_목록_포함됨(linesResponse,
                Arrays.asList(line1Response, line2Response));
    }


    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        Map<String, String> lineBody1 = createLineBody("1호선", "blue", 상행역.getId(), 하행역.getId(), 10);
        Map<String, String> lineBody2 = createLineBody("2호선", "green", 하행역.getId(), 신규역.getId(), 23);

        // given
        ExtractableResponse<Response> expectedResponse = 지하철_노선_생성_요청(lineBody1);
        지하철_노선_생성_요청(lineBody2);

        // when
        Long lineId = expectedResponse
                .as(LineResponse.class)
                .getId();
        ExtractableResponse<Response> actualResponse = 지하철_노선_조회_요청(lineId);

        // then
        지하철_노선_응답됨(actualResponse, expectedResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        Map<String, String> lineBody1 = createLineBody("1호선", "blue", 상행역.getId(), 하행역.getId(), 10);
        Long lineId = 지하철_노선_생성_요청(lineBody1)
                .as(LineResponse.class)
                .getId();

        // when
        String name = "9호선";
        String color = "yellow";
        Map<String, String> lineBody2 = createLineBody(name, color, 하행역.getId(), 신규역.getId(), 23);
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(
                lineId,
                lineBody2);

        // then
        지하철_노선_수정됨(response, name, color);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        Map<String, String> lineBody1 = createLineBody("1호선", "blue", 상행역.getId(), 하행역.getId(), 10);

        // given
        Long lineId = 지하철_노선_생성_요청(lineBody1)
                .as(LineResponse.class)
                .getId();

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineId);

        // then
        지하철_노선_제거됨(response);
    }
}
