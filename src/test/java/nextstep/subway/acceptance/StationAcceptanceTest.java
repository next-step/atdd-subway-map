package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseCleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    @LocalServerPort
    private int port;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        final Map<String, String> param = createParam("강남역");
        final ExtractableResponse<Response> response = createStationRequest(param);

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());

        // then
        final ExtractableResponse<Response> getStationsResponse = getStationsRequest();
        final List<String> stationNames = getStationsResponse.jsonPath().getList("name", String.class);

        assertThat(stationNames).hasSize(param.size());
        assertThat(stationNames).containsAll(param.values());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회합니다.")
    @Test
    void getStations() throws Exception {
        // given
        final List<Map<String, String>> params = createParam(List.of("강남역", "역삼역"));
        final List<String> createdStationNames = createStationRequest(params);

        // when
        final ExtractableResponse<Response> getStationsResponse = getStationsRequest();

        // then
        assertThat(getStationsResponse.statusCode()).isEqualTo(OK.value());

        // then
        final List<String> stationNames = getStationsResponse.jsonPath().getList("name", String.class);
        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).containsAll(createdStationNames);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제합니다.")
    @Test
    void deleteStations() throws Exception {
        // given
        final Map<String, String> param = createParam("강남역");
        final ExtractableResponse<Response> createStationResponse = createStationRequest(param);
        final Long savedStationId = createStationResponse.jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> deleteStationResponse = deleteStationRequest(savedStationId);

        // then
        assertThat(deleteStationResponse.statusCode()).isEqualTo(NO_CONTENT.value());

        // then
        final ExtractableResponse<Response> getStationsResponse = getStationsRequest();
        final List<String> stationNames = getStationsResponse.jsonPath().getList("name", String.class);

        assertThat(stationNames).hasSize(0);
    }

    private Map<String, String> createParam(String stationName) {
        final Map<String, String> param = new HashMap<>();
        param.put("name", stationName);

        return param;
    }

    private List<Map<String, String>> createParam(List<String> stationNames) {
        final List<Map<String, String>> params = new ArrayList<>();
        for (final String stationName : stationNames) {
            final Map<String, String> param = createParam(stationName);
            params.add(param);
        }

        return params;
    }

    private ExtractableResponse<Response> createStationRequest(Map<String, String> param) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(param)
                .contentType(APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        return response;
    }

    private List<String> createStationRequest(List<Map<String, String>> params) {
        final ArrayList<String> stationNames = new ArrayList<>();
        for (Map<String, String> param : params) {
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .body(param)
                    .contentType(APPLICATION_JSON_VALUE)
                    .when().post("/stations")
                    .then().log().all()
                    .extract();
            stationNames.add(response.jsonPath().getString("name"));
        }

        return stationNames;
    }

    private ExtractableResponse<Response> getStationsRequest() {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();

        return response;
    }

    private ExtractableResponse<Response> deleteStationRequest(Long stationId) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/stations/{stationId}", stationId)
                .then().log().all()
                .extract();

        return response;
    }
}