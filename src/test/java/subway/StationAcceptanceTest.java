package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.response.StationAcceptanceTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest{
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
        params.put("name", "강남역");

        ExtractableResponse<Response> response = StationAcceptanceTestUtils.createStationResponse(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> showResponse = StationAcceptanceTestUtils.showStationResponse();
        List<String> stationNames = showResponse.jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철성역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 전체를 조회한다.")
    @Test
    void showStations() {
        //given
        Map<String, String> station1 = new HashMap<>();
        station1.put("name", "강남역");
        Map<String, String> station2 = new HashMap<>();
        station2.put("name", "역삼역");
        StationAcceptanceTestUtils.createStationResponse(station1);
        StationAcceptanceTestUtils.createStationResponse(station2);

        List<String> stations = List.of("강남역", "역삼역");

        //when
        ExtractableResponse<Response> response = StationAcceptanceTestUtils.showStationResponse();
        List<String> stationNames = response.jsonPath().getList("name", String.class);

        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationNames).containsAll(stations),
                () -> assertThat(stationNames).containsExactly("강남역", "역삼역"),
                () -> assertEquals(stationNames.size(), stations.size())
        );
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        //given
        Map<String, String> station = new HashMap<>();
        station.put("name", "강남역");
        ExtractableResponse<Response> request = StationAcceptanceTestUtils.createStationResponse(station);
        Long id = request.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}