package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class SubwayLineAcceptanceTest extends AcceptanceTest {

    public static final String[] CLEAN_UP_TABLES = {"subway_line", "station"};

    @Autowired
    private CleanUpSchema cleanUpSchema;

    private SubwayLineCallApi subwayLineCallApi;

    public SubwayLineAcceptanceTest() {
        this.subwayLineCallApi = new SubwayLineCallApi();
    }

    @Override
    protected void preprocessing() {
        cleanUpSchema.execute(CLEAN_UP_TABLES);

        subwayLineCallApi.saveStation(Param.강남역);
        subwayLineCallApi.saveStation(Param.양재역);
        subwayLineCallApi.saveStation(Param.역삼역);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createSubwayLine() {
        // when
        ExtractableResponse<Response> saveResponse = subwayLineCallApi.saveSubwayLine(Param.신분당선);

        // then
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveResponse.header("Location")).isNotEmpty();

        // then
        ExtractableResponse<Response> response = subwayLineCallApi.findSubwayLines();
        assertThat(Actual.get(response, "[0].name", String.class)).isEqualTo("신분당선");
        assertThat(Actual.get(response, "[0].color", String.class)).isEqualTo("bg-red-600");
        assertThat(Actual.getList(response, "[0].stations.name", String.class)).isEqualTo(List.of("강남역","양재역"));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getSubwayLines() {
        // given
        subwayLineCallApi.saveSubwayLine(Param.신분당선);
        subwayLineCallApi.saveSubwayLine(Param.이호선);

        // when
        ExtractableResponse<Response> response = subwayLineCallApi.findSubwayLines();

        // then
        assertThat(Actual.getList(response, "name", String.class)).isEqualTo(List.of("신분당선", "2호선"));
        assertThat(Actual.getList(response, "color", String.class)).isEqualTo(List.of("bg-red-600", "bg-green-600"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getSubwayLine() {
        // given
        ExtractableResponse<Response> saveResponse = subwayLineCallApi.saveSubwayLine(Param.신분당선);

        // when
        Long id = Actual.get(saveResponse, "id", Long.class);
        ExtractableResponse<Response> response = subwayLineCallApi.findSubwayLineById(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(Actual.get(response, "name", String.class)).isEqualTo("신분당선");
        assertThat(Actual.get(response, "color", String.class)).isEqualTo("bg-red-600");
        assertThat(Actual.getList(response, "stations.name", String.class)).isEqualTo(List.of("강남역", "양재역"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void modifySubwayLine() {
        // given
        ExtractableResponse<Response> saveResponse = subwayLineCallApi.saveSubwayLine(Param.신분당선);

        // when
        Long id = Actual.get(saveResponse, "id", Long.class);
        ExtractableResponse<Response> modifyResponse = subwayLineCallApi.modifySubwayLineById(id, Param.이호선으로_수정);

        // then
        assertThat(modifyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> response = subwayLineCallApi.findSubwayLineById(id);
        assertThat(Actual.get(response, "name", String.class)).isEqualTo("2호선");
        assertThat(Actual.get(response, "color", String.class)).isEqualTo("bg-green-600");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteSubwayLine() {
        // given
        ExtractableResponse<Response> saveResponse = subwayLineCallApi.saveSubwayLine(Param.신분당선);

        // when
        Long id = Actual.get(saveResponse, "id", Long.class);
        ExtractableResponse<Response> response = subwayLineCallApi.deleteSubwayLineById(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * 검증 비교 대상 값 관련 클래스
     */
    private static class Actual {
        private static final Map<Class<?>, BiFunction<ExtractableResponse<Response>, String, ?>> EXTRACT_INFO_AT_SUBWAY_LINE_FUNCTIONS = Map.of(
                Long.class, (response, path) -> response.body().jsonPath().getLong(path),
                String.class, (response, path) -> response.body().jsonPath().getString(path)
        );

        /**
         * type에 해당하는 값을 반환
         * @param response
         * @param path
         * @param type
         * @param <T>
         * @return
         */
        private static <T> T get(ExtractableResponse<Response> response, String path, Class<T> type) {
            return (T) EXTRACT_INFO_AT_SUBWAY_LINE_FUNCTIONS.get(type).apply(response, path);
        }

        /**
         * type에 해당하는 리스트 반환
         * @param response
         * @param path
         * @param type
         * @param <T>
         * @return
         */
        private static <T> List<T> getList(ExtractableResponse<Response> response, String path, Class<T> type) {
            return response.jsonPath().getList(path, type);
        }
    }

    /**
     * 파라미터 관련 클래스
     */
    private static class Param {
        public static final Map<String, Object> 신분당선 = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        );

        public static final Map<String, Object> 이호선 = Map.of(
                "name", "2호선",
                "color", "bg-green-600",
                "upStationId", 1,
                "downStationId", 3,
                "distance", 8
        );

        public static final Map<String, String> 이호선으로_수정 = Map.of(
                "name", "2호선",
                "color", "bg-green-600"
        );

        public static final Map<String, String> 강남역 = Map.of("name", "강남역");

        public static final Map<String, String> 양재역 = Map.of("name", "양재역");

        public static final Map<String, String> 역삼역 = Map.of("name", "역삼역");
    }
}
