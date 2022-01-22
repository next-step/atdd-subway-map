package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /** When 지하철 노선 생성을 요청 하면 Then 지하철 노선 생성이 성공한다. */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        Map<String, String> lineRequest = new HashMap<>();
        lineRequest.put("name", "신분당선");
        lineRequest.put("color", "bg-red-600");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given()
                        .log()
                        .all()
                        .body(lineRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/lines")
                        .then()
                        .log()
                        .all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철 노선 생성을 요청 하고 Given 새로운 지하철 노선 생성을 요청 하고 When 지하철 노선 목록 조회를 요청 하면 Then 두 노선이 포함된 지하철
     * 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        String lineNameA = "신분당선";
        String lineColorA = "bg-red-600";

        String lineNameB = "2호선";
        String lineColorB = "bg-green-600";

        Map<String, String> lineRequestA = new HashMap<>();
        lineRequestA.put("name", lineNameA);
        lineRequestA.put("color", lineColorA);
        ExtractableResponse<Response> responseA =
          RestAssured.given()
            .log()
            .all()
            .body(lineRequestA)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then()
            .log()
            .all()
            .extract();

        Map<String, String> lineRequestB = new HashMap<>();
        lineRequestB.put("name", lineNameB);
        lineRequestB.put("color", lineColorB);
        ExtractableResponse<Response> responseB =
          RestAssured.given()
            .log()
            .all()
            .body(lineRequestB)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then()
            .log()
            .all()
            .extract();

        // when
        ExtractableResponse<Response> response =
          RestAssured.given()
            .log()
            .all()
            .when()
            .get("/lines")
            .then()
            .log()
            .all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(lineNameA, lineNameB);
    }

    /** Given 지하철 노선 생성을 요청 하고 When 생성한 지하철 노선 조회를 요청 하면 Then 생성한 지하철 노선을 응답받는다 */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {}

    /** Given 지하철 노선 생성을 요청 하고 When 지하철 노선의 정보 수정을 요청 하면 Then 지하철 노선의 정보 수정은 성공한다. */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {}

    /** Given 지하철 노선 생성을 요청 하고 When 생성한 지하철 노선 삭제를 요청 하면 Then 생성한 지하철 노선 삭제가 성공한다. */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {}
}
