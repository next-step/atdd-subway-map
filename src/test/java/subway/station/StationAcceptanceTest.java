package subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.db.AcceptanceTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        String stationName = "강낭역";
        //When 지하철역을 생성하면, Then 지하철역이 생성된다
        createStation(stationName);
        //Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
        assertThat(getAllStations()).containsAnyOf(stationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역을 생성하고 조회한다.")
    @Test
    void createAndGetStation() {
        //Given 2개의 지하철역을 생성하고, (createStation() test 에서 하나 이미 생성)
        createStation("왕십리역");
        createStation("답십리역");
        //When 지하철역 목록을 조회하면, Then 2개의 지하철역을 응답 받는다
        assertThat(getAllStations()).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 생성하고 삭제한다.")
    @Test
    void createAndDeleteStation() {
        String stationName = "김포공항역";
        //Given 지하철역을 생성하고
        Long stationID = createStation(stationName);
        //When 그 지하철역을 삭제하면
        deleteStation(stationID);
        //Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
        assertThat(getAllStations()).doesNotContain(stationName);
    }

    private Long createStation(String stationName) {
        // when 지하철역을 생성하면
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then 지하철역이 생성된다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then 지하철역의 ID 가 응답 된다
        StationResponse station = response.body().as(StationResponse.class);
        return station.getId();
    }

    private List<String> getAllStations() {

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList("name", String.class);
    }

    private void deleteStation(Long id) {
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().delete("/stations/"+id)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
