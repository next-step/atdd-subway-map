package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.dto.LineRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.utils.LineUtils.*;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * Given 상행 종점역 생성을 요청하고
     * Given 하행 종점역 생성을 요청하고
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 및 구간 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        LineRequest request = LineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("신도림")
                .downStationName("문래")
                .distance(7)
                .build();

        // when
        ExtractableResponse<Response> response = 지하철노선_생성요청(request);

        // then
        지하철노선_생성_성공(response);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성하면 안된다")
    @Test
    void duplicateLineName() {
        // given
        LineRequest request = LineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("신도림")
                .downStationName("문래")
                .distance(7)
                .build();
        지하철노선_생성요청(request);

        // when
        LineRequest request2 = LineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("영등포구청역")
                .downStationName("당산역")
                .distance(7)
                .build();
        ExtractableResponse<Response> response = 지하철노선_생성요청(request2);

        // then
        지하철노선_생성_실패(response);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        LineRequest request = LineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("신도림")
                .downStationName("문래")
                .distance(7)
                .build();
        지하철노선_생성요청(request);

        LineRequest request2 = LineRequest.builder()
                .lineName("1호선")
                .lineColor("bg-blue")
                .upStationName("부천역")
                .downStationName("소사역")
                .distance(5)
                .build();
        지하철노선_생성요청(request2);

        // when
        ExtractableResponse<Response> response = 지하철노선_목록조회_요청();

        // then
        지하철노선목록_조회_성공(response);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() throws Exception{
        // given
        LineRequest request = LineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("신도림")
                .downStationName("문래")
                .distance(7)
                .build();

        ExtractableResponse<Response> 지하철노선_생성응답 = 지하철노선_생성요청(request);
        Long 지하철노선_코드 = 지하철노선_생성응답.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철노선_단건조회_요청(지하철노선_코드);

        // then
        지하철노선_단건조회_성공(response);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        LineRequest request = LineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("신도림")
                .downStationName("문래")
                .distance(7)
                .build();

        ExtractableResponse<Response> 지하철노선_생성응답 = 지하철노선_생성요청(request);
        Long 지하철노선_코드 = 지하철노선_생성응답.jsonPath().getLong("id");

        Map<String, String> params = new HashMap<>();
        params.put("name", "신1호선");
        params.put("color", "bg-blue-200");

        // when
        ExtractableResponse<Response> response = 지하철노선_수정_요청(지하철노선_코드, params);

        // then
        지하철노선_수정_성공(response, params);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        LineRequest request = LineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("신도림")
                .downStationName("문래")
                .distance(7)
                .build();

        ExtractableResponse<Response> 지하철노선_생성응답 = 지하철노선_생성요청(request);

        // when
        ExtractableResponse<Response> response = 지하철노선_삭제_요청(지하철노선_생성응답.header("Location"));

        // then
        지하철노선_삭제_성공(response);
    }
}
