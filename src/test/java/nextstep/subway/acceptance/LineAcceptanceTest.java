package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.utils.ResponseUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static nextstep.subway.utils.ResponseUtils.*;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        Map<String, Object> param = new HashMap<>();
        param.put("name", "1호선");
        param.put("color", "blue darken-4");

        final ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(param)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/lines")
                        .then().log().all()
                        .extract();

        httpStatus가_CREATED면서_Location이_존재함(response);
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
        LineRequest lineRequest1 = new LineRequest("1호선", "blue darken-4");
        LineRequest lineRequest2 = new LineRequest("7호선", "green darken-3");

        RestAssured.given().log().all()
                .body(lineRequest1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        RestAssured.given().log().all()
                .body(lineRequest2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).contains("1호선");
        assertThat(response.jsonPath().getList("name")).contains("7호선");
        // todo 리팩토링, jsonpath
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        LineRequest lineRequest = new LineRequest("1호선", "blue darken-4");

        final ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        final String uri = createResponse.header("Location");

        final ExtractableResponse<Response> getResponse = RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();

        final String name = getResponse.jsonPath().get("name");
        assertThat(name).isEqualTo("1호선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        LineRequest lineRequest = new LineRequest("1호선", "blue darken-4");

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        final String uri = response.header("Location");

        LineRequest editedLineRequest = new LineRequest("7호선", "green darken-3");
        final ExtractableResponse<Response> editReponse = RestAssured.given().log().all()
                .body(editedLineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch(uri)
                .then().log().all()
                .extract();

        final String name = editReponse.jsonPath().get("name");
        final String color = editReponse.jsonPath().get("color");
        assertThat(name).contains("7호선");
        assertThat(color).contains("green darken-3");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        LineRequest lineRequest = new LineRequest("1호선", "blue darken-4");

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        final String uri = response.header("Location");

        final ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();

        ResponseUtils.httpStatus가_NO_CONTENT(deleteResponse);
    }
}
