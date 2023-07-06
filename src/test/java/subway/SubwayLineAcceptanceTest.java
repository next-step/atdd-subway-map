package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노션 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestDatabaseCleaner.class)
@Sql(value = {"classpath:db/station.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SubwayLineAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestDatabaseCleaner dbCleaner;

    private static Map<Long, String> 지하철역 = Map.of(1L, "지하철역", 2L, "새로운지하철역", 3L, "또다른지하철역");

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        dbCleaner.cleanUpLine();
        dbCleaner.cleanUpStation();
    }

    @DisplayName("지하철 노선을 생성할 때 upStation 에 해당하는 지하철역이 없으면 NOT_FOUND")
    @Test
    void createWithNoUpStation_notFound() {
        Map<String, Object> request = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 100L,
                "downStationId", 2L,
                "distance", 10L
        );

        ExtractableResponse<Response> response = 지하철노선_생성(request);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("지하철 노선을 생성할 때 downStation 에 해당하는 지하철역이 없으면 NOT_FOUND")
    @Test
    void createWithNoDownStation_notFound() {
        Map<String, Object> request = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1L,
                "downStationId", 200L,
                "distance", 10L
        );

        ExtractableResponse<Response> response = 지하철노선_생성(request);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("지하철 노선을 생성하면 목록 조회시 생성한 노선이 조회된다.")
    @Test
    void create_findAll() {
        Map<String, Object> request = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1L,
                "downStationId", 2L,
                "distance", 10L
        );
        ExtractableResponse<Response> response = 지하철노선_생성(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        지하철노선_목록_조회됨(Map.of(
                "id", 1,
                "name", "신분당선",
                "color", "bg-red-600",
                "stations", List.of(Map.of("id", 1, "name", 지하철역.get(1L)), Map.of("id", 2, "name", 지하철역.get(2L)))));
    }

    @DisplayName("지하철 노선을 두 개 생성하면 목록 조회시 생성한 노선이 두 가지 조회된다.")
    @Test
    void createTwo_findAll() {
        지하철노선_생성(Map.of("name", "신분당선", "color", "bg-red-600", "upStationId", 1L, "downStationId", 2L, "distance", 10L));
        지하철노선_생성(Map.of("name", "분당선", "color", "bg-red-600", "upStationId", 1L, "downStationId", 3L, "distance", 10L));

        지하철노선_목록_조회됨(
                Map.of("id", 1,
                        "name", "신분당선",
                        "color", "bg-red-600",
                        "stations", List.of(Map.of("id", 1, "name", 지하철역.get(1L)), Map.of("id",2, "name", 지하철역.get(2L))
                        )),
                Map.of("id", 2,
                        "name", "분당선",
                        "color", "bg-red-600",
                        "stations", List.of(Map.of("id", 1, "name", 지하철역.get(1L)), Map.of("id", 3, "name", 지하철역.get(3L))))
        );
    }

    @DisplayName("지하철 노선을 생성하고 해당 id로 조회시 생성한 노선이 조회된다.")
    @Test
    void create_findById() {
        Map<String, Object> request = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1L,
                "downStationId", 2L,
                "distance", 10L
        );
        ExtractableResponse<Response> response = 지하철노선_생성(request);

        지하철노선_조회됨(Map.of(
                "id", response.jsonPath().get("id"),
                "name", "신분당선",
                "color", "bg-red-600",
                "stations", List.of(Map.of("id", 1, "name", 지하철역.get(1L)), Map.of("id", 2, "name", 지하철역.get(2L)))));
    }

    @DisplayName("지하철 노선을 생성하고 업데이트할 때 잘못된 지하철 id를 넣을 경우 BAD_REQUEST")
    @Test
    void create_update_findByWrongId_badRequest() {
        Map<String, Object> createRequest = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1L,
                "downStationId", 2L,
                "distance", 10L
        );
        assertThat(지하철노선_생성(createRequest).jsonPath().getLong("id")).isEqualTo(1);

        Map<String, Object> updateRequest = Map.of(
                "id", 5,
                "name", "다른분당선",
                "color", "bg-red-600"
        );
        ExtractableResponse<Response> response = 지하철노선_수정(updateRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선을 생성하고 해당 id로 수정하면 수정한 노선이 조회된다.")
    @Test
    void create_update_findById() {
        Map<String, Object> createRequest = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1L,
                "downStationId", 2L,
                "distance", 10L
        );
        assertThat(지하철노선_생성(createRequest).jsonPath().getLong("id")).isEqualTo(1);

        Map<String, Object> updateRequest = Map.of(
                "id", 1,
                "name", "다른분당선",
                "color", "bg-red-600"
        );
        ExtractableResponse<Response> response = 지하철노선_수정(updateRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        지하철노선_조회됨(Map.of(
                "id", 1,
                "name", "다른분당선",
                "color", "bg-red-600",
                "stations", List.of(Map.of("id", 1, "name", 지하철역.get(1L)), Map.of("id", 2, "name", 지하철역.get(2L)))));
    }

    @DisplayName("지하철 노선을 생성하고 해당 id로 삭제하면 수정한 노선이 조회되지 않는다.")
    @Test
    void create_delete_notFound() {
        Map<String, Object> createRequest = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1L,
                "downStationId", 2L,
                "distance", 10L
        );
        assertThat(지하철노선_생성(createRequest).jsonPath().getLong("id")).isEqualTo(1);

        ExtractableResponse<Response> response = 지하철노선_삭제(1L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        지하철노선_조회안됨(1L);
    }

    private ExtractableResponse<Response> 지하철노선_생성(Map<String, Object> request){
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선_수정(Map<String, Object> request){
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", request.get("id"))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선_삭제(Long id){
        return RestAssured.given().log().all()
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    private void 지하철노선_조회됨(Map<String, Object> expected) {
        Map<String, Object> result = RestAssured.given().log().all()
                .when().get("/lines/{id}", expected.get("id"))
                .then().log().all()
                .extract()
                .jsonPath().get();

        assertThat(result).isEqualTo(expected);
    }

    private void 지하철노선_조회안됨(Long id) {
         RestAssured.given().log().all()
                .when().get("/lines/{id}", id)
                .then().log().all()
                .assertThat().statusCode(404);
    }

    private void 지하철노선_목록_조회됨(Map<String, Object>... expected) {
        List<Map<String, Object>> results = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract()
                .jsonPath().get();

        assertThat(results.size()).isEqualTo(expected.length);
        assertThat(results).containsExactly(expected);
    }
}
