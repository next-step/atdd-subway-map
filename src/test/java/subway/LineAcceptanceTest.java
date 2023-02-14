package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.StationAcceptanceTest.지하철역_생성;
import static subway.common.ResponseUtils.ID_추출;
import static subway.common.ResponseUtils.적절한_응답_코드를_받을_수_있다;
import static subway.common.ResponseUtils.n개의_이름_목록을_조회할_수_있다;
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
import org.opentest4j.AssertionFailedError;
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

    private String 강남역_ID;
    private String 방배역_ID;
    private String 역삼역_ID;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        강남역_ID = Long.toString(ID_추출(지하철역_생성(강남역)));
        방배역_ID = Long.toString(ID_추출(지하철역_생성(방배역)));
        역삼역_ID = Long.toString(ID_추출(지하철역_생성(역삼역)));
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성(강남역_ID, 방배역_ID);
        ExtractableResponse<Response> 지하철_노선_생성_응답 = 지하철_노선_생성(신분당선_파라미터);

        // then
        적절한_응답_코드를_받을_수_있다(지하철_노선_생성_응답, HttpStatus.CREATED);

        ExtractableResponse<Response> 지하철_노선_목록_조회_응답 = 지하철_노선_목록_조회();

        적절한_응답_코드를_받을_수_있다(지하철_노선_목록_조회_응답, HttpStatus.OK);
        목록에서_노선_정보를_찾을_수_있다(지하철_노선_목록_조회_응답, 신분당선_파라미터);
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
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성(강남역_ID, 방배역_ID);
        Map<String, String> 분당선_파라미터 = 분당선_파라미터_생성(강남역_ID, 역삼역_ID);
        List<Map<String, String>> params = Arrays.asList(신분당선_파라미터, 분당선_파라미터);
        params.forEach(LineAcceptanceTest::지하철_노선_생성);

        // when
        ExtractableResponse<Response> 지하철_노선_목록_조회_응답 = 지하철_노선_목록_조회();

        // then
        적절한_응답_코드를_받을_수_있다(지하철_노선_목록_조회_응답, HttpStatus.OK);
        n개의_이름_목록을_조회할_수_있다(지하철_노선_목록_조회_응답,
            params.stream().map(p -> p.get("name")).toArray(String[]::new));
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
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성(강남역_ID, 방배역_ID);
        long 신분당선_ID = ID_추출(지하철_노선_생성(신분당선_파라미터));

        // when
        ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회(신분당선_ID);

        // then
        적절한_응답_코드를_받을_수_있다(지하철_노선_조회_응답, HttpStatus.OK);
        노선_정보를_응답_받을_수_있다(지하철_노선_조회_응답, 신분당선_파라미터);
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
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성(강남역_ID, 방배역_ID);
        Long 신분당선_ID = 지하철_노선_생성(신분당선_파라미터).jsonPath().getLong("id");

        // when
        Map<String, String> 신분당선_수정_파라미터 = 신분당선_수정_파라미터_생성();
        ExtractableResponse<Response> 지하철_노선_수정_응답 = 지하철_노선_수정(신분당선_ID, 신분당선_수정_파라미터);

        // then
        적절한_응답_코드를_받을_수_있다(지하철_노선_수정_응답, HttpStatus.OK);

        ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회(신분당선_ID);

        적절한_응답_코드를_받을_수_있다(지하철_노선_조회_응답, HttpStatus.OK);
        수정된_노선_정보를_응답_받을_수_있다(지하철_노선_조회_응답, 신분당선_수정_파라미터);
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
        Map<String, String> 신분당선_파라미터 = 신분당선_파라미터_생성(강남역_ID, 방배역_ID);
        long 신분당선_ID = ID_추출(지하철_노선_생성(신분당선_파라미터));

        // when
        ExtractableResponse<Response> 지하철_노선_삭제_응답 = 지하철_노선_삭제(신분당선_ID);

        // then
        적절한_응답_코드를_받을_수_있다(지하철_노선_삭제_응답, HttpStatus.NO_CONTENT);

        ExtractableResponse<Response> 지하철_노선_조회_응답 = 지하철_노선_조회(신분당선_ID);
        assertThrows(AssertionFailedError.class,
            () -> 적절한_응답_코드를_받을_수_있다(지하철_노선_조회_응답, HttpStatus.OK));
    }

    public static ExtractableResponse<Response> 지하철_노선_생성(Map<String, String> params) {
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회(Long id) {
        return RestAssured.given().log().all()
            .when().get("/lines/{id}", id)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정(Long id, Map<String, String> params) {
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/{id}", id)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_삭제(Long id) {
        return RestAssured.given().log().all()
            .when().delete("/lines/{id}", id)
            .then().log().all()
            .extract();
    }

    private void 노선_정보를_응답_받을_수_있다(ExtractableResponse<Response> response, Map<String, String> params) {
        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.jsonPath().getString("name")).isEqualTo(params.get("name"));
        assertThat(response.jsonPath().getString("color")).isEqualTo(params.get("color"));
        assertThat(response.jsonPath().getString("stations.id[0]"))
            .isEqualTo(params.get("upStationId"));
        assertThat(response.jsonPath().getString("stations.id[1]"))
            .isEqualTo(params.get("downStationId"));

    }

    private void 목록에서_노선_정보를_찾을_수_있다(ExtractableResponse<Response> response, Map<String, String> params) {
        assertThat(response.jsonPath().getList("id").get(0)).isNotNull();
        assertThat(response.jsonPath().getList("name").get(0))
            .isEqualTo(params.get("name"));
        assertThat(response.jsonPath().getList("color").get(0))
            .isEqualTo(params.get("color"));
        assertThat(response.jsonPath().getString("stations[0].id[0]"))
            .isEqualTo(params.get("upStationId"));
        assertThat(response.jsonPath().getString("stations[0].id[1]"))
            .isEqualTo(params.get("downStationId"));
    }

    private void 수정된_노선_정보를_응답_받을_수_있다(ExtractableResponse<Response> response, Map<String, String> params) {
        assertThat(response.jsonPath().getString("name"))
            .isEqualTo(params.get("name"));
        assertThat(response.jsonPath().getString("color"))
            .isEqualTo(params.get("color"));
    }

}