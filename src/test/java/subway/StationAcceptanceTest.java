package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        final String stationName = "강남역";
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        System.out.println(response.body().toString());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(stationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void retrieveStations() {
        // given
        final int createdStations = 2;
        Map<String, String> gangnamPostRequestMap = new HashMap<>();
        gangnamPostRequestMap.put("name", "강남역");

        ExtractableResponse<Response> gangnamResponse =
                RestAssured.given().log().all()
                        .body(gangnamPostRequestMap)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();


        Map<String, String> yeoksamPostRequestMap = new HashMap<>();
        yeoksamPostRequestMap.put("name", "역삼역");

        ExtractableResponse<Response> yeoksamResponse =
                RestAssured.given().log().all()
                        .body(yeoksamPostRequestMap)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();


        // when
        ExtractableResponse<Response> retrieveStationsResponse =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract();

        assertThat(retrieveStationsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        assertThat(createdStations).isEqualTo(retrieveStationsResponse.body().jsonPath().getList("$").size());

    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void removeStation() {
        // given
        final String stationName = "강남역";
        Map<String, String> gangnamPostRequestMap = new HashMap<>();
        gangnamPostRequestMap.put("name", stationName);

        ExtractableResponse<Response> gangnamResponse =
                RestAssured.given().log().all()
                        .body(gangnamPostRequestMap)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        final String createdLocation = gangnamResponse.header("Location");
        final Integer createdId = gangnamResponse.body().jsonPath().get("id");

        // given: optional
        Map<String, String> yeoksamPostRequestMap = new HashMap<>();
        yeoksamPostRequestMap.put("name", "역삼역");

        ExtractableResponse<Response> yeoksamResponse =
                RestAssured.given().log().all()
                        .body(yeoksamPostRequestMap)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // when
        ExtractableResponse<Response> deletedStation = RestAssured.given().log().all()
                .when().delete(createdLocation)
                .then().log().all()
                .extract();

        assertThat(deletedStation.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> retrieveStationsResponse =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract();
        assertThat(retrieveStationsResponse.body().jsonPath().getList("id")).doesNotContain(createdId);

    }

}