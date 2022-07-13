package nextstep.subway.acceptance.client;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import nextstep.subway.domain.Line;
import org.springframework.http.MediaType;

public class LineRestAssured {

    public static String STATION_LINE_REQUEST_PATH = "/station/line";

    public ValidatableResponse getRequest() {
        return RestAssured.given().log().all()
                          .when().log().all()
                          .get(STATION_LINE_REQUEST_PATH)
                          .then().log().all();
    }

    public ValidatableResponse getRequest(Long id) {
        return RestAssured.given().log().all()
                          .pathParam("id", id)
                          .when().log().all()
                          .get(STATION_LINE_REQUEST_PATH + "/{id}")
                          .then().log().all();
    }

    public ValidatableResponse postRequest(Line body) {
        return RestAssured.given().log().all()
                          .body(body)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().log().all()
                          .post(STATION_LINE_REQUEST_PATH)
                          .then().log().all();
    }

    public ValidatableResponse putRequest(Line body, Long id) {
        return RestAssured.given().log().all()
                          .pathParam("id", id)
                          .and()
                          .body(body)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().log().all()
                          .put(STATION_LINE_REQUEST_PATH+ "/{id}")
                          .then().log().all();
    }

    public ValidatableResponse deleteRequest(Line line) {
        return RestAssured.given().log().all()
                          .pathParam("id", line.getId())
                          .when().log().all()
                          .delete(STATION_LINE_REQUEST_PATH + "/{id}")
                          .then().log().all();
    }
}
