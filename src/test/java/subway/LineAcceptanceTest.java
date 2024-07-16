package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.presentation.LineRequest;
import subway.presentation.LineResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LineAcceptanceTest {
    private Long 강남역;
    private Long 을지로4가역;
    private Long 또다른역;
    private LineRequest sinbundangLineRequest;

    @BeforeEach
    void setup() {
        강남역 = Long.valueOf(StationSteps.createStation("강남역").body().jsonPath().getString("id"));
        을지로4가역 = Long.valueOf(StationSteps.createStation("을지로4가역").body().jsonPath().getString("id"));
        또다른역 = Long.valueOf(StationSteps.createStation("또다른역").body().jsonPath().getString("id"));
        sinbundangLineRequest = new LineRequest("신분당선", "bg-red-600", 강남역, 을지로4가역, 10);
    }

    /**
     * Given: 새로운 지하철 노선 정보를 입력하고,
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        // given
        LineRequest newLine = new LineRequest("신분당선", "bg-red-600", 강남역, 을지로4가역, 10);

        // when
        ExtractableResponse<Response> response = LineSteps.createLine(newLine);

        // then
        List<String> allLineNames = LineSteps.findAllLineNames();
        Assertions.assertThat(allLineNames).containsAnyOf("신분당선");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given: 여러 개의 지하철 노선이 등록되어 있고,
     * When: 관리자가 지하철 노선 목록을 조회하면,
     * Then: 모든 지하철 노선 목록이 반환된다.
     */
    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void retrieveAllLines() {
        // given
        LineRequest fifthLineRequest = new LineRequest("5호선", "bg-purple-400", 강남역, 또다른역, 10);

        LineSteps.createLine(sinbundangLineRequest);
        LineSteps.createLine(fifthLineRequest);

        // when
        List<String> allLineNames = LineSteps.findAllLineNames();

        // then
        Assertions.assertThat(allLineNames).contains("신분당선", "5호선");
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */
    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void retrieveLine() {
        // given
        LineRequest fifthLineRequest = new LineRequest("5호선", "bg-purple-400", 강남역, 또다른역, 10);

        LineSteps.createLine(sinbundangLineRequest);

        ExtractableResponse<Response> response = LineSteps.createLine(fifthLineRequest);
        Long fifthLineId = Long.valueOf(response.body().jsonPath().getString("id"));

        // when
        LineResponse findline = LineSteps.findByLineId(fifthLineId);

        // then
        assertThat(findline.getName()).isEqualTo("5호선");
        assertThat(findline.getColor()).isEqualTo("bg-purple-400");
        assertThat(findline.getStations().size()).isEqualTo(2);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 수정하면,
     * Then: 해당 노선의 정보가 수정된다.
     */
    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // given
        ExtractableResponse<Response> response = LineSteps.createLine(sinbundangLineRequest);
        Long sinbundangLineId = Long.valueOf(response.body().jsonPath().getString("id"));

        // when
        LineSteps.updateLine(sinbundangLineId, "신분당선2호선", "bg-red-700");
        LineResponse findline = LineSteps.findByLineId(sinbundangLineId);

        // then
        assertThat(findline.getName()).isEqualTo("신분당선2호선");
        assertThat(findline.getColor()).isEqualTo("bg-red-700");
        assertThat(findline.getStations().size()).isEqualTo(2);
    }


    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 삭제하면,
     * Then: 관리자가 해당 노선을 삭제하면,
     */
    @Test
    @DisplayName("지하철 노선을 삭제한다.")
    void deleteLine() {
        // given
        ExtractableResponse<Response> response = LineSteps.createLine(sinbundangLineRequest);
        String sinbundangLineId = response.body().jsonPath().getString("id");

        // when
        LineSteps.deleteLine(sinbundangLineId);
        List<String> allLineNames = LineSteps.findAllLineNames();

        // then
        Assertions.assertThat(allLineNames).doesNotContain("신분당선");
        Assertions.assertThat(allLineNames.size()).isEqualTo(0);
    }

}
