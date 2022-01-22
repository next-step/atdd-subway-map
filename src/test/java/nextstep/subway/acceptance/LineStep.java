package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class LineStep {
    private LineStep() {}

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(final String name, final String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines")
                          .then().log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회(final long id) {
        return RestAssured.given().log().all()
                          .when()
                          .get("/lines/" + id)
                          .then().log().all()
                          .extract();
    }

    public enum 지하철_생성_수정_요청_Params {
        이호선("2호선", "bg-red-600"),
        삼호선("3호선", "bg-black-600");

        지하철_생성_수정_요청_Params(String name, String color) {
            this.name = name;
            this.color = color;
        }

        private final String name;
        private final String color;

        public String getName() {
            return name;
        }

        public String getColor() {
            return color;
        }
    }
}
