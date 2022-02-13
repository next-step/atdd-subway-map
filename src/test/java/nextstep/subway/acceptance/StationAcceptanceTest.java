package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.fixture.StationFixture.*;
import static nextstep.subway.utils.httpresponse.Header.uri;
import static nextstep.subway.utils.httpresponse.Response.delete;
import static nextstep.subway.utils.httpresponse.Response.get;
import static nextstep.subway.utils.httpresponse.StatusCode.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given
        ExtractableResponse<Response> 생성결과 = 역_생성(강남역_이름);

        // then
        created(생성결과);
        assertThat(uri(생성결과)).isNotBlank();
    }

    /**
     * Given 지하철 역 생성을 요청하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철역명 중복일 때 지하철역 생성 실패")
    @Test
    void duplicateNameIsNotAllowed() {
        // given
        역_생성(강남역_이름);

        // when
        ExtractableResponse<Response> 중복생성_response = 역_생성(강남역_이름);

        // then
        badRequest(중복생성_response);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void getStations() {
        /// given
        역_생성(강남역_이름);
        역_생성(역삼역_이름);

        // when
        ExtractableResponse<Response> response = get("/stations");

        // then
        ok(response);
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(강남역_이름, 역삼역_이름);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> 생성결과 = 역_생성(강남역_이름);

        // when
        ExtractableResponse<Response> response = delete(uri(생성결과));

        // then
        noContent(response);
    }
}
