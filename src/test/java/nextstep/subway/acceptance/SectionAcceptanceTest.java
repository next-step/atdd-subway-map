package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.Station;
import nextstep.subway.utils.LineSteps;
import nextstep.subway.utils.SectionSteps;
import nextstep.subway.utils.StationSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    String 노선_9호선 = "9호선";
    String 색상_9호선 = "갈색";
    String 가양역 = "가양역";
    String 증미역 = "증미역";
    String 등촌역 = "등촌역";
    /**
     * Scenario 지하철 구간 생성
     *    Given 지하철역 생성을 요청하고,
     *      AND 새로운 지하철역 생성을 요청하고,
     *      AND 지하철 노선 생성을 요청한다.
     *      AND 새로운 지하철역 생성을 요청하고,
     *     When 기존의 지하철 노선에 구간 등록을 요청하면
     *     Then 상행역은 해당 노선에 등록되어있는 하행 종점역으로 등록된다.
     */
    @Test
    void 지하철_구간_생성() {
        // given
        ExtractableResponse<Response> 가양역_생성_응답 = StationSteps.지하철역_생성_요청(가양역);
        Long 가양역_ID = StationSteps.getStationId(가양역_생성_응답);

        ExtractableResponse<Response> 증미역_생성_응답 = StationSteps.지하철역_생성_요청(증미역);
        Long 증미역_ID = StationSteps.getStationId(증미역_생성_응답);
        int distance1 = 10;

        ExtractableResponse<Response> 지하철_노선_생성_응답 =
                LineSteps.지하철_노선_생성_요청(노선_9호선, 색상_9호선, 가양역_ID, 증미역_ID, distance1);

        ExtractableResponse<Response> 등촌역_생성_응답 = StationSteps.지하철역_생성_요청(등촌역);
        Long 등촌역_ID = StationSteps.getStationId(등촌역_생성_응답);
        int distance2 = 5;

        // when
        Long lineId = LineSteps.getLineId(지하철_노선_생성_응답);
        Long upStationId = 증미역_ID;
        Long downStationId = 등촌역_ID;

        ExtractableResponse<Response> 노선_9호선_2구간_응답 =
                SectionSteps.지하철_구간_생성_요청(lineId, upStationId, downStationId, distance2);

        // then
        assertThat(노선_9호선_2구간_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Long expectedLineId = 노선_9호선_2구간_응답.jsonPath().getLong("lineId");
        Long expectedUpStationId = 노선_9호선_2구간_응답.jsonPath().getObject("upStation", Station.class).getId();
        Long expectedDownStationId = 노선_9호선_2구간_응답.jsonPath().getObject("downStation", Station.class).getId();
        int expectedDistance = 노선_9호선_2구간_응답.jsonPath().getInt("distance");
        assertThat(expectedLineId).isEqualTo(lineId);
        assertThat(expectedUpStationId).isEqualTo(upStationId);
        assertThat(expectedDownStationId).isEqualTo(downStationId);
        assertThat(expectedDistance).isEqualTo(distance2);
    }

    /**
     * Scenario 지하철 구간 삭제
     *    Given 지하철역 생성을 요청하고,
     *      AND 새로운 지하철역 생성을 요청하고,
     *      AND 지하철 노선 생성을 요청하고,
     *      AND 새로운 지하철역 생성을 요청하고,
     *      AND 구간 등록을 요청한다.
     *     When 구간 삭제를 요청한다.
     *     Then 하행 종점역이 제거된다.
     */
    @Test
    void 지하철_구간_삭제() {
        // given
        ExtractableResponse<Response> 가양역_생성_응답 = StationSteps.지하철역_생성_요청(가양역);
        Long 가양역_ID = StationSteps.getStationId(가양역_생성_응답);

        ExtractableResponse<Response> 증미역_생성_응답 = StationSteps.지하철역_생성_요청(증미역);
        Long 증미역_ID = StationSteps.getStationId(증미역_생성_응답);
        int distance1 = 10;

        ExtractableResponse<Response> 지하철_노선_생성_응답 =
                LineSteps.지하철_노선_생성_요청(노선_9호선, 색상_9호선, 가양역_ID, 증미역_ID, distance1);

        ExtractableResponse<Response> 등촌역_생성_응답 = StationSteps.지하철역_생성_요청(등촌역);
        Long 등촌역_ID = StationSteps.getStationId(등촌역_생성_응답);
        int distance2 = 5;

        Long lineId = LineSteps.getLineId(지하철_노선_생성_응답);

        SectionSteps.지하철_구간_생성_요청(lineId, 증미역_ID, 등촌역_ID, distance2);

        // when
        ExtractableResponse<Response> 지하철_구간_삭제_요청_응답 = SectionSteps.지하철_구간_삭제_요청(lineId, 등촌역_ID);

        // then
        assertThat(지하철_구간_삭제_요청_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
