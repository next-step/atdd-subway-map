package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.testsupport.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.support.StationRequest.지하철역_목록조회_요청;
import static nextstep.subway.acceptance.support.StationRequest.지하철역_삭제_요청;
import static nextstep.subway.acceptance.support.StationRequest.지하철역_생성_요청;
import static nextstep.subway.acceptance.support.StationRequest.지하철역_생성_요청후_식별자_반환;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> 지하철역_생성_응답 = 지하철역_생성_요청("강남역");

        // then
        assertThat(지하철역_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        지하철_목록조회후_생성한역_확인("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        지하철역_생성_요청("기흥역");
        지하철역_생성_요청("신갈역");

        // when & then
        지하철_목록조회후_생성한역_확인("기흥역", "신갈역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        long 지하철역 = 지하철역_생성_요청후_식별자_반환("기흥역");

        // when
        지하철역_삭제_요청(지하철역);

        // then
        지하철_목록조회후_제거된역_확인("기흥역");
    }

    private void 지하철_목록조회후_제거된역_확인(String... stationName) {
        ExtractableResponse<Response> getResponse = 지하철역_목록조회_요청();
        List<String> stationNames = getResponse.jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain(stationName);
    }

    private void 지하철_목록조회후_생성한역_확인(String... stationName) {
        List<String> stationNames = 지하철역_목록조회_요청().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsExactlyInAnyOrder(stationName);
    }
}