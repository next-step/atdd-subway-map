package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import javax.sound.sampled.Line;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        ExtractableResponse<Response> response = createStation("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = findAllStations();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하쳘역을 조회한다.")
    @Test
    void findStation() {
        // Gievn 2개의 지하철역을 생성한다.
        createStation("강남역");
        createStation("역삼역");
        // When 지하철역 목록을 조회하면
        List<String> stationNames = findAllStations();
        // Then 2개의 지하철역을 응답 받는다.
        assertThat(stationNames).containsExactly("강남역", "역삼역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하쳘역을 삭제한다.")
    @Test
    void deleteStation() {
        //Given 지하철역을 생성하고
        ExtractableResponse<Response> response = createStation("역삼역");
        String id = response.body().jsonPath().getString("id");

        //When 그 지하철역을 삭제하면
        deleteStation(id);

        //Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
        List<String> stationNames = findAllStations();
        assertThat(stationNames).doesNotContain("역삼역");
    }

    private ExtractableResponse<Response> createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private List<String> findAllStations() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

    private void deleteStation(String id) {
        RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all();
    }

}