package subway.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.util.AcceptanceTestHelper;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.StationFixture.강남역_ID;
import static subway.fixture.StationFixture.강남역_이름;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationAcceptanceTest extends AcceptanceTestHelper {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        var response = post(STATION_PATH, Map.of("name", 강남역_이름));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("두개의 지하철역을 입력받아 생성하고, 목록을 조회하면 입력한 두 지하철역이 존재해야 한다.")
    @Test
    void requestAndResponseForSubwayStationTest() {
        // given
        지하철역_생성("강남역");
        지하철역_생성("역삼역");

        // when
        List<Long> ids = 지하철역_목록조회();

        // then
        assertThat(ids).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 생성한뒤 생성한 지하철역을 삭제하면 처음에 생성한 지하철역은 목록에 없어야 한다")
    void createAndDeleteAfterFindNoContentsStation() {
        // given
        final Long id = 지하철역_생성(강남역_이름);

        // when
        지하철역_삭제(id);

        // then
        var ids = 지하철역_목록조회();
        assertThat(ids).doesNotContain(강남역_ID);
    }

    private List<Long> 지하철역_목록조회() {
        var response = get(STATION_PATH);
        return response.jsonPath()
                .getList("id", Long.class);

    }

    private void 지하철역_삭제(Long id) {
        final String deletePath = STATION_PATH + "/" + id;
        delete(deletePath);
    }
}