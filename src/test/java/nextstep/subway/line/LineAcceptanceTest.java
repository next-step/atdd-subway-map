package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
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

    Map<String, String> 파라미터_생성(String name, String color){
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        return param;
    }

    ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color){
        return RestAssured.given()
                .body(파라미터_생성(name, color))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when()
                .post("/lines")
                .then().log().all().extract();
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선_생성_요청_및_확인() {
        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response =
                지하철_노선_생성_요청("선릉", "green darken-1");

        // then
        // 지하철_노선_생성됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void 지하철_노선_목록_조회() {
        // given
        // 지하철_노선_등록되어_있음
        // 지하철_노선_등록되어_있음

        ExtractableResponse<Response> createResponse1 =
                지하철_노선_생성_요청("선릉", "green darken-1");

        ExtractableResponse<Response> createResponse2 =
                지하철_노선_생성_요청("강남", "green darken-1");

        // when
        // 지하철_노선_목록_조회_요청

        ExtractableResponse<Response> getResponses = 지하철_노선_목록_조회_결과_요청();

        // then
        // 지하철_노선_목록_응답됨
        // 지하철_노선_목록_포함됨
        assertThat(getResponses.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<LineResponse> lineResponses = 지하철_노선_목록_조회_결과_리스트(getResponses);
        assertThat(lineResponses.size()).isEqualTo(2);

        List<Long> expectedLineIds = 지하철_노선_목록_예상_아이디_리스트(createResponse1, createResponse2);
        List<Long> resultLineIds = 지하철_노선_목록_결과_아이디_리스트(getResponses);
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    ExtractableResponse<Response> 지하철_노선_목록_조회_결과_요청(){
        return RestAssured.given()
                .log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all().extract();
    }

    List<LineResponse> 지하철_노선_목록_조회_결과_리스트(ExtractableResponse<Response> getResponses){
        return getResponses.jsonPath().getList(".", LineResponse.class);
    }

    List<Long> 지하철_노선_목록_예상_아이디_리스트(ExtractableResponse<Response>... responses){
        return Arrays.stream(responses)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
    }

    List<Long> 지하철_노선_목록_결과_아이디_리스트(ExtractableResponse<Response> getResponses){
        return 지하철_노선_목록_조회_결과_리스트(getResponses).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void 지하철_노선_조회() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse =
                지하철_노선_생성_요청("선릉", "green darken-1");

        Long id = 생성된_Entity의_ID_가져오기(createResponse);

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> getResponse = 지하철_노선_조회_요청(id);

        // then
        // 지하철_노선_응답됨
        assertThat(getResponse.statusCode())
                .isEqualTo(HttpStatus.OK.value());
    }

    ExtractableResponse<Response> 지하철_노선_조회_요청(Long id){
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all().extract();
    }

    Long 생성된_Entity의_ID_가져오기(ExtractableResponse<Response> createResponse){
        return Long.parseLong(
                createResponse
                        .header("Location")
                        .split("/")[2]
        );
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void 지하철_노선_수정() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse =
                지하철_노선_생성_요청("선릉", "green darken-1");

        Long id = 생성된_Entity의_ID_가져오기(createResponse);

        // when
        // 지하철_노선_수정_요청
        Map<String, String> param = 파라미터_생성("선정릉", "red darken-1");
        ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(id, param);

        // then
        // 지하철_노선_수정됨
        assertThat(updateResponse.jsonPath().getMap(".").get("name"))
                .isEqualTo(param.get("name"));
        assertThat(updateResponse.jsonPath().getMap(".").get("color"))
                .isEqualTo(param.get("color"));
    }

    ExtractableResponse<Response> 지하철_노선_수정_요청(Long id, Map<String, String> param){
        return RestAssured.given()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when().put("/lines/" + id)
                .then().log().all().extract();
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void 지하철_노선_삭제() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse =
                지하철_노선_생성_요청("선릉", "green darken-1");

        Long id = 생성된_Entity의_ID_가져오기(createResponse);
        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> deleteResponse = 지하철_노선_삭제_요청(id);
        // then
        // 지하철_노선_삭제됨
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    ExtractableResponse<Response> 지하철_노선_삭제_요청(Long id){
        return RestAssured.given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + id)
                .then().log().all().extract();
    }

}
