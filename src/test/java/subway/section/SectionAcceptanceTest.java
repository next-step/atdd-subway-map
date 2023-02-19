package subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.db.AcceptanceTest;
import subway.line.LineRequest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.station.StationStep.지하철역_생성_요청;
import static  subway.section.SectionStep.지하철_구간_생성_요청;
import static  subway.section.SectionStep.지하철_구간_삭제_요청;
import static  subway.line.LineStep.지하철_노선_생성_요청;
import static  subway.line.LineStep.지하철_노선_조회_요청;

@DisplayName("지하철 구간 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest extends AcceptanceTest {

    private Long 신분당선;
    private Long 강남역;
    private Long 양재역;

    @BeforeEach
    public void setUp(){
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        Map<String, String> lineParam;
        lineParam = new HashMap<>();
        lineParam.put("name", "신분당선");
        lineParam.put("color", "bg-red-600");
        lineParam.put("upStationId", 강남역 + "");
        lineParam.put("downStationId", 양재역 + "");
        lineParam.put("distance", 10 + "");
        신분당선 = 지하철_노선_생성_요청(lineParam).jsonPath().getLong("id");
    }

    /**
     * When 지하철 노선에 구간 추가 요청
     * Then 노선에 새로운 구간이 추가
     */
    @DisplayName("지하철 노선에 구간을 등록")
    @Test
    void addSectionToLine() {
        // When 지하철 노선에 구간 추가 요청
        Long 정자역 = 지하철역_생성_요청("정자역").jsonPath().getLong("id");
        Map<String, String> param = new HashMap<>();
        param.put("upStationId", 양재역 + "");
        param.put("downStationId", 정자역 + "");
        param.put("distance", 6 + "");
        ExtractableResponse<Response> createResponse = 지하철_구간_생성_요청(신분당선, param);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // Then 노선에 새로운 구간이 추가
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 양재역, 정자역);
    }

    /**
     * Given 새로운 지하철 구간 생성 요청
     * When 지하철 노선에서 구간을 삭제 요청
     * Then 구간이 삭제됨
     */
    @DisplayName("지하철 노선에서 구간을 삭제")
    @Test
    void removeSectionToLine() {
        // Given 새로운 지하철 구산 생성 요청
        Long 정자역 = 지하철역_생성_요청("정자역").jsonPath().getLong("id");
        Map<String, String> createParam = new HashMap<>();
        createParam.put("upStationId", 양재역 + "");
        createParam.put("downStationId", 정자역 + "");
        createParam.put("distance", 6 + "");
        ExtractableResponse<Response> createResponse = 지하철_구간_생성_요청(신분당선, createParam);

        // When 지하철 노선에서 구간을 삭제 요청
        지하철_구간_삭제_요청(신분당선, 정자역);

        // Then 구간이 삭제 됨
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 양재역);
    }
}