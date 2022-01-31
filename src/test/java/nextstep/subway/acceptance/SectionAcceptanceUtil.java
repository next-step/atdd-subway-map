package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.springframework.http.MediaType;

public class SectionAcceptanceUtil {
    public static ExtractableResponse<Response> 지하철_구간_등록_요청(String location, SectionRequest requestBody) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .post(location + "/sections")
                .then()
                .log()
                .all()
                .extract();
    }
}
