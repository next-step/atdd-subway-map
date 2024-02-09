package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import utils.StationLineManager;
import utils.StationManager;

@DisplayName("지하철역 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@Sql("/truncate.sql")  // 어떤 방식이 더 효율적인걸까..?
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StationLineAcceptanceTest {

    @BeforeEach
    void setUp() {
        StationManager.save("지하철역");
        StationManager.save("새로운지하철역");
        StationManager.save("또다른지하철역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    // TODO: 지하철 노선 생성 인수 테스트 메서드 생성
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStationLine() {
        // when
        ExtractableResponse<Response> response = StationLineManager.save("신분당선", "bg-red-600", 1L, 2L, 10L);

        // then
        String result = response.jsonPath().getString("name");
        Assertions.assertThat(result).isEqualTo("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    // TODO: 지하철 노선 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findAllStationLine() {
        // given
        StationLineManager.save("신분당선", "bg-red-600", 1L, 2L, 10L);
        StationLineManager.save("분당선", "bg-green-600", 1L, 3L, 10L);

        // when
        ExtractableResponse<Response> response = StationLineManager.findAll();

        // then
        List<String> stations = response.jsonPath().getList("name", String.class);
        Assertions.assertThat(stations).containsExactly("신분당선", "분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    // TODO: 지하철 노선 조회 테스트 메서드 생성
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findStationLine() {
        // given
        StationLineManager.save("신분당선", "bg-red-600", 1L, 2L, 10L);

        // when
        ExtractableResponse<Response> response = StationLineManager.findById(1L);

        // then
        String result = response.jsonPath().getString("name");
        Assertions.assertThat(result).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    // TODO: 지하철 노선 수정 인수 테스트 메서드 생성
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateStationLine() {
        // given
        StationLineManager.save("신분당선", "bg-red-600", 1L, 2L, 10L);;

        // when
        StationLineManager.update(1L, "다른 분당선", "bg-red-600");

        // then
        ExtractableResponse<Response> response = StationLineManager.findById(1L);
        Assertions.assertThat(response.jsonPath().getString("name")).isEqualTo("다른 분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    // TODO: 지하철 노선 삭제 인수 테스트 메서드 생성
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteStationLine() {
        // given
        StationLineManager.save("신분당선", "bg-red-600", 1L, 2L, 10L);

        // when
        StationLineManager.delete(1L);
    }
}
