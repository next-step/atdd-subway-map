package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
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
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        String stationName = "강남역";
        ExtractableResponse<Response> response = StationApiTest.createStation(stationName);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = StationApiTest.getStationNames();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStations(){

        //given
        List<String> requestStationNames = new ArrayList<>(Arrays.asList(new String[]{"왕십리역", "마장역"}));

        requestStationNames.stream().map(StationApiTest::createStation)
                .forEach(response -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()));

        // when
        List<String> stationNames = StationApiTest.getStationNames();

        //then
        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames).containsExactly("왕십리역", "마장역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성

    @DisplayName("지하철역을 제거한다")
    @Test
    void deleteStation(){
        //given
        String requestStationName = "동작역";
        ExtractableResponse<Response> response = StationApiTest.createStation(requestStationName);

        //when
        response = StationApiTest.deleteStation(response);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        List<String> stationNames = StationApiTest.getStationNames();

        assertThat(stationNames.size()).isEqualTo(0);
        assertThat(stationNames).doesNotContain(requestStationName);
    }
}