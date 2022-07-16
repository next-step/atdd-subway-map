package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

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
        ExtractableResponse<Response> 강남역 = TestSetupUtils.buildStation("강남역");

        assertThat(강남역.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> stationsResponse = stationsResponse();
        assertThat(stationsResponse.jsonPath().getList("name")).contains("강남역");
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
        ExtractableResponse<Response> 강남역 = TestSetupUtils.buildStation("강남역");
        ExtractableResponse<Response> 교대역 = TestSetupUtils.buildStation("교대역");

        assertThat(강남역.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(교대역.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> stationsResponse = stationsResponse();

        // then
        assertThat(stationsResponse.jsonPath().getList("name", String.class))
                .containsExactly("강남역", "교대역");
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
        ExtractableResponse<Response> 강남역 = TestSetupUtils.buildStation("강남역");

        assertThat(강남역.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        long stationId = 강남역.jsonPath().getLong("id");
        ExtractableResponse<Response> deleteStationResponse = deleteStation(stationId);

        assertThat(deleteStationResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> stationsResponse = stationsResponse();
        assertThat(stationsResponse.jsonPath().getList("name")).doesNotContain("강남역");
    }

    private ExtractableResponse<Response> stationsResponse() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteStation(long stationId) {
        return RestAssured.given().log().all()
                .when().delete(URI.create("/stations/" + stationId))
                .then().log().all()
                .extract();
    }
}
