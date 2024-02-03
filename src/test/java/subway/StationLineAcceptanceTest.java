package subway;

import config.fixtures.subway.StationLineMockData;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.StationLineRequest;
import subway.dto.StationLineResponse;
import subway.entity.StationLine;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static config.fixtures.subway.StationMockData.역_10개;
import static org.assertj.core.api.Assertions.assertThat;
import static subway.StationLineSteps.*;
import static subway.StationSteps.지하철_역_생성_요청;
import static utils.HttpResponseUtils.getCreatedLocationId;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineAcceptanceTest {

    public static final String ID_KEY = "id";
    public static final String NAME_KEY = "name";
    public static final String COLOR_KEY = "color";
    public static final String UP_STATION_ID_KEY = "upStationId";
    public static final String DOWN_STATION_ID_KEY = "downStationId";
    public static final String DISTANCE_KEY = "distance";

    @BeforeEach
    void 초기_지하철_역_설정() {
        지하철_역_생성_요청(역_10개);
    }

    /**
     * When 지하철 노선을 생성하면
     * When  지하철 노선이 생성된다
     * Then  지하철 노선 목록 조회 시 생성된 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStationLine() {
        // given
        StationLineRequest 신분당선 = StationLineMockData.신분당선;

        // when
        지하철_노선_생성_요청_검증_포함(신분당선);

        // then
        assertThat(convertStationLineResponses(모든_지하철_노선_조회_요청())).usingRecursiveComparison()
                .ignoringFields("id", "stations")
                .isEqualTo(List.of(신분당선));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When  지하철 노선 목록을 조회하면
     * Then  지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findAllStationLine() {
        // given
        StationLineRequest 신분당선 = StationLineMockData.신분당선;
        StationLineRequest 분당선 = StationLineMockData.분당선;

        지하철_노선_생성_요청_검증_포함(신분당선);
        지하철_노선_생성_요청_검증_포함(분당선);

        // when
        assertThat(convertStationLineResponses(모든_지하철_노선_조회_요청())).usingRecursiveComparison()
                .ignoringFields("id", "stations")
                .isEqualTo(List.of(신분당선, 분당선));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When  생성한 지하철 노선을 조회하면
     * Then  생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findStationLine() {
        // given
        StationLineRequest 신분당선 = StationLineMockData.신분당선;
        ExtractableResponse<Response> response = 지하철_노선_생성_요청_검증_포함(신분당선);

        // when, then
        assertThat(convertStationLineResponse(지하철_노선_조회_요청(getCreatedLocationId(response)))).usingRecursiveComparison()
                .ignoringFields("id", "stations")
                .isEqualTo(신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When  생성한 지하철 노선을 수정하면
     * Then  해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateStationLine() {
        // given
        StationLineRequest 신분당선 = StationLineMockData.신분당선;
        StationLineRequest 수정된_신분당선 = StationLineMockData.수정된_신분당선;

        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청_검증_포함(신분당선);

        // when
        지하철_노선_수정_요청(수정된_신분당선, getCreatedLocationId(createResponse));

        // then
        assertThat(convertStationLineResponse(지하철_노선_조회_요청(getCreatedLocationId(createResponse)))).usingRecursiveComparison()
                .ignoringFields("id", "stations")
                .isEqualTo(수정된_신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When  생성한 지하철 노선을 삭제하면
     * Then  해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteStationLine() {
        // given
        StationLineRequest 신분당선 = StationLineMockData.신분당선;
        StationLineRequest 분당선 = StationLineMockData.분당선;

        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청_검증_포함(신분당선);
        지하철_노선_생성_요청_검증_포함(분당선);

        // when
        지하철_노선_삭제_요청(getCreatedLocationId(createResponse));

        // then
        assertThat(convertStationLineResponses(모든_지하철_노선_조회_요청())).usingRecursiveComparison()
                .ignoringFields("id", "stations")
                .isEqualTo(List.of(분당선));
    }

    private List<StationLineResponse> convertStationLineResponses(JsonPath jsonPath) {
        List<Long> ids = jsonPath.getList(ID_KEY, Long.class);
        List<String> names = jsonPath.getList(NAME_KEY, String.class);
        List<String> colors = jsonPath.getList(COLOR_KEY, String.class);

        return IntStream.range(0, names.size())
                .mapToObj(i -> new StationLineResponse(
                        ids.get(i),
                        names.get(i),
                        colors.get(i)
                ))
                .collect(Collectors.toList());
    }

    /**
     * 주어진 JsonPath로 부터 StationLineResponse 객체를 만들어서 반환
     *
     * @param jsonPath JSON 응답 객체
     * @return StationLineResponse 객체
     */
    private StationLineResponse convertStationLineResponse(JsonPath jsonPath) {
        return new StationLineResponse(
                jsonPath.getLong(ID_KEY),
                jsonPath.get(NAME_KEY).toString(),
                jsonPath.get(COLOR_KEY).toString()
        );
    }
}
