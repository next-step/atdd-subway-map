package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;
import java.util.stream.Collectors;

import static nextstep.subway.utils.ResponseUtils.httpStatus가_OK면서_ResponseBody가_존재함;
import static org.assertj.core.api.Assertions.assertThat;


public class StationUtils {
    public static List<Map<String, Object>> 지하철_역_데이터_생성(List<String> names) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (String name : names) {
            results.add(지하철_역_데이터_생성(name));
        }
        return results;
    }

    public static Map<String, Object> 지하철_역_데이터_생성(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

    public static List<ExtractableResponse<Response>> 지하철_역_생성_요청(List<Map<String, Object>> params) {
        List<ExtractableResponse<Response>> responseList = new ArrayList<>();
        for (Map<String, Object> param : params) {
            responseList.add(지하철_역_생성_요청(param));
        }
        return responseList;
    }

    public static ExtractableResponse<Response> 지하철_역_생성_요청(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_역_목록_요청() {
        return RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_역_삭제_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 생성요청한_지하철역이_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 생성요청한_지하철역들과_생성된_지하철역_목록이_동일함(List<ExtractableResponse<Response>> requestList, ExtractableResponse<Response> responseList) {
        final List<String> requestNames = requestList.stream()
                .map(r -> r.body())
                .map(r -> r.jsonPath().get("name"))
                .map(Object::toString)
                .collect(Collectors.toList());

        final List<String> responseName = responseList.jsonPath()
                .getList("name")
                .stream()
                .map(Objects::toString)
                .collect(Collectors.toList());
        assertThat(requestNames).isEqualTo(responseName);

        httpStatus가_OK면서_ResponseBody가_존재함(responseList);
    }

    public static void 중복이름으로_지하철_역_생성_실패함(ExtractableResponse<Response> duplicateResponse) {
        assertThat(duplicateResponse.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    public static void 삭제요청한_지하철_역이_존재하지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
