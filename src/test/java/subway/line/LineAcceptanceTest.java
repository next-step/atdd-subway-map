package subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.acceptance.AcceptanceTest;

@DisplayName("노선 관련 기능")
@AcceptanceTest
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 노선 생성")
    void createLine() {
        // given
        HashMap<String, String> params = 노선_파라미터_생성();

        // when
        ExtractableResponse<Response> response = 노선_생성(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("노선 목록 조회")
    void fineLine() {
        // given
        HashMap<String, String> params = 노선_파라미터_생성();
        노선_생성(params);
        params = 노선_파라미터_생성2();
        노선_생성(params);

        // when
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .when().get("/lines")
                       .then().log().all()
                       .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name", String.class).size()).isEqualTo(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("노선 단건 조회")
    void findLineDetail() {
        // given
        HashMap<String, String> params = 노선_파라미터_생성();
        ExtractableResponse<Response> createResponse = 노선_생성(params);

        // when
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .when().get("/lines/"+createResponse.jsonPath().getString("id"))
                       .then().log().all()
                       .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(params.get("name"));
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    @DisplayName("노선 수정")
    void modifyLine() {
        // given
        HashMap<String, String> params = 노선_파라미터_생성();
        ExtractableResponse<Response> createResponse = 노선_생성(params);
        HashMap<String, String> updateParam = new HashMap<>();
        updateParam.put("name", "다른분당선");
        updateParam.put("color", "bg-red-600");


        // when
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                       .body(updateParam)
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .when().put("/lines/"+createResponse.jsonPath().getString("id"))
                       .then().log().all()
                       .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    @DisplayName("노선 삭제")
    void removeLine() {
        // given
        HashMap<String, String> params = 노선_파라미터_생성();
        ExtractableResponse<Response> createResponse = 노선_생성(params);

        // when
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .when().delete("/lines/"+createResponse.jsonPath().getString("id"))
                       .then().log().all()
                       .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static ExtractableResponse<Response> 노선_생성(HashMap<String, String> params) {
        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/lines")
                          .then().log().all()
                          .extract();
    }

    private static HashMap<String, String> 노선_파라미터_생성() {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");
        return params;
    }
    private static HashMap<String, String> 노선_파라미터_생성2() {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-green-600");
        params.put("upStationId", "5");
        params.put("downStationId", "20");
        params.put("distance", "50");
        return params;
    }
}
