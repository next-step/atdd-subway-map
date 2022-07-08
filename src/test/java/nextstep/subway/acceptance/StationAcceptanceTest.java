package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    StationRepository stationRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        stationRepository.deleteAll();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    @Order(1)
    void createStation() {
        // when
        ExtractableResponse<Response> response = makeStation(Map.of("name", "역삼역"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = getStationNames();
        assertThat(stationNames).containsAnyOf("역삼역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    @Order(2)
    void getStations() {
        // when
        makeStation(Map.of("name", "선릉역"));
        makeStation(Map.of("name", "삼성역"));

        // when
        List<String> stationNames = getStationNames();

        // then
        assertThat(stationNames).hasSize(2).contains("삼성역", "선릉역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    @Order(3)
    void deleteStation() {
        // given
        Long id = makeStation(Map.of("name", "강남역")).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when().delete("/stations/{id}", id)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        List<String> stationNames = getStationNames();
        assertThat(stationNames).doesNotContain("강남역");
    }

    ExtractableResponse<Response> makeStation(Map<String, String> params) {
        return RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();
    }

    List<String> getStationNames() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }
}
