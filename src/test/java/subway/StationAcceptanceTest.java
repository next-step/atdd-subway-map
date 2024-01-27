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
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    String 강남역 = "강남역";
    String 역삼역 = "역삼역";

    /**
     * When 지하철역을 생성하면 Then 지하철역이 생성된다 Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        final ExtractableResponse<Response> response = createStation(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        final List<String> stationNames = getStationList().jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf(강남역);
    }



    /**
     * Given 2개의 지하철역을 생성하고 When 지하철역 목록을 조회하면 Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void findStationList() {
        // Given
        createStation(강남역);
        createStation(역삼역);

        // When
        final ExtractableResponse<Response> response = getStationList();

        // then
        final List<String> stationNameList = response.jsonPath().getList("name");
        assertThat(stationNameList.size()).isEqualTo(2);
        assertThat(stationNameList).contains(강남역, 역삼역);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void findStation() {
        // Given
        ExtractableResponse<Response> createResponse = createStation(강남역);
        StationResponse createdStation = createResponse.as(StationResponse.class);

        // When
        final ExtractableResponse<Response> response = getStation(createdStation.getId());

        // Then
        final String foundStation = response.jsonPath().getString("name");
        assertThat(foundStation).isEqualTo(강남역);
    }
    
    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철역 수정 테스트")
    @Test
    void updateStation() {
        // Given
        ExtractableResponse<Response> createResponse = createStation(강남역);
        StationResponse createdStation = createResponse.as(StationResponse.class);

        // When
        updateStation(createdStation.getId(), 역삼역);

        // Then
        ExtractableResponse<Response> getResponse = getStation(createdStation.getId());
        StationResponse foundStation = getResponse.as(StationResponse.class);

        assertThat(foundStation.getId()).isEqualTo(createdStation.getId());
        assertThat(foundStation.getName()).isEqualTo(역삼역);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 제거 테스트")
    @Test
    void deleteStation() {

        // Given
        final ExtractableResponse<Response> createResponse = createStation(강남역);
        final StationResponse createdStation = createResponse.as(StationResponse.class);

        // When
        deleteStation(createdStation);

        // Then
        ExtractableResponse<Response> stations = getStationList();
        assertThat(stations.jsonPath().getList("name")).doesNotContain(강남역);
    }

    private void updateStation(Long id, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/stations/" + id)
            .then().log().all()
            .extract();
    }

    private static ExtractableResponse<Response> deleteStation(StationResponse createdStation) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete("/stations/" + createdStation.getId())
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .extract();
    }

    private static ExtractableResponse<Response> getStationList() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/stations")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    private static ExtractableResponse<Response> createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> getStation(Long id) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/stations/" + id)
            .then().log().all()
            .extract();
    }
}
