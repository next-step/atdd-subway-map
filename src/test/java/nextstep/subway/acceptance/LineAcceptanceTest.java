package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.common.CommonLineAcceptance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.common.CommonLineAcceptance.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        Map<String, String> params = getParamsMap("신분당선", "bg-red-600");
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(params);

        // then
        String responseName = response.jsonPath().getString("name");
        String responseColor = response.jsonPath().getString("color");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(responseName).isEqualTo(params.get("name"));
        assertThat(responseColor).isEqualTo(params.get("color"));
        assertThat(response.header("location")).isNotEmpty();
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
        //given
        Map<String, String> params = getParamsMap("신분당선","bg-red-600");
        지하철_노선_생성_요청(params);

        //given
        Map<String, String> params2 = getParamsMap("2호선", "bg-green-600");
        지하철_노선_생성_요청(params2);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("lines")
                .then().log().all().extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).containsExactly(params.get("name"), params2.get("name"));
        assertThat(response.jsonPath().getList("color")).containsExactly(params.get("color"), params2.get("color"));
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        //given
        Map<String, String> params = getParamsMap("신분당선","bg-red-600");

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(params);

        // when
        response = RestAssured
                .given().log().all()
                .when().get(response.header("location"))
                .then().log().all().extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.jsonPath().getString("name")).isEqualTo(params.get("name"));
        assertThat(response.jsonPath().getString("color")).isEqualTo(params.get("color"));

    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        //given
        Map<String, String> params = getParamsMap("신분당선","bg-red-600");

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(params);

        // when
        Map<String, String> modifyParams = getParamsMap("구분당선","bg-blue-600");
        String location = response.header("location");

        response = RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
                .body(modifyParams)
                .when().put(location)
                .then().log().all().extract();
                //.header("content-length",45)
                //질문 : 해당 내용을 추가하면 ClientProtocolException : Content-Length header already present로 Error가 생기는 이유가 뭘까요?

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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
        Map<String, String> params = getParamsMap("신분당선","bg-red-600");
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(params);

        // when
        String location = response.header("location");

        response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(location)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }




}
