package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = StationAcceptanceFactory.createStation("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = StationAcceptanceFactory.getAllStations()
                .jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    @DisplayName("지하철 목록을 조회한다")
    @Test
    void getStations() {
        // given
        StationAcceptanceFactory.createStation("염창역");
        StationAcceptanceFactory.createStation("등촌역");
        // when
        ExtractableResponse<Response> response = StationAcceptanceFactory.getAllStations();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).containsExactlyInAnyOrder("염창역", "등촌역");
    }

    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> station = StationAcceptanceFactory.createStation("당산역");
        long stationId = station.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = StationAcceptanceFactory.deleteStation(stationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<String> stationNames = StationAcceptanceFactory.getAllStations()
                .jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain("당산역");
    }


}