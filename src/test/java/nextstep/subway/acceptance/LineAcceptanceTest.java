package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "9호선");
        params.put("color", "갈색");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/lines")
                        .then()
                        .log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/1");
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
        // given
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "9호선");
        params1.put("color", "갈색");

        RestAssured.given().log().all()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .log().all()
                .extract();

        // given
        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "5호선");
        params2.put("color", "보라색");

        RestAssured.given().log().all()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .log().all()
                .extract();

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/lines")
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).containsAll(lineNames);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "9호선");
        params.put("color", "갈색");

        ExtractableResponse<Response> createResponse =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/lines")
                        .then()
                        .log().all()
                        .extract();

        // when
        String url = createResponse.header("Location");
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get(url)
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String lineName = response.jsonPath().getString("name");
        assertThat(lineName).isEqualTo("9호선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성하지 않은 지하철 노선도를 조회하면
     * Then 빈 값을 응답한다.
     */
    @DisplayName("지하철 노선 조회 실패")
    @Test
    void getNoneLine() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "9호선");
        params.put("color", "갈색");

        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .log().all()
                .extract();

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/lines/2")
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "9호선");
        params.put("color", "갈색");

        ExtractableResponse<Response> createResponse =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/lines")
                        .then()
                        .log().all()
                        .extract();

        // when
        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("name", "5호선");
        updateParams.put("color", "보라색");
        String url = createResponse.header("Location");
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(updateParams)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .put(url)
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보가 없어 신규 생성한다.
     */
    @DisplayName("지하철 노선 수정 실패")
    @Test
    void updateNoneLine() {
        // when
        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("name", "5호선");
        updateParams.put("color", "보라색");
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(updateParams)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .put("/lines/" + 1)
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "9호선");
        params.put("color", "갈색");

        ExtractableResponse<Response> createResponse =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/lines")
                        .then()
                        .log().all()
                        .extract();

        // when
        String url = createResponse.header("Location");
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when()
                        .delete(url)
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
