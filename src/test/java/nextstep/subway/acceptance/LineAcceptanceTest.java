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
        //given
        Map<String, String> 신분당선 = new HashMap<>();
        신분당선.put("color", "bg-red-600");
        신분당선.put("name", "신분당선");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(신분당선)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철 노선 생성을 요청하고
     * When 같은 이름으로 지하철 노선 생성을 요청하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선명 중복 방지")
    @Test
    void duplicateNameIsNotAllowed() {
        //given
        Map<String, String> 신분당선 = new HashMap<>();
        신분당선.put("color", "bg-red-600");
        신분당선.put("name", "신분당선");

        ExtractableResponse<Response> 지하철_노선_생성_요청 = RestAssured.given().log().all()
                .body(신분당선)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        //when

        ExtractableResponse<Response> 중복된_노선명으로_노선_생성_요청 = RestAssured.given().log().all()
                .body(신분당선)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(중복된_노선명으로_노선_생성_요청.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
        /// given
        String 신분당선_이름 = "신분당선";
        Map<String, String> 신분당선 = new HashMap<>();
        신분당선.put("name", 신분당선_이름);
        신분당선.put("color", "bg-red-600");
        ExtractableResponse<Response> createResponse1 = RestAssured.given().log().all()
                .body(신분당선)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        String 이호선_이름 = "2호선";
        Map<String, String> 이호선 = new HashMap<>();
        이호선.put("name", 이호선_이름);
        이호선.put("color", "bg-green-600");
        ExtractableResponse<Response> createResponse2 = RestAssured.given().log().all()
                .body(이호선)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains(신분당선_이름, 이호선_이름);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        /// given
        String 신분당선_이름 = "신분당선";
        Map<String, String> 신분당선 = new HashMap<>();
        신분당선.put("name", 신분당선_이름);
        신분당선.put("color", "bg-red-600");
        ExtractableResponse<Response> createResponse1 = RestAssured.given().log().all()
                .body(신분당선)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines/1")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String lineName = response.jsonPath().get("name");
        assertThat(lineName).isEqualTo(신분당선_이름);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        /// given
        String 신분당선_이름 = "신분당선";
        Map<String, String> 신분당선 = new HashMap<>();
        신분당선.put("name", 신분당선_이름);
        신분당선.put("color", "bg-red-600");
        ExtractableResponse<Response> createResponse1 = RestAssured.given().log().all()
                .body(신분당선)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        String 구분당선_아름 = "구분당선";
        Map<String, String> 구분당선 = new HashMap<>();
        구분당선.put("name", 구분당선_아름);
        구분당선.put("color", "bg-blue-600");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(구분당선)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/1")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> updatedLine = RestAssured.given().log().all()
                .when()
                .get("/lines/1")
                .then().log().all()
                .extract();

        String updateName = updatedLine.jsonPath().get("name");
        assertThat(updateName).isEqualTo(구분당선_아름);
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
        String 신분당선_이름 = "신분당선";
        Map<String, String> 신분당선 = new HashMap<>();
        신분당선.put("name", 신분당선_이름);
        신분당선.put("color", "bg-red-600");
        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .body(신분당선)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
