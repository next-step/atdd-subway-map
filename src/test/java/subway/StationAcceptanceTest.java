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
     * When 지하철역을 생성하면 Then 지하철역이 생성된다 Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        final ExtractableResponse<Response> response = createStationResponse("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        final List<String> stationNames =
            RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }



    /**
     * Given 2개의 지하철역을 생성하고 When 지하철역 목록을 조회하면 Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void findStation() {
        // Given
        String[] 지하철_이름 = {"강남역", "역삼역"};
        createStationResponse(지하철_이름[0]);
        createStationResponse(지하철_이름[1]);

        // When
        final ExtractableResponse<Response> response = getStationList();

        // then
        final List<String> stationNameList = response.jsonPath().getList("name");
        assertThat(stationNameList.size()).isEqualTo(2);
        assertThat(stationNameList).contains(지하철_이름);
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
        final String 지하철역_이름 = "강남역";
        final ExtractableResponse<Response> createResponse = createStationResponse(지하철역_이름);
        final StationResponse createdStation = createResponse.as(StationResponse.class);

        // When
        RestAssured
            .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .delete("/stations/"+ createdStation.getId())
            .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
            .extract();

        // Then
        ExtractableResponse<Response> stations = getStationList();
        assertThat(stations.jsonPath().getList("name")).doesNotContain(지하철역_이름);
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

    private static ExtractableResponse<Response> createStationResponse(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }
}