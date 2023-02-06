package subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    private static final String STATION_NAME = "지하철역이름";
    private static final String NEW_STATION_NAME = "새로운지하철역이름";
    private static final String ANOTHER_STATION_NAME = "또다른지하철역이름";

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
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
        // when+then
        saveStation(STATION_NAME);
        saveStation(NEW_STATION_NAME);
        saveStation(ANOTHER_STATION_NAME);

        // then
        List<String> stationNames = findAllStations().jsonPath().getList("name", String.class);
        assertThat(stationNames).contains(STATION_NAME);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void readStations() {
        // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성

        //given
        saveStation(STATION_NAME);
        saveStation(NEW_STATION_NAME);

        //when
        ExtractableResponse<Response> readResponse = findAllStations();

        //then
        List<String> stationNames = readResponse.jsonPath().get("name");
        Assertions.assertThat(stationNames).containsExactly(STATION_NAME, NEW_STATION_NAME);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // TODO: 지하철역 제거 인수 테스트 메서드 생성

        //given
        Long id = saveStation(STATION_NAME);

        //when
        deleteStationById(id);

        //then
        Assertions.assertThat(findAllStations().jsonPath().getList("name")).doesNotContain(STATION_NAME);
    }

    private void deleteStationById(Long id) {
        RestAssured
                .given().log().all()
                .when()
                .delete("/stations/" + id)
                .then().log().all()
                .assertThat().statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    private ExtractableResponse<Response> findAllStations() {
        return RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value())
                .extract();
    }

    Long saveStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getObject("id", Long.class);
    }
}
