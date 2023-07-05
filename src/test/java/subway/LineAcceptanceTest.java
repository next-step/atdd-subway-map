package subway;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.StationResponse;
import subway.service.StationTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    static Map<String, String> 강남역_정보 = Map.of(
            "id", "1",
           "name", "kangnam"
    );

    static Map<String, String> 판교역_정보 = Map.of(
            "id", "2",
            "name", "pankyo"
    );

    static Map<String, String> 삼성역_정보 = Map.of(
            "id", "3",
            "name", "samsung"
    );

    // when
    static Map<String, String> 신분당선_생성_요청 = new HashMap<>();

    static Map<String, String> 이호선_생성_요청 = new HashMap<>();

    static {
        신분당선_생성_요청.putAll(
                Map.of(
                        "name", "신분당선",
                        "color", "bg-red-600",
                        "upStationId", "",
                        "downStationId", "",
                        "distance", "10"
                )
        );

        이호선_생성_요청.putAll(
                Map.of(
                        "name", "이호선",
                        "color", "bg-green-600",
                        "upStationId", "",
                        "downStationId", "",
                        "distance", "20"
                )
        );
    }


    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        // when
        지하철_노선_생성(신분당선_생성_요청, 강남역_정보, 판교역_정보);

        // then
        ExtractableResponse<Response> 노선_목록_조회_결과 = 지하철_노선_목록_조회();

        지하철_노선_생성_여부_검증(노선_목록_조회_결과, 신분당선_생성_요청, 강남역_정보, 판교역_정보);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     */
    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    void getLines() {
        // given
        지하철_노선_생성(신분당선_생성_요청, 강남역_정보, 판교역_정보);
        지하철_노선_생성(이호선_생성_요청, 강남역_정보, 삼성역_정보);

        // when
        ExtractableResponse<Response> 노선_목록_조회_결과 = 지하철_노선_목록_조회();

        // then
        지하철_노선_목록_검증(노선_목록_조회_결과, 신분당선_생성_요청, 강남역_정보, 판교역_정보, 이호선_생성_요청, 강남역_정보, 삼성역_정보);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철의 노선 정보를 응답받을 수 있다
     */
    @DisplayName("지하철 노선을 조회한다")
    @Test
    void getLine() {
        // Given
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철_노선_생성(신분당선_생성_요청, 강남역_정보, 판교역_정보);

        // when
        String lineUrl = 신분당선_생성_응답.header("Location");
        ExtractableResponse<Response> 노선_조회_결과 = 지하철_노선_조회(lineUrl);

        // then
        지하철_노선_조회_검증(노선_조회_결과, 신분당선_생성_요청);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다")
    @Test
    void changeLine() {
        // given
        Map<String, String> 노선_수정_요청_정보 = Map.of(
                "name", "다른분당선",
                "color", "bg-red-700"
        );
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철_노선_생성(신분당선_생성_요청, 강남역_정보, 판교역_정보);

        // when
        String lineUrl = 신분당선_생성_응답.header("Location");
        지하철_노선_수정_요청(lineUrl, 노선_수정_요청_정보);

        // then
        지하철_노선_수정_검증(lineUrl, 노선_수정_요청_정보);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철_노선_생성(신분당선_생성_요청, 강남역_정보, 판교역_정보);

        // when
        String lineUrl = 신분당선_생성_응답.header("Location");
        지하철_노선_삭제(lineUrl);

        // then
        지하철_삭제_여부_검증(지하철_노선_목록_조회());
    }

    private static ExtractableResponse<Response> 지하철_노선_생성(Map<String, String> 노선_생성_요청_정보, Map<String, String> 상행역_정보, Map<String, String> 하행역_정보) {
        노선_생성_요청_정보.put("upStationId", 상행역_정보.get("id"));
        노선_생성_요청_정보.put("downStationId", 하행역_정보.get("id"));

        StationTestUtils.지하철역_생성(상행역_정보);
        StationTestUtils.지하철역_생성(하행역_정보);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(노선_생성_요청_정보)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

    private static void 지하철_노선_생성_여부_검증(ExtractableResponse<Response> 노선_목록_조회_결과, Map<String, String> 노선_생성_요청_정보, Map<String, String> 상행역_정보, Map<String, String> 하행역_정보) {
        assertThat(노선_목록_조회_결과.jsonPath().getList("id")).isNotEmpty();
        assertThat(노선_목록_조회_결과.jsonPath().getList("name", String.class)).contains(노선_생성_요청_정보.get("name"));
        assertThat(노선_목록_조회_결과.jsonPath().getList("color", String.class)).contains(노선_생성_요청_정보.get("color"));
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .accept(ContentType.JSON)
                .get("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    private static void 지하철_노선_목록_검증(ExtractableResponse<Response> 노선_목록_조회_결과, Map<String, String> 노선1_생성_요청_정보, Map<String, String> 상행역1_정보, Map<String, String> 하행역1_정보, Map<String, String> 노선2_생성_요청_정보, Map<String, String> 상행역2_정보, Map<String, String> 하행역2_정보) {
        지하철_노선_생성_여부_검증(노선_목록_조회_결과, 노선1_생성_요청_정보, 상행역1_정보, 하행역1_정보);
        지하철_노선_생성_여부_검증(노선_목록_조회_결과, 노선2_생성_요청_정보, 상행역2_정보, 하행역2_정보);
    }

    private ExtractableResponse<Response> 지하철_노선_조회(String lineUrl) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .accept(ContentType.JSON)
                .get(lineUrl)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    private void 지하철_노선_조회_검증(ExtractableResponse<Response> 노선_조회_결과, Map<String, String> 노선_생성_요청_정보) {
        assertThat(노선_조회_결과.jsonPath().getString("id")).isNotEmpty();
        assertThat(노선_조회_결과.jsonPath().getString("name")).isEqualTo(노선_생성_요청_정보.get("name"));
        assertThat(노선_조회_결과.jsonPath().getString("color")).isEqualTo(노선_생성_요청_정보.get("color"));
        assertThat(노선_조회_결과.jsonPath().getList("stations", StationResponse.class))
                .hasSize(2)
                .extracting("id").containsExactlyInAnyOrder(Long.parseLong(노선_생성_요청_정보.get("upStationId")), Long.parseLong(노선_생성_요청_정보.get("downStationId")));
    }

    private static ExtractableResponse<Response> 지하철_노선_수정_요청(String lineUrl, Map<String, String> 노선_수정_요청_정보) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(노선_수정_요청_정보)
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .put(lineUrl)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    private void 지하철_노선_수정_검증(String lineUrl, Map<String, String> 노선_수정_요청_정보) {
        ExtractableResponse<Response> 노선_조회_결과 = 지하철_노선_조회(lineUrl);

        assertThat(노선_수정_요청_정보.get("name")).isEqualTo(노선_조회_결과.jsonPath().getString("name"));
        assertThat(노선_수정_요청_정보.get("color")).isEqualTo(노선_조회_결과.jsonPath().getString("color"));
    }

    private ExtractableResponse<Response> 지하철_노선_삭제(String lineUrl) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .delete(lineUrl)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        return response;
    }

    private void 지하철_삭제_여부_검증(ExtractableResponse<Response> 노선_조회_결과) {
        노선_조회_결과.jsonPath().getList("id").isEmpty();
    }
}
