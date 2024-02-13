package subway.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineFixture {

    public static ExtractableResponse<Response> 지하철노선_생성_요청(
            final Map<String, Object> params
    ) {
        return RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().extract();
    }

    public static ExtractableResponse<Response> getLines() {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then()
                .extract();
    }
}