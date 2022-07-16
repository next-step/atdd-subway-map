package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.applicaion.dto.StationLineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static nextstep.subway.api.ApiCall.*;

@DisplayName("지하철노선 관련 기능")
public class StationLineAcceptanceTest extends AcceptionceTest {

    @BeforeEach
    public void createMockData() {
        지하철역_생성("강남역");  // 1L
        지하철역_생성("역삼역");  // 2L
        지하철역_생성("수원역");  // 3L
        지하철역_생성("세류역");  // 4L
    }

    /**
     * when 지하철 노션을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철노선 생성")
    @Test
    void createStationLine() {
        // when 지하철 노선 생성
        지하철_노선_생성("2호선", "bg-green-600", 1L, 2L, 10);

        // 전체 지하철 노선 목록 조회
        List<String> stationList = 지하철_노선_목록_조회();

        StationAcceptanceTest.지하철역이_존재하는지_체크(stationList, "2호선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void findAllStationLineTest() {
        // given 2개의 지하철 노선 생성
        지하철_노선_생성("2호선", "bg-green-600", 1L, 2L, 5);
        지하철_노선_생성("1호선", "bg-blue-600", 3L, 4L, 10);

        //  when 지하철 노선 목록을 조회
        List<String> stationLineList = 지하철_노선_목록_조회();

        지하철_노선_사이즈_체크(stationLineList, 2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void findByStationLineByIdTest() {
        지하철_노선_생성("2호선", "bg-green-600", 1L, 2L, 5);

        // when
        String name = 지하철_노선_조회(1L).jsonPath().getString("name");

        동일한_값_인지_검증(name, "2호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateByStationLine() {
        지하철_노선_생성("2호선", "bg-green-600", 1L, 2L, 5);
        StationLineRequest 업데이트_내용 = StationLineRequest.builder()
            .name("신분당선")
            .color("bg-yellow-300")
            .build();

        // when -
        지하철_노선_업데이트(1L, 업데이트_내용);

        String name = 지하철_노선_조회(1L)
            .jsonPath().getString("name");

        동일한_값_인지_검증(name, "신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteByStationLine() {
        지하철_노선_생성("2호선", "bg-green-600", 1L, 2L, 5);

        지하철_노선_삭제(1L);

        List<String> stationList = 지하철_노선_목록_조회();

        StationAcceptanceTest.값이_포함되지_않는지_검증(stationList, "2호선");
    }

    private void 지하철_노선_사이즈_체크(List<String> stationLineList, int size) {
        assertThat(stationLineList).hasSize(size);
    }

    private void 동일한_값_인지_검증(String stationName, String name) {
        assertThat(stationName).isEqualTo(name);
    }

}
