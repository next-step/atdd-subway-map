package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import nextstep.subway.acceptance.client.StationClient;
import nextstep.subway.acceptance.util.JsonResponseConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    StationClient stationClient;

    @Autowired
    JsonResponseConverter responseConverter;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    @DirtiesContext
    void createStation() {
        // given
        String stationName = "강남역";

        // when
        stationClient.createStation(stationName);

        // then
        assertThat(responseConverter.convertToNames(stationClient.fetchStations()))
                .containsExactly(stationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    @DirtiesContext
    void getStations() {
        // given
        List<String> stationNames = List.of("강남역", "역삼역");

        // when
        stationClient.createStations(stationNames);

        // then
        assertThat(responseConverter.convertToNames(stationClient.fetchStations()))
                .hasSize(stationNames.size())
                .containsExactly(stationNames.toArray(String[]::new));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    @DirtiesContext
    void deleteStation() {
        // given
        String stationName = "강남역";
        Long stationId = responseConverter.convertToId(stationClient.createStation(stationName));

        // when
        stationClient.deleteStation(stationId);

        // then
        assertThat(responseConverter.convertToNames(stationClient.fetchStations()))
                .doesNotContain(stationName);
    }

}