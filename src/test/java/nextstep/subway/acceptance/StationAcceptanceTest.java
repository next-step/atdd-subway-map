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
        // when
        ExtractableResponse<Response> response = registerStation("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = findStations().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        //given
        registerStation("역삼역");
        registerStation("선릉역");

        //when
        ExtractableResponse<Response> response = findStations();

        List<String> responseList = response.jsonPath()
                .getList("name", String.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseList)
                .hasSize(2)
                .containsExactly("역삼역", "선릉역");
    }
    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        //given
        String stationName = "삼성역";
        ExtractableResponse<Response> response = registerStation(stationName);

        //when
        Integer id = response.jsonPath().get("id");
        ExtractableResponse<Response> deleteResponse = deleteStation(id);
        List<String> findStations = findStations().jsonPath().getList("name", String.class);

        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(findStations).doesNotContain(stationName);
    }

    private ExtractableResponse<Response> registerStation(String stationName) {
        Map<String, String> param = new HashMap<>();
        param.put("name", stationName);

        return RestAssured
                .given()
                    .log().all()
                    .body(param)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then()
                    .log().all()
                .extract();
    }

    private ExtractableResponse<Response> findStations() {
        return RestAssured
                .given()
                    .log().all()
                .when()
                    .get("/stations")
                .then()
                    .log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteStation(Integer id) {
        return RestAssured
                .given()
                    .log().all()
                .when()
                    .delete("/stations/" + id)
                .then()
                    .log().all()
                .extract();
    }

}