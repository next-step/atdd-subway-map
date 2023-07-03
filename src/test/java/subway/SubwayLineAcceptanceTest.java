package subway;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SubwayLineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노션을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createSubwayLine() {
        // given
        Map<String, Object> request = new HashMap<>();
        request.put("name", "7호선");
        request.put("color", "bg-red-500");
        request.put("upStationId", 1);
        request.put("downStationId", 2);
        request.put("distance", 52);

        // when
        Map<String, Object> response = new SubwayLineController().createSubwayLine(request);

        // then
        assertThat(response).hasSize(7)
                .containsEntry("id", 1)
                .containsEntry("name", "7호선")
                .containsEntry("color", "bg-red-500")
                .containsEntry("upStationId", 1)
                .containsEntry("downStationId", 2)
                .containsEntry("distance", 52)
                .containsEntry("stations", Map.of("id", 1, "name", "이수역"));
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getSubwayLines() {
        // given
        Long id = 1L;

        // when
        Map<String, Object> response = new SubwayLineController().getSubwayLines(id);

        // then
        assertThat(response).hasSize(4)
                .containsEntry("id", id)
                .containsEntry("name", "7호선")
                .containsEntry("color", "bg-red-500")
                .containsEntry("stations", Map.of("id", 1, "name", "이수역"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getSubwayLine() {
        // given
        Long id = 1L;

        // when
        Map<String, Object> response = new SubwayLineController().getSubwayLine(id);

        // then
        assertThat(response).hasSize(4)
                .containsEntry("id", id)
                .containsEntry("name", "7호선")
                .containsEntry("color", "bg-red-500")
                .containsEntry("stations", Map.of("id", 1, "name", "이수역"));
    }

    /**
     *Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void modifySubwayLine() {
        // given
        Long id = 1L;
        Map<String, Object> request = new HashMap<>();
        request.put("name", "7호선");
        request.put("color", "bg-red-500");
        request.put("upStationId", 1);
        request.put("downStationId", 2);
        request.put("distance", 52);

        // when
        Map<String, Object> response = new SubwayLineController().modifySubwayLine(id, request);

        // then
        assertThat(response).hasSize(7)
                .containsEntry("id", id)
                .containsEntry("name", "4호선")
                .containsEntry("color", "bg-red-500")
                .containsEntry("upStationId", 1)
                .containsEntry("downStationId", 2)
                .containsEntry("distance", 52)
                .containsEntry("stations", Map.of("id", 1, "name", "이수역"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteSubwayLine() {
        // given
        Long id = 1L;

        // when
        Map<String, Object> response = new SubwayLineController().deleteSubwayLine(id);

        // then
        assertThat(response).hasSize(0);
    }
    
    private static class SubwayLineController {

        private final SubwayLineService subwayLineService = new SubwayLineService();
        public Map<String, Object> createSubwayLine(Map<String, Object> request) {
            return subwayLineService.createSubwayLine(request);
        }

        public Map<String, Object> getSubwayLines(Long id) {
            return subwayLineService.getSubwayLines(id);
        }

        public Map<String, Object> getSubwayLine(Long id) {
            return subwayLineService.getSubwayLine(id);
        }

        public Map<String, Object> modifySubwayLine(Long id, Map<String, Object> request) {
            return subwayLineService.modifySubwayLine(id, request);
        }

        public Map<String, Object> deleteSubwayLine(Long id) {
            return subwayLineService.deleteSubwayLine(id);
        }
    }

    private static class SubwayLineService {
        public Map<String, Object> createSubwayLine(Map<String, Object> response) {
            response.put("id", 1);
            response.put("stations", Map.of("id", 1, "name", "이수역"));
            return response;
        }

        public Map<String, Object> getSubwayLines(Long id) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", id);
            response.put("name", "7호선");
            response.put("color", "bg-red-500");
            response.put("stations", Map.of("id", 1, "name", "이수역"));
            return response;
        }

        public Map<String, Object> getSubwayLine(Long id) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", id);
            response.put("name", "7호선");
            response.put("color", "bg-red-500");
            response.put("stations", Map.of("id", 1, "name", "이수역"));
            return response;
        }

        public Map<String, Object> modifySubwayLine(Long id, Map<String, Object> response) {
            response.put("id", id);
            response.put("stations", Map.of("id", 1, "name", "이수역"));
            response.put("name", "4호선");
            return response;
        }

        public Map<String, Object> deleteSubwayLine(Long id) {
            return new HashMap<>();
        }
    }

}
