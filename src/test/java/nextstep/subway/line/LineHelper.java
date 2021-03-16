package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineHelper {
    public static LineRequest 파라미터_생성(String name, String color, Long upStationId, Long downStationId, int distance) {
        return new LineRequest(name, color, upStationId, downStationId, distance);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color, Long upStationId, Long downStationId, int distance) {
        return RestAssured.given()
                .body(파라미터_생성(name, color, upStationId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when()
                .post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all().extract();
    }

    public static Long 생성된_Entity의_ID_가져오기(ExtractableResponse<Response> createResponse) {
        return Long.parseLong(
                createResponse
                        .header("Location")
                        .split("/")[2]
        );
    }


    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> getResponses){
        assertThat(getResponses.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
    public static void 지하철_노선_목록_리스트_사이즈_확인(List<LineResponse> lineResponses){
        assertThat(lineResponses.size()).isEqualTo(2);
    }
    public static void 지하철_노선_목록이_예상목록_포함하는지_확인(List<Long> expectedLineIds, List<Long> resultLineIds){
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_결과_요청(){
        return RestAssured.given()
                .log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all().extract();
    }

    public static List<LineResponse> 지하철_노선_목록_조회_결과_리스트(ExtractableResponse<Response> getResponses){
        return getResponses.jsonPath().getList(".", LineResponse.class);
    }

    public static List<Long> 지하철_노선_목록_예상_아이디_리스트(ExtractableResponse<Response>... responses){
        return Arrays.stream(responses)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
    }

    public static List<Long> 지하철_노선_목록_결과_아이디_리스트(ExtractableResponse<Response> getResponses){
        return 지하철_노선_목록_조회_결과_리스트(getResponses).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
    }


    public static void 지하철_노선_응답됨(ExtractableResponse<Response> getResponse){
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.jsonPath().getObject(".", LineResponse.class)
                .getStationResponses().size()).isEqualTo(3);
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long id, LineRequest lineRequest){
        return RestAssured.given()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when().put("/lines/" + id)
                .then().log().all().extract();
    }

    public static void 지하철_노선_수정_확인(LineRequest lineRequest, ExtractableResponse<Response> updateResponse){
        assertThat(updateResponse.jsonPath().getMap(".").get("name"))
                .isEqualTo(lineRequest.getName());
        assertThat(updateResponse.jsonPath().getMap(".").get("color"))
                .isEqualTo(lineRequest.getColor());
    }



    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long id){
        return RestAssured.given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + id)
                .then().log().all().extract();
    }

    public static void 지하철_노선_삭제_응답됨(ExtractableResponse<Response> deleteResponse){
        assertThat(deleteResponse.statusCode())
                .isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
