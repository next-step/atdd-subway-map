package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

@Sql(scripts = {"/truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @BeforeEach
    void setUp() {
        Map<String, String> paramsA = new HashMap<>();
        paramsA.put("name", "잠실역");
        given().log().all()
                .body(paramsA)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        Map<String, String> paramsB = new HashMap<>();
        paramsB.put("name", "용산역");
        given().log().all()
                .body(paramsB)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        Map<String, String> paramsC = new HashMap<>();
        paramsC.put("name", "건대입구역");
        given().log().all()
                .body(paramsC)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        Map<String, String> paramsD = new HashMap<>();
        paramsD.put("name", "성수역");
        given().log().all()
                .body(paramsD)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        //when
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "green");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        ExtractableResponse<Response> response = given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        //then
        ExtractableResponse<Response> findLinesResponse = given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Long savedId = response.jsonPath().getLong("id");
        List<Long> findId = findLinesResponse.jsonPath().getList("id", Long.class);
        assertThat(findId).containsExactly(savedId);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    void showLiens() {
        //given
        Map<String, String> paramsA = new HashMap<>();
        paramsA.put("name", "2호선");
        paramsA.put("color", "green");
        paramsA.put("upStationId", "1");
        paramsA.put("downStationId", "2");
        paramsA.put("distance", "10");
        ExtractableResponse<Response> createLineResponseA = given().log().all()
                .body(paramsA)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        Map<String, String> paramsB = new HashMap<>();
        paramsB.put("name", "1호선");
        paramsB.put("color", "blue");
        paramsB.put("upStationId", "3");
        paramsB.put("downStationId", "4");
        paramsB.put("distance", "10");
        ExtractableResponse<Response> createLineResponseB = given().log().all()
                .body(paramsB)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        //when
        ExtractableResponse<Response> response = given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Long savedIdA = createLineResponseA.jsonPath().getLong("id");
        Long savedIdB = createLineResponseB.jsonPath().getLong("id");
        List<Long> findId = response.jsonPath().getList("id", Long.class);
        assertThat(findId).containsExactly(savedIdA, savedIdB);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다")
    @Test
    void showLine() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "green");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        ExtractableResponse<Response> createLineResponse = given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        //when
        ExtractableResponse<Response> response = given().log().all()
                .pathParam("id", 1L)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Long savedId = createLineResponse.jsonPath().getLong("id");
        Long findId = response.jsonPath().getLong("id");
        assertThat(findId).isEqualTo(savedId);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다")
    @Test
    void updateLine() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "green");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        //when
        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("name", "1호선");
        updateParams.put("color", "blue");

        ExtractableResponse<Response> response = given().log().all()
                .pathParam("id", 1L)
                .body(updateParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/lines/{id}")
                .then().log().all()
                .extract();

        //then
        ExtractableResponse<Response> findLinesResponse = given().log().all()
                .pathParam("id", 1L)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String findName = findLinesResponse.jsonPath().getString("name");
        String findColor = findLinesResponse.jsonPath().getString("color");
        assertThat(findName).isEqualTo("1호선");
        assertThat(findColor).isEqualTo("blue");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다")
    @Test
    void deleteLine() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "green");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        //when
        ExtractableResponse<Response> response = given().log().all()
                .pathParam("id", 1L)
                .when().delete("/lines/{id}")
                .then().log().all()
                .extract();

        //then
        ExtractableResponse<Response> findLinesResponse = given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        List<Long> ids = findLinesResponse.jsonPath().getList("id", Long.class);
        assertThat(ids).isEmpty();
    }
}
