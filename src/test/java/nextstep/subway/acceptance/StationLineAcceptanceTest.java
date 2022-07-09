package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StationLineAcceptanceTest {

    public static final Map<Class, BiFunction<ExtractableResponse<Response>, String, ?>> EXTRACT_INFO_AT_STATION_LINE_FUNCTIONS = Map.of(
            Integer.class, (response, path) -> response.body().jsonPath().getInt(path),
            String.class, (response, path) -> response.body().jsonPath().getString(path)
    );

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        saveStation(Map.of("name", "강남역"));
        saveStation(Map.of("name", "양재역"));
        saveStation(Map.of("name", "역삼역"));
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createStationLine() {
        Map<String, Object> params = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        );

        // when
        ExtractableResponse<Response> saveResponse = saveStationLine(params);
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(saveResponse.header("Location")).isNotEmpty();

        // then
        ExtractableResponse<Response> response = findStationLines();
        assertThat(getInfoAtStationLine(response, "[0].name", String.class)).isEqualTo("신분당선");
        assertThat(getInfoAtStationLine(response, "[0].color", String.class)).isEqualTo("bg-red-600");
        assertThat(getStringListAtStationLine(response, "[0].stations.name")).isEqualTo(List.of("강남역","양재역"));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getStationLines() {
        // given
        Map<String, Object> params = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        );
        saveStationLine(params);

        Map<String, Object> params2 = Map.of(
                "name", "2호선",
                "color", "bg-green-600",
                "upStationId", 1,
                "downStationId", 3,
                "distance", 8
        );
        saveStationLine(params2);

        // when
        ExtractableResponse<Response> response = findStationLines();

        // then
        assertThat(getStringListAtStationLine(response, "name")).isEqualTo(List.of("신분당선", "2호선"));
        assertThat(getStringListAtStationLine(response, "color")).isEqualTo(List.of("bg-red-600", "bg-green-600"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getStationLine() {
        // given
        Map<String, Object> params = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        );
        ExtractableResponse<Response> saveResponse = saveStationLine(params);

        // when
        Integer id = getInfoAtStationLine(saveResponse, "id", Integer.class);
        ExtractableResponse<Response> response = findStationLineById(id);

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
    void modifyStationLine() {
        // given
        Map<String, Object> params = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        );
        ExtractableResponse<Response> saveResponse = saveStationLine(params);

        // when
        Integer id = getInfoAtStationLine(saveResponse, "id", Integer.class);
        Map<String, Object> modifyParams = Map.of(
                "name", "2호선",
                "color", "bg-green-600"
        );

        ExtractableResponse<Response> modifyResponse = RestAssured
                .given().log().all()
                .body(modifyParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
        assertThat(modifyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> response = findStationLineById(id);
        assertThat(getInfoAtStationLine(response, "name", String.class)).isEqualTo("2호선");
        assertThat(getInfoAtStationLine(response, "color", String.class)).isEqualTo("bg-green-600");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteStationLine() {
        // given
        Map<String, Object> params = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        );
        ExtractableResponse<Response> saveResponse = saveStationLine(params);

        // when
        Integer id = getInfoAtStationLine(saveResponse, "id", Integer.class);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> saveStationLine(Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> findStationLines() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> findStationLineById(int id) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> saveStation(Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private <T> T getInfoAtStationLine(ExtractableResponse<Response> response, String path, Class<T> type) {
        return (T) EXTRACT_INFO_AT_STATION_LINE_FUNCTIONS.get(type).apply(response, path);
    }

    private List<String> getStringListAtStationLine(ExtractableResponse<Response> response, String path) {
        return response.jsonPath().getList(path, String.class);
    }
}
