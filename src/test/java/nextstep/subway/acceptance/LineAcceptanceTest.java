package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.net.URI;
import java.util.Map;

import static nextstep.subway.acceptance.TestSetupUtils.지하철_노선_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하촐 노선 관리기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void 지하철_노선_생성_인수테스트() {
        // when
        지하철_노선_생성("신분당선", "red", "강남역", "교대역", 10);

        // then
        지하철_노선들이_존재한다("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    void 지하철_노선_목록조회_인수테스트() {
        // given
        지하철_노선_생성("신분당선", "red", "강남역", "교대역", 10);
        지하철_노선_생성("2호선", "green", "역삼역", "선릉역", 10);

        // when + then
        지하철_노선들이_존재한다("신분당선", "2호선");
    }

    private void 지하철_노선들이_존재한다(String... stationNames) {
        var 지하철_노선_목록 = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        assertThat(지하철_노선_목록.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(지하철_노선_목록.jsonPath().getList("name", String.class)).contains(stationNames);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void 지하철_노선_조회_인수테스트() {
        // given
        long 신분당선 = 지하철_노선_생성("신분당선", "red", "강남역", "교대역", 10);

        // when + then
        지하철_노선이_존재한다(신분당선);
    }

    private void 지하철_노선이_존재한다(Long stationId) {
        assertThat(지하철_노선_조회(stationId).statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    void 지하철_노선_수정_인수테스트() {
        // given
        long 신분당선 = 지하철_노선_생성("신분당선", "red", "강남역", "교대역", 10);

        // when
        자하철_노선을_수정한다(신분당선, "다른분당선", "orange");

        // then
        지하철_노선_정보가_일치한다(신분당선, "다른분당선", "orange");
    }

    private void 자하철_노선을_수정한다(long lineId, String name, String color) {
        RestAssured.given().log().all()
                .body(Map.of("name", name,
                        "color", color))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(URI.create("/lines/" + lineId))
                .then().log().all();
    }

    private void 지하철_노선_정보가_일치한다(long 신분당선_id, String name, String color) {
        var 지하철_노선 = 지하철_노선_조회(신분당선_id);

        assertThat(지하철_노선.jsonPath().getString("name")).isEqualTo(name);
        assertThat(지하철_노선.jsonPath().getString("color")).isEqualTo(color);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void 지하철_노선_삭제_인수테스트() {
        // given
        long 신분당선 = 지하철_노선_생성("신분당선", "red", "강남역", "교대역", 10);

        // when
        지하철_노선을_삭제한다(신분당선);

        // then
        지하철_노선이_존재하지_않는다(신분당선);
    }

    private void 지하철_노선을_삭제한다(long lineId) {
        RestAssured.given().log().all()
                .when().delete(URI.create("/lines/" + lineId))
                .then().log().all()
                .extract();
    }

    private void 지하철_노선이_존재하지_않는다(long lineId) {
        assertThat(지하철_노선_조회(lineId).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철_노선_조회(long lineId) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(URI.create("/lines/" + lineId))
                .then().log().all()
                .extract();
    }
}
