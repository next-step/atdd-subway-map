package subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.DatabaseStoreCleanup;
import subway.domain.Station;
import subway.domain.StationRepository;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 호선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private DatabaseStoreCleanup databaseStoreCleanup;

    @BeforeEach
    void setUp() {
        databaseStoreCleanup.cleanStore();
        final Station 강남역 = new Station("강남역");
        stationRepository.save(강남역);
        final Station 잠실역 = new Station("잠실역");
        stationRepository.save(잠실역);
        final Station 검암역 = new Station("검암역");
        stationRepository.save(검암역);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {

        final ExtractableResponse<Response> 이호선 = 지하철_노선_생성_요청("2호선", "bg-red-600", 1L,2L, 10);

        지하철_노선_생성됨(이호선);

        final ExtractableResponse<Response> 지하철_노선_목록_응답 = 지하철_노선_목록_조회_요청();

        지하철_노선_목록_조회됨(지하철_노선_목록_응답, "2호선", "bg-red-600", 2);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {

        지하철_노선_생성_요청("2호선", "bg-red-600", 1L,2L, 10);
        지하철_노선_생성_요청("신분당선", "bg-green-600", 1L,3L, 10);

        final ExtractableResponse<Response> 지하철_노선_목록_응답 = 지하철_노선_목록_조회_요청();

        지하철_노선_목록_조회됨(지하철_노선_목록_응답);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findLine() {

        final ExtractableResponse<Response> 이호선 = 지하철_노선_생성_요청("2호선", "bg-red-600", 1L,2L, 10);

        지하철_노선_생성됨(이호선);

        final ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회_요청(이호선);

        지하철_노선_조회됨(지하철_노선_조회_응답, "2호선", "bg-red-600", 2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보가 수정된다.
     */
    @DisplayName("지하철 노선 정보를 수정한다.")
    @Test
    void updateLine() {

        final ExtractableResponse<Response> 이호선_응답 = 지하철_노선_생성_요청("2호선", "bg-red-600", 1L,2L, 10);

        지하철_노선_생성됨(이호선_응답);

        final ExtractableResponse<Response> 이호선_수정_응답 = 지하철_노선_수정_요청(이호선_응답, "3호선", "bg-yellow-600");

        지하철_노선_수정됨(이호선_수정_응답);

        final ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회_요청(이호선_응답);

        지하철_노선_조회됨(지하철_노선_조회_응답, "3호선", "bg-yellow-600", 2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {

        final ExtractableResponse<Response> 이호선_응답 = 지하철_노선_생성_요청("2호선", "bg-red-600", 1L,2L, 10);

        지하철_노선_생성됨(이호선_응답);

        final ExtractableResponse<Response> 이호선_삭제_응답 = 지하철_노선_삭제_요청(이호선_응답);

        지하철_노선_삭제됨(이호선_삭제_응답);
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(final String name, final String color, final Long upStationId, final Long downStationId, final int distance) {

        final Map<String, Object> params = 파라미터_생성_요청(name, color, upStationId,downStationId, distance);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private Map<String, Object> 파라미터_생성_요청(String name, String color, Long upStationId, Long downStationId, int distance) {

        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    private void 지하철_노선_생성됨(final ExtractableResponse<Response> response) {

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private void 지하철_노선_목록_조회됨(ExtractableResponse<Response> response, final String lineName, final String lineColor, final int countOfStation) {

        final JsonPath jsonPath = response.response().body().jsonPath();

        assertAll(
                () -> assertThat(response.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPath.getString("[0].name")).isEqualTo(lineName),
                () -> assertThat(jsonPath.getString("[0].color")).isEqualTo(lineColor),
                () -> assertThat(jsonPath.getList("[0].stationResponses")).hasSize(countOfStation)
        );
    }

    private void 지하철_노선_목록_조회됨(final ExtractableResponse<Response> lineResponse) {

        final JsonPath 지하철_목록_응답_경로 = lineResponse.response().body().jsonPath();

        assertAll(
                () -> assertThat(lineResponse.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철_목록_응답_경로.getList("")).hasSize(2)
        );
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(final ExtractableResponse<Response> response) {

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(response.header("location"))
                .then().log().all()
                .extract();
    }

    private void 지하철_노선_조회됨(ExtractableResponse<Response> response, final String lineName, final String lineColor, final int countOfStation) {

        final JsonPath jsonPath = response.response().body().jsonPath();

        assertAll(
                () -> assertThat(response.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPath.getString("name")).isEqualTo(lineName),
                () -> assertThat(jsonPath.getString("color")).isEqualTo(lineColor),
                () -> assertThat(jsonPath.getList("stationResponses")).hasSize(countOfStation)
        );
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(final ExtractableResponse<Response> response, final String name, final String color) {

        final Map<String, Object> params = 파라미터_수정_요청(name, color);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(response.header("location"))
                .then().log().all()
                .extract();
    }

    private Map<String, Object> 파라미터_수정_요청(final String name, final String color) {

        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }

    private void 지하철_노선_수정됨(final ExtractableResponse<Response> response) {

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 지하철_노선_삭제_요청(final ExtractableResponse<Response> response) {

        return RestAssured
                .given().log().all()
                .when().delete(response.header("location"))
                .then().log().all()
                .extract();
    }

    private void 지하철_노선_삭제됨(final ExtractableResponse<Response> response) {

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
