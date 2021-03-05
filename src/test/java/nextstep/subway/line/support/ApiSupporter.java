package nextstep.subway.line.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.domain.Line;
import org.springframework.http.MediaType;

public class ApiSupporter {

    public static ExtractableResponse<Response> callCreatedApi(String stationName, String color) {
        Line line = new Line(stationName, color);
        return RestAssured
                    .given()
                        .body(line)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                        .post("/lines")
                    .then()
                        .extract();
    }

    public static ExtractableResponse<Response> callFindApi(String path) {
        return RestAssured
                .given().log().all()
                .when()
                    .get(path)
                .then().log().all()
                .extract();
    }
}
