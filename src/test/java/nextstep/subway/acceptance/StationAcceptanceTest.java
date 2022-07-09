package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.caller.StationApiCaller;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

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

    StationApiCaller stationApiCaller = new StationApiCaller();

    @BeforeEach
    public void setUp() {
        stationApiCaller.setPort(port);
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
        makeStationAndVerify(Map.of("name", "역삼역"));

        // then
        List<String> stationNames = getStationNamesAndVerify();
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
        makeStationAndVerify(Map.of("name", "선릉역"));
        makeStationAndVerify(Map.of("name", "삼성역"));

        // when
        List<String> stationNames = getStationNamesAndVerify();

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
        long id = makeStationAndVerify(Map.of("name", "강남역")).jsonPath().getLong("id");

        // when
        deleteStationAndVerify(id);

        // then
        List<String> stationNames = getStationNamesAndVerify();
        assertThat(stationNames).doesNotContain("강남역");
    }

    ExtractableResponse<Response> makeStationAndVerify(Map<String, String> params) {
        ExtractableResponse<Response> response = stationApiCaller.createStation(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    List<String> getStationNamesAndVerify() {
        ExtractableResponse<Response> response = stationApiCaller.getStations();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.jsonPath().getList("name", String.class);
    }

    void deleteStationAndVerify(long id) {
        ExtractableResponse<Response> response = stationApiCaller.deleteStation(id);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
