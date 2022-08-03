package nextstep.subway.provider;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class StationProvider {

    public static ExtractableResponse<Response> 지하철역_등록_요청(String stationName) {
        final Map<String, Object> param = 요청보낼_파라미터_생성(stationName);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(param)
                .contentType(APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        return response;
    }

    public static void 지하철역_등록_요청(List<String> stationNames) {
        final List<Map<String, Object>> params = 요청보낼_파라미터_생성(stationNames);
        for (Map<String, Object> param : params) {
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .body(param)
                    .contentType(APPLICATION_JSON_VALUE)
                    .when().post("/stations")
                    .then().log().all()
                    .extract();
        }
    }

    public static Long 지하쳘역_등록됨(String stationName) {
        final ExtractableResponse<Response> 지하철역_등록_응답 = 지하철역_등록_요청(stationName);
        return 지하철역_등록_응답.jsonPath().getLong("id");
    }

    public static void 지하쳘역_등록됨(List<String> stationNames) {
        지하철역_등록_요청(stationNames);
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();

        return response;
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(Long stationId) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/stations/{stationId}", stationId)
                .then().log().all()
                .extract();

        return response;
    }

    private static Map<String, Object> 요청보낼_파라미터_생성(String stationName) {
        final Map<String, Object> param = new HashMap<>();
        param.put("name", stationName);

        return param;
    }

    private static List<Map<String, Object>> 요청보낼_파라미터_생성(List<String> stationNames) {
        final List<Map<String, Object>> params = new ArrayList<>();
        for (final String stationName : stationNames) {
            final Map<String, Object> param = 요청보낼_파라미터_생성(stationName);
            params.add(param);
        }

        return params;
    }
}
