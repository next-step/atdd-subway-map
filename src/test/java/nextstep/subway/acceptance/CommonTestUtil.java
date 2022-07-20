package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class CommonTestUtil {
    protected ExtractableResponse<Response> getLine(long lineId) {
        return RestAssured
            .given().log().all()
            .when().get("/lines/{id}",lineId)
            .then().log().all()
            .extract();
    }

    protected ExtractableResponse<Response> getLines() {
        return RestAssured
            .given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }
}
