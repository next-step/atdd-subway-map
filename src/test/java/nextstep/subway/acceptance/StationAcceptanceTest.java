package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.utils.StationSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    String 가양역 = "가양역";
    String 화곡역 = "화곡역";

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @Test
    void 지하철역_생성() {
        // when
        ExtractableResponse<Response> 지하철역_생성_응답 = StationSteps.지하철역_생성_요청("가양역");

        // then
        assertThat(지하철역_생성_응답.statusCode()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(지하철역_생성_응답.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
     */
    @Test
    void 지하철역_목록_조회() {
        /// given
        StationSteps.지하철역_생성_요청(가양역);
        StationSteps.지하철역_생성_요청(화곡역);

        // when
        ExtractableResponse<Response> 지하철역_목록_조회_응답 = StationSteps.지하철역_목록_조회_요청();

        assertThat(지하철역_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = 지하철역_목록_조회_응답.jsonPath().getList("name");
        assertThat(stationNames).contains(가양역, 화곡역);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @Test
    void 지하철역_삭제() {
        // given
        ExtractableResponse<Response> 지하철역_생성_응답 = StationSteps.지하철역_생성_요청(가양역);

        // when
        String stationId = StationSteps.getStationId(지하철역_생성_응답);
        ExtractableResponse<Response> 지하철역_삭제_응답 = StationSteps.지하철역_삭제_요청(stationId);

        // then
        assertThat(지하철역_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * When 지하철 노선의 정보 삭제을 요청 하면
     * Then 지하철 노선이 삭제되지 않는다.
     */
    @Test
    void 존재하지_않는_지하철역_삭제() {
        // when
        String stationId = "2";
        ExtractableResponse<Response> 지하철역_삭제_응답 = StationSteps.지하철역_삭제_요청(stationId);

        // then
        assertThat(지하철역_삭제_응답.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(지하철역_삭제_응답.jsonPath().getString("message"))
                .isEqualTo(String.format(StationService.STATION_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, stationId));
    }

    /**
     * Given 지하철역 생성을 요청하고,
     * When 같은 이름으로 지하철약 생성을 요청하면,
     * Then 지하철역 생성이 실패한다.
     */
    @Test
    void 중복_이름으로_지하철역_생성() {
        // given
        StationSteps.지하철역_생성_요청(가양역);

        // when
        ExtractableResponse<Response> 지하철역_중복_생성_응답 = StationSteps.지하철역_생성_요청(가양역);

        // then
        assertThat(지하철역_중복_생성_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(지하철역_중복_생성_응답.jsonPath().getString("message"))
                .isEqualTo(String.format(StationService.STATION_DUPLICATE_REGISTRATION_EXCEPTION_MESSAGE, 가양역));
    }
}
