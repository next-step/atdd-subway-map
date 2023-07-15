package subway;

import helper.RequestApiHelper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import subway.dto.LineRequest;
import subway.model.Station;

import java.util.List;
import java.util.Map;

import static helper.LineTestHelper.지하철노선을_생성한다;
import static helper.LineTestHelper.지하철노선을_조회한다;
import static helper.StationTestHelper.지하철역을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SectionAcceptanceTest {

    private final LineRequest LINE_1 = new LineRequest.Builder()
            .name("신분당선")
            .color("bg-red-600")
            .upStationId(1L)
            .downStationId(2L)
            .distance(10L)
            .build();

    @BeforeEach
    void createStations() {
        지하철역을_생성한다("지하철역");
        지하철역을_생성한다("새로운지하철역");
        지하철역을_생성한다("또다른지하철역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 해당 노선의 구간 목록을 조회하면
     * Then 생성한 구간을 찾을 수 있다.
     */
    @DisplayName("노선 생성 시 기본 구간 등록")
    @Test
    void createDefaultSection() {
        // given
        String lineId = 지하철노선을_생성한다(LINE_1)
                .jsonPath()
                .get("id")
                .toString();

        // when
        List<Long> sections = 지하철구간목록을_조회한다(lineId);

        // then
        assertThat(sections).hasSize(1);
    }

    private static List<Long> 지하철구간목록을_조회한다(String lineId) {
        return RequestApiHelper.get("/lines/" + lineId + "/sections")
                .jsonPath()
                .getList("id", Long.class);
    }


    /**
     * Given 지하철 노선에 새로운 구간을 등록하고,
     * When 해당 노선의 구간 목록과 노선을 조회하면
     * Then 생성한 구간을 찾을 수 있다.
     * Then 노선의 하행 종점역과 거리가 업데이트 된다.
     */
    @DisplayName("구간 등록 기능")
    @Test
    void addSection() {
        // given
        String lineId = 지하철노선을_생성한다(LINE_1)
                .jsonPath()
                .get("id")
                .toString();

        Map<String, Object> params = Map.of(
                "upStationId", 2,
                "downStationId", 3,
                "distance", 10
        );
        String sectionId = RequestApiHelper.post("/lines/" + lineId + "/sections", params)
                .jsonPath()
                .get("id")
                .toString();

        // when
        List<Long> sections = 지하철구간목록을_조회한다(lineId);
        ExtractableResponse<Response> line = 지하철노선을_조회한다(lineId);

        // then
        assertThat(sections).contains(Long.valueOf(sectionId));
        Long downStationId = line.jsonPath().getList("stations", Station.class).get(1).getId();
        assertThat(downStationId).isEqualTo(3);
        assertThat(line.jsonPath().getObject("distance", Long.class)).isEqualTo(20);
    }
}
