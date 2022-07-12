package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends BaseAcceptanceTest {
    public static final String NAME = "name";

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
        ExtractableResponse<Response> response = createStation(Map.of(NAME, "강남역"));

        // then
        checkResponseStatus(response, HttpStatus.CREATED);

        // then
        List<String> stationNames = getAllStations()
                .jsonPath()
                .getList(NAME, String.class);
        assertThat(stationNames).containsAnyOf("강남역");
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
        createStation(Map.of(NAME, "방배역"));
        createStation(Map.of(NAME, "신논현역"));

        // when
        ExtractableResponse<Response> response = getAllStations();
        checkResponseStatus(response, HttpStatus.OK);

        // then
        assertThat(response.jsonPath().getList(".")).hasSize(2);
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
        long id = createStation(Map.of(NAME, "사당역"))
                .jsonPath()
                .getLong("id");

        // when
        ExtractableResponse<Response> response = deleteStation(id);
        checkResponseStatus(response, HttpStatus.NO_CONTENT);

        // then
        List<Long> allStationIds = getAllStations()
                .jsonPath()
                .getList("id", Long.class);


        assertThat(allStationIds).isNotIn(id);
    }


    public static ExtractableResponse<Response> createStation(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> getAllStations() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteStation(long id) {
        return RestAssured.given().log().all()
                .when().delete("/stations/{id}", id)
                .then().log().all()
                .extract();
    }

}