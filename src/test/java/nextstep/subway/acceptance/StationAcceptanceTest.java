package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends BaseAcceptance {

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        dataBaseCleaner.afterPropertiesSet();
        dataBaseCleaner.tableClear();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        final ExtractableResponse<Response> response = createStation("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역을 조회한다.")
    void getStations() {
        //given
        createStation("잠실역");
        createStation("대림역");

        //when
        final List<String> stationNames = RestAssured.given()
            .when().get("/stations")
            .then().log().all()
            .extract().jsonPath().getList("name", String.class);

        //then
        assertThat(stationNames).hasSize(2)
            .containsAnyOf("잠실역", "대림역");

    }


    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("지하철역을 제거한다.")
    void deleteStation() {
        //given
        createStation("잠실역");

        //when
        final ExtractableResponse<Response> deleteExtract = RestAssured.given().log().all()
            .when().delete("/stations/1")
            .then().log().all()
            .extract();

        assertThat(deleteExtract.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        final List<String> name = RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract().jsonPath().getList("name", String.class);

        assertThat(name).isNotIn("잠실역");
    }

}