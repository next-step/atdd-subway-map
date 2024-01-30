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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성하고 노선 목록을 조회한다")
    @Test
    void createLine() {

        //when
        StationRestAssuredCRUD.createStation("강남역");
        StationRestAssuredCRUD.createStation("양재역");

        Map<String, Object> param = new HashMap<>();
        param.put("name", "신분당선");
        param.put("color", "bg-red-600");
        param.put("upStationId", 1);
        param.put("downStationId", 2);
        param.put("distance", 10);

        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(param)
                .when()
                    .post("/lines")
                .then().log().all()
                .extract();

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Long createdId = createResponse.response().body().jsonPath().getLong("id");

        // then
        List<Long> lineIdList = RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("id", Long.class);

        assertThat(lineIdList).contains(createdId);

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("2개의 지하철 노선을 생성하고 노선 목록을 조회한다")
    @Test
    void createAndShowTwoLines() {

        //given
        StationRestAssuredCRUD.createStation("미금역");
        StationRestAssuredCRUD.createStation("강남역");
        StationRestAssuredCRUD.createStation("서현역");

        Map<String, Object> param = new HashMap<>();
        param.put("name", "신분당선");
        param.put("color", "bg-red-600");
        param.put("upStationId", 1);
        param.put("downStationId", 2);
        param.put("distance", 10);

        ExtractableResponse<Response> shinBundangResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        param.put("name", "수인분당선");
        param.put("color", "bg-yellow-600");
        param.put("upStationId", 1);
        param.put("downStationId", 3);
        param.put("distance", 10);

        ExtractableResponse<Response> suinBundangResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/lines")
                .then().log().all()
                .extract();

        List<String> names =  response.jsonPath().getList("name", String.class);

        // then
        assertThat(names).containsAll(List.of("신분당선", "수인분당선"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 생성하고 노선을 조회한다")
    @Test
    void test() {

        //given
        StationRestAssuredCRUD.createStation("강남역");
        StationRestAssuredCRUD.createStation("양재역");

        Map<String, Object> param = new HashMap<>();
        param.put("name", "신분당선");
        param.put("color", "bg-red-600");
        param.put("upStationId", 1);
        param.put("downStationId", 2);
        param.put("distance", 10);

        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        Long createId = createResponse.body().jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("id", createId)
                .when()
                    .get("/lines/{id}")
                .then().log().all()
                .extract();

        // then
        assertThat(response.jsonPath().getList("id")).containsExactly(createId);

    }
}
