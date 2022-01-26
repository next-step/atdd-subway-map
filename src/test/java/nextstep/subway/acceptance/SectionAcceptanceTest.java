package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static nextstep.subway.acceptance.LineStepDefinition.*;
import static nextstep.subway.acceptance.SectionStepDefinition.*;

@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * Given 지하철 노선을 생성하고
     * Given 하행 지하철역 생성하고
     * When 지하철 구간 생성을 요청하면
     * Then 지하철 구간 생성이 성공한다.
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void createSection() {
        // given
        Long 강남역 = 상행_지하철역_생성_요청("강남역");
        Long 양재역 = 하행_지하철역_생성_요청("양재역");
        Map<String, String> lineParams = 지하철_노선_파라미터_생성(
                "신분당선",
                "bg-red-600",
                강남역,
                양재역,
                10);
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성_요청(lineParams);
        Long lineId = lineResponse.body().jsonPath().getLong("id");

        Long 양재시민의숲역 = 하행_지하철역_생성_요청("양재시민의숲역");

        // when
        Map<String, String> params = 지하철_구간_파라미터_생성(양재역, 양재시민의숲역, 10);
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(lineId, params);

        // then
        지하철_구간_생성_완료(response);
    }

    /**
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * Given 지하철 노선을 생성하고
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * When 노선에 등록되어있는 하행 종점역과 다른 상행 지하철역으로 지하철 구간 생성을 요청하면
     * Then 지하철 구간 생성이 실패한다.
     */
    @DisplayName("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다")
    @Test
    void createSectionWithInvalidUpStation() {
        // given
        Long 강남역 = 상행_지하철역_생성_요청("강남역");
        Long 양재역 = 하행_지하철역_생성_요청("양재역");
        Map<String, String> lineParams = 지하철_노선_파라미터_생성(
                "신분당선",
                "bg-red-600",
                강남역,
                양재역,
                10);
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성_요청(lineParams);
        Long lineId = lineResponse.body().jsonPath().getLong("id");

        Long 청계산입구역 = 상행_지하철역_생성_요청("청계산입구역");
        Long 양재시민의숲역 = 하행_지하철역_생성_요청("양재시민의숲역");

        // when
        Map<String, String> params = 지하철_구간_파라미터_생성(청계산입구역, 양재시민의숲역, 10);
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(lineId, params);

        // then
        지하철_구간_생성_실패(response);
    }

    /**
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * Given 지하철 노선을 생성하고
     * When 노선에 이미 등록되어있는 역을 하행역으로 갖는 지하철 구간 생성을 요청하면
     * Then 지하철 구간 생성이 실패한다.
     */
    @DisplayName("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다")
    @Test
    void createSectionWithInvalidDownStation() {
        // given
        Long 강남역 = 상행_지하철역_생성_요청("강남역");
        Long 양재역 = 하행_지하철역_생성_요청("양재역");
        Map<String, String> lineParams = 지하철_노선_파라미터_생성(
                "신분당선",
                "bg-red-600",
                강남역,
                양재역,
                10);
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성_요청(lineParams);
        Long lineId = lineResponse.body().jsonPath().getLong("id");

        // when
        Map<String, String> params = 지하철_구간_파라미터_생성(양재역, 강남역, 10);
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(lineId, params);

        // then
        지하철_구간_생성_실패(response);
    }
}
