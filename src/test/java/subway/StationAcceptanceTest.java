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
import static org.junit.jupiter.api.Assertions.assertAll;

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
        Map<String, String> params = new HashMap<>();
        params.put("name", MockStations.강남역);

        ExtractableResponse<Response> response = StationTestUtils.prepareRestAssuredGiven(params)
            .when().post("/stations")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = 지하철역_조회();
        assertThat(stationNames).containsAnyOf(MockStations.강남역);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("등록된 지하철역 목록을 조회한다.")
    void showStations() {
        // given
        StationTestUtils.createStations(List.of(MockStations.서울대입구역, MockStations.봉천역));

        // when
        List<String> stationNames = 지하철역_조회();

        // then
        assertAll(
            () -> assertThat(stationNames.size()).isEqualTo(2),
            () -> assertThat(stationNames).containsAll(
                List.of(MockStations.서울대입구역, MockStations.봉천역)
            )
        );
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("생성해둔 지하철 역을 삭제하면, 목록 조회시 해당 역을 찾을 수 없다.")
    void deleteStation() {
        // given
        String stationName = "강남역";
        Long id = StationTestUtils.createStation(stationName);

        // when
        RestAssured
            .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/stations/" + id);

        // then
        List<String> stationNames = 지하철역_조회();
        assertThat(지하철역_조회()).doesNotContain(stationName);
    }

    private List<String> 지하철역_조회() {
        return StationTestUtils.prepareRestAssuredGiven()
            .when().get("/stations")
            .then().log().all()
            .extract().jsonPath().getList("name", String.class);
    }
}
