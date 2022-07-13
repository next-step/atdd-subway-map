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
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    public static final String STATION_NAME1 = "강남역";
    public static final String STATION_NAME2 = "삼성역";

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
        // when
        ExtractableResponse<Response> response = 지하철_역_생성(STATION_NAME1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = 지하철_역_목록_조회()
                .jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(STATION_NAME1);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        지하철_역_생성(STATION_NAME1);
        지하철_역_생성(STATION_NAME1);

        // when
        ExtractableResponse<Response> response = 지하철_역_목록_조회();

        // then
        assertThat(response.jsonPath().getList("name").size()).isEqualTo(2);
        assertThat(response.jsonPath().getList("name")).isEqualTo(Arrays.asList(STATION_NAME1, STATION_NAME1));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> toDeleteStation = 지하철_역_생성(STATION_NAME1);
        Long id = toDeleteStation.jsonPath().getLong("id");
        지하철_역_생성(STATION_NAME2);

        // when
        지하철_역_제거(id);

        // then
        ExtractableResponse<Response> response = 지하철_역_목록_조회();
        assertThat(response.jsonPath().getList("id")
                .stream()
                .filter(stationId -> stationId == id)
                .count())
                .isEqualTo(0);
    }

    ExtractableResponse<Response> 지하철_역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> 지하철_역_목록_조회() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    void 지하철_역_제거(Long id) {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/" + id)
                .then().log().all();
    }
}