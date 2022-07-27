package nextstep.subway.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.utils.DatabaseInitializer;
import nextstep.subway.acceptance.utils.LineAcceptanceTestUtils;
import nextstep.subway.acceptance.utils.StationAcceptanceTestUtils;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static nextstep.subway.acceptance.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@ActiveProfiles("test")
public class LineAcceptanceTest extends BaseTest {
    private static final String LINE_NAME_5 = "5호선";
    private static final String LINE_COLOR_5 = "#996CAC";
    private static final Long LINE_DISTANCE_5 = 48L;
    private static final String LINE_NAME_5_UP = "5호선 상행선";
    private static final String LINE_COLOR_5_UP = "#996CAD";
    private static final String LINE_NAME_9 = "9호선";
    private static final String LINE_COLOR_9 = "#BDB092";
    private static final Long LINE_DISTANCE_9 = 37L;

    private static final String LINE_NOTFOUND_MESSAGE = "해당 id의 지하철 노선이 존재하지 않습니다.";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DatabaseInitializer databaseInitializer;

    private final StationAcceptanceTestUtils stationAcceptanceTestUtils = new StationAcceptanceTestUtils();

    private final LineAcceptanceTestUtils lineAcceptanceTestUtils = new LineAcceptanceTestUtils();


    private LineRequest LINE_5;
    private LineRequest LINE_9;

    @BeforeEach
    public void setUp() {
        Long upStationId1 = stationAcceptanceTestUtils.지하철_역_생성(STATION_NAME1).jsonPath().getLong("id");
        Long downStationId1 = stationAcceptanceTestUtils.지하철_역_생성(STATION_NAME2).jsonPath().getLong("id");
        Long upStationId2 = stationAcceptanceTestUtils.지하철_역_생성(STATION_NAME3).jsonPath().getLong("id");
        Long downStationId2 = stationAcceptanceTestUtils.지하철_역_생성(STATION_NAME4).jsonPath().getLong("id");
        LINE_5 = new LineRequest(LINE_NAME_5, LINE_COLOR_5, upStationId1, downStationId1, LINE_DISTANCE_5);
        LINE_9 = new LineRequest(LINE_NAME_9, LINE_COLOR_9, upStationId2, downStationId2, LINE_DISTANCE_9);
    }

    @AfterEach
    public void initializeTables() {
        databaseInitializer.execute();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선이 생성된다
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = lineAcceptanceTestUtils.지하철_노선_생성(LINE_5);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().get("name").equals(LINE_NAME_5)).isTrue();

        // then
        List<String> lineNames = lineAcceptanceTestUtils.지하철_노선_목록_조회()
                .jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf(LINE_NAME_5);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showAllLines() {
        // given
        lineAcceptanceTestUtils.지하철_노선_생성(LINE_5);
        lineAcceptanceTestUtils.지하철_노선_생성(LINE_9);

        // when
        ExtractableResponse<Response> response = lineAcceptanceTestUtils.지하철_노선_목록_조회();

        // then
        assertThat(response.jsonPath().getList("name").size()).isEqualTo(2);
        assertThat(response.jsonPath().getList("name")).containsExactly(LINE_5.getName(), LINE_9.getName());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findLine() {
        // given
        Long id = lineAcceptanceTestUtils.지하철_노선_생성(LINE_5).jsonPath().getLong("id");
        lineAcceptanceTestUtils.지하철_노선_생성(LINE_9);

        // when
        ExtractableResponse<Response> response = lineAcceptanceTestUtils.지하철_노선_조회(id);

        // then
        assertThat(response.jsonPath().get("name").equals(LINE_5.getName())).isTrue();
        assertThat(response.jsonPath().get("color").equals(LINE_5.getColor())).isTrue();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성되지 않은 지하철 노선을 조회하면
     * Then IllegalArgumentException 와 ErrorResponse 를 응답받을 수 있다.
     */
    @DisplayName("존재하지 않는 지하철 노선을 조회한다.")
    @Test
    void findLineIllegalArgumentException() {
        // given
        lineAcceptanceTestUtils.지하철_노선_생성(LINE_5);

        // when
        ExtractableResponse<Response> response = lineAcceptanceTestUtils.지하철_노선_조회(2L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo(LINE_NOTFOUND_MESSAGE);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() throws JsonProcessingException {
        // given
        LineResponse initialResponse = objectMapper.readValue(
                        lineAcceptanceTestUtils.지하철_노선_생성(LINE_5).body().asString(),
                        LineResponse.class);

        LineRequest request = LineRequest.builder()
                .name(LINE_NAME_5_UP)
                .color(LINE_COLOR_5_UP)
                .build();

        // when
        ExtractableResponse<Response> updatedResponse
                = lineAcceptanceTestUtils.지하철_노선_수정(initialResponse.getId(), request);
        ExtractableResponse<Response> response = lineAcceptanceTestUtils.지하철_노선_조회(initialResponse.getId());

        LineResponse expectedLineResponse = objectMapper.readValue(response.body().asString(), LineResponse.class);
        initialResponse.updateResponse(request);

        // then
        assertThat(updatedResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(expectedLineResponse).isEqualTo(initialResponse);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        Long id = lineAcceptanceTestUtils.지하철_노선_생성(LINE_5).jsonPath().getLong("id");
        lineAcceptanceTestUtils.지하철_노선_생성(LINE_9);

        // when
        lineAcceptanceTestUtils.지하철_노선_삭제(id);

        // then
        List<Long> ids = lineAcceptanceTestUtils.지하철_노선_목록_조회().jsonPath().getList("id", Long.class);
        assertThat(ids.stream()
                .filter(lineId -> lineId == id)
                .count())
                .isEqualTo(0);
    }
}
