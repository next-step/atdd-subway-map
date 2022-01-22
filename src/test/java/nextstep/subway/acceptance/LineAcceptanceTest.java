package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     * @see nextstep.subway.ui.LineController#createLine
     */
    @Test
    void 지하철_노선_생성_테스트() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "GTX-A");
        params.put("color", "bg-red-900");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
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
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     * @see nextstep.subway.ui.LineController#findAllLines()
     */
    @Test
    void 지하철_노선_목록_조회_테스트() {
        Map<String, String> gtxALineParams = new HashMap<>();
        gtxALineParams.put("name", "GTX-A");
        gtxALineParams.put("color", "bg-red-900");

        Map<String, String> shinbundangLineParams = new HashMap<>();
        shinbundangLineParams.put("name", "신분당선");
        shinbundangLineParams.put("color", "bg-red-500");

        // given
        RestAssured.given().log().all()
                .body(gtxALineParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        RestAssured.given().log().all()
                .body(shinbundangLineParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> lines = RestAssured.given().log().all()
                .params(new HashMap<>())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then().log().all()
                .extract();

        // then
        List<String> lineNames = lines.body().jsonPath().getList("name");

        assertThat(lines.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineNames).contains("신분당선", "GTX-A");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     * @see nextstep.subway.ui.LineController#getLine
     */
    @Test
    void 지하철_노선_조회_테스트() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "GTX-A");
        params.put("color", "bg-red-900");

        // given
        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("id", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines/{id}")
                .then().log().all()
                .extract();

        // then
        String lineName = response.body().jsonPath().get("name").toString();
        String lineColor = response.body().jsonPath().get("color").toString();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineName).isEqualTo("GTX-A");
        assertThat(lineColor).isEqualTo("bg-red-900");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     * @see nextstep.subway.ui.LineController#updateLine
     */
    @Test
    void 지하철_노선_수정_테스트() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "GTX-A");
        params.put("color", "bg-red-900");

        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("color", "bg-red-800");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("id", 1L)
                .body(updateParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/{id}")
                .then().log().all()
                .extract();

        // then
        ExtractableResponse<Response> updatedLine = RestAssured.given().log().all()
                .pathParam("id", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines/{id}")
                .then().log().all()
                .extract();
        String lineName = updatedLine.body().jsonPath().get("name").toString();
        String lineColor = updatedLine.body().jsonPath().get("color").toString();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineName).isEqualTo("GTX-A");
        assertThat(lineColor).isEqualTo("bg-red-800");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     * @see nextstep.subway.ui.LineController#deleteLine
     */
    @Test
    void 지하철_노선_삭제_테스트() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "GTX-A");
        params.put("color", "bg-red-900");

        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        RestAssured.given().log().all()
                .pathParam("id", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/{id}")
                .then().log().all()
                .extract();

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
