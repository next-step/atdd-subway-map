package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jdk.nashorn.internal.ir.annotations.Ignore;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static nextstep.subway.station.StationSteps.지하철_역_생성_요청;

@DisplayName("지하철 노선 관련 기능")
@Ignore
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(노선_2호선());

        // then
        지하철_노선_생성됨(response);
    }


    @DisplayName("기존에 존재하는 노선 이름으로 노선을 생성한다.")
    @Test
    void createLineWithDuplicatedName() {
        // given
        지하철_노선_생성_요청(노선_2호선());

        // when
        ExtractableResponse<Response> response =  지하철_노선_생성_요청(노선_2호선_중복());

        // then
        지하철_노선_생성_실패(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_생성_요청(노선_2호선());
        ExtractableResponse<Response> createResponse2 = 지하철_노선_생성_요청(노선_1호선());

        // when
        ExtractableResponse<Response> getResponse = 지하철_노선_목록_조회_요청();

        // then
        지하철_생성한_노선_목록_조회_성공(Stream.of(createResponse1, createResponse2), getResponse);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(노선_2호선());

        // when
        Long createId = parseIdFromResponseHeader(createResponse);
        ExtractableResponse<Response> getResponse = 노선_ID로_조회(createId);

        // then
        지하철_생성한_노선_조회_성공(createResponse, getResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(노선_2호선());
        Long createId = parseIdFromResponseHeader(createResponse);

        // when
        ExtractableResponse<Response> modifyResponse = 지하철_노선_수정_요청(노선_1호선(), createId);

        // then
        지하철_노선_수정_성공(modifyResponse);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(노선_2호선());
        Long createId = parseIdFromResponseHeader(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선_제거_요청(createId);

        // then
        지하철_노선_제거_성공(deleteResponse);
    }

    private static Map<String, Object> 노선_2호선() {
        ExtractableResponse<Response> stationResponse1 = 지하철_역_생성_요청(강남역);
        Long upStationId = parseIdFromResponseHeader(stationResponse1);
        ExtractableResponse<Response> stationResponse2 = 지하철_역_생성_요청(역삼역);
        Long downStationId = parseIdFromResponseHeader(stationResponse2);
        Map<String, Object> params = makeLine("2호선","bg-green-600", upStationId, downStationId,7);
        return params;
    }

    private static Map<String, Object> 노선_2호선_중복() {
        ExtractableResponse<Response> stationResponse1 = 지하철_역_생성_요청(선릉역);
        Long upStationId = parseIdFromResponseHeader(stationResponse1);
        ExtractableResponse<Response> stationResponse2 = 지하철_역_생성_요청(삼성역);
        Long downStationId = parseIdFromResponseHeader(stationResponse2);
        Map<String, Object> params = makeLine("2호선","bg-green-600", upStationId, downStationId,7);
        return params;
    }

    private static Map<String, Object> 노선_1호선() {
        ExtractableResponse<Response> stationResponse1 = 지하철_역_생성_요청(시청역);
        Long upStationId = parseIdFromResponseHeader(stationResponse1);
        ExtractableResponse<Response> stationResponse2 = 지하철_역_생성_요청(서울역);
        Long downStationId = parseIdFromResponseHeader(stationResponse2);
        Map<String, Object> params = makeLine("1호선","bg-blue-600", upStationId, downStationId,7);
        return params;
    }

    public static Map<String, Object> makeLine(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

}
