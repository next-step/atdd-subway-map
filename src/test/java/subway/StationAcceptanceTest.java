package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.common.BaseAcceptanceTest;
import subway.common.DatabaseCleaner;
import subway.domain.Station;
import subway.executor.StationServiceExecutor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StationAcceptanceTest extends BaseAcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = StationServiceExecutor.createStation("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = StationServiceExecutor.showStations()
                .jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역 목록 조회")
    void findStations() {
        //given
        StationServiceExecutor.createStation("강남역");
        StationServiceExecutor.createStation("언주역");
        //when
        ExtractableResponse<Response> response = StationServiceExecutor.showStations();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        //then
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertAll(
                () -> assertThat(stationNames).hasSize(2),
                () -> assertThat(stationNames).containsAnyOf("강남역", "언주역")
        );

    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @Test
    @DisplayName("지하철역 삭제")
    void removeStation() {
        //given
        Station 강남역 = StationServiceExecutor.createStation("강남역").as(Station.class);
        Station 언주역 = StationServiceExecutor.createStation("언주역").as(Station.class);
        Station 개봉역 = StationServiceExecutor.createStation("개봉역").as(Station.class);
        //when
        StationServiceExecutor.deleteStation(강남역.getId());
        //then
        ExtractableResponse<Response> response = StationServiceExecutor.showStations();
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertAll(
                () -> assertThat(stationNames).hasSize(2),
                () -> assertThat(stationNames).containsOnly("언주역", "개봉역")
        );

    }

}
