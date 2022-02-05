package nextstep.subway.testfixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineAcceptanceTestFixture {
    public static final String 신분당선_이름 = "신분당선";
    public static final String 신분당선_색상 = "bg-red-600";

    public static final String 구분당선_이름 = "구분당선";
    public static final String 구분당선_색상 = "bg-blue-600";

    public static ExtractableResponse<Response> 지하철_노선_조회(String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성(String name, String color) {
        Map<String, String> 노선 = 역_생성(name, color);

        return RestAssured.given().log().all()
                .body(노선)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static Map<String, String> 역_생성(String name, String color) {
        Map<String, String> 역 = new HashMap<>();
        역.put("name", name);
        역.put("color", color);
        return 역;
    }

    public static String uri(ExtractableResponse<Response> response) {
        return response.header("Location");
    }
}
