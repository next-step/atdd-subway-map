package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static nextstep.subway.acceptance.LineStepDefinition.*;
import static nextstep.subway.acceptance.SectionStepDefinition.*;
import static nextstep.subway.acceptance.StationStepDefinition.*;

@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    Long 강남역;
    Long 양재역;
    Map<String, String> lineParams;
    ExtractableResponse<Response> lineResponse;
    Long lineId;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").body().jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").body().jsonPath().getLong("id");
        lineParams = 지하철_노선_파라미터_생성(
                "신분당선",
                "bg-red-600",
                강남역,
                양재역,
                10);
        lineResponse = 지하철_노선_생성_요청(lineParams);
        lineId = lineResponse.body().jsonPath().getLong("id");
    }

    /**
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * Given 지하철 노선을 생성하고
     * Given 하행 지하철역을 생성하고
     * When 지하철 구간 생성을 요청하면
     * Then 지하철 구간 생성이 성공한다.
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void createSection() {
        // given
        Long 양재시민의숲역 = 지하철역_생성_요청("양재시민의숲역").body().jsonPath().getLong("id");

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
     * Given 상행 지하철역을 생성하고
     * Given 하행 지하철역을 생성하고
     * When 노선에 등록되어있는 하행 종점역과 다른 상행 지하철역으로 지하철 구간 생성을 요청하면
     * Then 지하철 구간 생성이 실패한다.
     */
    @DisplayName("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다")
    @Test
    void createSectionWithInvalidUpStation() {
        // given
        Long 청계산입구역 = 지하철역_생성_요청("청계산입구역").body().jsonPath().getLong("id");
        Long 양재시민의숲역 = 지하철역_생성_요청("양재시민의숲역").body().jsonPath().getLong("id");

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
        // when
        Map<String, String> params = 지하철_구간_파라미터_생성(양재역, 강남역, 10);
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(lineId, params);

        // then
        지하철_구간_생성_실패(response);
    }

    /**
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * Given 지하철 노선을 생성하고
     * Given 하행 지하철역을 생성하고
     * Given 지하철 구간을 생성하고
     * When 지하철 구간 삭제를 요청하면
     * Then 지하철 구간 삭제가 성공한다.
     */
    @DisplayName("지하철 구간 삭제")
    @Test
    void deleteSection() {
        // given
        Long 양재시민의숲역 = 지하철역_생성_요청("양재시민의숲역").body().jsonPath().getLong("id");
        Map<String, String> params = 지하철_구간_파라미터_생성(양재역, 양재시민의숲역, 10);
        ExtractableResponse<Response> createResponse = 지하철_구간_생성_요청(lineId, params);
        Long downStationId = createResponse.body().jsonPath().getLong("downStationId");

        // when
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(lineId, downStationId);

                // then
        지하철_구간_삭제_완료(response);
    }

    /**
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * Given 지하철 노선을 생성하고
     * Given 하행 지하철역을 생성하고
     * Given 지하철 구간을 생성하고
     * When 하행 종점역이 아닌 역으로 지하철 구간 삭제를 요청하면
     * Then 지하철 구간 삭제가 실패한다.
     */
    @DisplayName("지하철 노선에 등록된 마지막 역(하행 종점역)만 제거할 수 있다")
    @Test
    void deleteSectionWithInvalidDownStation() {
        // given
        Long 양재시민의숲역 = 지하철역_생성_요청("양재시민의숲역").body().jsonPath().getLong("id");
        Map<String, String> params = 지하철_구간_파라미터_생성(양재역, 양재시민의숲역, 10);
        ExtractableResponse<Response> createResponse = 지하철_구간_생성_요청(lineId, params);

        // when
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(lineId, 양재역);

        // then
        지하철_구간_삭제_실패(response);
    }

    /**
     * Given 상행 지하철역 생성하고
     * Given 하행 지하철역 생성하고
     * Given 지하철 노선을 생성하고
     * When 구간 삭제를 요청하면
     * Then 지하철 구간 삭제가 실패한다.
     */
    @DisplayName("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다")
    @Test
    void deleteSectionWhenOnlyOne() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(lineId, 양재역);

        // then
        지하철_구간_삭제_실패(response);
    }
}
