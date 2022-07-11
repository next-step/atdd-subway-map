package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        var response = createStationWithName("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        var stationNames = RestAssured
                        .when()
                            .get("/stations")
                        .then()
                            .extract().jsonPath().getList("name", String.class);

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
        createStationWithName("신사역");
        createStationWithName("강남역");

        // when
        var response = RestAssured
                .when()
                    .get("/stations")
                .then()
                    .extract();

        // then
        var stationNames = response.jsonPath().getList("name", String.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationNames).containsExactlyInAnyOrder("신사역", "강남역")
        );
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
        var stationName = "양재역";
        var createResponse = createStationWithName(stationName);
        var stationId = createResponse.body().jsonPath().getLong("id");

        // when
        var deleteResponse = RestAssured
                .given()
                    .pathParam("stationId", stationId)
                .when()
                    .delete("/stations/{stationId}")
                .then()
                    .extract();

        // then
        var stationNames = RestAssured
                .when()
                    .get("/stations")
                .then()
                    .extract().jsonPath().getList("name", String.class);

        assertAll(
                () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(stationNames).doesNotContain(stationName)
        );

    }

    ExtractableResponse<Response> createStationWithName(String stationName) {
        var requestBody = new HashMap<String, String>();
        requestBody.put("name", stationName);

        return RestAssured
                .given()
                    .body(requestBody)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then()
                    .extract();
    }

}