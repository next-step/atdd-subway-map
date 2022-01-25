package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class StationUtils {
    public static List<Map<String, String>> Station_데이터_생성(List<String> names) {
        List<Map<String, String>> results = new ArrayList<>();
        for (String name : names) {
            results.add(Station_데이터_생성(name));
        }
        return results;
    }

    public static Map<String, String> Station_데이터_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

    public static List<ExtractableResponse<Response>> Station_생성_요청(List<Map<String, String>> params) {
        List<ExtractableResponse<Response>> responseList = new ArrayList<>();
        for (Map<String, String> param : params) {
            responseList.add(Station_생성_요청(param));
        }
        return responseList;
    }

    public static ExtractableResponse<Response> Station_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> Station_목록_요청() {
        return RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> Station_삭제_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 생성요청_Station_name_list와_생성된_Station_name_list가_동일함(List<ExtractableResponse<Response>> requestList, ExtractableResponse<Response> responseList) {
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
    }

    public static void 중복이름으로_지하철_역_생성_실패함(ExtractableResponse<Response> duplicateResponse) {
        assertThat(duplicateResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 삭제요청한_지하철_역이_존재하지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
