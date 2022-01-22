package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    @BeforeEach
    void beforeEach() {
        Map<String, String> createLineParam1 = new HashMap<>();
        createLineParam1.put("name", "신분당선");
        createLineParam1.put("color", "bg-red-600");

        RestAssured.given().log().all()
                .body(createLineParam1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        Map<String, String> createLineParam2 = new HashMap<>();
        createLineParam2.put("name", "2호선");
        createLineParam2.put("color", "bg-green-600");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(createLineParam2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        Map<String, String> createLineParam = new HashMap<>();
        createLineParam.put("name", "신분당선");
        createLineParam.put("color", "bg-red-600");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(createLineParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
//        when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();

//        then
        List<String> linesName = response.jsonPath().getList("name");
        assertThat(linesName).contains("신분당선", "2호선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("id", 1L)
                .when()
                .get("/lines/{id}")
                .then().log().all()
                .extract();

        String lineName = response.jsonPath().getString("name");
        assertThat(lineName).contains("신분당선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // when
        Map<String, String> updateLineParam = new HashMap<>();
        updateLineParam.put("name", "2호선");
        updateLineParam.put("color", "bg-green-600");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("id", 1L)
                .body(updateLineParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/{id}")
                .then().log().all()
                .extract();

        // then
        String lineName = response.jsonPath().getString("name");
        String lineColor = response.jsonPath().getString("color");
        assertThat(lineName).isEqualTo("2호선");
        assertThat(lineColor).isEqualTo("bg-green-600");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //when
        ExtractableResponse response = RestAssured.given().log().all()
                .pathParam("id", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/{id}")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }
}
