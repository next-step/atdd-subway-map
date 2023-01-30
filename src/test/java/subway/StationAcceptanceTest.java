package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static subway.given.GivenStationApi.*;

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
        ExtractableResponse<Response> response = createStationApi("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                getStationsApi().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }



    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        // given
        createStationApi(STATION_1);
        createStationApi(STATION_2);

        // when
        final List<String> stationNames = getStationsApi()
                .jsonPath()
                .getList("name", String.class);

        // then
        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames).containsAnyOf(STATION_1, STATION_2);
    }

    private static ExtractableResponse<Response> getStationsApi() {
        return given().log().all()
                .when()
                .get(STATIONS_PATH)
                .then().log().all()
                .extract();
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteByStationId() {
        // given
        final long stationId = createStationApi(STATION_3)
                .jsonPath()
                .getLong("id");

        // when
        given().log().all()
                .when()
                .delete(STATIONS_PATH + "/" + stationId)
                .then().log().all();

        // then
        final List<String> stationNames = getStationsApi().jsonPath()
                .getList("name", String.class);
        assertThat(stationNames).doesNotContain(STATION_3);
    }

}
