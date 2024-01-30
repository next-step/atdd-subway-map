package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineRequest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(value = "/table_truncate.sql")
@DisplayName("지하철 노선 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    private static boolean setUpIsDone = false;

    private static final LineRequest LINE_REQUEST_1 = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
    private static final LineRequest LINE_REQUEST_2 = new LineRequest("분당선", "bg-green-600", 1L, 3L, 7L);

    @BeforeEach
    public void setUp() {
        if (setUpIsDone) return;

        StationAcceptanceTest.makeStation("gangnam");
        StationAcceptanceTest.makeStation("yeoksam");
        StationAcceptanceTest.makeStation("samseong");

        setUpIsDone = true;
    }

    public ExtractableResponse<Response> makeLine(LineRequest lineRequest) {
        return RestAssured
                .given().log().all()
                .when()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public ExtractableResponse<Response> getLines() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private static ExtractableResponse<Response> getLine(Long id) {
        ExtractableResponse<Response> response = RestAssured
                .given()
                .when()
                .get("/lines/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        return response;
    }

    /**
     * when 지하철 노선을 생성하면
     * then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> newLineResponse = makeLine(LINE_REQUEST_1);

        // then
        List<Long> ids = getLines().jsonPath().getList("id", Long.class);

        assertThat(ids).containsOnly(newLineResponse.jsonPath().getLong("id"));
    }

    /**
     * given 2개의 지하철 노선을 생성하고
     * when 지하철 노선 목록을 조회하면
     * then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void showLines() {
        // given
        Long id_1 = makeLine(LINE_REQUEST_1).jsonPath().getLong("id");
        Long id_2 = makeLine(LINE_REQUEST_2).jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = getLines();

        //then
        assertThat(response.jsonPath().getList("id", Long.class)).containsOnly(id_1, id_2);
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성한 지하철 노선을 조회하면
     * then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 단일 조회")
    @Test
    void showLine() {
        // given
        Long id = makeLine(LINE_REQUEST_1).jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = getLine(id);

        //then
        assertThat(response.jsonPath().getLong("id")).isEqualTo(id);
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성한 지하철 노선을 수정하면
     * then 해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        Long id = makeLine(LINE_REQUEST_1).jsonPath().getLong("id");

        // when
        Map<String, String> request = Map.of("name", "다른분당선", "color", "bg-red-600");

        RestAssured
                .given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .put("/lines/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> response = getLine(id);

        assertThat(response.jsonPath().getString("name")).isEqualTo("다른분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600");
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성한 지하철 노선을 삭제하면
     * then 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        Long id = makeLine(LINE_REQUEST_1).jsonPath().getLong("id");

        // when
        RestAssured
                .given().log().all()
                .when()
                .delete("/lines/" + id)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> response = getLines();
        assertThat(response.jsonPath().getList("id", Long.class)).doesNotContain(id);
    }
}
