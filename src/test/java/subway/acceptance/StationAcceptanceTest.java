package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.fixture.FieldFixture.식별자_아이디;
import static subway.common.fixture.FieldFixture.역_이름;
import static subway.common.fixture.StationFixture.강남역;
import static subway.common.fixture.StationFixture.서울대입구역;
import static subway.common.util.JsonPathUtil.리스트로_데이터_추출;
import static subway.common.util.JsonPathUtil.문자열로_데이터_추출;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> 지하철역_생성_결과 = 지하철역_생성_요청(강남역.요청_데이터_생성());

        // then
        assertThat(지하철역_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(리스트로_데이터_추출(지하철역_목록_조회_요청(), 역_이름)).containsAnyOf(강남역.역_이름());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void selectStations() {
        // given
        지하철역_생성_요청(강남역.요청_데이터_생성());
        지하철역_생성_요청(서울대입구역.요청_데이터_생성());

        // when
        ExtractableResponse<Response> 지하철역_목록_조회_결과 = 지하철역_목록_조회_요청();

        // then
        assertThat(리스트로_데이터_추출(지하철역_목록_조회_결과, 역_이름)).contains(강남역.역_이름(), 서울대입구역.역_이름());
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        String 생성된_지하철역_id = 문자열로_데이터_추출(지하철역_생성_요청(강남역.요청_데이터_생성()), 식별자_아이디);

        // when
        지하철역_삭제_요청(생성된_지하철역_id);

        // then
        assertThat(리스트로_데이터_추출(지하철역_목록_조회_요청(), 역_이름)).doesNotContain(강남역.역_이름());
    }
}
