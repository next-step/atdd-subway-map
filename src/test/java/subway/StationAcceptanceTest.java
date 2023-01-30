package subway;

import groovy.util.logging.Log;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    JsonPath createStation(String stationName) {
        // when
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        JsonPath jsonPath = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath();

        List<String> stationNames = jsonPath.getList("name", String.class);
        assertThat(stationNames).containsAnyOf(stationName);

        return jsonPath;
    }

    JsonPath getAllStations() {

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());


        return RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath();
    }

    void deleteStation(Long id) {
        // when
        Map<String, Long> params = new HashMap<>();
        params.put("id", id);

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        //.body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().delete("/stations/"+id)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        createStation("강남역");
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
        createStation("왕십리역");
        List<String> stationNames = getAllStations().getList("name", String.class);
        assertThat(stationNames).hasSize(2);
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
        JsonPath jsonPath = createStation(stationName);
        Long stationID = -1L;

        List<StationResponse> stationList = jsonPath.getList("", StationResponse.class);
        Iterator iter = stationList.iterator();
        while(iter.hasNext()){
            StationResponse station = StationResponse.class.cast(iter.next());
            if (station.getName().equals(stationName)) {
                stationID = station.getId();
                break;
            }
        }

        assertThat(stationID).isNotEqualTo(-1L);

        deleteStation(stationID);
        List<String> stationNames = getAllStations().getList("name", String.class);
        assertThat(stationNames).doesNotContain(stationName);
    }
}