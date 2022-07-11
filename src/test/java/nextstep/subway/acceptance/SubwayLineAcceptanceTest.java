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
        subwayLineCallApi.saveStation(Param.양재시민의숲역);
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
     * Given 지하철 노선을 등록하고
     * When 지하철 구간을 생성하면
     * Then 지하철 노선 조회 시 생성한 구간을 찾을 수 있다
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void createSubwaySection() {
        // given
        ExtractableResponse<Response> saveLineResponse = subwayLineCallApi.saveSubwayLine(Param.신분당선);

        // then
        assertThat(saveLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveLineResponse.header("Location")).isNotNull();

        // when
        Long lineId = Actual.get(saveLineResponse, "id", Long.class);
        ExtractableResponse<Response> saveSectionResponse = subwayLineCallApi.saveSubwaySection(lineId, Param.신분당선_구간);

        // then
        assertThat(saveSectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveSectionResponse.header("Location")).isNotNull();

        // then
        ExtractableResponse<Response> response = subwayLineCallApi.findSubwayLines();
        assertThat(Actual.getList(response, "stations.name", String.class)).isEqualTo(List.of("강남역","양재역","양재시민의숲역"));
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 하행 종점역에 등록되지 않은 상행역을 등록하면
     * Then 예외가 발생한다.
     */
    @DisplayName("상행역 등록 시 하행 종점역에 등록되지 않은 역인 경우 예외")
    @Test
    void given_createSubwaySection_when_upStationIdNotInLastStation_then_exception() {
        // given
        ExtractableResponse<Response> saveLineResponse = subwayLineCallApi.saveSubwayLine(Param.신분당선);

        // then
        assertThat(saveLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveLineResponse.header("Location")).isNotNull();

        // when
        Long lineId = Actual.get(saveLineResponse, "id", Long.class);
        ExtractableResponse<Response> saveResponse = subwayLineCallApi.saveSubwaySection(lineId, Param.신분당선_구간_잘못된_상행역);

        // then
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 이미 등록된 역을 하행역으로 등록하면
     * Then 예외가 발생한다.
     */
    @DisplayName("하행역 등록 시 이미 등록된 역인 경우 예외")
    @Test
    void given_createSubwaySection_when_downStationIdAlreadySaveStation_then_exception() {
        // given
        ExtractableResponse<Response> saveLineResponse = subwayLineCallApi.saveSubwayLine(Param.신분당선);

        // then
        assertThat(saveLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveLineResponse.header("Location")).isNotNull();

        // when
        Long lineId = Actual.get(saveLineResponse, "id", Long.class);
        ExtractableResponse<Response> saveResponse = subwayLineCallApi.saveSubwaySection(lineId, Param.신분당선_구간_잘못된_하행역);

        // then
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선, 지하철 구간을 등록하고
     * When 생성한 지하철 구간을 삭제하면
     * Then 해당 지하철 구간이 삭제된다.
     */
    @DisplayName("지하철 구간 삭제")
    @Test
    void deleteSubwaySection() {
        // given
        ExtractableResponse<Response> saveLineResponse = subwayLineCallApi.saveSubwayLine(Param.신분당선);

        // then
        assertThat(saveLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveLineResponse.header("Location")).isNotNull();

        // when
        Long lineId = Actual.get(saveLineResponse, "id", Long.class);
        ExtractableResponse<Response> saveSectionResponse = subwayLineCallApi.saveSubwaySection(lineId, Param.신분당선_구간);

        // then
        assertThat(saveSectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveSectionResponse.header("Location")).isNotNull();

        // when
        ExtractableResponse<Response> findResponse = subwayLineCallApi.findSubwayLineById(lineId);
        Long stationId = Actual.get(findResponse, "station[2].id", Long.class);
        ExtractableResponse<Response> response = subwayLineCallApi.deleteSubwaySectionById(lineId, stationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선, 지하철 구간을 등록하고
     * When 하행 종점역이 아닌 다른 역을 삭제하면
     * Then 예외가 발생한다.
     */
    @DisplayName("지하철 노선의 하행 종점역이 아닌 다른 역 삭제 시 예외")
    @Test
    void given_createSubwaySection_when_deleteNotLastStationId_then_exception() {
        // given
        ExtractableResponse<Response> saveLineResponse = subwayLineCallApi.saveSubwayLine(Param.신분당선);

        // then
        assertThat(saveLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveLineResponse.header("Location")).isNotNull();

        // when
        Long lineId = Actual.get(saveLineResponse, "id", Long.class);
        ExtractableResponse<Response> saveSectionResponse = subwayLineCallApi.saveSubwaySection(lineId, Param.신분당선_구간);

        // then
        assertThat(saveSectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveSectionResponse.header("Location")).isNotNull();

        // when
        Long stationId = Actual.get(saveSectionResponse, "stations[2].id", Long.class);
        ExtractableResponse<Response> response = subwayLineCallApi.deleteSubwaySectionById(lineId, stationId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 구간이 1개인 상태에서 삭제하면
     * Then 예외가 발생한다.
     */
    @DisplayName("지하철 구간이 1개인 경우 삭제 시 예외")
    @Test
    void given_createSubwaySection_when_deleteOneOfSection_then_exception() {
        // given
        ExtractableResponse<Response> saveLineResponse = subwayLineCallApi.saveSubwayLine(Param.신분당선);

        // then
        assertThat(saveLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveLineResponse.header("Location")).isNotNull();

        // when
        Long lineId = Actual.get(saveLineResponse, "id", Long.class);
        Long stationId = Actual.get(saveLineResponse, "stations[1].id", Long.class);
        ExtractableResponse<Response> response = subwayLineCallApi.deleteSubwaySectionById(lineId, stationId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

        public static final Map<String, Object> 신분당선_구간 = Map.of(
                "upStationId", 2,
                "downStationId", 4,
                "distance", 10
        );

        public static final Map<String, Object> 신분당선_구간_잘못된_상행역 = Map.of(
                "upStationId", 5,
                "downStationId", 4,
                "distance", 10
        );

        public static final Map<String, Object> 신분당선_구간_잘못된_하행역 = Map.of(
                "upStationId", 2,
                "downStationId", 1,
                "distance", 10
        );

        public static final Map<String, String> 강남역 = Map.of("name", "강남역");

        public static final Map<String, String> 양재역 = Map.of("name", "양재역");

        public static final Map<String, String> 역삼역 = Map.of("name", "역삼역");

        public static final Map<String, String> 양재시민의숲역 = Map.of("name", "양재시민의숲역");
    }
}
