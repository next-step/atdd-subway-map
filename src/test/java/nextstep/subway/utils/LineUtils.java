package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LineUtils {
    public static List<Map<String, String>> 지하철_노선_데이터_생성(List<Map<String, String>> lines) {
        List<Map<String, String>> results = new ArrayList<>();
        for (Map<String, String> line : lines) {
            results.add(지하철_노선_데이터_생성(line.get("name"), line.get("color")));
        }
        return results;
    }

    public static Map<String, String> 지하철_노선_데이터_생성(String name, String color) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        return param;
    }

    public static List<ExtractableResponse<Response>> 지하철_노선_생성요청(List<Map<String, String>> params) {
        List<ExtractableResponse<Response>> responseList = new ArrayList<>();
        for (Map<String, String> param: params) {
            responseList.add(지하철_노선_생성요청(param));
        }
        return responseList;
    }

    public static ExtractableResponse<Response> 지하철_노선_생성요청(Map<String, String> param) {
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정요청(Map<String, String> param, String uri) {
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록요청() {
        return RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void responseList에_호선이_존재함(ExtractableResponse<Response> responseList, List<String> lineNames) {
        final List<String> nameList = responseList.jsonPath().getList("name");
        assertThat(nameList).isEqualTo(lineNames);
    }

    public static void responseList에_호선이_존재함(ExtractableResponse<Response> responseList, String lineName) {
        final String name = responseList.jsonPath().get("name");
        assertThat(name).contains(lineName);
    }

    public static void 중복이름으로_지하철_노선_생성_실패함(ExtractableResponse<Response> duplicateResponse) {
        assertThat(duplicateResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
