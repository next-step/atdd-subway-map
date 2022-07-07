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
import java.util.LinkedHashMap;
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
        final Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
                RestAssured.given()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        final List<String> stationNames =
                RestAssured.given()
                        .when().get("/stations")
                        .then().extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        // given
        final List<String> stationNamesToInsert = List.of("선릉역", "역삼역");
        stationNamesToInsert.forEach((stationName) -> RestAssured.given()
                .body(Map.of("name", stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().extract());

        // when
        final ExtractableResponse<Response> response =
                RestAssured.given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/stations")
                        .then().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        final List<String> respondedNames = response.body().jsonPath().getList("name", String.class);
        respondedNames.containsAll(stationNamesToInsert);
        assertThat(respondedNames).hasSize(stationNamesToInsert.size());
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        final String stationNameToBeDeleted = "선릉역";
        final String stationNameToBeRemained = "역삼역";
        final List<String> stationNamesToInsert = List.of(stationNameToBeDeleted, stationNameToBeRemained);
        stationNamesToInsert.forEach((stationName) -> RestAssured.given()
                .body(Map.of("name", stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().extract());

        // given
        // FIXME: jsonPath로 우아하게 가져올 방법 없나...
        final List<LinkedHashMap<String, String>> respondedBody = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().extract()
//                .jsonPath().get("$[0].name");
                .jsonPath().get();
        final LinkedHashMap<String, String> stationToBeDeleted = respondedBody.get(0);

        // when
        final ExtractableResponse<Response> deleteResponse = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/{id}", stationToBeDeleted.get("id"))
                .then().extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        final List<String> remainedStations = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().extract()
                .jsonPath().getList("name");
        assertThat(remainedStations).doesNotContain(stationToBeDeleted.get("name"));
    }
}
