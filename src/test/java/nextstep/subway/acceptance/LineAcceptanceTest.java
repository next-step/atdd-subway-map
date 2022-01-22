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
        // given
        String bgRed600 = "bg-red-600";
        String 신분당선 = "신분당선";

        // when
        ExtractableResponse<Response> response = LineSteps.지하철_노선_생성_요청(신분당선, bgRed600);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Integer id = response.jsonPath().get("id");
        String name = response.jsonPath().get("name");
        String color = response.jsonPath().get("color");
        String createdDate = response.jsonPath().get("createdDate");
        String modifiedDate = response.jsonPath().get("modifiedDate");

        assertThat(id).isNotNull();
        assertThat(name).isEqualTo(신분당선);
        assertThat(color).isEqualTo(bgRed600);
        assertThat(createdDate).isNotNull();
        assertThat(modifiedDate).isNotNull();

    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("지하철 노선 중복이름 생성")
    @Test
    void createDuplicateNameLine() {
        // given
        String bgRed600 = "bg-red-600";
        String 신분당선 = "신분당선";

        LineSteps.지하철_노선_생성_요청(신분당선, bgRed600);

        // when
        ExtractableResponse<Response> response = LineSteps.지하철_노선_생성_요청(신분당선, bgRed600);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
        // given
        String bgRed600 = "bg-red-600";
        String 신분당선 = "신분당선";

        ExtractableResponse<Response> createResponse1 = LineSteps.지하철_노선_생성_요청(신분당선, bgRed600);

        Integer id1 = createResponse1.jsonPath().get("id");
        String createDate1 = createResponse1.jsonPath().get("createdDate");
        String modifiedDate1 = createResponse1.jsonPath().get("modifiedDate");
        String bgGreen600 = "bg-green-600";
        String line2 = "2호선";

        ExtractableResponse<Response> createResponse2 = LineSteps.지하철_노선_생성_요청(line2, bgGreen600);

        Integer id2 = createResponse2.jsonPath().get("id");
        String createDate2 = createResponse2.jsonPath().get("createdDate");
        String modifiedDate2 = createResponse2.jsonPath().get("modifiedDate");

        String url = LineSteps.DEFAULT_PATH;
        ExtractableResponse<Response> response = LineSteps.지하철_노선_조회_요청(url);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> names = response.jsonPath().getList("name");
        List<String> colors = response.jsonPath().getList("color");
        List<Integer> ids = response.jsonPath().getList("id");
        List<String> createdDates = response.jsonPath().getList("createdDate");
        List<String> modifiedDates = response.jsonPath().getList("modifiedDate");

        assertThat(names).contains(신분당선, line2);
        assertThat(colors).contains(bgRed600, bgGreen600);
        assertThat(ids).contains(id1, id2);
        assertThat(createdDates).contains(createDate1, createDate2);
        assertThat(modifiedDates).contains(modifiedDate1, modifiedDate2);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        String bgRed600 = "bg-red-600";
        String 신분당선 = "신분당선";
        Map<String, String> params1 = new HashMap<>();
        params1.put("color", bgRed600);
        params1.put("name", 신분당선);

        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청(신분당선, bgRed600);

        Integer id = createResponse.jsonPath().get("id");
        String createdDate = createResponse.jsonPath().getString("createdDate");
        String modifiedDate = createResponse.jsonPath().getString("modifiedDate");

        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = LineSteps.지하철_노선_조회_요청(uri);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Integer lineId = response.jsonPath().get("id");
        String lineName = response.jsonPath().getString("name");
        String lineColor = response.jsonPath().getString("color");
        String lineCreatedDate = response.jsonPath().get("createdDate");
        String lineModifiedDate = response.jsonPath().getString("modifiedDate");
        assertThat(lineId).isEqualTo(id);
        assertThat(lineName).isEqualTo(신분당선);
        assertThat(lineColor).isEqualTo(bgRed600);
        assertThat(lineCreatedDate).isEqualTo(createdDate);
        assertThat(lineModifiedDate).isEqualTo(modifiedDate);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        String bgRed600 = "bg-red-600";
        String 신분당선 = "신분당선";
        Map<String, String> params1 = new HashMap<>();
        params1.put("color", bgRed600);
        params1.put("name", 신분당선);

        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청(신분당선, bgRed600);

        Integer id = createResponse.jsonPath().get("id");
        String createdDate = createResponse.jsonPath().getString("createdDate");
        String modifiedDate = createResponse.jsonPath().getString("modifiedDate");

        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Integer lineId = response.jsonPath().get("id");
        String lineName = response.jsonPath().getString("name");
        String lineColor = response.jsonPath().getString("color");
        String lineCreatedDate = response.jsonPath().get("createdDate");
        String lineModifiedDate = response.jsonPath().getString("modifiedDate");
        assertThat(lineId).isEqualTo(id);
        assertThat(lineName).isEqualTo(신분당선);
        assertThat(lineColor).isEqualTo(bgRed600);
        assertThat(lineCreatedDate).isEqualTo(createdDate);
        assertThat(lineModifiedDate).isEqualTo(modifiedDate);
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
        String bgRed600 = "bg-red-600";
        String 신분당선 = "신분당선";

        ExtractableResponse<Response> createResponse = LineSteps.지하철_노선_생성_요청(신분당선, bgRed600);

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
