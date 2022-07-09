package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@Transactional(readOnly = true)
public class SubwayLineAcceptanceTest extends AcceptanceTest {

    public static final List<String> CLEAN_UP_TABLES = List.of("subway_line", "station");

    @PersistenceContext
    private EntityManager entityManager;

    private CallApi callApi;

    public SubwayLineAcceptanceTest() {
        this.callApi = new CallApi();
    }

    @Override
    protected void settings() {
        cleanUp();

        callApi.saveStation(Param.강남역);
        callApi.saveStation(Param.양재역);
        callApi.saveStation(Param.역삼역);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createSubwayLine() {
        // when
        ExtractableResponse<Response> saveResponse = callApi.saveSubwayLine(Param.신분당선);
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveResponse.header("Location")).isNotEmpty();

        // then
        ExtractableResponse<Response> response = callApi.findSubwayLines();
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
        callApi.saveSubwayLine(Param.신분당선);
        callApi.saveSubwayLine(Param.이호선);

        // when
        ExtractableResponse<Response> response = callApi.findSubwayLines();

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
        ExtractableResponse<Response> saveResponse = callApi.saveSubwayLine(Param.신분당선);

        // when
        Long id = Actual.get(saveResponse, "id", Long.class);
        ExtractableResponse<Response> response = callApi.findSubwayLineById(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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
        ExtractableResponse<Response> saveResponse = callApi.saveSubwayLine(Param.신분당선);

        // when
        Long id = Actual.get(saveResponse, "id", Long.class);
        ExtractableResponse<Response> modifyResponse = callApi.modifySubwayLineById(id, Param.이호선으로_수정);
        assertThat(modifyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> response = callApi.findSubwayLineById(id);
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
        ExtractableResponse<Response> saveResponse = callApi.saveSubwayLine(Param.신분당선);

        // when
        Long id = Actual.get(saveResponse, "id", Long.class);
        ExtractableResponse<Response> response = callApi.deleteSubwayLineById(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     *  테이블 데이터 초기화
     */
    private void cleanUp() {
        entityManager.flush();
        CLEAN_UP_TABLES.forEach(table -> {
            entityManager.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + table + " ALTER COLUMN id RESTART WITH 1").executeUpdate();
        });
    }

    /**
     * 검증 비교 대상 값 관련 클래스
     */
    private static class Actual {
        private static final Map<Class, BiFunction<ExtractableResponse<Response>, String, ?>> EXTRACT_INFO_AT_SUBWAY_LINE_FUNCTIONS = Map.of(
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
        private static  <T> T get(ExtractableResponse<Response> response, String path, Class<T> type) {
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
        private static  <T> List<T> getList(ExtractableResponse<Response> response, String path, Class<T> type) {
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

    /**
     * 지하철 노선 관련 API 호출 관련 클래스
     */
    private static class CallApi {

        /**
         * 지하철 노선 저장
         * @param params
         * @return
         */
        public ExtractableResponse<Response> saveSubwayLine(Map<String, Object> params) {
            return RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(params)
                    .when().post("/lines")
                    .then().log().all()
                    .extract();
        }

        /**
         * 지하철 노선 수정
         * @param id
         * @param params
         * @return
         */
        public ExtractableResponse<Response> modifySubwayLineById(Long id, Map<String, String> params) {
            return RestAssured
                    .given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().put("/lines/{id}", id)
                    .then().log().all()
                    .extract();
        }

        /**
         * 지하철 노선 삭제
         * @param id
         * @return
         */
        public ExtractableResponse<Response> deleteSubwayLineById(Long id) {
            return RestAssured
                    .given().log().all()
                    .when().delete("/lines/{id}", id)
                    .then().log().all()
                    .extract();
        }

        /**
         * 지하철 노선 목록 조회
         * @return
         */
        public ExtractableResponse<Response> findSubwayLines() {
            return RestAssured
                    .given().log().all()
                    .when().get("/lines")
                    .then().log().all()
                    .extract();
        }

        /**
         * 지하철 노선 조회
         * @param id
         * @return
         */
        public ExtractableResponse<Response> findSubwayLineById(Long id) {
            return RestAssured
                    .given().log().all()
                    .when().get("/lines/{id}", id)
                    .then().log().all()
                    .extract();
        }

        /**
         * 지하철 저장
         * @param params
         * @return
         */
        public ExtractableResponse<Response> saveStation(Map<String, String> params) {
            return RestAssured
                    .given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/stations")
                    .then().log().all()
                    .extract();
        }
    }
}
