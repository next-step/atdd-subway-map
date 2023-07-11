package subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.LineCreateRequest;

public class LineTestUtils {

    public static Long 지하철_노선을_등록한다(LineCreateRequest lineCreateRequest) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(lineCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        return response.jsonPath().getLong("id");
    }
}
