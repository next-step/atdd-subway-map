package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DisplayName("지하철 노선 관련 기능")
@Sql({"classpath:/reset-all-table.sql", "classpath:/station-init.sql", "classpath:/subwayLineColor-init.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubwayLineAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     * CreateParam 정보: ( name, color, upStationId, downStationId, distance )
     */
    @DisplayName("지하철 노선을 생성합니다.")
    @Test
    void createSubwayLine() throws Exception {
        // when
        final Map<String, Object> params = createParams(
                List.of("name", "color", "upStationId", "downStationId", "distance"),
                List.of("신분당선", "bg-red-600", 1, 2, 10));
        final ExtractableResponse<Response> response = createSubwayLineRequest(params);

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        final List<Object> stations = response.jsonPath().getList("stations");
        assertThat(stations.size()).isEqualTo(2);

        // then
        final ExtractableResponse<Response> getSubwayLinesResponse = getSubwayLinesRequest();
        final List<String> subwayLineNames = getSubwayLinesResponse.jsonPath().getList("name", String.class);

        assertThat(subwayLineNames).hasSize(1);
        assertThat(subwayLineNames).contains(String.valueOf(params.get("name")));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회합니다.")
    @Test
    void getSubwayLines() throws Exception {
        // given
        final Map<String, Object> params1 = createParams(
                List.of("name", "color", "upStationId", "downStationId", "distance"),
                List.of("신분당선", "bg-red-600", 1, 2, 10));
        final Map<String, Object> params2 = createParams(
                List.of("name", "color", "upStationId", "downStationId", "distance"),
                List.of("분당선", "bg-green-600", 1, 3, 10));
        final List<Map<String, Object>> paramsList = List.of(params1, params2);
        final List<String> createdSubwayLineNames = createSubwayLineRequest(paramsList);

        // when
        final ExtractableResponse<Response> response = getSubwayLinesRequest();

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());

        // then
        final List<String> subwayLineNames = response.jsonPath().getList("name", String.class);

        assertThat(subwayLineNames).hasSize(paramsList.size());
        assertThat(subwayLineNames).containsAll(createdSubwayLineNames);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다
     */
    @DisplayName("지하철 노선을 조회합니다.")
    @Test
    void getSubwayLine() throws Exception {
        // given
        final Map<String, Object> params = createParams(
                List.of("name", "color", "upStationId", "downStationId", "distance"),
                List.of("신분당선", "bg-red-600", 1, 2, 10));
        final ExtractableResponse<Response> createSubwayLineResponse = createSubwayLineRequest(params);
        final long createdSubwayLineId = createSubwayLineResponse.jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> response = getSubwayLineRequest(createdSubwayLineId);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        final long getSubwayLineId = response.jsonPath().getLong("id");
        assertThat(getSubwayLineId).isEqualTo(createdSubwayLineId);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정합니다.")
    @Test
    void updateSubwayLine() throws Exception {
        // given
        final Map<String, Object> params = createParams(
                List.of("name", "color", "upStationId", "downStationId", "distance"),
                List.of("신분당선", "bg-red-600", 1, 2, 10));
        final ExtractableResponse<Response> createSubwayLineResponse = createSubwayLineRequest(params);
        final long createdSubwayLineId = createSubwayLineResponse.jsonPath().getLong("id");

        final Map<String, Object> updateParams = createParams(
                List.of("name", "color"),
                List.of("다른분당선", "bg-green-600")
        );

        // when
        final ExtractableResponse<Response> response = updateSubwayLineRequest(createdSubwayLineId, updateParams);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());

        // then
        final ExtractableResponse<Response> getSubwayLineResponse = getSubwayLineRequest(createdSubwayLineId);
        assertThat(getSubwayLineResponse.jsonPath().getString("name")).isEqualTo(updateParams.get("name"));
        assertThat(getSubwayLineResponse.jsonPath().getString("color")).isEqualTo(updateParams.get("color"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제합니다.")
    @Test
    void deleteSubwayLine() throws Exception {
        // given
        final Map<String, Object> params = createParams(
                List.of("name", "color", "upStationId", "downStationId", "distance"),
                List.of("신분당선", "bg-red-600", 1, 2, 10));
        final ExtractableResponse<Response> createSubwayLineResponse = createSubwayLineRequest(params);
        final long createdSubwayLineId = createSubwayLineResponse.jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> response = deleteSubwayLineRequest(createdSubwayLineId);

        // then
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());

        // then
        final ExtractableResponse<Response> getSubwayLinesResponse = getSubwayLinesRequest();
        final List<Object> subwayLineIdList = getSubwayLinesResponse.jsonPath().getList("id");
        assertThat(subwayLineIdList).hasSize(0);
    }

    private Map<String, Object> createParams(List<String> keys, List<Object> values) {
        if (keys.size() != values.size()) {
            throw new RuntimeException("생성하려는 key 와 value 의 length 가 같아야 합니다.");
        }

        final Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            params.put(keys.get(i), values.get(i));
        }

        return params;
    }

    private ExtractableResponse<Response> createSubwayLineRequest(Map<String, Object> params) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/subway-lines")
                .then().log().all()
                .extract();

        return response;
    }

    private List<String> createSubwayLineRequest(List<Map<String, Object>> paramsList) {
        final List<String> subwayLineNames = new ArrayList<>();
        for (final Map<String, Object> params : paramsList) {
            final ExtractableResponse<Response> response = createSubwayLineRequest(params);

            final String subwayLineName = response.jsonPath().getString("name");
            subwayLineNames.add(subwayLineName);
        }

        return subwayLineNames;
    }

    private ExtractableResponse<Response> getSubwayLinesRequest() {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/subway-lines")
                .then().log().all()
                .extract();

        return response;
    }

    private ExtractableResponse<Response> getSubwayLineRequest(Long subwayLineId) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/subway-lines/" + subwayLineId)
                .then().log().all()
                .extract();

        return response;
    }

    private ExtractableResponse<Response> updateSubwayLineRequest(Long subwayLineId, Map<String, Object> params) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/subway-lines/" + subwayLineId)
                .then().log().all()
                .extract();

        return response;
    }

    private ExtractableResponse<Response> deleteSubwayLineRequest(Long subwayLineId) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/subway-lines/" + subwayLineId)
                .then().log().all()
                .extract();

        return response;
    }
}
