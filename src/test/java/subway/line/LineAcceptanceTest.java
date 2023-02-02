package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.station.StationAcceptanceTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    public static final String SHINBUNDANG_LINE = "신분당선";
    public static final String RED = "be-red-600";
    public static final String BUNDANG_LINE = "분당선";
    public static final String GREEN = "bg-green-600";

    @BeforeEach
    void setUp() {
        StationAcceptanceTest.createStation("강남역");
        StationAcceptanceTest.createStation("미금역");
        StationAcceptanceTest.createStation("구의역");
    }

    private LineRequest givenLineRequest1() {
        return LineRequest.builder()
                .name(SHINBUNDANG_LINE)
                .color(RED)
                .upStationId(1L)
                .downStationId(2L)
                .distance(10L)
                .build();
    }

    private LineRequest givenLineRequest2() {
        return LineRequest.builder()
                .name(BUNDANG_LINE)
                .color(GREEN)
                .upStationId(1L)
                .downStationId(3L)
                .distance(10L)
                .build();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = createLine(givenLineRequest1());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(getStationNames()).containsAnyOf("신분당선");
    }

    private ExtractableResponse<Response> createLine(LineRequest request) {
        return given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private List<String> getStationNames() {
        return findAllLines()
                .jsonPath()
                .getList("name", String.class);
    }

    private ExtractableResponse<Response> findAllLines() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        List<LineRequest> lines = List.of(
                givenLineRequest1(),
                givenLineRequest2()
        );
        lines.forEach(this::createLine);

        // when
        ExtractableResponse<Response> response = findAllLines();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath()
                .getList("name", String.class))
                .hasSameSizeAs(lines)
                .containsAll(lines.stream()
                        .map(LineRequest::getName)
                        .collect(Collectors.toList()));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        // given
        LineRequest request = givenLineRequest1();
        createLine(request);

        // when
        ExtractableResponse<Response> response = findLineById(1L);

        // then
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name"))
                .isEqualTo(request.getName());
        assertThat(response.jsonPath().getString("color"))
                .isEqualTo(request.getColor());
        assertThat(response.jsonPath().getList("stations.id", Long.class))
                .contains(request.getUpStationId(), request.getDownStationId());
    }

    private ExtractableResponse<Response> findLineById(Long id) {
        return given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();
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
        createLine(givenLineRequest2());

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "다른분당선");
        params.put("color", "bg-red-600");

        ExtractableResponse<Response> response = given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/1")
                .then().log().all()
                .extract();

        // then
        LineResponse line = getLineById(1L);

        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(line.getName())
                .isEqualTo(params.get("name"));
        assertThat(line.getColor())
                .isEqualTo(params.get("color"));
    }

    private LineResponse getLineById(Long id) {
        return findLineById(id).jsonPath().getObject("$", LineResponse.class);
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
        createLine(givenLineRequest1());

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when().delete("/lines/1")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
