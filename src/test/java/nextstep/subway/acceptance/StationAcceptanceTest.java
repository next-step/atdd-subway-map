package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when 지하철역을 생성하면
        ExtractableResponse<Response> response = createStation("강남역");
        // then 지하철역이 생성된다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        // then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
        assertThat(findStations().jsonPath().getList("name")).containsAnyOf("강남역");
    }

    private ExtractableResponse<Response> findStations() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createStation(String stationName) {

        Map<String, String> param = new HashMap<>();
        param.put("name", stationName);

        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void findAllStations() {
        // given 2개의 지하철역을 생성하고
        createStation("강남역");
        createStation("역삼역");

        // when 지하철역 목록을 조회하면
        List<String> stationNames = findStations().jsonPath().getList("name");

        // then 2개의 지하철역을 응답 받는다
        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames).containsAnyOf("강남역");
        assertThat(stationNames).containsAnyOf("역삼역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 삭제한다.")
    @Test
    void deleteStation() {

        String stationName = "강남역";

        // 지하철역 생성
        ExtractableResponse<Response> createResponse = createStation(stationName);

        // 지하철역 삭제
        ExtractableResponse<Response> deleteResponse = deleteStation(createResponse.jsonPath().get("id"));

        // 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
        List<String> stationNames = findStations().jsonPath().getList("name");
        assertThat(stationNames).doesNotContain(stationName);
    }

    private ExtractableResponse<Response> deleteStation(int id) {
        return RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();
    }

}