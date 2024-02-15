package support.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;

public class LineSteps {


    public static final String LINE_BASE_PATH = "/lines";

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, Object> request) {
        return RestAssured.given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(LINE_BASE_PATH)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(LINE_BASE_PATH)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_단일_조회_요청(Long 노선_아이디) {
        return RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(LINE_BASE_PATH + "/{lineId}", 노선_아이디)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long 노선_아이디, Map<String, Object> body) {
        return RestAssured.given().log().all()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put(LINE_BASE_PATH + "/{lineId}", 노선_아이디)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long 노선_아이디) {
        return RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(LINE_BASE_PATH + "/{lineId}", 노선_아이디)
            .then().log().all()
            .extract();
    }

    public static List<String> 지하철_노선_응답에서_이름_목록_추출(ExtractableResponse<Response> 지하철_노선_응답) {
        return 지하철_노선_응답.jsonPath()
            .getList("name", String.class);
    }

    public static List<Long> 지하철_노선_응답에서_아이디_목록_추출(ExtractableResponse<Response> 지하철_노선_응답) {
        return 지하철_노선_응답.jsonPath()
            .getList("id", Long.class);
    }

    public static Long 지하철_노선_응답에서_아이디_추출(ExtractableResponse<Response> 지하철_노선_응답) {
        return 지하철_노선_응답.jsonPath().getLong("id");
    }

    public static String 지하철_노선_응답에서_이름_추출(ExtractableResponse<Response> 지하철_노선_응답) {
        return 지하철_노선_응답.jsonPath().get("name");
    }

    public static String 지하철_노선_응답에서_색상_추출(ExtractableResponse<Response> 지하철_노선_응답) {
        return 지하철_노선_응답.jsonPath().get("color");
    }
}
