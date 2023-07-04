package subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.common.RestAssuredUtils;
import subway.common.RestAssuredCondition;
import subway.station.domain.Station;
import subway.station.repository.StationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    private final String STATION_API_URI = "/api/stations";
    private final String SLASH = "/";

    @Autowired
    StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        stationRepository.deleteAll();
    }

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

        ExtractableResponse<Response> response =
                RestAssuredUtils.create(new RestAssuredCondition(STATION_API_URI, params));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = RestAssuredUtils
                .inquriy(new RestAssuredCondition(STATION_API_URI))
                .jsonPath()
                .getList("name", String.class);
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
    void getStations() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "우리집역");
        RestAssuredUtils.create(new RestAssuredCondition(STATION_API_URI, params));

        params.put("name", "역삼역");
        RestAssuredUtils.create(new RestAssuredCondition(STATION_API_URI, params));

        // when
        ExtractableResponse<Response> response =
                RestAssuredUtils.inquriy(new RestAssuredCondition(STATION_API_URI));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", Station.class).size()).isEqualTo(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void removeStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "삼성역");
        ExtractableResponse<Response> postResponse = RestAssuredUtils.create(new RestAssuredCondition(STATION_API_URI, params));
        Station stations = postResponse.jsonPath().getObject(".", Station.class);

        // when
        ExtractableResponse<Response> deleteResponse =
                RestAssuredUtils.delete(new RestAssuredCondition(STATION_API_URI + SLASH + String.valueOf(stations.getId())));

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<String> stationNames = RestAssuredUtils
                .inquriy(new RestAssuredCondition(STATION_API_URI))
                .jsonPath()
                .getList("name", String.class);
        assertThat(stationNames).doesNotContain("삼성역");
    }

}
