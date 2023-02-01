package subway;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다")
    @Test
    void 노선_생성() {
        String lineName = "2호선";
        지하철_노선_생성_요청(lineName);

        List<String> lineNames = 전체_지하철_노선_조회().jsonPath().getList("name", String.class);

        assertThat(lineNames).containsAnyOf(lineName);
    }



    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void 노선_목록_조회() {
        String eightLineName = "8호선";
        String twoLineName = "2호선";

        지하철_노선_생성_요청(eightLineName);
        지하철_노선_생성_요청(twoLineName);

        List<String> lineNames = 지하철_목록_노선을_조회한다();

        지하철_노선이_조회된다(eightLineName, twoLineName, lineNames);
    }

    private List<String> 지하철_목록_노선을_조회한다() {
        return RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
    }

    private void 지하철_노선이_조회된다(String eightLineName, String twoLineName, List<String> lineNames) {
        assertThat(lineNames).contains(eightLineName, twoLineName);
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void 노선_조회() {
        String eightLineName = "8호선";

        // given
        JsonPath createdLineJsonPath = 지하철_노선_생성_요청(eightLineName).jsonPath();

        // when
        Integer createdLineId = createdLineJsonPath.getInt("id");
        JsonPath lineJsonPath = 지하철_노선_조회(createdLineId).jsonPath();

        // then
        String createdLineName = createdLineJsonPath.get("name");
        생성한_지하철_노선_정보_응답_확인(createdLineName, createdLineId, lineJsonPath);
    }

    private void 생성한_지하철_노선_정보_응답_확인(String createdLineName, Integer createdLineId, JsonPath lineJsonPath) {
        assertThat(lineJsonPath.getInt("id")).isEqualTo(createdLineId);
        assertThat(lineJsonPath.getString("name")).isEqualTo(createdLineName);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void 노선_수정() {

        // given
        String eightLineName = "8호선";
        JsonPath createdLineJsonPath = 지하철_노선_생성_요청(eightLineName).jsonPath();
        String createdLineName = createdLineJsonPath.get("name");
        int createdLineId = createdLineJsonPath.getInt("id");

        // when
        JsonPath lineJsonPath = 지하철_노선_조회(createdLineId).jsonPath();
        assertThat(lineJsonPath.getString("name")).isEqualTo(createdLineName);
        assertThat(lineJsonPath.getInt("id")).isEqualTo(createdLineId);

        //then
        String modifyLineName = "2호선";
        Map<String, String> params = new HashMap<>();
        params.put("name", modifyLineName);

        JsonPath modifiedLineJsonPath = 지하철_노선_수정(params, createdLineId).jsonPath();
        assertThat(modifiedLineJsonPath.getString("name")).isEqualTo(modifyLineName);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void 노선_삭제() {
        String eightLineName = "8호선";
        JsonPath lineJsonPath = 지하철_노선_생성_요청(eightLineName).jsonPath();

        int lineId = lineJsonPath.getInt("id");
        ExtractableResponse<Response> response = 지하철_노선을_삭제한다(lineId);
        지하철_노선이_삭제되었다(response);
    }

    private ExtractableResponse<Response> 지하철_노선을_삭제한다(int lineId) {
        return RestAssured.given().log().all()
                .when().delete("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 전체_지하철_노선_조회() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(String lineName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);

        return RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_조회(long lineId) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정(Map<String, String> params, long lineId) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    private void 지하철_노선이_삭제되었다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
