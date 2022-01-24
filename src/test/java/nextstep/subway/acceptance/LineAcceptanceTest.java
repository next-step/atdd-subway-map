package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공 한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        final Map<String, String> requestParams = new HashMap<>();
        requestParams.put("color", "bg-red-600");
        requestParams.put("name", "신분당선");

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParams)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답 받는다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        final Map<String, String> requestParams1 = new HashMap<>();
        requestParams1.put("name", "신분당선");
        requestParams1.put("color", "bg-red-600");

        final Map<String, String> requestParams2 = new HashMap<>();
        requestParams2.put("name", "2호선");
        requestParams2.put("color", "bg-green-600");

        Arrays.asList(requestParams1, requestParams2)
                .forEach(requestParams -> RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(requestParams)
                        .when()
                        .post("/lines")
                        .then().log().all()
                        .extract());

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("name")).contains(requestParams1.get("name"), requestParams2.get("name"))
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답 받는다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        final Map<String, String> requestParams = new HashMap<>();
        requestParams.put("name", "신분당선");
        requestParams.put("color", "bg-red-600");

        final ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParams)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get(createResponse.header("Location"))
                .then().log().all()
                .extract();

        // then
        final JsonPath responseBody = response.jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(responseBody.getLong("id")).isNotNull(),
                () -> assertThat(responseBody.getString("name")).isEqualTo(requestParams.get("name"))
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공 한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        final Map<String, String> createRequestParams = new HashMap<>();
        createRequestParams.put("name", "신분당선");
        createRequestParams.put("color", "bg-red-600");

        final ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequestParams)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        final String location = createResponse.header("Location");
        final Map<String, String> updateRequestParams = new HashMap<>();
        updateRequestParams.put("name", "구분당선");
        updateRequestParams.put("color", "bg-blue-600");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateRequestParams)
                .when()
                .put(location)
                .then().log().all()
                .extract();

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get(location)
                .then().log().all()
                .extract();

        // then
        final JsonPath responseBody = response.jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(responseBody.getString("name")).isEqualTo(updateRequestParams.get("name")),
                () -> assertThat(responseBody.getString("color")).isEqualTo(updateRequestParams.get("color"))
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공 한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        final Map<String, String> requestParams = new HashMap<>();
        requestParams.put("name", "신분당선");
        requestParams.put("color", "bg-red-600");

        final ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParams)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete(createResponse.header("Location"))
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
