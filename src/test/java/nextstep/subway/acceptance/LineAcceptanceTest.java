package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
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
    @DisplayName("지하철 노선 생성 테스트")
    @Test
    void 지하철_노선_생성_테스트() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_API(GTXA노선);

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
    @DisplayName("지하철 노선 이름 중복 생성 방지 테스트")
    @Test
    void 지하철_노선_이름_중복_생성_방지_테스트() {
        // given
        지하철_노선_생성_API(GTXA노선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_API(GTXA노선);

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
    @DisplayName("지하철 노선 목록 조회 테스트")
    @Test
    void 지하철_노선_목록_조회_테스트() {
        // given
        지하철_노선_생성_API(GTXA노선);
        지하철_노선_생성_API(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_전체_리스트_조회_API();

        // then
        List<String> lineNames = response.body().jsonPath().getList("name");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineNames).contains("GTX-A", "신분당선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     * @see nextstep.subway.ui.LineController#getLine
     */
    @DisplayName("지하철 노선 조회 테스트")
    @Test
    void 지하철_노선_조회_테스트() {
        // given
        지하철_노선_생성_API(GTXA노선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_단건_조회_API(1L);

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
    @DisplayName("지하철 노선 수정 테스트")
    @Test
    void 지하철_노선_수정_테스트() {
        // given
        지하철_노선_생성_API(GTXA노선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_API(1L, 노선색상);

        // then
        ExtractableResponse<Response> updatedLine = 지하철_노선_단건_조회_API(1L);
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
    @DisplayName("지하철 노선 삭제 테스트")
    @Test
    void 지하철_노선_삭제_테스트() {
        // given
        지하철_노선_생성_API(GTXA노선);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선_삭제_API(1L);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    static Map<String, String> GTXA노선;
    static Map<String, String> 신분당선;
    static Map<String, String> 노선색상;

    @BeforeAll
    public static void 초기화() {
        GTXA노선 = new HashMap<>();
        GTXA노선.put("name", "GTX-A");
        GTXA노선.put("color", "bg-red-900");

        신분당선 = new HashMap<>();
        신분당선.put("name", "신분당선");
        신분당선.put("color", "bg-red-500");

        노선색상 = new HashMap<>();
        노선색상.put("color", "bg-red-800");
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
