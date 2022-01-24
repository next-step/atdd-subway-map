package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // when
        final ExtractableResponse<Response> response = 정상적인_지하철_역_생성_요청한다("강남역");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("지하철역 중복 이름")
    @Test
    void createDuplicateStation() {
        // given
        final String 강남역 = "강남역";
        정상적인_지하철_역_생성_요청한다(강남역);

        // when
        final ExtractableResponse<Response> response = 정상적인_지하철_역_생성_요청한다(강남역);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("duplicate station name occurred"))
        );
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void getStations() {
        /// given
        final String 강남역 = "강남역";
        final String 역삼역 = "역삼역";

        정상적인_지하철_역_생성_요청한다(강남역);
        정상적인_지하철_역_생성_요청한다(역삼역);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("name")).contains(강남역, 역삼역)
        );
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        final ExtractableResponse<Response> createResponse = 정상적인_지하철_역_생성_요청한다("강남역");

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 정상적인_지하철_역_생성_요청한다(final String name) {
        final Map<String, String> params = 정상적인_지하철_역_생성_데이터를_만든다(name);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
        return response;
    }

    private Map<String, String> 정상적인_지하철_역_생성_데이터를_만든다(final String name) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }
}
