package nextstep.subway.acceptance;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:/truncate.sql")
public class StationLineAcceptanceTest {

    public static String STATION_LINE_REQUEST_PATH = "/station/line";
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * given 노선을 생성하면
     * when 노선이 생성된다.
     * then 목록 조회시 생성된 노선을 확인할 수 있다.
     */
    @DisplayName("노선을 생성한다")
    @Test
    void createLine() {
        //given
        Map<String, Object> body = createLineRequestBody(
                        new Line("신분당선", "red", 1L, 3L));
        ExtractableResponse<Response> createResponse = 노선을_생성한다(body);

        //when
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        assertThat(노선_목록을_조회한다().getList("name")).containsAnyOf("신분당선");
    }


    /** given 2개의 지하철 노선을 생성하고
     *  when 지하철 노선 목록을 조회하면
     *  then 생성된 지하철 노선을 확인할 수 있다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        //given
        Map<String, Object> 경의중앙선 = createLineRequestBody(
                        new Line("경의중앙선", "blue", 3L, 4L));
        노선을_생성한다(경의중앙선);

        Map<String, Object> 분당선 = createLineRequestBody(
                        new Line("분당선", "yellow", 5L, 6L));
        노선을_생성한다(분당선);

        //when, then
        assertThat(노선_목록을_조회한다().getList("name", String.class)).contains("경의중앙선", "분당선");
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성된 지하철 노선을 조회하면
     * then 생성된 노선을 응답받을 수 있다
     */
    @DisplayName("지하철 노선 단일 조회")
    @Test
    void getLine() {
        // given
        Map<String, Object> 우이신설 = createLineRequestBody(
                        new Line("우이신설", "gold", 10L, 20L, 10));
        ExtractableResponse<Response> createResponse = 노선을_생성한다(우이신설);

        // when
        JsonPath 노선 = 노선_단일_조회(createResponse.jsonPath().getLong("id"));

        // then 생성한 지하철 노선 확인
        assertThat(노선.getString("name")).isEqualTo("우이신설");
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성된 노선을 수정하면
     * then 변경된 노선을 확인할 수 있다
     */
    @DisplayName("지하철 노선 정보 수정")
    @Test
    void updateLine() {
        //given
        Map<String, Object> 경춘선 = createLineRequestBody(
                new Line("경춘선", "green", 20L, 21L, 11));
        Long id = 노선을_생성한다(경춘선).jsonPath().getLong("id");

        //when
        경춘선.put("name", "춘경선");
        경춘선.put("color", "red");
        Line line = 노선_정보를_수정한다(경춘선, id);

        //then
        assertThat(line.getName()).isEqualTo("춘경선");
        assertThat(line.getColor()).isEqualTo("red");
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성된 노선을 삭제하면
     * then 해당 노선을 조회할 수 없다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given
        Map<String, Object> 신림선 = createLineRequestBody(
                        new Line("신림선", "blue", 80L, 90L, 12));
        Line line = 노선을_생성한다(신림선).as(Line.class);

        //when
        assertThat(노선을_삭제한다(line)).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        assertThat(노선_목록을_조회한다().getList("id", Long.class)).isEmpty();
    }

    private int 노선을_삭제한다(Line line) {
        return RestAssured.given().log().all()
                          .pathParam("id", line.getId())
                          .when().log().all()
                          .delete(STATION_LINE_REQUEST_PATH + "/{id}")
                          .then().log().all()
                   .extract().statusCode();
    }

    private Map<String, Object> createLineRequestBody(Line line) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", line.getName());
        body.put("color", line.getColor());
        body.put("upStationId", line.getUpStationId());
        body.put("downStationId", line.getDownStationId());
        body.put("distance", line.getDistance());
        return body;
    }

    private ExtractableResponse<Response> 노선을_생성한다(Map<String, Object> body) {
        return RestAssured.given().log().all()
                          .body(body)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().log().all()
                          .post(STATION_LINE_REQUEST_PATH)
                          .then().log().all()
                          .extract();
    }

    private JsonPath 노선_목록을_조회한다() {
        return RestAssured.given().log().all()
                          .when().log().all()
                          .get(STATION_LINE_REQUEST_PATH)
                          .then().log().all()
                          .extract().jsonPath();
    }

    private JsonPath 노선_단일_조회(Long id) {
        return RestAssured.given().log().all()
                          .pathParam("id", id)
                          .when().log().all()
                          .get(STATION_LINE_REQUEST_PATH + "/{id}")
                          .then().log().all()
                          .extract().jsonPath();
    }

    private Line 노선_정보를_수정한다(Map<String, Object> body, Long id) {
        return RestAssured.given().log().all()
                          .pathParam("id", id)
                          .and()
                          .body(body)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().log().all()
                          .put(STATION_LINE_REQUEST_PATH+ "/{id}")
                          .then().log().all()
                          .extract().as(Line.class);
    }

}
