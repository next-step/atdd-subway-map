package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames =
                RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given

        Map<String, String> sinBunDangParam = new HashMap<>();
        sinBunDangParam.put("name", "신분당선");
        sinBunDangParam.put("color", "bg-red-600");
        sinBunDangParam.put("upStationId", "1");
        sinBunDangParam.put("downStationId", "2");
        sinBunDangParam.put("distance", "10");

        RestAssured.given().log().all()
                .body(sinBunDangParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        Map<String, String> line2Param = new HashMap<>();
        line2Param.put("name", "2호선");
        line2Param.put("color", "bg-green-600");
        line2Param.put("upStationId", "2");
        line2Param.put("downStationId", "3");
        line2Param.put("distance", "10");

        RestAssured.given().log().all()
                .body(line2Param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // when
        List<String> lineNames =
                RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);

        // then
        assertThat(lineNames).containsAnyOf("신분당선", "2호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLine() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        ExtractableResponse<Response> createResponse =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();


        // when
        String lineName =
                RestAssured.given().log().all()
                        .when().get("/lines/" + createResponse.jsonPath().getLong("id"))
                        .then().log().all()
                        .extract().jsonPath().getString("name");

        // then
        assertThat(lineName).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        ExtractableResponse<Response> createResponse =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        // when
        params.put("name", "다른분당선");
        params.put("color", "bg-red-600");

        ExtractableResponse<Response> updateResponse =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().put("/lines/" + createResponse.jsonPath().getLong("id"))
                        .then().log().all()
                        .extract();

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        ExtractableResponse<Response> createResponse =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        // when
        ExtractableResponse<Response> deleteResponse =
                RestAssured.given().log().all()
                        .when().delete("/lines/" + createResponse.jsonPath().getLong("id"))
                        .then().log().all()
                        .extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
