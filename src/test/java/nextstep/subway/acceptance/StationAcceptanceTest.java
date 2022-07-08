package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

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
    int port;
    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        stationRepository.deleteAll();
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
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        final ExtractableResponse<Response> response = createStationRequest(params);

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());

        // then
        final ExtractableResponse<Response> getStationsResponse = getStationsRequest();
        final List<String> stationNames = getStationsResponse.jsonPath().getList("name", String.class);

        assertThat(stationNames.size()).isEqualTo(params.size());
        assertThat(stationNames).containsAll(params.values());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회합니다.")
    @Test
    void getStations() throws Exception {
        // given
        // 생성할 역 이름 리스트
        final List<String> stations = new ArrayList<>();
        stations.add("강남역");
        stations.add("역삼역");

        // 생성해야할 리스트의 길이만큼 반복문을 돌며 역을 생성합니다
        for (final String station : stations) {
            final HashMap<String, String> param = new HashMap<>();
            param.put("name", station);

            final ExtractableResponse<Response> createStationResponse = createStationRequest(param);

            assertThat(createStationResponse.statusCode()).isEqualTo(CREATED.value());
        }

        // when
        final ExtractableResponse<Response> getStationsResponse = getStationsRequest();

        // then
        assertThat(getStationsResponse.statusCode()).isEqualTo(OK.value());

        // then
        final List<String> stationNames = getStationsResponse.jsonPath().getList("name", String.class);
        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames).contains("강남역", "역삼역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 삭제합니다.")
    @Test
    void deleteStations() throws Exception {
        // given
        final HashMap<String, String> param = new HashMap<>();
        param.put("name", "강남역");

        final ExtractableResponse<Response> createStationResponse = createStationRequest(param);

        assertThat(createStationResponse.statusCode()).isEqualTo(CREATED.value());

        // 저장된 지하철 역의 ID
        final String savedStationId = createStationResponse.jsonPath().getString("id");

        // when
        final ExtractableResponse<Response> deleteStationResponse = deleteStationRequest(savedStationId);

        // then
        assertThat(deleteStationResponse.statusCode()).isEqualTo(NO_CONTENT.value());

        // then
        final ExtractableResponse<Response> getStationsResponse = getStationsRequest();
        final List<String> stationNames = getStationsResponse.jsonPath().getList("name", String.class);

        assertThat(stationNames.size()).isEqualTo(0);
    }

    private ExtractableResponse<Response> createStationRequest(Map<String, String> body) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(body)
                .contentType(APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then()
                .extract();

        return response;
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
                .when().delete("/stations/" + stationId)
                .then().log().all()
                .extract();

        return response;
    }
}