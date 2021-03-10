package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params){
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(Map<String, String> params){
        return 지하철_노선_생성_요청(params);
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(){
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_목록_포함됨(List<ExtractableResponse<Response>> responseList, ExtractableResponse<Response> response){
        List<Long> expectedLineIds = responseList.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath()
                .getList(".", LineResponse.class)
                .stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }


    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> response){
        // given
        String uri = response.header("Location");

        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> creteResponse, ExtractableResponse<Response> response){
        // given
        String uri = creteResponse.header("Location");
        Long id = Long.parseLong(uri.split("/")[2]);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        // 해당 id가 업데이트 되었나 데이터 검증
        assertThat(response.jsonPath().getLong("id")).isEqualTo(id);
    }


    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> response, Map<String, String> params){
        // given
        String uri = response.header("Location");
        // then
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> response){
        // given
        String uri = response.header("Location");
        // then
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_구간_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}