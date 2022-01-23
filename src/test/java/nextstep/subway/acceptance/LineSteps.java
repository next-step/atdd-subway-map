package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import org.springframework.http.MediaType;

public class LineSteps {
    public static LineRequest 신분당선_요청_생성() {
        return LineRequest.of(
            "신분당선",
            "bg-red-600"
        );
    }

    public static LineRequest 이호선_요청_생성() {
        return LineRequest.of(
            "2호선",
            "bg-green-600"
        );
    }

    public static ExtractableResponse<Response> 지하철_노선_생성(LineRequest lineRequest) {
        return RestAssured.given().log().all()
            .body(lineRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();
    }
}
