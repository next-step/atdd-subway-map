package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
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
        ExtractableResponse<Response> response = lineCreateRequest(lineRequest);

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
        ExtractableResponse<Response> responseA = lineCreateRequest(lineRequestA);

        Map<String, String> lineRequestB = new HashMap<>();
        lineRequestB.put("name", lineNameB);
        lineRequestB.put("color", lineColorB);
        ExtractableResponse<Response> responseB = lineCreateRequest(lineRequestB);

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all().when().get("/lines").then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(lineNameA, lineNameB);
    }

    /** Given 지하철 노선 생성을 요청 하고 When 생성한 지하철 노선 조회를 요청 하면 Then 생성한 지하철 노선을 응답받는다 */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        String lineName = "신분당선";
        String lineColor = "bg-red-600";

        Map<String, String> lineRequestA = new HashMap<>();
        lineRequestA.put("name", lineName);
        lineRequestA.put("color", lineColor);
        ExtractableResponse<Response> createResponse = lineCreateRequest(lineRequestA);

        long id = createResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> readLineResponse =
          RestAssured.given().log().all().when().get("/lines/" + id).then().log().all().extract();

        assertThat(readLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        String responseLineName = readLineResponse.jsonPath().getString("name");
        assertThat(responseLineName).isEqualTo(lineName);
    }

    /** Given 지하철 노선 생성을 요청 하고 When 지하철 노선의 정보 수정을 요청 하면 Then 지하철 노선의 정보 수정은 성공한다. */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        String lineName = "신분당선";
        String lineColor = "bg-red-600";

        Map<String, String> createRequest = new HashMap<>();
        createRequest.put("name", lineName);
        createRequest.put("color", lineColor);
        ExtractableResponse<Response> createResponse = lineCreateRequest(createRequest);

        long id = createResponse.jsonPath().getLong("id");

        // when
        String updateLineName = "구분당선";
        String updateLineColor = "bg-blue-600";
        Map<String, String> updateRequest = new HashMap<>();
        updateRequest.put("name", updateLineName);
        updateRequest.put("color", updateLineColor);
        ExtractableResponse<Response> updateResponse = RestAssured.given()
          .log()
          .all()
          .body(updateRequest)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .put("/lines/" + id)
          .then()
          .log()
          .all()
          .extract();

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> readUpdatedLineResponse = specificLineReadRequest(id);
        assertThat(readUpdatedLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        String readUpdatedLineName = readUpdatedLineResponse.jsonPath().getString("name");
        assertThat(readUpdatedLineName).isEqualTo(updateLineName);
    }

    /** Given 지하철 노선 생성을 요청 하고 When 생성한 지하철 노선 삭제를 요청 하면 Then 생성한 지하철 노선 삭제가 성공한다. */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {}

    /** 반복되는 생성 코드를 줄이기 위해 createRequest 를 따로 작성 */
    static ExtractableResponse<Response> lineCreateRequest(Map<String, String> lineRequest){
        return RestAssured.given()
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
    }

    static ExtractableResponse<Response> specificLineReadRequest(Long id){
        return RestAssured.given().log().all().when().get("/lines/" + id).then().log().all().extract();
    }
}
