package nextstep.subway.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceBaseTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void testCreateStation() {
        // when
        final ExtractableResponse<Response> response = testRestApi(
                HttpMethod.POST,
                "/stations",
                Map.of("name", "강남역")
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        final List<String> stationNames = getAllStations().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void testGetStations() {
        // given
        final List<String> stationNamesToInsert = List.of("선릉역", "역삼역");
        stationNamesToInsert.forEach((stationName) -> createStation(
                Map.of("name", stationName)
        ));

        // when
        final ExtractableResponse<Response> response = testRestApi(
                HttpMethod.GET,
                "/stations"
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        final List<String> respondedNames = response.body().jsonPath().getList("name", String.class);
        assertThat(respondedNames).containsAll(stationNamesToInsert);
        assertThat(respondedNames).hasSize(stationNamesToInsert.size());
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void testDeleteStation() {
        // given
        final String stationNameToBeDeleted = "선릉역";
        final String stationNameToBeRemained = "역삼역";
        final List<String> stationNamesToInsert = List.of(stationNameToBeDeleted, stationNameToBeRemained);

        // given
        final List<Map<Object, Object>> respondedBody = stationNamesToInsert.stream()
                .map((stationName) -> createStation(
                        Map.of("name", stationName)
                ).getMap("$"))
                .collect(Collectors.toList());
        final Map<Object, Object> stationToBeDeleted = respondedBody.get(0);

        // when
        final ExtractableResponse<Response> deleteResponse = testRestApi(
                HttpMethod.DELETE,
                "/stations/{id}",
                Collections.emptyMap(),
                stationToBeDeleted.get("id")
        );

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        final List<String> remainedStations = getAllStations().getList("name");
        assertThat(remainedStations).doesNotContain(stationNameToBeDeleted);
        assertThat(remainedStations).contains(stationNameToBeRemained);
    }

    private JsonPath createStation(final Map<String, Object> request) {
        return testRestApi(
                HttpMethod.POST,
                "/stations",
                request
        ).jsonPath();
    }

    private JsonPath getAllStations() {
        return testRestApi(
                HttpMethod.GET,
                "/stations"
        ).jsonPath();
    }
}
