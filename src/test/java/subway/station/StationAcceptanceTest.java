package subway.station;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static subway.utils.AssertUtil.assertEqualToNames;
import static subway.utils.StationUtil.*;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        createStationResultResponse("강남역");

        // then
        List<String> stationNames = showStationsResultResponse().getList("name");
        assertEqualToNames(stationNames, "강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("지하철역 목록 조회")
    void showStations() {
        // given
        createStationResultResponse("강남역");
        createStationResultResponse("역삼역");

        // when
        JsonPath response = showStationsResultResponse();
        List<String> stationNames = response.getList("name");

        // then
        assertEqualToNames(stationNames, "강남역", "역삼역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("지하철역 제거")
    void deleteStation() {
        // given
        Long id = createStationResultResponse("강남역").getLong("id");

        // when
        deleteStationResult(id);

        // then
        List<String> stationNames = showStationsResultResponse().getList("name");
        assertEqualToNames(stationNames);
    }
}
