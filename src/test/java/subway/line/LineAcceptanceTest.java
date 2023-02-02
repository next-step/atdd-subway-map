package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import javax.annotation.PostConstruct;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.station.StationAcceptanceTest.지하철역_등록됨;

@DisplayName("지하철노선 관련 기능")
@Sql(value = "/LineAcceptance.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private long firstStationId;
    private long secondStationId;

    @PostConstruct
    void setup() {
        // given
        firstStationId = 지하철역_등록됨("강남역").body().jsonPath().getLong("id");
        secondStationId = 지하철역_등록됨("역삼역").body().jsonPath().getLong("id");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        final String line = "2호선";
        var response = 지하철_노선을_생성됨(line, "green darken-2", firstStationId, secondStationId, 10L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(지하철_노선_조회().jsonPath().getList("name")).contains(line);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        지하철_노선을_생성됨("1호선", "blue darken-4", firstStationId, secondStationId, 20L);
        지하철_노선을_생성됨("2호선", "green darken-2", firstStationId, secondStationId, 10L);

        // when
        var response = 지하철_노선_조회();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        long lineId = 지하철_노선을_생성됨("2호선", "green darken-2", firstStationId, secondStationId, 10L).jsonPath().getLong("id");

        // when
        var response = 지하철_노선_Id로_조회됨(lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("2호선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("green darken-2");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void setLine() {
        // given
        long lineId = 지하철_노선을_생성됨("2호선", "green darken-2", firstStationId, secondStationId, 10L).jsonPath().getLong("id");

        // when
        var response = 지하철_노선_수정됨(lineId, "12호선", "blue darken-4");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        response = 지하철_노선_Id로_조회됨(lineId);
        assertThat(response.jsonPath().getString("name")).isEqualTo("12호선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("blue darken-4");
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
        long lineId = 지하철_노선을_생성됨("2호선", "green darken-2", firstStationId, secondStationId, 10L).jsonPath().getLong("id");

        // when
        var response = 지하철_노선_삭제됨(lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        response = 지하철_노선_조회();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).doesNotContain("2호선");
    }

    private ExtractableResponse<Response> 지하철_노선_조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_Id로_조회됨(long lineId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}", lineId)
                .then().statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정됨(long lineId, String name, String color) {
        final Map<String, Object> params = Map.of(
                "name", name,
                "color", color);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", lineId)
                .then().statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_삭제됨(long lineId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{id}", lineId)
                .then().statusCode(HttpStatus.NO_CONTENT.value())
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선을_생성됨(String name, String color, Long upStationId, Long downStationId, long distance) {
        final Map<String, Object> params = Map.of(
                "name", name,
                "color", color,
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance);

        var response = 지하철_노선_등록_요청(params);
        요청_성공(response);
        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_등록_요청(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract();
    }

    public static void 요청_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}