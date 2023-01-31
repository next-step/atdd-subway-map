package subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.station.StationApiClient.*;

@DisplayName("지하철역 관련 기능")
@Sql("classpath:sql/delete-records.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> createStationResponse = requestCreateStation("강남역");

        // then
        assertThat(createStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> showStationsResponse = requestShowStations();
        List<String> stationNames = showStationsResponse.jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void showStations() {
        // given
        requestCreateStation("서울역");
        requestCreateStation("부산역");

        // when
        ExtractableResponse<Response> showStationsResponse = requestShowStations();
        List<String> stationNames = showStationsResponse.jsonPath().getList("name", String.class);

        // then
        assertThat(stationNames.size()).isEqualTo(2);
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
        ExtractableResponse<Response> createStationResponse = requestCreateStation("역삼역");

        Long stationId = createStationResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> deleteStationResponse = requestDeleteStation(stationId);

        assertThat(deleteStationResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> showStationsResponse = requestShowStations();
        List<String> stationNames = showStationsResponse.jsonPath().getList("name", String.class);

        assertThat(stationNames).doesNotContain("역삼역");
        assertThat(stationNames).isEmpty();
    }
}