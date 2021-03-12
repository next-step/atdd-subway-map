package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineHelper {
    public static Map<String, String> 파라미터_생성(String name, String color) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);
        return param;
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        return RestAssured.given()
                .body(파라미터_생성(name, color))
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
}
