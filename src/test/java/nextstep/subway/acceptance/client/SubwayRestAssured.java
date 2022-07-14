package nextstep.subway.acceptance.client;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.springframework.http.MediaType;

public class SubwayRestAssured<T> {


    public ValidatableResponse getRequest(String path) {
        return RestAssured.given().log().all()
                          .when().log().all()
                          .get(path)
                          .then().log().all();
    }

    public ValidatableResponse getRequest(String path, Long id) {
        return RestAssured.given().log().all()
                          .pathParam("id", id)
                          .when().log().all()
                          .get(path)
                          .then().log().all();
    }

    public ValidatableResponse postRequest(String path, T body) {
        return RestAssured.given().log().all()
                          .body(body)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().log().all()
                          .post(path)
                          .then().log().all();
    }

    public void putRequest(String path, Long id, T body) {
         RestAssured.given().log().all()
                          .pathParam("id", id)
                          .and()
                          .body(body)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().log().all()
                          .put(path)
                          .then().log().all();
    }

    public ValidatableResponse deleteRequest(String path, Long id) {
        return RestAssured.given().log().all()
                          .pathParam("id", id)
                          .when().log().all()
                          .delete(path)
                          .then().log().all();
    }
}
