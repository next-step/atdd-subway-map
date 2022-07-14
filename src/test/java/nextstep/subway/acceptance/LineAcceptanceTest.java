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

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceBaseTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void testCreateLine() {
        // when
        final ExtractableResponse<Response> response = testRestApi(
                HttpMethod.POST,
                "/lines",
                Map.of(
                        "name", "신분당선",
                        "color", "bg-red-600",
                        "upStationId", 1,
                        "downStationId", 2,
                        "distance", 10
                )
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        final JsonPath resultPath = getAllLines();
        assertThat(resultPath.getList("name")).containsOnly("신분당선");
        assertThat(resultPath.getList("color")).containsOnly("bg-red-600");
        assertThat(resultPath.getList("upStationId")).containsOnly(1);
        assertThat(resultPath.getList("downStationId")).containsOnly(2);
        assertThat(resultPath.getList("distance")).containsOnly(10);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void testGetLineList() {
        // given
        createLine(Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        ));
        createLine(Map.of(
                "name", "분당선",
                "color", "bg-yellow-660",
                "upStationId", 1,
                "downStationId", 3,
                "distance", 15
        ));

        // when
        final ExtractableResponse<Response> response = testRestApi(
                HttpMethod.GET,
                "/lines"
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        final JsonPath resultPath = response.jsonPath();
        assertThat(resultPath.getList("name")).hasSize(2).containsOnly("신분당선", "분당선");
        assertThat(resultPath.getList("color")).hasSize(2).containsOnly("bg-red-600", "bg-yellow-660");
        assertThat(resultPath.getList("upStationId")).hasSize(2).containsOnly(1);
        assertThat(resultPath.getList("downStationId")).hasSize(2).containsOnly(2, 3);
        assertThat(resultPath.getList("distance")).hasSize(2).containsOnly(10, 15);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노션을 조회한다.")
    @Test
    void testGetLine() {
        // given
        final ExtractableResponse<Response> newBundangLineResponse = createLine(Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        ));
        createLine(Map.of(
                "name", "분당선",
                "color", "bg-yellow-660",
                "upStationId", 1,
                "downStationId", 3,
                "distance", 15
        ));

        // when
        final ExtractableResponse<Response> response = testRestApi(
                HttpMethod.GET,
                "/lines/{id}",
                Collections.emptyMap(),
                newBundangLineResponse.jsonPath().getLong("id")
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        final JsonPath resultPath = response.jsonPath();
        final JsonPath newBundangLinePath = newBundangLineResponse.jsonPath();
        assertThat(resultPath.getString("name")).isEqualTo(newBundangLinePath.getString("name"));
        assertThat(resultPath.getString("color")).isEqualTo(newBundangLinePath.getString("color"));
        assertThat(resultPath.getString("upStationId")).isEqualTo(newBundangLinePath.getString("upStationId"));
        assertThat(resultPath.getString("downStationId")).isEqualTo(newBundangLinePath.getString("downStationId"));
        assertThat(resultPath.getString("distance")).isEqualTo(newBundangLinePath.getString("distance"));

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void testUpdateLine() {
        // when
        final ExtractableResponse<Response> response = createLine(Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        ));
        final Long targetId = response.jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> result = testRestApi(
                HttpMethod.PUT,
                "/lines/{id}",
                Map.of(
                        "name", "닥터나우선",
                        "color", "bg-orange-630"
                ),
                targetId
        );

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        final JsonPath responsePath = getLine(targetId);
        assertThat(responsePath.getString("name")).isEqualTo("닥터나우선");
        assertThat(responsePath.getString("color")).isEqualTo("bg-orange-630");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void testDeleteLine() {
        // given
        final JsonPath pathNotToBeDeleted = createLine(Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        )).jsonPath();
        final JsonPath pathToBeDeleted = createLine(Map.of(
                "name", "분당선",
                "color", "bg-yellow-660",
                "upStationId", 1,
                "downStationId", 3,
                "distance", 15
        )).jsonPath();

        // when
        final ExtractableResponse<Response> deleteResponse = testRestApi(
                HttpMethod.DELETE,
                "/lines/{id}",
                Collections.emptyMap(),
                pathToBeDeleted.getLong("id")
        );

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        final List<String> remainedStations = getAllLines().getList("name");
        assertThat(remainedStations).contains(pathNotToBeDeleted.getString("name"));
        assertThat(remainedStations).doesNotContain(pathToBeDeleted.getString("name"));
    }

    private JsonPath createLine(final Map<String, Object> request) {
        return testRestApi(
                HttpMethod.POST,
                "/lines",
                request
        ).jsonPath();
    }

    private JsonPath getAllLines() {
        return testRestApi(
                HttpMethod.GET,
                "/lines"
        ).jsonPath();
    }

    private JsonPath getLine(final Long id) {
        return testRestApi(
                HttpMethod.GET,
                "/lines{id}",
                Collections.emptyMap(),
                id
        ).jsonPath();
    }
}
