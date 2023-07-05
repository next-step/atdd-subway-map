package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        dbCleaner.cleanUpStation();
    }

    @DisplayName("지하철 노선을 생성하면 목록 조회시 생성한 노선이 조회된다.")
    @Test
    void create_find() {
        Map<String, Object> request = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1L,
                "downStationId", 2L,
                "distance", 10L
        );
        ExtractableResponse<Response> response = 지하철노선_생성(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        지하철노선_목록_조회됨(Map.of("id", 1L, "name", "신분당선",  "color", "bg-red-600", "stations", List.of(지하철역.get(1L), 지하철역.get(2L))));
    }

    @DisplayName("지하철 노선을 두 개 생성하면 목록 조회시 생성한 노선이 두 가지 조회된다.")
    @Test
    void createTwo_findTwo() {
        지하철노선_생성(Map.of("name", "신분당선", "color", "bg-red-600", "upStationId", 1L, "downStationId", 2L, "distance", 10L));
        지하철노선_생성(Map.of("name", "분당선", "color", "bg-red-600", "upStationId", 1L, "downStationId", 3L, "distance", 10L));

        지하철노선_목록_조회됨(
                Map.of("id", 1L, "name", "신분당선",  "color", "bg-red-600", "stations", List.of(지하철역.get(1L), 지하철역.get(2L))),
                Map.of("id", 1L, "name", "분당선",  "color", "bg-red-600", "stations", List.of(지하철역.get(1L), 지하철역.get(3L)))
        );
    }

    private ExtractableResponse<Response> 지하철노선_생성(Map<String, Object> request){
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private void 지하철노선_목록_조회됨(Map<String, Object>... expected) {
        List<Map<String, Object>> results = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract()
                .jsonPath().getList("stations");

        assertThat(results.size()).isEqualTo(expected.length);
        assertThat(results).containsExactly(expected);
    }
}
