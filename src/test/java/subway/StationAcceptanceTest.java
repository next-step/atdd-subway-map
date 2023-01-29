package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철 역을 생성한다.")
    @Test
    void createStation() {
        String name = "강남역";
        // when
        StationUtils.createStation(name);

        // then
        List<String> stationNames =
                RestAssured
                        .given().spec(StationUtils.getRequestSpecification())
                        .when().get("/stations")
                        .then().extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(name);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철 역을 조회한다.")
    @Test
    void selectStation() {
        // given
        String station1 = "강남역";
        String station2 = "판교역";
        StationUtils.createStation(station1);
        StationUtils.createStation(station2);

        // when
        List<String> list = StationUtils.selectStations().jsonPath()
                .getList("name", String.class);

        // then
        assertThat(list).containsExactly(station1, station2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철 역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        String station = "강남역";
        StationUtils.createStation(station);

        // when
        ExtractableResponse<Response> response =
                RestAssured
                        .given().spec(StationUtils.getRequestSpecification()).log().all()
                        .when().delete("/stations/1")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        List<String> list = StationUtils.selectStations().jsonPath()
                .getList("name", String.class);
        assertThat(list).isEmpty();
    }



}