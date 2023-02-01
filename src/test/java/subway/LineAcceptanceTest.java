package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.StationAcceptanceTest.지하철역_생성;
import static subway.fixtures.LineFixtures.분당선_파라미터_생성;
import static subway.fixtures.LineFixtures.신분당선_수정_파라미터_생성;
import static subway.fixtures.LineFixtures.신분당선_파라미터_생성;
import static subway.fixtures.StationFixtures.강남역;
import static subway.fixtures.StationFixtures.방배역;
import static subway.fixtures.StationFixtures.역삼역;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.common.DatabaseCleanup;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        지하철역_생성(강남역);
        지하철역_생성(방배역);
        지하철역_생성(역삼역);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성();
        지하철_노선_생성(신분당선_파라미터);

        // then
        ExtractableResponse<Response> response = 지하철_노선_목록_조회();
        assertThat(response.jsonPath().getList("id").get(0)).isNotNull();
        assertThat(response.jsonPath().getList("name").get(0)).isEqualTo(신분당선_파라미터.get("name"));
        assertThat(response.jsonPath().getList("color").get(0)).isEqualTo(신분당선_파라미터.get("color"));
        assertThat(response.jsonPath().getString("stations[0].id[0]")).isEqualTo(신분당선_파라미터.get("upStationId"));
        assertThat(response.jsonPath().getString("stations[0].id[1]")).isEqualTo(신분당선_파라미터.get("downStationId"));

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    void showLines() {
        // given
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성();
        Map<String, String> 분당선_파라미터 = 분당선_파라미터_생성();
        List<Map<String, String>> params = Arrays.asList(신분당선_파라미터, 분당선_파라미터);
        params.forEach(this::지하철_노선_생성);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회();

        // then
        assertThat(response.jsonPath().getList("name").size()).isEqualTo(params.size());

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다")
    @Test
    void showLine() {
        // given
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성();
        Long lineId = 지하철_노선_생성(신분당선_파라미터).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회(lineId);

        // then
        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.jsonPath().getString("name")).isEqualTo(신분당선_파라미터.get("name"));
        assertThat(response.jsonPath().getString("color")).isEqualTo(신분당선_파라미터.get("color"));
        assertThat(response.jsonPath().getString("stations.id[0]")).isEqualTo(신분당선_파라미터.get("upStationId"));
        assertThat(response.jsonPath().getString("stations.id[1]")).isEqualTo(신분당선_파라미터.get("downStationId"));

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다")
    @Test
    void updateLine() {
        // given
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성();
        Long lineId = 지하철_노선_생성(신분당선_파라미터).jsonPath().getLong("id");

        // when
        Map<String, String> 신분당선_수정_파라미터 = 신분당선_수정_파라미터_생성();
        지하철_노선_수정(lineId, 신분당선_수정_파라미터);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회(lineId);
        assertThat(response.jsonPath().getString("name")).isEqualTo(신분당선_수정_파라미터.get("name"));
        assertThat(response.jsonPath().getString("color")).isEqualTo(신분당선_수정_파라미터.get("color"));

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 삭제한다")
    @Test
    void deleteLine() {
        // given
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성();
        Long lineId = 지하철_노선_생성(신분당선_파라미터).jsonPath().getLong("id");

        // when
        지하철_노선_삭제(lineId);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회(lineId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private ExtractableResponse<Response> 지하철_노선_생성(Map<String, String> params) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

    private ExtractableResponse<Response> 지하철_노선_조회(Long id) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when().get("/lines/{id}", id)
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

    private ExtractableResponse<Response> 지하철_노선_수정(Long id, Map<String, String> params) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{id}", id)
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

    private ExtractableResponse<Response> 지하철_노선_삭제(Long id) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when().delete("/lines/{id}", id)
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        return response;
    }

}