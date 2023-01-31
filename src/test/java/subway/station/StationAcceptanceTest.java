package subway.station;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StationAcceptanceTest {
    private static final String TEST_STATION1_NAME = "지하철역1";
    private static final String TEST_STATION2_NAME = "지하철역2";

    private final StationApiClient stationApiClient = new StationApiClient();

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = stationApiClient.createStation(TEST_STATION1_NAME);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = stationApiClient.getStations()
            .jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(TEST_STATION1_NAME);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 2개를 생성한다.")
    @Test
    void createTwoStations() {
        // given
        stationApiClient.createStation(TEST_STATION1_NAME);
        stationApiClient.createStation(TEST_STATION2_NAME);

        // when
        ExtractableResponse<Response> response = stationApiClient.getStations();

        // then
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).containsExactly(TEST_STATION1_NAME, TEST_STATION2_NAME);
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
        long stationId = stationApiClient.createStation(TEST_STATION1_NAME).jsonPath().getLong("id");

        // when
        stationApiClient.deleteStation(stationId);

        // then
        List<String> stationNames = stationApiClient.getStations().jsonPath().getList("name", String.class);
        assertThat(stationNames).isEmpty();
    }
}
