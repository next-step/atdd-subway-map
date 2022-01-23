package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class LineStep {
    private static int dummyCounter = 0;
    private static final String NAME_FORMAT = "%d호선";
    private static final String COLOR_FORMANT = "bg-red-%d";

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

    public static ExtractableResponse<Response> 지하철_노선_생성_요청() {
        return 지하철_노선_생성_요청(nextName(), nextColor());
    }

    public static String nextName() {
        return String.format(NAME_FORMAT, ++dummyCounter);
    }

    public static String nextColor() {
        return String.format(COLOR_FORMANT, ++dummyCounter);
    }
}
