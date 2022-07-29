package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 교대역 = "교대역";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성합니다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> stationResponse = 지하철역_생성(강남역);

        // then
        assertThatStatus(stationResponse, CREATED);

        // then
        ExtractableResponse<Response> stationsResponse = 지하철역_목록_조회();
        List<String> stationNames = stationsResponse
                .jsonPath()
                .getList("name");
        assertThatStatus(stationsResponse, OK);
        assertThat(stationNames).containsAnyOf(강남역);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회합니다.")
    @Test
    void getStations() {
        //given
        지하철역_생성(강남역);
        지하철역_생성(교대역);

        //when
        ExtractableResponse<Response> stationsResponse = 지하철역_목록_조회();
        List<String> stationNames = stationsResponse
                .jsonPath()
                .getList("name");

        //then
        assertThatStatus(stationsResponse, OK);
        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).contains(강남역, 교대역);

    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거합니다.")
    @Test
    void deleteStation() {
        //given
        Long stationId = 지하철역_생성(교대역)
                .jsonPath()
                .getLong("id");

        //when
        ExtractableResponse<Response> deleteResponse = 지하철역_제거(stationId);

        //then
        List<String> stationNames = 지하철역_목록_조회()
                .jsonPath()
                .getList("name");

        assertThatStatus(deleteResponse, NO_CONTENT);
        assertThat(stationNames).doesNotContain(교대역);

    }

    public ExtractableResponse<Response> 지하철역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then()
                .log().all()
                .extract();

    }

    private ExtractableResponse<Response> 지하철역_목록_조회() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_제거(Long stationId) {
        return RestAssured.given().log().all()
                .when().delete("stations/{id}", stationId)
                .then()
                .log().all()
                .extract();
    }

    private void assertThatStatus(ExtractableResponse<Response> response, HttpStatus expectedStatus) {
        assertThat(response.statusCode()).isEqualTo(expectedStatus.value());
    }

}