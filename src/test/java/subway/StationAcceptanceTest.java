package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    private final String STATION_API_PATH = "/stations";
    private final String STATION_GANGNAM = "강남역";
    private final String STATION_SAMSUNG = "삼성역";
    private final String PARAM_NAME = "name";
    private final String PARAM_ID = "id";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when & then
        지하철역_생성(STATION_GANGNAM);

        // then
        List<String> stationNames = getJsonPathList(지하철역_목록_조회(), PARAM_NAME);
        assertThat(stationNames).containsAnyOf(STATION_GANGNAM);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStationList() {
        // given
        지하철역_생성(STATION_GANGNAM);
        지하철역_생성(STATION_SAMSUNG);

        // when
        List<String> stationNames = getJsonPathList(지하철역_목록_조회(), PARAM_NAME);

        // then
        assertThat(stationNames).containsOnly(STATION_GANGNAM, STATION_SAMSUNG);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        long stationId = 지하철역_생성(STATION_GANGNAM).jsonPath().getLong(PARAM_ID);

        // when
        지하철역_삭제(stationId);

        // then
        List<String> stationNames = getJsonPathList(지하철역_목록_조회(), PARAM_NAME);
        assertThat(stationNames).doesNotContainAnyElementsOf(List.of(STATION_GANGNAM));
    }

    private ExtractableResponse<Response> 지하철역_생성(String name) {
        Map<String, String> params = generateStationParams(name);
        ExtractableResponse<Response> response = HttpRequest.sendPostRequest(STATION_API_PATH, params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    private ExtractableResponse<Response> 지하철역_목록_조회() {
        ExtractableResponse<Response> response = HttpRequest.sendGetRequest(STATION_API_PATH);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

    private void 지하철역_삭제(long stationId) {
        ExtractableResponse<Response> response = HttpRequest.sendDeleteRequest(STATION_API_PATH + "/" + stationId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Map<String, String> generateStationParams(String name) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME, name);
        return params;
    }

    private List<String> getJsonPathList(ExtractableResponse<Response> response, String path) {
        return response.jsonPath().getList(path, String.class);
    }
}