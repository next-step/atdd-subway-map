package subway;

import static org.assertj.core.api.Assertions.assertThat;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import support.annotation.AcceptanceTest;

@DisplayName("지하철역 관련 기능")
@AcceptanceTest
public class StationAcceptanceTest {

    public static final String GANGNAM_STATION_NAME = "강남역";
    public static final String SAMSUNG_STATION_NAME = "삼성역";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", GANGNAM_STATION_NAME);

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
            RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(GANGNAM_STATION_NAME);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStationList() {
        // given
        saveStation(GANGNAM_STATION_NAME);
        saveStation(SAMSUNG_STATION_NAME);

        // when
        ExtractableResponse<Response> response = getStations();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.body().as(List.class)).hasSize(2);
            assertThat(response.jsonPath().getList("name")).containsAnyOf(GANGNAM_STATION_NAME, SAMSUNG_STATION_NAME);
        });

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
        ExtractableResponse<Response> saveResponse = saveStation(SAMSUNG_STATION_NAME);
        saveStation(SAMSUNG_STATION_NAME);
        Integer deleteStationId = saveResponse.jsonPath().get("id");

        // when
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/{stationId}", deleteStationId)
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            ResponseBodyExtractionOptions currentStations = getStations().body();
            assertThat(currentStations.as(List.class)).hasSize(1);
            assertThat(currentStations.jsonPath().getList("name")).doesNotContain(GANGNAM_STATION_NAME);
        });

    }


    private ExtractableResponse<Response> getStations() {
        return RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/stations")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> saveStation(String name) {
        return RestAssured.given()
            .body(Map.of("name", name))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }

}
