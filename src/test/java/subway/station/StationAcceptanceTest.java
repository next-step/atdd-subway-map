package subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.AcceptanceTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.station.StationAcceptanceFactory.*;
import static subway.station.StationNameConstraints.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void 지하철_역_생성() {
        // when
        ExtractableResponse<Response> response = createStation(GANG_NAM);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = getAllStations()
                .jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(GANG_NAM);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철 목록을 조회한다")
    @Test
    void 지하철_목록_조회() {
        // given
        createStation(YEOM_CHANG);
        createStation(DEUNG_CHON);
        // when
        ExtractableResponse<Response> response = getAllStations();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames).containsExactlyInAnyOrder(YEOM_CHANG, DEUNG_CHON)
                .hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void 지하철_역_삭제() {
        // given
        ExtractableResponse<Response> station = createStation(DANG_SAN);
        long stationId = station.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = deleteStation(stationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<String> stationNames = getAllStations()
                .jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain(DANG_SAN);
    }


}