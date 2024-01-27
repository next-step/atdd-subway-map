package subway;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@Sql(scripts = "/reset-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void 지하철역_생성() {
        // when
        String stationName = "강남역";
        ExtractableResponse<Response> response = this.createStation(stationName);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                given()
                    .log().all()
                .when()
                    .get("/stations")
                .then()
                    .log().all()
                .extract()
                    .jsonPath()
                    .getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철 목록을 조회한다.")
    @Test
    void 지하철역_목록_조회() {
        // given
        String stationName1 = "교대역";
        this.createStation(stationName1);
        String stationName2 = "역삼역";
        this.createStation(stationName2);

        // when
        ExtractableResponse<Response> response = this.getStationList();

        // then
        JsonPath jsonPath = response.jsonPath();
        List<String> stationNames = jsonPath.getList("name", String.class);

        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).contains("교대역", "역삼역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void 지하철역_삭제() {
        // given
        String stationName = "강남역";
        ExtractableResponse<Response> createStationResponse = this.createStation(stationName);

        // when
        String location = createStationResponse.header("Location");
        String stationId = location.replaceAll(".*/(\\d+)$", "$1");

        given()
        .when()
            .delete("/stations/{id}", stationId)
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .log().all();

        // then
        ExtractableResponse<Response> responseAfterStationDeletion = this.getStationList();
        JsonPath jsonPathAfterStationDeletion = responseAfterStationDeletion.jsonPath();
        List<String> stationNames = jsonPathAfterStationDeletion.getList("name", String.class);

        assertThat(stationNames).hasSize(0);
        assertThat(stationNames).doesNotContain("강남역");
    }

    private ExtractableResponse<Response> getStationList() {
        ExtractableResponse<Response> response =
                given()
                    .log().all()
                .when()
                    .get("/stations")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .log().all()
                .extract();

        return response;
    }

    private ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response =
                given()
                    .log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then()
                    .log().all()
                .extract();

        return response;
    }

}