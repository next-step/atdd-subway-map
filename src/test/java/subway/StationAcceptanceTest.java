package subway;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private static final String ENDPOINT = "/stations";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    @DisplayName("등록된 모든 지하철역을 조회한다.")
    @Test
    void findAllStations() {
        // Given 2개의 지하철역을 생성하고
        StationApi.create(ENDPOINT, new JsonBodyParams("name", "강남역"));
        StationApi.create(ENDPOINT, new JsonBodyParams("name", "서울대입구역"));

        // When 지하철역 목록을 조회하면
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);

        // Then 2개의 지하철역을 응답받는다.
        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).containsExactly("강남역", "서울대입구역");
    }

    @DisplayName("등록된 지하철을 삭제하면 삭제된 역은 조회할 수 없다.")
    @Test
    void deleteAllStations() {
        // Given 지하철역을 생성하고
        ExtractableResponse<Response> 강남역_생성_응답 = StationApi.create(ENDPOINT, new JsonBodyParams("name", "강남역"));
        ExtractableResponse<Response> 서울대입구역_생성_응답 = StationApi.create(ENDPOINT, new JsonBodyParams("name", "서울대입구역"));

        Long 강남역_아이디 = 강남역_생성_응답.jsonPath().getLong("id");
        Long 서울대입구역_아이디 = 서울대입구역_생성_응답.jsonPath().getLong("id");

        // When 그 지하철역을 삭제하면
        RestAssured.given().log().all()
                .when().delete(String.format("/stations/%s", 강남역_아이디))
                .then().log().all();

        // Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
        List<Long> stationIds =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("id", Long.class);

        assertThat(stationIds).containsExactly(서울대입구역_아이디);
        assertThat(stationIds).doesNotContain(강남역_아이디);
    }
}

class JsonBodyParams {
    String key;
    String value;

    public JsonBodyParams(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}

class StationApi {
    static ExtractableResponse<Response> create(String endpoint, JsonBodyParams jsonBodyParams) {
        Map<String, String> params = new HashMap<>();
        params.put(jsonBodyParams.getKey(), jsonBodyParams.getValue());

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(endpoint)
                .then().log().all()
                .extract();
    }
}
