package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");

        // when
        ExtractableResponse<Response> response = given().log().all()
                .body(params)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(response.header("Location")).isNotBlank();
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
        String 이호선 = "2호선";
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", 이호선);
        params1.put("color", "bg-green-600");

        ExtractableResponse<Response> createResponse1 = given().log().all()
                .body(params1)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // and
        String 신분당선 = "신분당선";
        Map<String, String> params2 = new HashMap<>();
        params2.put("name", 신분당선);
        params2.put("color", "bg-red-600");

        ExtractableResponse<Response> createResponse2 = given().log().all()
                .body(params2)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(response.jsonPath().getList("name")).contains(이호선, 신분당선);
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
        String 이호선 = "2호선";
        Map<String, String> params = new HashMap<>();
        params.put("name", 이호선);
        params.put("color", "bg-green-600");

        ExtractableResponse<Response> createResponse = given().log().all()
                .body(params)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = given().log().all()
                .when()
                .get(createResponse.header("Location"))
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(이호선);
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
        String 이호선 = "2호선";
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", 이호선);
        params1.put("color", "bg-green-600");

        ExtractableResponse<Response> createResponse = given().log().all()
                .body(params1)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        String uri = createResponse.header("Location");
        String 신분당선 = "신분당선";
        Map<String, String> params2 = new HashMap<>();
        params2.put("name", 신분당선);
        params2.put("color", "bg-red-600");

        ExtractableResponse<Response> updateResponse = given().log().all()
                .body(params2)
                .when()
                .contentType(APPLICATION_JSON_VALUE)
                .put(uri)
                .then().log().all()
                .extract();

        // then
        ExtractableResponse<Response> findResponse = given().log().all()
                .accept(APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();

        assertThat(updateResponse.statusCode()).isEqualTo(OK.value());
        assertThat(findResponse.statusCode()).isEqualTo(OK.value());
        assertThat(findResponse.jsonPath().getString("name")).isEqualTo(신분당선);
        assertThat(findResponse.jsonPath().getString("color")).isEqualTo("bg-red-600");
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
        String 이호선 = "2호선";
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", 이호선);
        params1.put("color", "bg-green-600");

        ExtractableResponse<Response> createResponse = given().log().all()
                .body(params1)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }
}
