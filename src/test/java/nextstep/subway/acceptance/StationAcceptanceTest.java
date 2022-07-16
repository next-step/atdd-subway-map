package nextstep.subway.acceptance;

import org.junit.jupiter.api.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static nextstep.subway.api.ApiCall.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptionceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when 지하철역 생성
        지하철역_생성("강남역");

        List<String> stationList = 지하철역_목록_조회();

        지하철역이_존재하는지_체크(stationList, "강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // when - 지하철역 생성
        지하철역_생성("강남역");
        지하철역_생성("선릉역");

        List<String> stationList = 지하철역_목록_조회();

        목록개수_확인(stationList, 2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        Long id = 지하철역_생성("강남역");

        // when
        지하철역_삭제(id);

        // then - 지하철 목록을 찾을 수 없는지 확인
        List<String> stationList = 지하철역_목록_조회();

        값이_포함되지_않는지_검증(stationList, "강남역");
    }

    public static void 지하철역이_존재하는지_체크(List<String> stationList, String name) {
        assertThat(stationList).containsExactly(name);
    }

    public static void 목록개수_확인(List<String> stationList, int size) {
        assertThat(stationList).hasSize(size);
    }

    public static void 값이_포함되지_않는지_검증(List<String> stationList, String name) {
        assertThat(stationList).doesNotContain(name);
    }

}