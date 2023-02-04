package subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import subway.controller.request.LineRequest;
import subway.controller.request.SectionRequest;
import subway.util.AcceptanceTestHelper;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static subway.fixture.LineFixture.신분당선_요청;
import static subway.fixture.SectionFixture.역삼역_교대역_구간_요청;
import static subway.fixture.StationFixture.강남역_ID;
import static subway.fixture.StationFixture.교대역_ID;
import static subway.fixture.StationFixture.역삼역_ID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@DisplayName("지하철 구간 관련 기능")
class SectionAcceptanceTest extends AcceptanceTestHelper {

    @BeforeEach
    void setup() {
        지하철역_생성("강남역");
        지하철역_생성("역삼역");
        지하철역_생성("교대역");
        지하철역_생성("양재역");
    }

    /**
     * Given -> 구간이 포함되어있는 지하철 노선을 생성하고
     * When -> 해당 노선에 구간을 등록하면
     * Then -> 해당 노선 조회 시 추가된 구간이 포함되어 있다.
     */
    @DisplayName("지하철 노선에 구간을 등록한다")
    @Test
    void addSection() {
        // given
        var createResponse = 지하철노선_생성(신분당선_요청);
        String location = 경로_추출(createResponse);

        구간_등록(location, 역삼역_교대역_구간_요청);

        assertThat(노선_지하철역_ID_목록_조회(location)).containsExactly(강남역_ID, 역삼역_ID, 교대역_ID);
    }

    private ExtractableResponse<Response> 구간_등록(String path, SectionRequest request) {
        return post(path + SECTION_PATH, request);
    }

    private List<Long> 노선_지하철역_ID_목록_조회(String location) {
        return get(location)
                .jsonPath()
                .getList("stations.id", Long.class);
    }

    public ExtractableResponse<Response> 지하철노선_생성(final LineRequest req) {
        return post(LINE_PATH, req);
    }

    public long 지하철역_생성(String name) {

        return post(STATION_PATH, Map.of("name", name))
                .jsonPath().getLong("id");
    }
}
