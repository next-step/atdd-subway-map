package nextstep.subway.acceptance;


import io.restassured.RestAssured;
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
import java.util.List;
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
        assertThat(노선_목록을_조회한다().jsonPath().getList("name")).containsAnyOf("신분당선");
    }

    private Map<String, Object> createLineRequestBody(Line line) {
        return Map.of("name", line.getName(),
                "color", line.getColor(),
                "upStationId", line.getUpStationId(),
                "downStationId", line.getDownStationId());
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

    private ExtractableResponse<Response> 노선_목록을_조회한다() {
        return RestAssured.given().log().all()
                          .when().log().all()
                          .get(STATION_LINE_REQUEST_PATH)
                          .then().log().all()
                          .extract();
    }

    /** given 2개의 지하철 노선을 생성하고
     *  when 지하철 노선 목록을 조회하면
     *  then 생성된 지하철 노선을 확인할 수 있다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given 2개의 지하철 노선 생성
        Map<String, Object> 경의중앙선 = Map.of("name" ,"경의중앙선", "color", "blue", "upStationId", 3L, "downStationId", 4L);
        RestAssured.given().log().all()
                   .body(경의중앙선)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when().log().all()
                   .post("/station/line")
                   .then().log().all();

        Map<String, Object> 분당선 = Map.of("name" ,"분당선", "color", "yellow", "upStationId", 5L, "downStationId", 6L);
        RestAssured.given().log().all()
                   .body(분당선)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when().log().all()
                   .post("/station/line")
                   .then().log().all();

        //when 지하철 노선 목록 조회
        List<String> lineNames = RestAssured.given().log().all()
                                       .when().log().all()
                                       .get("/station/line")
                                       .then().log().all()
                                       .extract().jsonPath().getList("name", String.class);

        //then 생성된 지하철 노선 확인
        assertThat(lineNames).contains("경의중앙선", "분당선");
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성된 지하철 노선을 조회하면
     * then 생성된 노선을 응답받을 수 있다
     */
    @DisplayName("지하철 노선 단일 조회")
    @Test
    void getLine() {
        // given 지하철 노선 생성
        Map<String, Object> 우이신설 = Map.of("name", "우이신설", "color","gold", "upStationId",10L, "downStationId", 20L);
        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                                                           .body(우이신설)
                                                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                           .when().log().all()
                                                           .post("/station/line")
                                                           .then().log().all()
                                                           .extract();
        // when 지하철 노선 조회
        String name = RestAssured.given().log().all()
                              .pathParam("id", createResponse.jsonPath().getLong("id"))
                              .when().log().all()
                              .get("/station/line/{id}")
                              .then().log().all()
                              .extract().jsonPath().get("name");

        // then 생성한 지하철 노선 확인
        assertThat(name).isEqualTo("우이신설");
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
        Map<String, Object> 경춘선 = new HashMap<>();
        경춘선.put("name", "경춘선");
        경춘선.put("color", "green");
        경춘선.put("upStationId", 20L);
        경춘선.put("downStationId", 21L);
        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                                                           .body(경춘선)
                                                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                           .when().log().all()
                                                           .post("/station/line")
                                                           .then().log().all()
                                                           .extract();
        //when
        경춘선.put("name", "춘경선");
        경춘선.put("color", "red");
        Line line = RestAssured.given().log().all()
                               .pathParam("id", createResponse.jsonPath().getLong("id"))
                               .and()
                               .body(경춘선)
                               .contentType(MediaType.APPLICATION_JSON_VALUE)
                               .when().log().all()
                               .patch("/station/line/{id}")
                               .then().log().all()
                               .extract().as(Line.class);
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
        Map<String, Object> 신림선 = Map.of("name", "신림선", "color", "blue", "upStationId", 80L, "downStationId",90L);
        Line line = RestAssured.given().log().all()
                             .body(신림선)
                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                             .when().log().all()
                             .post("/station/line")
                             .then().log().all()
                             .extract().as(Line.class);
        //when
        RestAssured.given().log().all()
                .pathParam("id", line.getId())
                .when().log().all()
                .delete("/station/line/{id}")
                .then().log().all();

        //then
        List<Long> ids = RestAssured.given().log().all()
                                     .when().log().all()
                                     .get("/station/line")
                                     .then().log().all()
                                     .extract().jsonPath().getList("id", Long.class);

        assertThat(ids).isEmpty();
    }

}
