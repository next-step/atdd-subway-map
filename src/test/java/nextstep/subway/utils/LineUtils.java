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

import static nextstep.subway.utils.ResponseUtils.httpStatus가_OK면서_ResponseBody가_존재함;
import static org.assertj.core.api.Assertions.assertThat;

public class LineUtils {
    public static Map<String, Object> 지하철_노선_데이터_생성(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        param.put("upStationId", upStationId);
        param.put("downStationId", downStationId);
        param.put("distance", distance);
        return param;
    }

    public static List<ExtractableResponse<Response>> 지하철_노선_생성요청(List<Map<String, Object>> params) {
        List<ExtractableResponse<Response>> responseList = new ArrayList<>();
        for (Map<String, Object> param: params) {
            responseList.add(지하철_노선_생성요청(param));
        }
        return responseList;
    }

    public static ExtractableResponse<Response> 지하철_노선_생성요청(Map<String, Object> param) {
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정요청(Map<String, Object> param, String uri) {
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

    public static ExtractableResponse<Response> 지하철_모든_노선_목록요청() {
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

    public static void 생성_요청한_지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 생성요청한_지하철_노선들이_포함된_응답을_받음(ExtractableResponse<Response> responseList, List<String> lineNames) {
        httpStatus가_OK면서_ResponseBody가_존재함(responseList);
        final List<String> nameList = responseList.jsonPath().getList("name");
        assertThat(nameList).isEqualTo(lineNames);
    }

    public static void 생성요청한_지하철_노선이_포함된_응답을_받음(ExtractableResponse<Response> responseList, String lineName) {
        httpStatus가_OK면서_ResponseBody가_존재함(responseList);
        final String name = responseList.jsonPath().get("name");
        assertThat(name).contains(lineName);
    }

    public static void 지하철노선_수정요청이_성공함(ExtractableResponse<Response> editResponse, String lineName) {
        httpStatus가_OK면서_ResponseBody가_존재함(editResponse);
        final String name = editResponse.jsonPath().get("name");
        assertThat(name).contains(lineName);
    }

    public static void 중복이름으로_지하철_노선_생성_실패함(ExtractableResponse<Response> duplicateResponse) {
        assertThat(duplicateResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    public static void 삭제요청한_지하철_노선이_존재하지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
