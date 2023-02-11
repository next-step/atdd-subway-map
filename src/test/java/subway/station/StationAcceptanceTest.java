package subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static subway.AssertionUtils.*;
import static subway.station.StationApi.*;
import static subway.station.StationFixture.STATION_A;
import static subway.station.StationFixture.STATION_B;

@DisplayName("지하철역 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStationTest() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성(STATION_A);

        // then
        응답코드_201을_반환한다(response);

        // then
        목록은_다음을_포함한다(지하철역_이름_목록_조회(), STATION_A.getName());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void testGetStations() {
        // given
        지하철역_생성(STATION_A);
        지하철역_생성(STATION_B);

        // when & then
        목록은_다음을_포함한다(지하철역_이름_목록_조회(), STATION_A.getName(), STATION_B.getName());
    }


    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void testDeleteStation() {
        final StationResponse 생성된_지하철역 = 지하철역_생성(STATION_A).as(StationResponse.class);

        // when
        final ExtractableResponse<Response> response = 지하철역_삭제(생성된_지하철역.getId());

        // then
        응답코드_204를_반환한다(response);
        목록은_다음을_포함하지_않는다(지하철역_이름_목록_조회(), STATION_A.getName());
    }
}