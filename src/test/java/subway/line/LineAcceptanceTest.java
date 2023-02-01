package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineController.LINE_URI_PATH;

@Sql(value = "classpath:/init-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        LineRequest lineRequest = createLine("신분당선", "bg-red-600", 1L, 2L, 10);

        ExtractableResponse<Response> response = requestSaveLine(lineRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> findLineNames = getLineNames();
        assertThat(findLineNames).containsAnyOf("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 지하철 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineRequest lineRequest1 = createLine("신분당선", "bg-red-600", 1L, 2L, 10);
        LineRequest lineRequest2 = createLine("분당선", "bg-yellow-600", 3L, 4L, 10);
        requestSaveLines(lineRequest1, lineRequest2);

        // when
        List<String> findLineNames = getLineNames();

        // then
        assertThat(findLineNames).hasSize(2).containsAnyOf("신분당선", "분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선 목록을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답 받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineRequest lineRequest = createLine("신분당선", "bg-red-600", 1L, 2L, 10);
        ExtractableResponse<Response> saveResponse = requestSaveLine(lineRequest);
        Long saveLineId = saveResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> findLine = findLineById(saveLineId);
        Long findLineId = findLine.jsonPath().getLong("id");
        String findLineName = findLine.jsonPath().getString("name");

        // then
        assertThat(findLineId).isEqualTo(saveLineId);
        assertThat(findLineName).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineRequest lineSaveRequest = createLine("구분당선", "bg-black-600", 1L, 2L, 10);
        ExtractableResponse<Response> saveResponse = requestSaveLine(lineSaveRequest);
        Long saveLineId = saveResponse.jsonPath().getLong("id");
        String saveLineName = saveResponse.jsonPath().getString("name");
        assertThat(saveLineName).isEqualTo("구분당선");

        // when
        LineRequest lineUpdateRequest = createLine("신분당선", "bg-red-600", 1L, 2L, 10);
        ExtractableResponse<Response> updateResponse = updateLine(saveLineId, lineUpdateRequest);

        // then
        Long updateLineId = updateResponse.jsonPath().getLong("id");
        String updateLineName = updateResponse.jsonPath().getString("name");
        assertThat(updateLineId).isEqualTo(saveLineId);
        assertThat(updateLineName).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineRequest lineSaveRequest = createLine("구분당선", "bg-black-600", 1L, 2L, 10);
        ExtractableResponse<Response> saveResponse = requestSaveLine(lineSaveRequest);
        Long saveLineId = saveResponse.jsonPath().getLong("id");

        // when
        deleteLineById(saveLineId);

        // then
        ExtractableResponse<Response> findResponse = findLineById(saveLineId);
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private LineRequest createLine(String name, String color, Long upStationId, Long downStationId, int distance) {
        return new LineRequest(name, color, upStationId, downStationId, distance);
    }

    private void requestSaveLines(LineRequest... lineRequests) {
        Arrays.stream(lineRequests).forEach(LineAcceptanceTest::requestSaveLine);
    }

    public static ExtractableResponse<Response> requestSaveLine(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private List<String> getLineNames() {
        return findAllLine().jsonPath().getList("name", String.class);
    }

    public static ExtractableResponse<Response> findAllLine() {
        return RestAssured.given().log().all()
                .when().get(LINE_URI_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> findLineById(Long id) {
        return RestAssured.given().log().all()
                .when().get(LINE_URI_PATH + "/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateLine(Long id, LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(LINE_URI_PATH + "/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static void deleteLineById(Long id) {
        RestAssured.given().log().all()
                .when().delete(LINE_URI_PATH + "/" + id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }
}
