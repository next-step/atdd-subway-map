package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {

        ExtractableResponse<Response> response = 지하철_노선_등록("신분당선","bg-red-600");
        // then
        // 지하철_노선_생성됨
        지하철_노선_생성_성공(response);
        assertThat("신분당선").isEqualTo(response.jsonPath().get("name"));
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록("신분당선","bg-red-600");
        ExtractableResponse<Response> createResponse2 = 지하철_노선_등록("2호선","bg-green-600");

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = 지하철_전체노선_조회_요청(createResponse2);

        // then
        // 지하철_노선_목록_응답됨
        지하철_노선_응답_성공(response);

        // 지하철_노선_목록_포함됨
        List<Long> expectedLineIds = Arrays.asList(createResponse1, createResponse2).stream()
                .map(list -> Long.parseLong(list.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> responseLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(list -> list.getId())
                .collect(Collectors.toList());

        assertThat(responseLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록("신분당선","bg-red-600");

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse1);

        // then
        // 지하철_노선_응답됨
        지하철_노선_응답_성공(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = 지하철_노선_등록("신분당선","bg-red-600");

        Map<String, String> updateParam = new HashMap<>();
        updateParam.put("color","bg-blue-600");
        updateParam.put("name", "구분당선");

        // when
        // 지하철_노선_수정_요청
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(createResponse,updateParam);

        // then
        // 지하철_노선_수정됨
        지하철_노선_응답_성공(response);
        assertThat(updateParam.get("name")).isEqualTo(response.jsonPath().get("name"));
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = 지하철_노선_등록("신분당선","bg-red-600");

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createResponse);

        // then
        // 지하철_노선_삭제됨
        지하철_노선_삭제_성공(response);
    }

    /** 중복코드 리팩터링 */
    private String getResponseURI(ExtractableResponse<Response> response){
        if(response!=null) {
            return response.header("Location");
        }else
            return null;
    }

    private ExtractableResponse<Response> 지하철_노선_등록(String name, String color){

        ExtractableResponse<Response> createResponse;
        Map<String, String> param1 = new HashMap<>();

        param1.put("color", color);
        param1.put("name", name);

        createResponse = RestAssured.given().log().all()
                .body(param1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        return createResponse;
    }

    private ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> request){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete(getResponseURI(request))
                .then().log().all().extract();

        return response;
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> request){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get(getResponseURI(request))
                .then().log().all().extract();
        return response;
    }

    private ExtractableResponse<Response> 지하철_전체노선_조회_요청(ExtractableResponse<Response> request){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get(getResponseURI(request).split("/")[1])
                .then().log().all().extract();
        return response;
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> request,Map<String, String> param){
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(getResponseURI(request))
                .then().log().all().extract();

        return response;
    }

    private void 지하철_노선_응답_성공(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_생성_성공(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 지하철_노선_삭제_성공(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}