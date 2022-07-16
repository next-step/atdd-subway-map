package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.net.URI;

import static nextstep.subway.acceptance.TestSetupUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @Test
    void 지하철_역_생성_인수테스트() {
        // when
        var 강남역 = 지하철_역_생성("강남역");

        // then
        assertThat(강남역.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        지하철_역이_존재한다("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    void 지하철_역_목록_조회_인수테스트() {
        // given
        var 강남역 = 지하철_역_생성("강남역");
        var 교대역 = 지하철_역_생성("교대역");

        assertThat(강남역.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(교대역.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when + then
        지하철_역이_존재한다("강남역", "교대역");
    }

    private void 지하철_역이_존재한다(String... stationName) {
        var 지하철_역들 = 지하철_역들을_조회한다();
        assertThat(지하철_역들.jsonPath().getList("name", String.class)).contains(stationName);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    void 지하철_역_제거_인수테스트() {
        // given
        var 강남역 = 지하철_역_생성("강남역");

        assertThat(강남역.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        long 강남역_id = 강남역.jsonPath().getLong("id");
        var 지하철_역_제거 = 지하철_역을_제거한다(강남역_id);

        assertThat(지하철_역_제거.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        지하철_역이_존재하지_않는다("강남역");
    }

    private ExtractableResponse<Response> 지하철_역을_제거한다(long stationId) {
        return RestAssured.given().log().all()
                .when().delete(URI.create("/stations/" + stationId))
                .then().log().all()
                .extract();
    }

    private void 지하철_역이_존재하지_않는다(String stationName) {
        ExtractableResponse<Response> stationsResponse = 지하철_역들을_조회한다();

        assertThat(stationsResponse.jsonPath().getList("name", String.class)).doesNotContain(stationName);
    }

    private ExtractableResponse<Response> 지하철_역들을_조회한다() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }
}
