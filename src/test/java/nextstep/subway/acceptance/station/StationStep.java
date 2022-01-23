package nextstep.subway.acceptance.station;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class StationStep {
    private static int dummyCounter = 0;
    private static final String NAME_FORMAT = "%d역";

    private StationStep() {
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/stations")
                          .then().log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청() {
        return 지하철역_생성_요청(nextName());
    }

    public static String nextName() {
        return String.format(NAME_FORMAT, dummyCounter++);
    }
}
