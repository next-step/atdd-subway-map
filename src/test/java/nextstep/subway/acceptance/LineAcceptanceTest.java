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
        ExtractableResponse<Response> response = 지하철_노선_생성_API(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     * @see nextstep.subway.ui.LineController#createLine
     */
    @Test
    void 지하철_노선_이름_중복_생성_방지_테스트() {
        // given
        지하철_노선_생성("GTX-A", "bg-red-900");

        Map<String, String> params = new HashMap<>();
        params.put("name", "GTX-A");
        params.put("color", "bg-red-900");

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_API(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
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
        // given
        지하철_노선_생성("GTX-A", "bg-red-900");
        지하철_노선_생성("신분당선", "bg-red-500");

        // when
        ExtractableResponse<Response> response = 지하철_노선_전체_리스트_조회_API();

        // then
        List<String> lineNames = response.body().jsonPath().getList("name");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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
        // given
        String 노선  = "GTX-A";
        String 노선색  = "bg-red-900";
        지하철_노선_생성(노선, 노선색);

        // when
        ExtractableResponse<Response> response = 지하철_노선_단건_조회_API(1L);

        // then
        String lineName = response.body().jsonPath().get("name").toString();
        String lineColor = response.body().jsonPath().get("color").toString();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineName).isEqualTo(노선);
        assertThat(lineColor).isEqualTo(노선색);
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
        String 노선  = "GTX-A";
        String 노선색  = "bg-red-800";
        지하철_노선_생성(노선, "bg-red-900");

        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("color", 노선색);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_API(1L, updateParams);

        // then
        ExtractableResponse<Response> updatedLine = 지하철_노선_단건_조회_API(1L);
        String lineName = updatedLine.body().jsonPath().get("name").toString();
        String lineColor = updatedLine.body().jsonPath().get("color").toString();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineName).isEqualTo(노선);
        assertThat(lineColor).isEqualTo(노선색);
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
        지하철_노선_생성("GTX-A", "bg-red-900");

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선_삭제_API(1L);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    ExtractableResponse<Response> 지하철_노선_생성(String 노선, String 노선색) {
        Map<String, String> params = new HashMap<>();
        params.put("name", 노선);
        params.put("color", 노선색);

        return 지하철_노선_생성_API(params);
    }

    ExtractableResponse<Response> 지하철_노선_생성_API(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> 지하철_노선_전체_리스트_조회_API() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> 지하철_노선_단건_조회_API(Long id) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines/{id}")
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> 지하철_노선_수정_API(Long id, Map<String, String> updateParams) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .body(updateParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/{id}")
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> 지하철_노선_삭제_API(Long id) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/{id}")
                .then().log().all()
                .extract();
    }
}
