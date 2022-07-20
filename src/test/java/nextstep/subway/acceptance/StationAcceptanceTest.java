package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends BasicAcceptanceTest {

    private static final String gangnamStation = "강남역";
    private static final String donongStation = "도농역";
    private static final String gooriStation = "구리역";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = stationRestAssured.saveStation(gangnamStation);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = findAllStationNames();
        assertThat(stationNames).containsAnyOf(gangnamStation);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @Test
    void 지하철역_목록_조회() {
        stationRestAssured.saveAllStation(Arrays.asList(donongStation, gooriStation));

        ExtractableResponse<Response> stations = stationRestAssured.findAllStations();
        assertThat(stations.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> names = stationRestAssured.findAllStations().jsonPath().getList("name", String.class);
        assertThat(names).containsAnyOf(donongStation, gooriStation);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @Test
    void 지하철역_제거() {
        long id = stationRestAssured.saveStation("회기역").jsonPath().getLong("id");

        ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
            .when().delete("/stations/" + id)
            .then().log().all()
            .extract();

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private List<String> findAllStationNames() {
        return stationRestAssured.findAllStations().jsonPath().getList("name", String.class);
    }
}