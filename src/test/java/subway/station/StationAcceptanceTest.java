package subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.util.Extractor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StationAcceptanceTest {

    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "약삼역";
    private static final String STATION_URL = "/station";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = saveStation(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(getStationName()).containsAnyOf(강남역);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void searchStation() {
        //given
        List<String> list = Arrays.asList(강남역, 역삼역);
        list.forEach(this::saveStation);

        //when
        List<String> stations = getStationName();

        //then
        assertThat(stations).hasSize(list.size());
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void removeStation() {
        //given
        ExtractableResponse<Response> response = saveStation(강남역);
        Long id = response.jsonPath().getObject("id", Long.class);

        //when
        Extractor.delete(STATION_URL + "/" + id);

        //then
        assertThat(getStationName()).doesNotContain(강남역);
    }

    private ExtractableResponse<Response> saveStation(String name) {

        Map<String, String> params = new HashMap<>();
        params.put("name", name);

       return Extractor.post(STATION_URL, params);
    }

    private List<String> getStationName() {

        return Extractor.get(STATION_URL)
            .jsonPath().getList("name", String.class);
    }
}