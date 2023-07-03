package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.StationRequestFixture.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철_역_생성("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = 지하철_역_목록_조회().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회 한다.")
    @Test
    void showStations() {
        // given
        지하철_역_생성("강남역");
        지하철_역_생성("논현역");

        // when
        List<String> stationNames = 지하철_역_목록_조회().jsonPath().getList("name", String.class);

        // then
        assertThat(stationNames).containsAnyOf("강남역", "논현역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거 한다.")
    @Test
    void deleteStation() {
        // given
        Long stationId = 지하철_역_생성("강남역").jsonPath().getLong("id");

        // when
        지하철_역_삭제(stationId);

        // then
        List<String> stationNames = 지하철_역_목록_조회().jsonPath().getList("name", String.class);

        assertThat(stationNames).doesNotContain("강남역");
    }
}
