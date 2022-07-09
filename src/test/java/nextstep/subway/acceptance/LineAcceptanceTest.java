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
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        지하철역_추가("삼성역");
        지하철역_추가("역삼역");
        지하철역_추가("강남역");
    }

    private ExtractableResponse<Response> 지하철역_추가(String name){
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선_생성() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void 지하철_노선_목록_조회() {
        // given
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "신분당선");
        params1.put("color", "bg-red-600");
        params1.put("upStationId", "1");
        params1.put("downStationId", "2");
        params1.put("distance", "10");

        RestAssured.given().log().all()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "분당선");
        params2.put("color", "bg-green-600");
        params2.put("upStationId", "1");
        params2.put("downStationId", "3");
        params2.put("distance", "5");

        RestAssured.given().log().all()
                .body(params2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> names = response.jsonPath().getList("name", String.class);
        assertThat(names).contains("신분당선", "분당선");
        assertThat(names).hasSize(2);
    }
}