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
    String 염창역 = "염창역";
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
     * Scenario 지하철 구간 생성 실패 - 상행역과 하행 종점역이 다른 경우
     *    Given 지하철역 생성을 요청하고,
     *      AND 새로운 지하철역 생성을 요청하고,
     *      AND 지하철 노선 생성을 요청한다.
     *    Given 지하철역 생성을 요청하고,
     *      AND 새로운 지하철역 생성을 요청하고,
     *     When 하행 종점역과 다른 상행역으로 구간 생성 요청을 하면,
     *     Then 구간 등록은 실패한다.
     */
    @Test
    void 지하철_구간_생성_실패_상행역과_하행_종점역이_다른_경우() {
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
        ExtractableResponse<Response> 염창역_생성_응답 = StationSteps.지하철역_생성_요청(염창역);
        Long 염창역_ID = StationSteps.getStationId(염창역_생성_응답);
        int distance2 = 5;

        // when
        Long lineId = LineSteps.getLineId(지하철_노선_생성_응답);
        ExtractableResponse<Response> 노선_9호선_2구간_응답 =
                SectionSteps.지하철_구간_생성_요청(lineId, 등촌역_ID, 염창역_ID, distance2);

        // then
        assertThat(노선_9호선_2구간_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Scenario 지하철 구간 생성 실패 - 새로운 구간의 하행역이 현재 등록되어 있는 구간의 역일 경우
     *    Given 지하철역 생성을 요청하고,
     *      AND 새로운 지하철역 생성을 요청하고,
     *      AND 지하철 노선 생성을 요청한다.
     *     When 신규 구간의 하행역을 이미 등록되어 있는 구간의 역으로 구간 생성 요청을 하면,
     *     Then 구간 등록은 실패한다.
     */
    @Test
    void 지하철_구간_생성_실패_새로운_구간의_하행역이_현재_등록되어_있는_구간의_역일_경우() {
        // given
        ExtractableResponse<Response> 가양역_생성_응답 = StationSteps.지하철역_생성_요청(가양역);
        Long 가양역_ID = StationSteps.getStationId(가양역_생성_응답);
        ExtractableResponse<Response> 증미역_생성_응답 = StationSteps.지하철역_생성_요청(증미역);
        Long 증미역_ID = StationSteps.getStationId(증미역_생성_응답);
        int distance = 10;

        ExtractableResponse<Response> 지하철_노선_생성_응답 =
                LineSteps.지하철_노선_생성_요청(노선_9호선, 색상_9호선, 가양역_ID, 증미역_ID, distance);

        // when
        Long lineId = LineSteps.getLineId(지하철_노선_생성_응답);
        ExtractableResponse<Response> 노선_9호선_2구간_응답 =
                SectionSteps.지하철_구간_생성_요청(lineId, 증미역_ID, 가양역_ID, distance);

        // then
        assertThat(노선_9호선_2구간_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

    /**
     * Scenario 지하철 구간 삭제 - 하행 종점역이 아닌 역을 제거할 경우
     *    Given 지하철역 생성을 요청하고,
     *      AND 새로운 지하철역 생성을 요청하고,
     *      AND 지하철 노선 생성을 요청하고,
     *      AND 새로운 지하철역 생성을 요청하고,
     *      AND 구간 등록을 요청한다.
     *     When 하행 종점역이 아닌 구간 삭제를 요청한다.
     *     Then 구간 삭제가 실패한다.
     */
    @Test
    void 지하철_구간_삭제_실패_하행_종점역이_아닌_역을_제거할_경우() {
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
        ExtractableResponse<Response> 지하철_구간_삭제_요청_응답 = SectionSteps.지하철_구간_삭제_요청(lineId, 증미역_ID);

        // then
        assertThat(지하철_구간_삭제_요청_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Scenario 지하철 구간 삭제 - 구간이 1개인 경우
     *    Given 지하철역 생성을 요청하고,
     *      AND 새로운 지하철역 생성을 요청하고,
     *      AND 지하철 노선 생성을 요청하고,
     *     When 구간 삭제 요청을 하면,
     *     Then 구간 삭제가 실패한다.
     */
    @Test
    void 지하철_구간_삭제_실패_구간이_1개인_경우() {
        // given
        ExtractableResponse<Response> 가양역_생성_응답 = StationSteps.지하철역_생성_요청(가양역);
        Long 가양역_ID = StationSteps.getStationId(가양역_생성_응답);

        ExtractableResponse<Response> 증미역_생성_응답 = StationSteps.지하철역_생성_요청(증미역);
        Long 증미역_ID = StationSteps.getStationId(증미역_생성_응답);
        int distance1 = 10;

        ExtractableResponse<Response> 지하철_노선_생성_응답 =
                LineSteps.지하철_노선_생성_요청(노선_9호선, 색상_9호선, 가양역_ID, 증미역_ID, distance1);

        Long lineId = LineSteps.getLineId(지하철_노선_생성_응답);

        // when
        ExtractableResponse<Response> 지하철_구간_삭제_요청_응답 = SectionSteps.지하철_구간_삭제_요청(lineId, 증미역_ID);

        // then
        assertThat(지하철_구간_삭제_요청_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
