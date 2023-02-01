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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    @BeforeEach
    void setUp() {
        createStation("강남역");
        createStation("미금역");
        createStation("구의역");
    }

    private void createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        LineRequest request = LineRequest.builder()
                .name("신분당선")
                .color("be-red-600")
                .upStationId(1L)
                .downStationId(2L)
                .distance(10L)
                .build();

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);

        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.CREATED.value());
        assertThat(stationNames)
                .containsAnyOf("신분당선");
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
                LineRequest.builder()
                        .name("신분당선")
                        .color("be-red-600")
                        .upStationId(1L)
                        .downStationId(2L)
                        .distance(10L)
                        .build(),
                LineRequest.builder()
                        .name("분당선")
                        .color("bg-green-600")
                        .upStationId(1L)
                        .downStationId(3L)
                        .distance(10L)
                        .build()
        );
        lines.forEach(this::createLine);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath()
                .getList("name", String.class))
                .hasSameSizeAs(lines)
                .containsAll(lines.stream()
                        .map(LineRequest::getName)
                        .collect(Collectors.toList()));
    }

    private void createLine(LineRequest request) {
        RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all();
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
        LineRequest request = LineRequest.builder()
                .name("신분당선")
                .color("be-red-600")
                .upStationId(1L)
                .downStationId(2L)
                .distance(10L)
                .build();

        createLine(request);

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when().get("/lines/1")
                        .then().log().all()
                        .extract();

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

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineRequest request = LineRequest.builder()
                .name("분당선")
                .color("bg-green-600")
                .upStationId(1L)
                .downStationId(3L)
                .distance(10L)
                .build();

        createLine(request);

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "다른분당선");
        params.put("color", "bg-red-600");

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/1")
                .then().log().all()
                .extract();

        // then
        ExtractableResponse<Response> line = RestAssured
                .given().log().all()
                .when().get("/lines/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(line.jsonPath().getString("name"))
                .isEqualTo(params.get("name"));
        assertThat(line.jsonPath().getString("color"))
                .isEqualTo(params.get("color"));
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
        LineRequest request = LineRequest.builder()
                .name("분당선")
                .color("bg-green-600")
                .upStationId(1L)
                .downStationId(3L)
                .distance(10L)
                .build();

        createLine(request);

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
