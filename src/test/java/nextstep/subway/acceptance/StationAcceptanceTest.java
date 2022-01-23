package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.StationSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @Test
    void 지하철역_생성() {
        // when
        ExtractableResponse<Response> 지하철역_생성_응답 = StationSteps.지하철역_생성_요청("/stations", "강남역");

        // then
        assertThat(지하철역_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
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
        StationSteps.지하철역_생성_요청("/stations", "강남역");
        StationSteps.지하철역_생성_요청("/stations", "역삼역");

        // when
        ExtractableResponse<Response> 지하철역_목록_조회_응답 = StationSteps.지하철역_목록_조회_요청("/stations");

        assertThat(지하철역_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = 지하철역_목록_조회_응답.jsonPath().getList("name");
        assertThat(stationNames).contains("강남역", "역삼역");
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @Test
    void 지하철역_삭제() {
        // given
        ExtractableResponse<Response> 지하철역_생성_응답 = StationSteps.지하철역_생성_요청("/stations", "강남역");

        // when
        String url = 지하철역_생성_응답.header("Location");
        ExtractableResponse<Response> 지하철역_삭제_응답 = StationSteps.지하철역_삭제_요청(url);

        // then
        assertThat(지하철역_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
