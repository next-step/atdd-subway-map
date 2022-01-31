package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.response.LineResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.utils.LineSteps;
import nextstep.subway.utils.SectionSteps;
import nextstep.subway.utils.StationSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    String 노선_9호선 = "9호선";
    String 색상_9호선 = "갈색";

    String 노선_5호선 = "5호선";
    String 색상_5호선 = "보라색";

    String 가양역 = "가양역";
    String 증미역 = "증미역";
    String 까치산역 = "까치산역";
    String 화곡역 = "화곡역";

    /**
     * Scenario 지하철 노선 생성
     * Given 지하철역 생성을 요청하고,
     * Given 새로운 지하철역 생성을 요청한다.
     * When 지하철 노선 생성을 요청하면,
     * Then 지하철 노선 생성이 성공한다.
     * Then 생성된 노선에는 상행 종점과 하행 종점 번호와 번호 간 거리도 생성된다.
     */
    @Test
    void 지하철_노선_생성() {
        // given
        ExtractableResponse<Response> 가양역_생성_응답 = StationSteps.지하철역_생성_요청(가양역);
        Long 가양역_ID = StationSteps.getStationId(가양역_생성_응답);

        ExtractableResponse<Response> 증미역_생성_응답 = StationSteps.지하철역_생성_요청(증미역);
        Long 증미역_ID = StationSteps.getStationId(증미역_생성_응답);

        int distance = 10;

        // when
        ExtractableResponse<Response> 지하철_노선_생성_응답 =
                LineSteps.지하철_노선_생성_요청(노선_9호선, 색상_9호선, 가양역_ID, 증미역_ID, distance);

        // then
        assertThat(지하철_노선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(지하철_노선_생성_응답.header("Location")).isEqualTo("/lines/1");
    }

    /**
     * Given 지하철역 생성을 요청하고,
     *   AND 새로운 지하철역 생성을 요청하고,
     *   AND 지하철 노선 생성을 요청 하고,
     * Given 새로운 지하철역 생성을 요청하고,
     *   AND 새로운 지하철역 생성을 요청하고,
     *   AND 지하철 노선 생성을 요청 하고,
     *  When 지하철 노선 목록 조회를 요청 하면
     *  Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @Test
    void 지하철_노선_목록_조회() {
        // given
        ExtractableResponse<Response> 가양역_생성_응답 = StationSteps.지하철역_생성_요청(가양역);
        Long 가양역_ID = StationSteps.getStationId(가양역_생성_응답);

        ExtractableResponse<Response> 증미역_생성_응답 = StationSteps.지하철역_생성_요청(증미역);
        Long 증미역_ID = StationSteps.getStationId(증미역_생성_응답);

        ExtractableResponse<Response> 화곡역_생성_응답 = StationSteps.지하철역_생성_요청(화곡역);
        Long 화곡역_ID = StationSteps.getStationId(화곡역_생성_응답);

        ExtractableResponse<Response> 까치산역_생성_응답 = StationSteps.지하철역_생성_요청(까치산역);
        Long 까치산역_ID = StationSteps.getStationId(까치산역_생성_응답);

        int distance = 10;

        LineSteps.지하철_노선_생성_요청(노선_9호선, 색상_9호선, 가양역_ID, 증미역_ID, distance);
        LineSteps.지하철_노선_생성_요청(노선_5호선, 색상_5호선, 화곡역_ID, 까치산역_ID, distance);

        // when
        ExtractableResponse<Response> 지하철_노선_목록_조회_응답 = LineSteps.지하철_노선_목록_조회_요청();

        // then
        assertThat(지하철_노선_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> lineNames = 지하철_노선_목록_조회_응답.jsonPath().getList("name");
        List<String> lineColors = 지하철_노선_목록_조회_응답.jsonPath().getList("color");
        List<String> expectedLineNames = asList(노선_9호선, 노선_5호선);
        List<String> expectedLineColors = asList(색상_9호선, 색상_5호선);
        assertThat(lineNames).containsAll(expectedLineNames);
        assertThat(lineColors).containsAll(expectedLineColors);
    }

    /**
     * Given 지하철역 생성을 요청하고,
     *   AND 새로운 지하철역 생성을 요청하고,
     *   AND 지하철 노선 생성을 요청 하고,
     *   AND 새로운 지하철역 생성을 요청하고,
     *   AND 기존의 지하철 노선에 구간 등록을 요청하고,
     *  When 생성한 지하철 노선 조회를 요청 하면
     *  Then 생성한 지하철 노선에 해당하는 역을 구간 기준 오름차순으로 응답한다.
     */
    @Test
    void 지하철_노선_조회() {
        // given
        ExtractableResponse<Response> 가양역_생성_응답 = StationSteps.지하철역_생성_요청(가양역);
        Long 가양역_ID = StationSteps.getStationId(가양역_생성_응답);

        ExtractableResponse<Response> 증미역_생성_응답 = StationSteps.지하철역_생성_요청(증미역);
        Long 증미역_ID = StationSteps.getStationId(증미역_생성_응답);

        int distance = 10;

        ExtractableResponse<Response> 지하철_노선_생성_응답 = LineSteps.지하철_노선_생성_요청(노선_9호선, 색상_9호선, 가양역_ID, 증미역_ID, distance);
        Long lineId = LineSteps.getLineId(지하철_노선_생성_응답);

        ExtractableResponse<Response> 화곡역_생성_응답 = StationSteps.지하철역_생성_요청(화곡역);
        Long 화곡역_ID = StationSteps.getStationId(화곡역_생성_응답);
        int distance2 = 5;
        SectionSteps.지하철_구간_생성_요청(lineId, 증미역_ID, 화곡역_ID, distance2);

        // when
        ExtractableResponse<Response> 지하철_노선_조회_응답 = LineSteps.지하철_노선_조회_요청(lineId);

        // then
        assertThat(지하철_노선_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        String lineName = 지하철_노선_조회_응답.jsonPath().getString("name");
        assertThat(lineName).isEqualTo(노선_9호선);

        String lineColor = 지하철_노선_조회_응답.jsonPath().getString("color");
        assertThat(lineColor).isEqualTo(색상_9호선);

        List<Station> stations = 지하철_노선_조회_응답.jsonPath().getList("stations", Station.class);
        assertThat(stations.size()).isEqualTo(3);
    }

    /**
     * Given 지하철역 생성을 요청하고,
     * Given 새로운 지하철역 생성을 요청하고,
     * When 지하철 노선도를 조회하면,
     * Then 지하철 노선 조회가 실패한다.
     */
    @Test
    void 존재하지_않는_노선_조회() {
        // given
        ExtractableResponse<Response> 가양역_생성_응답 = StationSteps.지하철역_생성_요청(가양역);
        Long 가양역_ID = StationSteps.getStationId(가양역_생성_응답);

        ExtractableResponse<Response> 증미역_생성_응답 = StationSteps.지하철역_생성_요청(증미역);
        Long 증미역_ID = StationSteps.getStationId(증미역_생성_응답);

        int distance = 10;

        // given
        LineSteps.지하철_노선_생성_요청(노선_9호선, 색상_9호선, 가양역_ID, 증미역_ID, distance);

        // when
        Long lineId = 2L;
        ExtractableResponse<Response> 지하철_노선_조회_응답 = LineSteps.지하철_노선_조회_요청(lineId);

        // then
        assertThat(지하철_노선_조회_응답.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(지하철_노선_조회_응답.jsonPath().getString("message"))
                .isEqualTo(String.format(LineService.LINE_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, lineId));
    }

    /**
     * Given 지하철역 생성을 요청하고,
     * Given 새로운 지하철역 생성을 요청하고,
     * Given 지하철 노선 생성을 요청 하고,
     * When 지하철 노선의 정보 수정을 요청 하면,
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @Test
    void 지하철_노선_수정() {
        // given
        ExtractableResponse<Response> 가양역_생성_응답 = StationSteps.지하철역_생성_요청(가양역);
        Long 가양역_ID = StationSteps.getStationId(가양역_생성_응답);

        ExtractableResponse<Response> 증미역_생성_응답 = StationSteps.지하철역_생성_요청(증미역);
        Long 증미역_ID = StationSteps.getStationId(증미역_생성_응답);

        int distance = 10;
        ExtractableResponse<Response> 지하철_노선_생성_응답 = LineSteps.지하철_노선_생성_요청(노선_9호선, 색상_9호선, 가양역_ID, 증미역_ID, distance);


        // when
        Long lineId = LineSteps.getLineId(지하철_노선_생성_응답);
        ExtractableResponse<Response> 지하철_노선_수정_응답 = LineSteps.지하철_노선_수정_요청(lineId, 노선_5호선, 색상_5호선);

        // then
        assertThat(지하철_노선_수정_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> 지하철_노선_조회_응답 = LineSteps.지하철_노선_조회_요청(lineId);
        String lineName = 지하철_노선_조회_응답.jsonPath().getString("name");
        String lineColor = 지하철_노선_조회_응답.jsonPath().getString("color");
        assertThat(lineName).isEqualTo(노선_5호선);
        assertThat(lineColor).isEqualTo(색상_5호선);
    }

    /**
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보가 수정되지 않는다.
     */
    @Test
    void 존재하지_않는_노선_수정() {
        // when
        Long lineId = 2L;
        ExtractableResponse<Response> 지하철_노선_수정_응답 = LineSteps.지하철_노선_수정_요청(lineId, 노선_5호선, 색상_5호선);

        // then
        assertThat(지하철_노선_수정_응답.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(지하철_노선_수정_응답.jsonPath().getString("message"))
                .isEqualTo(String.format(LineService.LINE_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, lineId));
    }

    /**
     * Given 지하철역 생성을 요청하고,
     * Given 새로운 지하철역 생성을 요청하고,
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @Test
    void 지하철_노선_삭제() {
        // given
        ExtractableResponse<Response> 가양역_생성_응답 = StationSteps.지하철역_생성_요청(가양역);
        Long 가양역_ID = StationSteps.getStationId(가양역_생성_응답);

        ExtractableResponse<Response> 증미역_생성_응답 = StationSteps.지하철역_생성_요청(증미역);
        Long 증미역_ID = StationSteps.getStationId(증미역_생성_응답);

        int distance = 10;

        ExtractableResponse<Response> 지하철_노선_생성_응답 = LineSteps.지하철_노선_생성_요청(노선_9호선, 색상_9호선, 가양역_ID, 증미역_ID, distance);

        // when
        Long lineId = LineSteps.getLineId(지하철_노선_생성_응답);
        ExtractableResponse<Response> 지하철_노선_삭제_응답 = LineSteps.지하철_노선_삭제_요청(lineId);

        // then
        assertThat(지하철_노선_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> 지하철_노선_조회_응답 = LineSteps.지하철_노선_조회_요청(lineId);
        assertThat(지하철_노선_조회_응답.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 지하철역 생성을 요청하고,
     * Given 새로운 지하철역 생성을 요청하고,
     * Given 지하철 노선 생성을 요청하고,
     * When 같은 이름으로 지하철 노선 생성을 요청하면,
     * Then 지하철 노선 생성이 실패한다.
     */
    @Test
    void 중복_이름으로_지하철_노선_생성() {
        // given
        ExtractableResponse<Response> 가양역_생성_응답 = StationSteps.지하철역_생성_요청(가양역);
        Long 가양역_ID = StationSteps.getStationId(가양역_생성_응답);

        ExtractableResponse<Response> 증미역_생성_응답 = StationSteps.지하철역_생성_요청(증미역);
        Long 증미역_ID = StationSteps.getStationId(증미역_생성_응답);

        int distance = 10;

        LineSteps.지하철_노선_생성_요청(노선_9호선, 색상_9호선, 가양역_ID, 증미역_ID, distance);

        // when
        ExtractableResponse<Response> 지하철_노선_중복_생성_응답 = LineSteps.지하철_노선_생성_요청(노선_9호선, 색상_9호선, 가양역_ID, 증미역_ID, distance);

        // then
        assertThat(지하철_노선_중복_생성_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(지하철_노선_중복_생성_응답.jsonPath().getString("message"))
                .isEqualTo(String.format(LineService.LINE_DUPLICATE_REGISTRATION_EXCEPTION_MESSAGE, 노선_9호선));
    }
}
